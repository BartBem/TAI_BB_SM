package org.example.wypozyczalnia.security;

import org.example.wypozyczalnia.entity.Uzytkownik;
import org.example.wypozyczalnia.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * SERWIS - SerwisAutoryzacji (Dawniej CustomUserDetailsService)
 * 
 * Łączy Spring Security z naszą bazą danych.
 * Tłumaczy "naszego" Użytkownika na "Usera" zrozumiałego dla Springa.
 */
@Service
public class SerwisAutoryzacji implements UserDetailsService {

    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    public SerwisAutoryzacji(UzytkownikRepository uzytkownikRepository) {
        this.uzytkownikRepository = uzytkownikRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Szukamy po emailu (to jest nasz login)
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika z email: " + email));

        // Konwertujemy naszego Uzytkownika na UserDetails (obiekt Spring Security)
        return new User(
                uzytkownik.getEmail(),
                uzytkownik.getHasloHash(),
                Collections.emptyList() // Na razie brak ról/uprawnień
        );
    }
}
