package com.api.adoptify.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.adoptify.entity.AppUser;
import com.api.adoptify.entity.Role; // Import the actual Role entity
import com.api.adoptify.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

class CustomUserDetailServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoadUserByUsernameWhenUserExists() {
        // Given AppUser
        AppUser appUser = new AppUser();
        appUser.setEmail("hikmet@gmail.com");
        appUser.setPassword("password123");

        // Given Roles
        Role role = new Role();
        role.setName("USER");
        List<Role> roles = Collections.singletonList(role);

        appUser.setRoles(roles);

        when(appUserRepository.findByEmail("hikmet@gmail.com")).thenReturn(Optional.of(appUser));

        //When Call method
        UserDetails userDetails = customUserDetailService.loadUserByUsername("hikmet@gmail.com");

        // Then Assertions
        assertEquals("hikmet@gmail.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldLoadUserByUsernameWhenUserDoesNotExist() {
        // Given
        when(appUserRepository.findByEmail("hikmet@gmail.com")).thenReturn(Optional.empty());

        // Then Assertions
        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailService.loadUserByUsername("hikmet@gmail.com")
        );
    }
}
