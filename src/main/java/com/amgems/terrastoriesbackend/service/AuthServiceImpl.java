package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.config.IKeycloakAdminClient;
import com.amgems.terrastoriesbackend.config.IKeycloakAuthClient;
import com.amgems.terrastoriesbackend.config.KeycloakProperties;
import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import com.amgems.terrastoriesbackend.utils.GeneralMappers;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    // Cliente Feign hacia la Admin API de Keycloak (crear usuarios).
    // La autenticación de este cliente (client_credentials) ya la resuelve
    // KeycloakFeignInterceptorConfig de forma transparente.
    private final IKeycloakAdminClient keycloakAdminClient;

    // Cliente Feign hacia el endpoint de tokens de Keycloak (login de usuarios finales).
    private final IKeycloakAuthClient keycloakAuthClient;

    private final KeycloakProperties keycloakProperties;

    @Override
    public KeycloakTokenResponse register(CreateUserDTO user) throws Exception {
        Map<String, Object> userPayload = GeneralMappers.createUserDtoToMap(user);

        try (Response response = keycloakAdminClient.createUser(userPayload)) {
            if (response.status() != HttpStatus.CREATED.value()) {
                throw new IllegalStateException(
                        "Keycloak no pudo crear el usuario. Código de respuesta: " + response.status());
            }
        }

        // Tras crear el usuario, se autentica de inmediato con las mismas credenciales
        // para devolver al cliente un token de acceso utilizable sin un segundo request.
        return login(user.getUserName(), user.getPassword());
    }

    @Override
    public KeycloakTokenResponse login(String username, String password) {
        MultiValueMap<String, String> formData = GeneralMappers.loginToFormData(
                username,
                password,
                keycloakProperties.getClientId(),
                keycloakProperties.getClientSecret()
        );
        return keycloakAuthClient.getToken(formData);
    }
}
