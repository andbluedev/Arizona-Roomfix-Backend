package com.roomfix.api.auth.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String password;
    private String mail;
}
