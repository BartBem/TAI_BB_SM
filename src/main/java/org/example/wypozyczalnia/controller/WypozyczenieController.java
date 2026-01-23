package org.example.wypozyczalnia.controller;

import org.example.wypozyczalnia.entity.Uzytkownik;
import org.example.wypozyczalnia.entity.Wypozyczenie;
import org.example.wypozyczalnia.repository.UzytkownikRepository;
import org.example.wypozyczalnia.service.WypozyczenieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KONTROLER - WypozyczenieController
 * 
 * Endpointy do zarządzania wypożyczeniami.
 * 
 * POST /api/wypozyczenia?filmId=2 -> Wypożycz film dla ZALOGOWANEGO użytkownika
 * GET /api/wypozyczenia -> Pobierz wszystkie (dla admina)
 */
@RestController
@RequestMapping("/api/wypozyczenia")
public class WypozyczenieController {

    private final WypozyczenieService wypozyczenieService;
    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    public WypozyczenieController(WypozyczenieService wypozyczenieService, UzytkownikRepository uzytkownikRepository) {
        this.wypozyczenieService = wypozyczenieService;
        this.uzytkownikRepository = uzytkownikRepository;
    }

    // ============================================
    // POST /api/wypozyczenia
    // Wypożycz film (tylko dla zalogowanego - ID bierzemy z sesji)
    // Przykład: POST http://localhost:8080/api/wypozyczenia?filmId=2
    // ============================================
    @PostMapping
    public Wypozyczenie utworzWypozyczenie(
            @RequestParam Long filmId,
            Authentication authentication) {

        // 1. Sprawdź kto jest zalogowany
        String email = authentication.getName(); // Spring Security daje nam email z sesji

        // 2. Znajdź użytkownika w bazie
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Błąd: Nie znaleziono zalogowanego użytkownika w bazie"));

        // 3. Wykonaj wypożyczenie dla tego użytkownika
        return wypozyczenieService.wypozyczFilm(uzytkownik.getUzytkownikId(), filmId);
    }

    // ============================================
    // GET /api/wypozyczenia
    // ============================================
    @GetMapping
    public List<Wypozyczenie> pobierzWszystkie() {
        return wypozyczenieService.pobierzWszystkieWypozyczenia();
    }

    // ============================================
    // GET /api/wypozyczenia/moje
    // ============================================
    @GetMapping("/moje")
    public List<Wypozyczenie> pobierzMoje(Authentication authentication) {
        String email = authentication.getName();
        return wypozyczenieService.pobierzMojeWypozyczenia(email);
    }
}
