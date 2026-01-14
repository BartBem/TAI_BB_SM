package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    // List<Film> findByTytul(String tytul);

}
