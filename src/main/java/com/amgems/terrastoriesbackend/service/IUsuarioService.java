package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;

import java.util.List;
import java.util.Map;

public interface IUsuarioService {
    String crearUsuario(CreateUserDTO userDTO) throws Exception;

    List<UserInfoDTO> listarUsuarios();

    void cambiarEstadoUsuario(String userId, boolean enabled);
}
