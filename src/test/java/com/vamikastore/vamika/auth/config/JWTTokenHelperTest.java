package com.vamikastore.vamika.auth.config;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JWTTokenHelperTest {

    private JWTTokenHelper jwtTokenHelper;

    @BeforeEach
    public void setUp() {
        jwtTokenHelper = new JWTTokenHelper();
        // Set the secret key for testing
        ReflectionTestUtils.setField(jwtTokenHelper, "secretKey", "Y2h2YW1zaGlrcmlzaG5hYWtoaWxhcmVkZHlzZWNyZXRrZXk=");
    }

    @Test
    public void testGenerateToken() {
        String token = jwtTokenHelper.generateToken("testuser");
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testValidateToken() {
        String token = jwtTokenHelper.generateToken("testuser");
        boolean isValid = jwtTokenHelper.validateToken(token, new org.springframework.security.core.userdetails.User("testuser", "", null));
        assertTrue(isValid);
    }

    @Test
    public void testGetUserNameFromToken() {
        String token = jwtTokenHelper.generateToken("testuser");
        String username = jwtTokenHelper.getUserNameFromToken(token);
        assertEquals("testuser", username);
    }
}