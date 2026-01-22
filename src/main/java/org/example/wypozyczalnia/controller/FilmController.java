package org.example.wypozyczalnia.controller;

import org.example.wypozyczalnia.entity.Film;
import org.example.wypozyczalnia.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * 
 * KONTROLER - FilmController
 * 
 * Kontroler odpowiada na żądania HTTP.
 * Każda metoda to jeden "endpoint" (adres URL).
 * 
 * @RestController = ta klasa zwraca dane (JSON), nie strony HTML
 * @RequestMapping = prefiks dla wszystkich endpointów w tej klasie
 */

@RestController
@RequestMapping("/api/filmy") // Wszystkie endpointy zaczynają się od /api/filmy
public class FilmController {

    // @Autowired = Spring automatycznie "wstrzykuje" repozytorium
    // Nie musisz tworzyć obiektu ręcznie (new FilmRepository())
    @Autowired
    private FilmRepository filmRepository;

    // ============================================
    // GET /api/filmy - pobierz wszystkie filmy
    // ============================================
    @GetMapping
    public List<Film> pobierzWszystkieFilmy() {
        return filmRepository.findAll();
    }


    // ============================================
    // GET /api/filmy/1 - pobierz film o ID=1
    // ============================================
    @GetMapping("/{id}")
    public Film pobierzFilmPoId(@PathVariable Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    // ============================================
    // POST /api/filmy - dodaj nowy film
    // ============================================
    @PostMapping
    public Film dodajFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    // ============================================
    // DELETE /api/filmy/1 - usuń film o ID=1
    // ============================================
    @DeleteMapping("/{id}")
    public String usunFilm(@PathVariable Long id) {
        filmRepository.deleteById(id);
        return "Film usunięty!";
    }

    // ============================================
    // GET /api/filmy/test - prosty test
    // ============================================
    @GetMapping("/test")
    public String test() {
        return "Kontroler działa! Filmów w bazie: " + filmRepository.count();
    }
}
