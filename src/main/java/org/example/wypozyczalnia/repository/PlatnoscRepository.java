package org.example.wypozyczalnia.repository;

import org.example.wypozyczalnia.entity.Platnosc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlatnoscRepository extends JpaRepository<Platnosc, Long> {

    // Znajdź płatność po ID wypożyczenia
    Optional<Platnosc> findByWypozyczenie_WypozyczenieId(Long wypozyczenieId);

    // Znajdź po ID sesji Stripe (przydatne przy webhookach)
    Optional<Platnosc> findByStripeSessionId(String stripeSessionId);
}
