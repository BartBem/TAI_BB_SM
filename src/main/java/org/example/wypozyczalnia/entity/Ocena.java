package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**siema1
 * KLASA ENTITY - OCENA
 * 
 * Użytkownik może ocenić film (1-5 gwiazdek).
 * Jeden użytkownik może dać tylko jedną ocenę na film (UNIQUE constraint).
 */
@Entity
@Table(name = "ocena", uniqueConstraints = @UniqueConstraint(columnNames = { "uzytkownik_id", "film_id" }))
public class Ocena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ocena_id")
    private Long ocenaId;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id", nullable = false)
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    // Ocena od 1 do 5
    @NotNull(message = "Ocena jest wymagana")
    @Min(value = 1, message = "Minimalna ocena to 1")
    @Max(value = 5, message = "Maksymalna ocena to 5")
    @Column(nullable = false)
    private Integer wartosc;

    @Column(name = "data_oceny")
    private LocalDateTime dataOceny = LocalDateTime.now();

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Ocena() {
    }

    public Ocena(Uzytkownik uzytkownik, Film film, Integer wartosc) {
        this.uzytkownik = uzytkownik;
        this.film = film;
        this.wartosc = wartosc;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getOcenaId() {
        return ocenaId;
    }

    public void setOcenaId(Long ocenaId) {
        this.ocenaId = ocenaId;
    }

    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Integer getWartosc() {
        return wartosc;
    }

    public void setWartosc(Integer wartosc) {
        this.wartosc = wartosc;
    }

    public LocalDateTime getDataOceny() {
        return dataOceny;
    }

    public void setDataOceny(LocalDateTime dataOceny) {
        this.dataOceny = dataOceny;
    }
}
