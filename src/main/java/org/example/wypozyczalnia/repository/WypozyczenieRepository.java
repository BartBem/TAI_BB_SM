package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Wypozyczenie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WypozyczenieRepository extends JpaRepository<Wypozyczenie, Long> {

    // Historia wypożyczeń danego użytkownika
    List<Wypozyczenie> findByUzytkownik_UzytkownikId(Long uzytkownikId);

    // Wypożyczenia danego filmu
    List<Wypozyczenie> findByFilm_FilmId(Long filmId);

    // Wypożyczenia o konkretnym statusie (np. aktywne)
    List<Wypozyczenie> findByStatus(String status);

    // Sprawdź czy użytkownik ma już ten film (aktywny lub oczekujący)
    boolean existsByUzytkownik_UzytkownikIdAndFilm_FilmIdAndStatusIn(Long uzytkownikId, Long filmId,
            List<String> statusy);
}
