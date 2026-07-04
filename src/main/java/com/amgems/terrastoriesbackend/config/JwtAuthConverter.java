package com.amgems.terrastoriesbackend.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String CLIENT_ID = "terra-stories-backend";

    @Override
    public AbstractAuthenticationToken convert(Jwt token) {
        Collection<GrantedAuthority> authorities = extractClientRoles(token);

        String preferredUsername = token.getClaimAsString("preferred_username");
        String principalAttribute = (preferredUsername != null) ? preferredUsername : token.getSubject();

        return new JwtAuthenticationToken(token, authorities, principalAttribute);
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractClientRoles(Jwt token) {
        if (token.hasClaim("roles")) {
            List<String> roles = token.getClaimAsStringList("roles");
            if (roles != null) {
                return roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList());
            }
        }

        Map<String, Object> resourceAccess = token.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID)) {
            return Collections.emptyList();
        }

        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(CLIENT_ID);
        List<String> clientRoles = (List<String>) clientAccess.get("roles");

        if (clientRoles == null) {
            return Collections.emptyList();
        }

        return clientRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}