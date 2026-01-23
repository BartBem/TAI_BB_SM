package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Aktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AktorRepository extends JpaRepository<Aktor, Long> {

    List<Aktor> findByNazwisko(String nazwisko);

    List<Aktor> findByImieAndNazwisko(String imie, String nazwisko);
}
