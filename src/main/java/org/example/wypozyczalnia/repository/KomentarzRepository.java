package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Komentarz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KomentarzRepository extends JpaRepository<Komentarz, Long> {

    // Wszystkie komentarze do danego filmu (np. do wyświetlenia na stronie filmu)
    List<Komentarz> findByFilm_FilmId(Long filmId);

    // Wszystkie komentarze danego użytkownika
    List<Komentarz> findByUzytkownik_UzytkownikId(Long uzytkownikId);
}
