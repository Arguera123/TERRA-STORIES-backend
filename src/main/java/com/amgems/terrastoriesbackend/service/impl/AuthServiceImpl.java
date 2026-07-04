package com.amgems.terrastoriesbackend.service.impl;

import com.amgems.terrastoriesbackend.config.IKeycloakAdminClient;
import com.amgems.terrastoriesbackend.config.IKeycloakAuthClient;
import com.amgems.terrastoriesbackend.config.KeycloakProperties;
import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import com.amgems.terrastoriesbackend.service.IAuthService;
import com.amgems.terrastoriesbackend.utils.GeneralMappers;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public KeycloakTokenResponse login(String username, String password) {
        MultiValueMap<String, String> formData = GeneralMappers.loginToFormData(
                username,
                password,
                keycloakProperties.getClientId(),
                keycloakProperties.getClientSecret()
        );
        return keycloakAuthClient.getToken(formData);
    }

    @Override
    public UserInfoDTO obtenerInformacionUsuario(Jwt jwt) {
        // Extraemos los roles procesados que Spring Security ya guardó en el Contexto
        Set<String> roles = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", "")) // Quitamos el prefijo para devolverlos limpios al Front
                .collect(Collectors.toSet());

        return UserInfoDTO.builder()
                .id(jwt.getSubject()) // El "sub" de Keycloak es el ID único del usuario
                .username(jwt.getClaimAsString("preferred_username"))
                .nombre(jwt.getClaimAsString("name")) // Keycloak junta el nombre y apellido en "name"
                .email(jwt.getClaimAsString("email"))
                .roles(roles)
                .build();
    }
}
