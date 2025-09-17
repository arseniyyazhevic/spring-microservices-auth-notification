package com.ay.auth.dto.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}

