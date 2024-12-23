package com.api.adoptify.controller;


import com.api.adoptify.dto.request.LoginRequest;
import com.api.adoptify.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;

    public LoginController(AuthenticationManager authenticationManager, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.appUserService = appUserService;
    }

    @PostMapping()
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Authentication işlemini SecurityContext'e set ediyoruz
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Oturuma kullanıcı bilgisi ekleme
            String appUserName = appUserService.getAppUserNameByUserEmail(loginRequest.getEmail());
            session.setAttribute("appUserName", appUserName);

            // Başarılı giriş durumunda HTTP 200 dön
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException ex) {
            // Hatalı giriş durumunda HTTP 401 dön
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
