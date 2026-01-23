package com.josue.authorization_server.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private String email;
    private List<RoleDto> roles;
}
