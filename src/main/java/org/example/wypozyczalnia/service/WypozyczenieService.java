package org.example.wypozyczalnia.service;

import org.example.wypozyczalnia.entity.Film;
import org.example.wypozyczalnia.entity.Uzytkownik;
import org.example.wypozyczalnia.entity.Wypozyczenie;
import org.example.wypozyczalnia.repository.FilmRepository;
import org.example.wypozyczalnia.repository.UzytkownikRepository;
import org.example.wypozyczalnia.repository.WypozyczenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERWIS - WypozyczenieService
 * 
 * To jest serce aplikacji - TUTAJ jest logika biznesowa.
 * - sprawdza czy użytkownik i film istnieją
 * - oblicza daty wypożyczenia
 * - ustawia statusy
 */
@Service
public class WypozyczenieService {

    private final WypozyczenieRepository wypozyczenieRepository;
    private final FilmRepository filmRepository;
    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    public WypozyczenieService(WypozyczenieRepository wypozyczenieRepository,
            FilmRepository filmRepository,
            UzytkownikRepository uzytkownikRepository) {
        this.wypozyczenieRepository = wypozyczenieRepository;
        this.filmRepository = filmRepository;
        this.uzytkownikRepository = uzytkownikRepository;
    }

    /**
     * Główna metoda biznesowa: Wypożycz film
     */
    @Transactional // Cała operacja musi się udać albo wcale (ROLLBACK w razie błędu)
    public Wypozyczenie wypozyczFilm(Long uzytkownikId, Long filmId) {

        // 1. Sprawdź czy użytkownik istnieje
        Uzytkownik uzytkownik = uzytkownikRepository.findById(uzytkownikId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o ID: " + uzytkownikId));

        // 2. Sprawdź czy film istnieje
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono filmu o ID: " + filmId));

        // 2.5 Walidacja: Czy użytkownik już ma ten film wypożyczony?
        boolean juzWypozyczony = wypozyczenieRepository.existsByUzytkownik_UzytkownikIdAndFilm_FilmIdAndStatusIn(
                uzytkownikId, filmId,
                List.of("aktywne", "oczekuje_oplacenia", "OCZEKUJE_OPLACENIA", "OPLACONE", "AKTYWNE"));

        if (juzWypozyczony) {
            throw new RuntimeException("Masz już aktywne wypożyczenie tego filmu! Sprawdź zakładkę 'Moje filmy'.");
        }

        // 3. Stwórz nowe wypożyczenie
        Wypozyczenie wypozyczenie = new Wypozyczenie();
        wypozyczenie.setUzytkownik(uzytkownik);
        wypozyczenie.setFilm(film);
        wypozyczenie.setDataUtworzenia(LocalDateTime.now());
        wypozyczenie.setDataStartu(LocalDateTime.now());

        // Domyślnie wypożyczamy na 48 godzin (2 dni)
        wypozyczenie.setDataKonca(LocalDateTime.now().plusDays(2));

        // Status początkowy
        wypozyczenie.setStatus("OCZEKUJE_OPLACENIA"); // Lub AKTYWNE jeśli bez płatności

        // 4. Zapisz w bazie
        return wypozyczenieRepository.save(wypozyczenie);
    }

    public List<Wypozyczenie> pobierzWszystkieWypozyczenia() {
        return wypozyczenieRepository.findAll();
    }

    public List<Wypozyczenie> pobierzMojeWypozyczenia(String email) {
        Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        return wypozyczenieRepository.findByUzytkownik_UzytkownikId(uzytkownik.getUzytkownikId());
    }
}
