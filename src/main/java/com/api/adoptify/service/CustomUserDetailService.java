package com.api.adoptify.service;


import com.api.adoptify.entity.AppUser;
import com.api.adoptify.entity.Role;
import com.api.adoptify.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email '%s' not found", email)));

        String[] roleNames = appUser.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(roleNames)
                .build();
    }
}
