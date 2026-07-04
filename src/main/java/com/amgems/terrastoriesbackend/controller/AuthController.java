package com.amgems.terrastoriesbackend.controller;

import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import com.amgems.terrastoriesbackend.domain.DTO.UserInfoDTO;
import com.amgems.terrastoriesbackend.service.IAuthService;
import com.amgems.terrastoriesbackend.utils.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<KeycloakTokenResponse> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return ResponseEntity.ok(authService.login(username, password));
    }

    @GetMapping("/whoami")
    public ResponseEntity<GenericResponse> whoami(@AuthenticationPrincipal Jwt jwt) {
        UserInfoDTO userInfoDTO = authService.obtenerInformacionUsuario(jwt);

        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .data(userInfoDTO)
                .build().buildResponse();
    }

    @PreAuthorize("hasRole('role-user')")
    @GetMapping("/auth-test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is working!");
    }
}
