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
    private final IKeycloakAdminClient keycloakAdminClient;
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
        Set<String> roles = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return UserInfoDTO.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .nombre(jwt.getClaimAsString("name"))
                .email(jwt.getClaimAsString("email"))
                .roles(roles)
                .build();
    }
}
