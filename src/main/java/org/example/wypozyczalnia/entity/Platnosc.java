package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * KLASA ENTITY - PŁATNOŚĆ
 * 
 * Reprezentuje płatność za wypożyczenie.
 * 
 * RELACJA 1:1 z WYPOŻYCZENIEM:
 * - Każde wypożyczenie ma dokładnie jedną płatność
 * - UNIQUE na wypozyczenie_id wymusza tę regułę w bazie
 */
@Entity
@Table(name = "platnosc")
public class Platnosc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platnosc_id")
    private Long platnoscId;

    // ============================================
    // RELACJA ONE-TO-ONE z WYPOŻYCZENIEM
    // ============================================
    // @JoinColumn z unique=true wymusza relację 1:1

    @OneToOne
    @JoinColumn(name = "wypozyczenie_id", unique = true, nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Wypozyczenie wypozyczenie;

    @NotNull(message = "Kwota jest wymagana")
    @Positive(message = "Kwota musi być większa od 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal kwota;

    // Metoda: karta, blik, przelew
    @Column(length = 50)
    private String metoda;

    // Statusy: oczekuje, oplacona, odrzucona, zwrot
    @Column(length = 30)
    private String status = "oczekuje";

    // ============================================
    // POLA DLA STRIPE
    // ============================================
    // Te pola będą wypełniane po integracji ze Stripe

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(name = "stripe_session_id")
    private String stripeSessionId;

    @Column(name = "data_utworzenia")
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

    @Column(name = "data_oplacenia")
    private LocalDateTime dataOplacenia;

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Platnosc() {
    }

    public Platnosc(Wypozyczenie wypozyczenie, BigDecimal kwota) {
        this.wypozyczenie = wypozyczenie;
        this.kwota = kwota;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getPlatnoscId() {
        return platnoscId;
    }

    public void setPlatnoscId(Long platnoscId) {
        this.platnoscId = platnoscId;
    }

    public Wypozyczenie getWypozyczenie() {
        return wypozyczenie;
    }

    public void setWypozyczenie(Wypozyczenie wypozyczenie) {
        this.wypozyczenie = wypozyczenie;
    }

    public BigDecimal getKwota() {
        return kwota;
    }

    public void setKwota(BigDecimal kwota) {
        this.kwota = kwota;
    }

    public String getMetoda() {
        return metoda;
    }

    public void setMetoda(String metoda) {
        this.metoda = metoda;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeSessionId() {
        return stripeSessionId;
    }

    public void setStripeSessionId(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }

    public LocalDateTime getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(LocalDateTime dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }

    public LocalDateTime getDataOplacenia() {
        return dataOplacenia;
    }

    public void setDataOplacenia(LocalDateTime dataOplacenia) {
        this.dataOplacenia = dataOplacenia;
    }
}
