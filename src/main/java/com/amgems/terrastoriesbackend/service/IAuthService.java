package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import org.springframework.security.oauth2.jwt.Jwt;


public interface IAuthService {
    KeycloakTokenResponse login(String username, String password);
    UserInfoDTO obtenerInformacionUsuario(Jwt jwt);
}
