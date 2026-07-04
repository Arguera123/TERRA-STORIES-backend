package com.amgems.terrastoriesbackend.controller;

import com.amgems.terrastoriesbackend.domain.DTO.CreateUserDTO;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import com.amgems.terrastoriesbackend.service.IUsuarioService;
import com.amgems.terrastoriesbackend.utils.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GenericResponse> crearUsuario(@RequestBody @Valid CreateUserDTO userDTO) throws Exception {
        String userId = usuarioService.crearUsuario(userDTO);
        return GenericResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Usuario creado exitosamente")
                .data(Map.of("id", userId))
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<GenericResponse> listarUsuarios() {
        List<UserInfoDTO> usuarios = usuarioService.listarUsuarios();
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Listado de usuarios obtenido exitosamente")
                .data(usuarios)
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}/estado")
    public ResponseEntity<GenericResponse> cambiarEstadoUsuario(
            @PathVariable String userId,
            @RequestParam boolean enabled) {
        usuarioService.cambiarEstadoUsuario(userId, enabled);
        String mensaje = enabled ? "Usuario activado exitosamente" : "Usuario desactivado exitosamente";
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message(mensaje)
                .build().buildResponse();
    }
}
