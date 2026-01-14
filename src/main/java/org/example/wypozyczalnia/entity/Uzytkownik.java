package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * KLASA ENTITY - UŻYTKOWNIK
 * 
 * Ta klasa reprezentuje tabelę "uzytkownik" w bazie danych.
 * Każdy obiekt tej klasy = jeden wiersz w tabeli.
 * 
 * WAŻNE ADNOTACJE:
 * 
 * @Entity - mówi Spring Boot że to tabela w bazie
 * @Table - określa nazwę tabeli (jeśli inna niż nazwa klasy)
 * @Id - oznacza klucz główny
 * @GeneratedValue - automatyczne numerowanie (IDENTITY = SERIAL w PostgreSQL)
 */
@Entity
@Table(name = "uzytkownik")
public class Uzytkownik {

    // ============================================
    // POLA (odpowiadają kolumnom w tabeli)
    // ============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uzytkownik_id")
    private Long uzytkownikId;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Nieprawidłowy format email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Nick jest wymagany")
    @Size(min = 3, max = 100, message = "Nick musi mieć 3-100 znaków")
    @Column(unique = true, nullable = false)
    private String nick;

    @NotBlank(message = "Hasło jest wymagane")
    @Column(name = "haslo_hash", nullable = false)
    private String hasloHash;

    @Column(length = 100)
    private String imie;

    @Column(length = 100)
    private String nazwisko;

    @Column(name = "data_rejestracji")
    private LocalDateTime dataRejestracji = LocalDateTime.now();

    @Column(length = 20)
    private String status = "aktywny";

    // ============================================
    // KONSTRUKTORY
    // ============================================

    // Pusty konstruktor - wymagany przez JPA
    public Uzytkownik() {
    }

    // Konstruktor z podstawowymi danymi
    public Uzytkownik(String email, String nick, String hasloHash) {
        this.email = email;
        this.nick = nick;
        this.hasloHash = hasloHash;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================
    // Gettery "pobierają" wartość pola
    // Settery "ustawiają" wartość pola

    public Long getUzytkownikId() {
        return uzytkownikId;
    }

    public void setUzytkownikId(Long uzytkownikId) {
        this.uzytkownikId = uzytkownikId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHasloHash() {
        return hasloHash;
    }

    public void setHasloHash(String hasloHash) {
        this.hasloHash = hasloHash;
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

    public LocalDateTime getDataRejestracji() {
        return dataRejestracji;
    }

    public void setDataRejestracji(LocalDateTime dataRejestracji) {
        this.dataRejestracji = dataRejestracji;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
