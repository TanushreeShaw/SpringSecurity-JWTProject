package com.jwt.spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthenticationRequest represents the request payload for user authentication.
 * It contains the username and password fields.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String userName;
    private String password;
}
