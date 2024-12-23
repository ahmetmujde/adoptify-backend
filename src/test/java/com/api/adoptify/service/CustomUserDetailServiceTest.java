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
import java.util.List;
import java.util.Optional;

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
        appUser.setPassword("hikmet123");

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
        assertEquals("hikmet123", userDetails.getPassword());
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

    @Test
    void shouldHandleUserWithNoRoles() {
        // Given AppUser
        AppUser appUser = new AppUser();
        appUser.setEmail("hikmet@gmail.com");
        appUser.setPassword("hikmet123");
        appUser.setRoles(Collections.emptyList()); // No roles

        when(appUserRepository.findByEmail("hikmet@gmail.com")).thenReturn(Optional.of(appUser));

        // When
        UserDetails userDetails = customUserDetailService.loadUserByUsername("hikmet@gmail.com");

        // Then
        assertEquals("hikmet@gmail.com", userDetails.getUsername());
        assertEquals("hikmet123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void shouldHandleUserWithMultipleRoles() {
        // Given AppUser
        AppUser appUser = new AppUser();
        appUser.setEmail("hikmet@gmail.com");
        appUser.setPassword("hikmet123");

        // Given Roles
        Role roleUser = new Role();
        roleUser.setName("USER");
        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        appUser.setRoles(List.of(roleUser, roleAdmin));

        when(appUserRepository.findByEmail("hikmet@gmail.com")).thenReturn(Optional.of(appUser));

        // When
        UserDetails userDetails = customUserDetailService.loadUserByUsername("hikmet@gmail.com");

        // Then
        assertEquals("hikmet@gmail.com", userDetails.getUsername());
        assertEquals("hikmet123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldHandleUnexpectedErrorDuringFindByEmail() {
        // Given
        when(appUserRepository.findByEmail("hikmet@gmail.com"))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Then
        assertThrows(RuntimeException.class, () ->
                customUserDetailService.loadUserByUsername("hikmet@gmail.com"));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailService.loadUserByUsername(null));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailService.loadUserByUsername(""));
    }
}
