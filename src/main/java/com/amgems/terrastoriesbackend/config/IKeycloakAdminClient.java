package com.amgems.terrastoriesbackend.config;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;

///Define un cliente de Feign llamado feign-admin que apunta al url donde esta nuestra instancia de Keycloak y mediante configuracion, permitira que la peticion sea interceptada y se pueda inyectar un token valido para nuestro cliente
@FeignClient(name = "feign-admin", url = "${keycloak.server-url}", configuration = KeycloakFeignInterceptorConfig.class)
public interface IKeycloakAdminClient {
    //Metodo post realizado al realm que hemos creado al apartado de users que nos devolvera una Response de Feign (Asegurar que la importacion de Response venga de import feign.Response)
    @PostMapping("/admin/realms/${keycloak.realm}/users")
    Response createUser(@RequestBody Map<String, Object> user);

    @GetMapping("/admin/realms/${keycloak.realm}/users")
    List<Map<String, Object>> getUsers();

    @GetMapping("/admin/realms/${keycloak.realm}/users/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") String userId);

    @PutMapping(value = "/admin/realms/${keycloak.realm}/users/{userId}", consumes = "application/json")
    Response updateUser(@PathVariable("userId") String userId, @RequestBody Map<String, Object> user);
}
