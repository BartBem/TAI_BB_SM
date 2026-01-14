package org.example.wypozyczalnia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * KONFIGURACJA BEZPIECZEŃSTWA
 * 
 * Na razie wyłączamy zabezpieczenia, żeby móc testować API.
 * Później skonfigurujemy prawdziwe logowanie.
 * 
 * @Configuration = ta klasa zawiera konfigurację Springa
 * @EnableWebSecurity = włącza Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Wyłącz CSRF (potrzebne do testowania POST/DELETE)
                .csrf(csrf -> csrf.disable())
                // Pozwól na dostęp do wszystkich endpointów bez logowania
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }
}
