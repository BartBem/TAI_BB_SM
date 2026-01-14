package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * KLASA ENTITY - KOMENTARZ
 * 
 * Użytkownik może pisać recenzje/komentarze do filmów.
 * Jeden użytkownik może napisać wiele komentarzy do tego samego filmu.
 */
@Entity
@Table(name = "komentarz")
public class Komentarz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "komentarz_id")
    private Long komentarzId;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id", nullable = false)
    private Uzytkownik uzytkownik;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @NotBlank(message = "Treść komentarza jest wymagana")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String tresc;

    @Column(name = "data_dodania")
    private LocalDateTime dataDodania = LocalDateTime.now();

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Komentarz() {
    }

    public Komentarz(Uzytkownik uzytkownik, Film film, String tresc) {
        this.uzytkownik = uzytkownik;
        this.film = film;
        this.tresc = tresc;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getKomentarzId() {
        return komentarzId;
    }

    public void setKomentarzId(Long komentarzId) {
        this.komentarzId = komentarzId;
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

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public LocalDateTime getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(LocalDateTime dataDodania) {
        this.dataDodania = dataDodania;
    }
}
