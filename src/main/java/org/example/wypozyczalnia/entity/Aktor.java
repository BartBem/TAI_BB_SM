package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * KLASA ENTITY - AKTOR
 * 
 * Reprezentuje aktora grającego w filmach.
 */
@Entity
@Table(name = "aktor")
public class Aktor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aktor_id")
    private Long aktorId;

    @NotBlank(message = "Imię aktora jest wymagane")
    @Column(nullable = false, length = 100)
    private String imie;

    @NotBlank(message = "Nazwisko aktora jest wymagane")
    @Column(nullable = false, length = 100)
    private String nazwisko;

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Aktor() {
    }

    public Aktor(String imie, String nazwisko) {
        this.imie = imie;
        this.nazwisko = nazwisko;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getAktorId() {
        return aktorId;
    }

    public void setAktorId(Long aktorId) {
        this.aktorId = aktorId;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    // Metoda pomocnicza - pełne imię i nazwisko
    public String getPelneNazwisko() {
        return imie + " " + nazwisko;
    }
}
