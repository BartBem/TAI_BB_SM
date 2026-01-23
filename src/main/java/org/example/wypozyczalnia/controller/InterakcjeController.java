package org.example.wypozyczalnia.controller;

import org.example.wypozyczalnia.dto.DodajKomentarzRequest;
import org.example.wypozyczalnia.dto.DodajOceneRequest;
import org.example.wypozyczalnia.entity.Komentarz;
import org.example.wypozyczalnia.entity.Ocena;
import org.example.wypozyczalnia.service.InterakcjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interakcje")
public class InterakcjeController {

    private final InterakcjeService interakcjeService;

    @Autowired
    public InterakcjeController(InterakcjeService interakcjeService) {
        this.interakcjeService = interakcjeService;
    }

    // ============================================
    // POST /api/interakcje/ocena
    // Dodaj ocenę (wymaga zalogowania)
    // ============================================
    @PostMapping("/ocena")
    public Ocena dodajOcene(
            @jakarta.validation.Valid @RequestBody DodajOceneRequest req,
            Authentication authentication) {

        return interakcjeService.dodajOcene(authentication.getName(), req);
    }

    // ============================================
    // POST /api/interakcje/komentarz
    // Dodaj komentarz (wymaga zalogowania)
    // ============================================
    @PostMapping("/komentarz")
    public Komentarz dodajKomentarz(
            @jakarta.validation.Valid @RequestBody DodajKomentarzRequest req,
            Authentication authentication) {

        return interakcjeService.dodajKomentarz(authentication.getName(), req);
    }

    // ============================================
    // GET /api/interakcje/film/{filmId}
    // Pobierz komentarze (publiczne? Nie, SecurityConfig to blokuje domyślnie,
    // trzeba by dodać permitAll w Configu jeśli chcemy publicznie)
    // Na razie zostawiamy dla zalogowanych.
    // ============================================
    @GetMapping("/film/{filmId}")
    public List<Komentarz> pobierzKomentarze(@PathVariable Long filmId) {
        return interakcjeService.pobierzKomentarzeDoFilmu(filmId);
    }

    // ============================================
    // NOWE ENDPOINTY (ZINTEGROWANE)
    // ============================================

    @PostMapping("/opinia")
    public String dodajOpinie(
            @jakarta.validation.Valid @RequestBody org.example.wypozyczalnia.dto.DodajOpinieRequest req,
            Authentication authentication) {
        interakcjeService.dodajOpinie(authentication.getName(), req);
        return "Opinia dodana!";
    }

    @GetMapping("/film/{filmId}/opinie")
    public List<org.example.wypozyczalnia.dto.OpiniaResponse> pobierzOpinie(@PathVariable Long filmId) {
        return interakcjeService.pobierzOpinieDoFilmu(filmId);
    }

    @DeleteMapping("/opinia")
    public String usunOpinie(
            @RequestParam Long filmId,
            Authentication authentication) {
        interakcjeService.usunOpinie(authentication.getName(), filmId);
        return "Opinia usunięta!";
    }
}
