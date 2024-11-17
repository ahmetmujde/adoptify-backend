package com.api.adoptify.controller;


import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private AppUserService appUserService;

    public RegisterController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping()
    public ResponseEntity<AppUserResponseDto> createUser(@RequestBody AppUserRequestDto appUserRequestDto) {
        AppUserResponseDto savedUser = appUserService.createUser(appUserRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

}
