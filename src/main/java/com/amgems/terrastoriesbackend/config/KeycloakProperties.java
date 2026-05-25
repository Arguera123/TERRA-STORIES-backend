package com.amgems.terrastoriesbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakProperties {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String realm;
}
