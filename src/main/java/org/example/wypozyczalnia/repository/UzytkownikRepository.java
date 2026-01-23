package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Long> {

    // Metody do logowania i rejestracji
    Optional<Uzytkownik> findByEmail(String email);

    Optional<Uzytkownik> findByNick(String nick);

    // Sprawdzanie czy użytkownik istnieje (do walidacji rejestracji)
    boolean existsByEmail(String email);

    boolean existsByNick(String nick);
}
