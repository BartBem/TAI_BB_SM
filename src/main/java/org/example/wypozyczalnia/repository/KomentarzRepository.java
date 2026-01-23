package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Komentarz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KomentarzRepository extends JpaRepository<Komentarz, Long> {

    // Pobierz komentarze do filmu (posortowane od najnowszych)
    List<Komentarz> findByFilm_FilmIdOrderByDataDodaniaDesc(Long filmId);

    List<Komentarz> findByUzytkownik_UzytkownikIdAndFilm_FilmId(Long uzytkownikId, Long filmId);
}
