package com.vamikastore.vamika.auth.services;

import com.vamikastore.vamika.auth.dto.RegistrationRequest;
import com.vamikastore.vamika.auth.dto.RegistrationResponse;
import com.vamikastore.vamika.auth.entities.Authority;
import com.vamikastore.vamika.auth.entities.User;
import com.vamikastore.vamika.auth.repositories.UserDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    @Mock
    private UserDetailRepository userDetailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registrationRequest = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    public void testCreateUser() {
        when(userDetailRepository.findByEmail(registrationRequest.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");
        when(authorityService.getUserAuthority()).thenReturn(Collections.singletonList(new Authority()));

        RegistrationResponse response = registrationService.createUser(registrationRequest);

        assertEquals(200, response.getCode());
        assertEquals("User registered successfully", response.getMessage());
        verify(userDetailRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendMail(any(User.class));
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {
        User existingUser = new User();
        when(userDetailRepository.findByEmail(registrationRequest.getEmail())).thenReturn(existingUser);

        RegistrationResponse response = registrationService.createUser(registrationRequest);

        assertEquals(400, response.getCode());
        assertEquals("Email already exists", response.getMessage());
        verify(userDetailRepository, never()).save(any(User.class));
    }
}