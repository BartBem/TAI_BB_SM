package org.example.wypozyczalnia.service;

import org.example.wypozyczalnia.entity.Film;
import org.example.wypozyczalnia.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SERWIS - FilmService
 * 
 * Pośredniczy między Kontrolerem a Repozytorium.
 * Tutaj powinna znajdować się logika biznesowa dotycząca filmów
 * (np. walidacja przy dodawaniu, filtrowanie, itp.).
 */
@Service
public class FilmService {

    private final FilmRepository filmRepository;

    private final org.example.wypozyczalnia.repository.GatunekRepository gatunekRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository,
            org.example.wypozyczalnia.repository.GatunekRepository gatunekRepository) {
        this.filmRepository = filmRepository;
        this.gatunekRepository = gatunekRepository;
    }

    public List<Film> pobierzWszystkieFilmy(String gatunek, Double minOcena) {
        // Ta metoda może zostać jako proste proxy lub używać nowej szukajFilmow
        // Dla kompatybilności wstecznej użyjemy nowej szukajFilmow
        return szukajFilmow(null, gatunek, null, null, minOcena);
    }

    public List<Film> szukajFilmow(String tytul, String gatunek, Integer rok, java.math.BigDecimal cenaMax,
            Double minOcena) {
        // Zamiana pustych ciągów na null
        if (tytul != null && tytul.isEmpty())
            tytul = null;
        if (gatunek != null && gatunek.isEmpty())
            gatunek = null;

        return filmRepository.szukajFilmow(tytul, gatunek, rok, cenaMax, minOcena);
    }

    public List<org.example.wypozyczalnia.entity.Gatunek> pobierzWszystkieGatunki() {
        return gatunekRepository.findAll();
    }

    public Optional<Film> pobierzFilmPoId(Long id) {
        return filmRepository.findById(id);
    }

    // Metoda z prostą logiką - sprawdza czy film istnieje przed dodaniem
    public Film dodajFilm(Film film) {
        // Tu można dodać walidację, np. czy cena > 0
        if (film.getCenaWypozyczenia() == null || film.getCenaWypozyczenia().doubleValue() <= 0) {
            throw new IllegalArgumentException("Cena filmu musi być większa od 0");
        }
        return filmRepository.save(film);
    }

    public void usunFilm(Long id) {
        filmRepository.deleteById(id);
    }
}
