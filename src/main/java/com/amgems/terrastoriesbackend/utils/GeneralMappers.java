package com.amgems.terrastoriesbackend.utils;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneralMappers {
    public static Map<String, Object> createUserDtoToMap(CreateUserDTO user) {
        return Map.of(
                "username", user.getUserName(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "enabled", true,
                "emailVerified", true,
                "credentials", List.of(Map.of("type", "password", "value", user.getPassword(), "temporary", false))
        );
    }

    public static MultiValueMap<String, String> loginToFormData(String username, String password, String clientId, String clientSecret) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        return formData;
    }

    public static UserInfoDTO mapToUserInfoDTO(Map<String, Object> keycloakUser) {
        if (keycloakUser == null) return null;

        String firstName = (String) keycloakUser.get("firstName");
        String lastName = (String) keycloakUser.get("lastName");
        String nombreCompleto = (firstName != null || lastName != null)
                ? ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim()
                : (String) keycloakUser.get("username");

        return UserInfoDTO.builder()
                .id((String) keycloakUser.get("id"))
                .username((String) keycloakUser.get("username"))
                .nombre(nombreCompleto)
                .email((String) keycloakUser.get("email"))
                .enabled((Boolean) keycloakUser.get("enabled"))
                .roles(new java.util.HashSet<>())
                .build();
    }
}
