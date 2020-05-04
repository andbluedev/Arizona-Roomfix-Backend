package com.roomfix.api.auth.dto;

import com.roomfix.api.user.entity.UserRole;
import lombok.Data;

@Data
public class LoginResponseDto {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private UserRole role;
    private String token;
}
