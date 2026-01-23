package org.example.wypozyczalnia.controller;

import org.example.wypozyczalnia.entity.Film;
import org.example.wypozyczalnia.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * KONTROLER - FilmController
 * 
 * Teraz używa SERWISU (FilmService), a nie bezpośrednio repozytorium.
 * To lepsza praktyka - kontroler tylko "przyjmuje zamówienia",
 * a serwis "gotuje dania" (wykonuje logikę).
 */
@RestController
@RequestMapping("/api/filmy")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // ============================================
    // GET /api/filmy
    // ============================================
    // ============================================
    // GET /api/filmy
    // ============================================
    @GetMapping
    public List<Film> pobierzWszystkieFilmy(
            @RequestParam(required = false) String gatunek,
            @RequestParam(required = false) Double minOcena) {
        return filmService.pobierzWszystkieFilmy(gatunek, minOcena);
    }

    // ============================================
    // GET /api/filmy/szukaj - ZAAWANSOWANE WYSZUKIWANIE
    // ============================================
    @GetMapping("/szukaj")
    public List<Film> szukajFilmow(
            @RequestParam(required = false) String tytul,
            @RequestParam(required = false) String gatunek,
            @RequestParam(required = false) Integer rok,
            @RequestParam(required = false) java.math.BigDecimal cenaMax,
            @RequestParam(required = false) Double minOcena) {
        return filmService.szukajFilmow(tytul, gatunek, rok, cenaMax, minOcena);
    }

    // ============================================
    // GET /api/filmy/gatunki
    // ============================================
    @GetMapping("/gatunki")
    public List<org.example.wypozyczalnia.entity.Gatunek> pobierzGatunki() {
        return filmService.pobierzWszystkieGatunki();
    }

    // ============================================
    // GET /api/filmy/{id}
    // ============================================
    @GetMapping("/{id}")
    public Film pobierzFilmPoId(@PathVariable Long id) {
        return filmService.pobierzFilmPoId(id).orElse(null);
    }

    // ============================================
    // POST /api/filmy
    // ============================================
    @PostMapping
    public Film dodajFilm(@RequestBody Film film) {
        return filmService.dodajFilm(film);
    }

    // ============================================
    // DELETE /api/filmy/{id}
    // ============================================
    @DeleteMapping("/{id}")
    public String usunFilm(@PathVariable Long id) {
        filmService.usunFilm(id);
        return "Film usunięty!";
    }

    // ============================================
    // GET /api/filmy/test
    // ============================================
    @GetMapping("/test")
    public String test() {
        return "Kontroler działa! Filmów w bazie: " + filmService.pobierzWszystkieFilmy(null, null).size();
    }
}
