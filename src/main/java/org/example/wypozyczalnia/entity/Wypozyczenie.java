package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/** nie
 
 * KLASA ENTITY - WYPOŻYCZENIE
 * 
 * Reprezentuje pojedyncze wypożyczenie filmu przez użytkownika.
 * 
 * RELACJE:
 * - @ManyToOne z Uzytkownik (jeden użytkownik może mieć wiele wypożyczeń)
 * - @ManyToOne z Film (jeden film może być wypożyczony wiele razy)
 * - @OneToOne z Platnosc (jedno wypożyczenie = jedna płatność)
 */
@Entity
@Table(name = "wypozyczenie")
public class Wypozyczenie {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wypozyczenie_id")
    private Long wypozyczenieId;

    // ============================================
    // RELACJA MANY-TO-ONE z UŻYTKOWNIKIEM
    // ============================================
    // Wiele wypożyczeń może należeć do jednego użytkownika
    // @JoinColumn - wskazuje kolumnę z kluczem obcym

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id", nullable = false)
    private Uzytkownik uzytkownik;

    // ============================================
    // RELACJA MANY-TO-ONE z FILMEM
    // ============================================
    // Ten sam film może być wypożyczony wiele razy

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Column(name = "data_utworzenia")
    private LocalDateTime dataUtworzenia = LocalDateTime.now();

    @Column(name = "data_startu")
    private LocalDateTime dataStartu;

    @Column(name = "data_konca")
    private LocalDateTime dataKonca;

    // Statusy: oczekuje_oplacenia, aktywne, wygasle, anulowane
    @Column(length = 30)
    private String status = "oczekuje_oplacenia";

    // ============================================
    // RELACJA ONE-TO-ONE z PŁATNOŚCIĄ
    // ============================================
    // mappedBy = pole "wypozyczenie" w klasie Platnosc
    // cascade = operacje na wypożyczeniu przenoszą się na płatność

    @OneToOne(mappedBy = "wypozyczenie", cascade = CascadeType.ALL)
    private Platnosc platnosc;

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Wypozyczenie() {
    }

    public Wypozyczenie(Uzytkownik uzytkownik, Film film) {
        this.uzytkownik = uzytkownik;
        this.film = film;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getWypozyczenieId() {
        return wypozyczenieId;
    }

    public void setWypozyczenieId(Long wypozyczenieId) {
        this.wypozyczenieId = wypozyczenieId;
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

    public LocalDateTime getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(LocalDateTime dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }

    public LocalDateTime getDataStartu() {
        return dataStartu;
    }

    public void setDataStartu(LocalDateTime dataStartu) {
        this.dataStartu = dataStartu;
    }

    public LocalDateTime getDataKonca() {
        return dataKonca;
    }

    public void setDataKonca(LocalDateTime dataKonca) {
        this.dataKonca = dataKonca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Platnosc getPlatnosc() {
        return platnosc;
    }

    public void setPlatnosc(Platnosc platnosc) {
        this.platnosc = platnosc;
    }
}
