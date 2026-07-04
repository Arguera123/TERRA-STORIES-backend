package com.amgems.terrastoriesbackend.domain.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class UserInfoDTO {
    private String id;
    private String username;
    private String nombre;
    private String email;
    private Set<String> roles;
    private Boolean enabled;
}