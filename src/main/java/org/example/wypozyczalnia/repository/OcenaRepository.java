package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Ocena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OcenaRepository extends JpaRepository<Ocena, Long> {

    // Sprawdź czy użytkownik już ocenił ten film
    Optional<Ocena> findByUzytkownik_UzytkownikIdAndFilm_FilmId(Long uzytkownikId, Long filmId);

    List<Ocena> findByFilm_FilmId(Long filmId);
}
