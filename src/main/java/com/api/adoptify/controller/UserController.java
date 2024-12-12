package com.api.adoptify.controller;

import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponseDto>> getAllUsers() {
        List<AppUserResponseDto> users = appUserService.getUsers();
        return ResponseEntity.ok(users);
    }
}
