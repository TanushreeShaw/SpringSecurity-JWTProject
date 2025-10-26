package com.jwt.spring.security.controller;

import com.jwt.spring.security.dto.AuthenticationRequest;
import com.jwt.spring.security.service.PersonDetailsServiceImpl;
import com.jwt.spring.security.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PersonController {

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

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
        final String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> response = new LinkedHashMap<>();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            response.put("message", "Here is your original login information based on the JWT");
            response.put("username", userDetails.getUsername());
            response.put("password", userDetails.getPassword()); // original password is usually encoded
        } else {
            response.put("message", "No authenticated user found");
        }

        return ResponseEntity.ok(response);
    }

}
