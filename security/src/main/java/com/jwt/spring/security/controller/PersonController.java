package com.jwt.spring.security.controller;

import com.jwt.spring.security.dto.AuthenticationRequest;
import com.jwt.spring.security.endpoint.PersonEndpoint;
import com.jwt.spring.security.service.PersonDetailsServiceImpl;
import com.jwt.spring.security.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PersonController handles authentication and user verification endpoints.
 * It provides endpoints to generate JWT tokens and verify user details.
 */
@RestController
public class PersonController implements PersonEndpoint {

    private final AuthenticationManager authManager;
    private final PersonDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public PersonController(AuthenticationManager authManager,
                            PersonDetailsServiceImpl userDetailsService,
                            JwtUtils jwtUtils) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authenticationRequest the authentication request containing username and password
     * @return a ResponseEntity containing the JWT token or an error message
     */
    @Override
    public ResponseEntity<?> generateToken(AuthenticationRequest authenticationRequest) {

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                    authenticationRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Verifies the JWT token and retrieves user details.
     *
     * @return a ResponseEntity containing user details or an error message
     */
    @Override
    public ResponseEntity<Map<String, Object>> getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> userDetailsResponse = new LinkedHashMap<>();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            userDetailsResponse.put("message", "Here is your original login information based on the JWT");
            userDetailsResponse.put("username", userDetails.getUsername());
            userDetailsResponse.put("password", userDetails.getPassword());
        } else {
            userDetailsResponse.put("message", "No authenticated user found");
        }

        return ResponseEntity.ok(userDetailsResponse);
    }

}
