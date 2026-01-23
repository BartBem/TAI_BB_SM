package org.example.wypozyczalnia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class KonfiguracjaBezpieczenstwa {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Wyłączamy CSRF dla uproszczenia frontendu
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Włączamy CORS dla Reacta
                .authorizeHttpRequests(auth -> auth
                        // Frontend testowy (USUNIĘTO STARY INDEX.HTML)
                        // .requestMatchers("/", "/index.html", "/static/**").permitAll()

                        // Publiczne endpointy (dostępne dla każdego)
                        .requestMatchers("/api/autoryzacja/**").permitAll() // Logowanie i Rejestracja (PL)
                        .requestMatchers(HttpMethod.GET, "/api/filmy/**").permitAll() // Przeglądanie filmów
                        .requestMatchers(HttpMethod.GET, "/api/interakcje/film/**").permitAll() // Czytanie opinii

                        // Reszta wymaga zalogowania
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/api/autoryzacja/wyloguj")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(200)));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.List.of("*"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
