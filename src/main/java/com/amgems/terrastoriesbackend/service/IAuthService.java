package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;


public interface IAuthService {

    KeycloakTokenResponse login(String username, String password);
}
