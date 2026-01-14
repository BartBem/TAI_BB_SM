package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * KLASA ENTITY - FILM
 * 
 * Reprezentuje film dostępny do wypożyczenia.
 * Zawiera relacje M:N z gatunkami i aktorami.
 * 
 * WAŻNE - RELACJA @ManyToMany:
 * Film może mieć WIELE gatunków, gatunek może być w WIELU filmach.
 * To wymaga tabeli łączącej (film_gatunek).
 */
@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long filmId;

    @NotBlank(message = "Tytuł jest wymagany")
    @Column(nullable = false)
    private String tytul;

    @Column(columnDefinition = "TEXT")
    private String opis;

    @Column(name = "rok_produkcji")
    private Integer rokProdukcji;

    @Column(name = "czas_trwania_min")
    private Integer czasTrwaniaMin;

    @Column(name = "plakat_url", length = 500)
    private String plakatUrl;

    @NotNull(message = "Cena jest wymagana")
    @Positive(message = "Cena musi być większa od 0")
    @Column(name = "cena_wypozyczenia", nullable = false, precision = 10, scale = 2)
    private BigDecimal cenaWypozyczenia;

    // ============================================
    // RELACJA MANY-TO-MANY z GATUNKAMI
    // ============================================
    // @ManyToMany - wiele filmów do wielu gatunków
    // @JoinTable - definiuje tabelę łączącą
    // joinColumns - klucz obcy do tej tabeli (film)
    // inverseJoinColumns - klucz obcy do drugiej tabeli (gatunek)

    @ManyToMany
    @JoinTable(name = "film_gatunek", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "gatunek_id"))
    private Set<Gatunek> gatunki = new HashSet<>();

    // ============================================
    // RELACJA MANY-TO-MANY z AKTORAMI
    // ============================================

    @ManyToMany
    @JoinTable(name = "film_aktor", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "aktor_id"))
    private Set<Aktor> aktorzy = new HashSet<>();

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Film() {
    }

    public Film(String tytul, BigDecimal cenaWypozyczenia) {
        this.tytul = tytul;
        this.cenaWypozyczenia = cenaWypozyczenia;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Integer getRokProdukcji() {
        return rokProdukcji;
    }

    public void setRokProdukcji(Integer rokProdukcji) {
        this.rokProdukcji = rokProdukcji;
    }

    public Integer getCzasTrwaniaMin() {
        return czasTrwaniaMin;
    }

    public void setCzasTrwaniaMin(Integer czasTrwaniaMin) {
        this.czasTrwaniaMin = czasTrwaniaMin;
    }

    public String getPlakatUrl() {
        return plakatUrl;
    }

    public void setPlakatUrl(String plakatUrl) {
        this.plakatUrl = plakatUrl;
    }

    public BigDecimal getCenaWypozyczenia() {
        return cenaWypozyczenia;
    }

    public void setCenaWypozyczenia(BigDecimal cenaWypozyczenia) {
        this.cenaWypozyczenia = cenaWypozyczenia;
    }

    public Set<Gatunek> getGatunki() {
        return gatunki;
    }

    public void setGatunki(Set<Gatunek> gatunki) {
        this.gatunki = gatunki;
    }

    public Set<Aktor> getAktorzy() {
        return aktorzy;
    }

    public void setAktorzy(Set<Aktor> aktorzy) {
        this.aktorzy = aktorzy;
    }

    // Metody pomocnicze do dodawania gatunków i aktorów
    public void dodajGatunek(Gatunek gatunek) {
        this.gatunki.add(gatunek);
    }

    public void dodajAktora(Aktor aktor) {
        this.aktorzy.add(aktor);
    }
}
