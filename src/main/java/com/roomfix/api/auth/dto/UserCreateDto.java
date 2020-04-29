package com.roomfix.api.auth.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String mail;
    private String password;
}
