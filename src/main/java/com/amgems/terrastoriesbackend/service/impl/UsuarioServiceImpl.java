package com.amgems.terrastoriesbackend.service.impl;

import com.amgems.terrastoriesbackend.config.IKeycloakAdminClient;
import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import com.amgems.terrastoriesbackend.service.IUsuarioService;
import com.amgems.terrastoriesbackend.utils.GeneralMappers;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    private final IKeycloakAdminClient keycloakAdminClient;
    @Override
    public String crearUsuario(CreateUserDTO userDTO) throws Exception {
        Map<String, Object> userPayload = GeneralMappers.createUserDtoToMap(userDTO);
        try (Response response = keycloakAdminClient.createUser(userPayload)) {
            if (response.status() != HttpStatus.CREATED.value()) {
                throw new IllegalStateException(
                        "Keycloak no pudo crear el usuario. Código de respuesta: " + response.status());
            }
            Collection<String> locationHeaders = response.headers().get("Location");
            if (locationHeaders != null && !locationHeaders.isEmpty()) {
                String location = locationHeaders.iterator().next();
                return location.substring(location.lastIndexOf("/") + 1);
            }
        }
        return null;
    }

    @Override
    public List<UserInfoDTO> listarUsuarios() {
        List<Map<String, Object>> usuarios = keycloakAdminClient.getUsers();

        if (usuarios == null) {
            return java.util.Collections.emptyList();
        }

        return usuarios.stream()
                .map(GeneralMappers::mapToUserInfoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cambiarEstadoUsuario(String userId, boolean enabled) {
        Map<String, Object> user = keycloakAdminClient.getUserById(userId);
        Map<String, Object> updates = new HashMap<>(user);
        updates.put("enabled", enabled);
        updates.remove("credentials");

        try (Response response = keycloakAdminClient.updateUser(userId, updates)) {
            if (response.status() != HttpStatus.NO_CONTENT.value() && response.status() != HttpStatus.OK.value()) {
                throw new IllegalStateException(
                        "No se pudo actualizar el estado del usuario en Keycloak. Código: " + response.status());
            }
        }
    }
}
