package com.jwt.spring.security.endpoint;

import com.jwt.spring.security.dto.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * PersonEndpoint defines the REST API endpoints for user authentication and verification.
 * It includes methods for generating JWT tokens and retrieving user details.
 */
@RequestMapping("/person")
public interface PersonEndpoint {

    @PostMapping(value = "/authenticate", produces = "application/json")
    ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest authRequest);

    @GetMapping("/verify")
    ResponseEntity<Map<String, Object>> getUserDetails();

}