package com.api.adoptify.config;

import com.api.adoptify.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailsService;

    public SecurityConfig(CustomUserDetailService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:3000"); // Frontend URL'si
                    config.addAllowedMethod("*"); // Tüm HTTP yöntemlerini izin ver
                    config.addAllowedHeader("*"); // Tüm header'ları izin ver
                    config.setAllowCredentials(true); // Çerezleri izin ver
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/cities").permitAll()
                        .requestMatchers("/districts").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/session/create").permitAll()
                        .requestMatchers("/session/get").permitAll()
                        .requestMatchers("/session/invalidate").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Logout endpoint'i
                        .logoutSuccessUrl("/") // Logout sonrası yönlendirme
                        .invalidateHttpSession(true) // Oturumu geçersiz kıl
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Created When needed session
                        .maximumSessions(1) // At the same time there is one session created
                        .maxSessionsPreventsLogin(true) // new logins blocked
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
