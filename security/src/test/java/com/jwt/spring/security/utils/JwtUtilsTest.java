package com.jwt.spring.security.utils;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the JwtUtils class.
 * These tests cover token generation, claim extraction, and token validation.
 */
class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        userDetails = User.builder()
                .username("testuser")
                .password("testpass")
                .roles("USER")
                .build();
    }

    /**
     * Test generating a JWT token and extracting the username from it.
     */
    @Test
    void testGenerateTokenAndExtractUsername() {
        String token = jwtUtils.generateToken(userDetails);
        String username = jwtUtils.extractUsername(token);
        assertEquals("testuser", username);
    }

    /**
     * Test extracting a claim (expiration date) from the JWT token.
     */
    @Test
    void testExtractClaim() {
        String token = jwtUtils.generateToken(userDetails);
        Date expiration = jwtUtils.extractClaim(token, claims -> claims.getExpiration());
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    /**
     * Test validating a valid JWT token.
     */
    @Test
    void testValidateTokenValid() {
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.validateToken(token, userDetails));
    }

    /**
     * Test validating a JWT token with an invalid username.
     */
    @Test
    void testValidateTokenInvalidUsername() {
        String token = jwtUtils.generateToken(userDetails);
        UserDetails userDetailInfo = User.builder()
                .username("otheruser")
                .password("testpass")
                .roles("USER")
                .build();
        assertFalse(jwtUtils.validateToken(token, userDetailInfo));
    }

    /**
     * Test validating an expired JWT token.
     */
    @Test
    void testValidateTokenExpired() throws InterruptedException {
        UserDetails shortExpiryUser = User.builder()
                .username("shortuser")
                .password("testpass")
                .roles("USER")
                .build();
        JwtUtils shortExpiryJwtUtils = new JwtUtils() {
            @Override
            public String generateToken(UserDetails userDetails) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 1000)) // 1 second
                        .signWith(this.getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };
        String token = shortExpiryJwtUtils.generateToken(shortExpiryUser);
        Thread.sleep(1100);

        assertThrows(ExpiredJwtException.class, () -> {
            shortExpiryJwtUtils.validateToken(token, shortExpiryUser);
        });
    }
}