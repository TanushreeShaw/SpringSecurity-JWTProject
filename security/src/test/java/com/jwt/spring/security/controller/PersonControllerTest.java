package com.jwt.spring.security.controller;

import com.jwt.spring.security.dto.AuthenticationRequest;
import com.jwt.spring.security.service.PersonDetailsServiceImpl;
import com.jwt.spring.security.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PersonController class.
 * These tests cover token generation and user detail retrieval.
 */
class PersonControllerTest {

    private AuthenticationManager authManager;
    private PersonDetailsServiceImpl userDetailsService;
    private JwtUtils jwtUtils;
    private PersonController personController;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        userDetailsService = mock(PersonDetailsServiceImpl.class);
        jwtUtils = mock(JwtUtils.class);
        personController = new PersonController(authManager, userDetailsService, jwtUtils);
    }

    /**
     * Test generating a JWT token successfully.
     */
    @Test
    void testGenerateTokenSuccess() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName("user");
        authenticationRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("jwt-token");

        ResponseEntity<?> generateToken = personController.generateToken(authenticationRequest);

        assertEquals(200, generateToken.getStatusCodeValue());
        assertTrue(generateToken.getBody().toString().contains("jwt-token"));
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Test generating a JWT token with invalid credentials.
     */
    @Test
    void testGenerateTokenInvalidCredentials() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName("user");
        authenticationRequest.setPassword("wrong_password");

        doThrow(new RuntimeException("Bad credentials"))
                .when(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> generateToken = personController.generateToken(authenticationRequest);

        assertEquals(401, generateToken.getStatusCodeValue());
        assertEquals("Invalid username or password", generateToken.getBody());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService, jwtUtils);
    }

    /**
     * Test retrieving user details when the user is authenticated.
     */
    @Test
    void testGetUserDetailsUserAuthenticated() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getPassword()).thenReturn("testpass");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            ResponseEntity<Map<String, Object>> personControllerUserDetails = personController.getUserDetails();

            assertEquals(200, personControllerUserDetails.getStatusCodeValue());
            assertEquals("Here is your original login information based on the JWT", personControllerUserDetails.getBody().get("message"));
            assertEquals("testuser", personControllerUserDetails.getBody().get("username"));
            assertEquals("testpass", personControllerUserDetails.getBody().get("password"));
        }
    }

    /**
     * Test retrieving user details when there is no authenticated user.
     */
    @Test
    void testGetUserDetailsNoAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            ResponseEntity<Map<String, Object>> userDetails = personController.getUserDetails();

            assertEquals(200, userDetails.getStatusCodeValue());
            assertEquals("No authenticated user found", userDetails.getBody().get("message"));
            assertFalse(userDetails.getBody().containsKey("username"));
            assertFalse(userDetails.getBody().containsKey("password"));
        }
    }

}