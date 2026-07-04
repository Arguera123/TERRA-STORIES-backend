package com.amgems.terrastoriesbackend.config;

import com.amgems.terrastoriesbackend.domain.DTO.KeycloakTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Define un cliente de OpenFeign con el nombre keycloak-service en la url donde esta almacenado nuestra instancia de Keycloak
//La linea de configuration permite interceptar la peticion y configurar el formato a uno valido para Keycloak
@FeignClient(name = "keycloak-service", url = "${keycloak.server-url}", configuration = KeycloakAuthFeignConfig.class)
public interface IKeycloakAuthClient {
    // Metodo de tipo post realizado a nuestro realm, mediante el protocolo openid-connect que obtendra un token y envia un Body de tipo MultiValueMap<String, String> con el nombre formData. Esta funcion devuelve un KeycloakTokenResponse, definido anteriormente
    @PostMapping(value = "/realms/${keycloak.realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public KeycloakTokenResponse getToken(@RequestBody MultiValueMap<String, String> formData);

}
