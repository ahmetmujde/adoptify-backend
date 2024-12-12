package com.api.adoptify.service;


import com.api.adoptify.entity.AppUser;
import com.api.adoptify.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldIfThereIsAppUserNameGivenByAppUserEmail() {
        // Give
        String email = "zehra@gmail.com";
        AppUser mockUser = new AppUser();
        mockUser.setId(1L);
        mockUser.setUserName("zehra");
        mockUser.setEmail(email);


        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // When
        String appUserName = appUserService.getAppUserNameByUserEmail(email);

        // Assert
        assertEquals("zehra", appUserName);
    }

    @Test
    void shouldIfNotExistAppUserNameGivenByAppUserEmail() {
        //given
        String exceptedEmail = "zehra@gmail.com";

        //when
        when(appUserRepository.findByEmail(exceptedEmail)).thenReturn(Optional.empty());

        // then
        String result = appUserService.getAppUserNameByUserEmail(exceptedEmail);

        // Assert
        assertNull(result);
    }
}