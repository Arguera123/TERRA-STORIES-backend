package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;

import java.util.List;
import java.util.Map;

public interface IUsuarioService {
    String crearUsuario(CreateUserDTO userDTO) throws Exception;

    List<Map<String, Object>> listarUsuarios();

    void cambiarEstadoUsuario(String userId, boolean enabled);
}
