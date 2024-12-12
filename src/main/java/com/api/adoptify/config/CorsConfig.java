package com.api.adoptify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000"); // React uygulamasının adresi
        config.addAllowedMethod("*"); // Tüm HTTP metodlarına izin ver
        config.addAllowedHeader("*"); // Tüm header'lara izin ver
        config.setAllowCredentials(true); // Kimlik bilgileri paylaşımına izin ver

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Tüm endpointler için geçerli
        return new CorsFilter(source);
    }
}
