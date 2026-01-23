package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOZYTORIUM - FilmRepository
 * 
 * To jest interfejs (nie klasa!) który rozszerza JpaRepository.
 * Spring automatycznie tworzy implementację za Ciebie.
 * 
 * JpaRepository<Film, Long> oznacza:
 * - Film = typ encji (tabela)
 * - Long = typ klucza głównego (film_id)
 * 
 * METODY KTÓRE DOSTAJESZ ZA DARMO:
 * - save(film) → zapisz film do bazy
 * - findById(id) → znajdź film po ID
 * - findAll() → pobierz wszystkie filmy
 * - deleteById(id) → usuń film po ID
 * - count() → policz ile filmów jest w bazie
 * - existsById(id) → sprawdź czy film istnieje
 */
@Repository // Oznacza, że to repozytorium (warstwa danych)
public interface FilmRepository extends JpaRepository<Film, Long> {

    // To wszystko! Reszta działa automatycznie.
    // Możesz dodać własne metody, np:
    // Znajdź filmy, które mają gatunek o podanej nazwie (IgnoreCase = bez względu
    // na wielkość liter)
    // Np. findByGatunki_NazwaIgnoreCase("komedia") znajdzie filmy z gatunkiem
    // "Komedia"
    List<Film> findByGatunki_NazwaIgnoreCase(String nazwa);

    // Znajdź filmy z oceną większą lub równą minOcena
    @org.springframework.data.jpa.repository.Query("SELECT f FROM Film f JOIN f.oceny o GROUP BY f HAVING AVG(o.wartosc) >= :minOcena")
    List<Film> findFilmyZMinSredniaOcena(Double minOcena);

    /**
     * Zaawansowane wyszukiwanie z wieloma filtrami.
     * Używamy COALESCE i NULL checków, aby filtry były opcjonalne.
     * DISTINCT jest potrzebny, bo JOIN z gatunkami może dublować wyniki.
     */
    @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT f.* FROM film f " +
            "LEFT JOIN film_gatunek fg ON f.film_id = fg.film_id " +
            "LEFT JOIN gatunek g ON fg.gatunek_id = g.gatunek_id " +
            "LEFT JOIN ocena o ON f.film_id = o.film_id " +
            "WHERE " +
            "(CAST(:tytul AS TEXT) IS NULL OR LOWER(f.tytul) LIKE LOWER('%' || CAST(:tytul AS TEXT) || '%')) AND " +
            "(CAST(:gatunek AS TEXT) IS NULL OR LOWER(g.nazwa) LIKE LOWER('%' || CAST(:gatunek AS TEXT) || '%')) AND " +
            "(CAST(:rok AS INTEGER) IS NULL OR f.rok_produkcji = CAST(:rok AS INTEGER)) AND " +
            "(CAST(:cenaMax AS DECIMAL) IS NULL OR f.cena_wypozyczenia <= CAST(:cenaMax AS DECIMAL)) " +
            "GROUP BY f.film_id " +
            "HAVING (CAST(:minOcena AS DOUBLE PRECISION) IS NULL OR AVG(o.wartosc) >= CAST(:minOcena AS DOUBLE PRECISION))", nativeQuery = true)
    List<Film> szukajFilmow(
            @org.springframework.data.repository.query.Param("tytul") String tytul,
            @org.springframework.data.repository.query.Param("gatunek") String gatunek,
            @org.springframework.data.repository.query.Param("rok") Integer rok,
            @org.springframework.data.repository.query.Param("cenaMax") java.math.BigDecimal cenaMax,
            @org.springframework.data.repository.query.Param("minOcena") Double minOcena);
}
