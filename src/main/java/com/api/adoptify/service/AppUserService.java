package com.api.adoptify.service;

import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.entity.AppUser;
import com.api.adoptify.entity.Role;
import com.api.adoptify.mapper.AppUserMapper;
import com.api.adoptify.repository.AppUserRepository;
import com.api.adoptify.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService  {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public AppUserResponseDto createUser(AppUserRequestDto appUserRequestDto) {
        AppUser appUser = AppUserMapper.mapToAppUser(appUserRequestDto);

        if (appUserRepository.existsByEmail(appUserRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + appUserRequestDto.getEmail());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        appUser.getRoles().add(userRole);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser savedAppUser = appUserRepository.save(appUser);
        return AppUserMapper.mapToAppUserResponseDto(savedAppUser);
    }
}
