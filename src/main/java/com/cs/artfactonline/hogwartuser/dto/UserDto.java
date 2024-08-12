package com.cs.artfactonline.hogwartuser.dto;

import jakarta.validation.constraints.NotEmpty;

//Ne pas envoyer le mdp au client
public record UserDto(
        Integer id,
        @NotEmpty(message = "usernamee is required.")
        String username,
        Boolean enable,
        @NotEmpty(message = "roles are required.")
        String roles
){};
