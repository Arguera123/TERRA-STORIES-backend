package com.amgems.terrastoriesbackend.config;

import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Configuration
@RequiredArgsConstructor
public class KeycloakFeignInterceptorConfig {
    // A traves de keycloakAuthClient podemos acceder al metodo getToken
    private final iKeycloakAuthClient keycloakAuthClient;
    // Obtenemos las properties asociadas en nuestro application.yml
    private final KeycloakProperties keycloakProperties;

    @Bean
    public RequestInterceptor getKeycloakAuthInterceptor() {
        return requestTemplate -> {
            //devolvemos una requestTemplate con un MultiValueMap<String, String> que contendra los siguientes valores
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            // El Id de nuestro Client en Keycloak
            form.add("client_id", keycloakProperties.getClientId());
            // El Secret de nuestro Client en Keycloak
            form.add("client_secret", keycloakProperties.getClientSecret());
            //Especificamos que el tipo de entidad que se esta autenticando es un client mediante client credentials
            form.add("grant_type", "client_credentials");

            //Obtenemos un token valido para nuestro cliente
            KeycloakTokenResponse token = keycloakAuthClient.getToken(form);

            //Lo inyectamos en el header de nuestra peticion
            requestTemplate.header("Authorization", "Bearer " + token.getAccessToken());
        };
    }
}
