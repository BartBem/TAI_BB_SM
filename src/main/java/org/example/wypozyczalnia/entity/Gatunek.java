package org.example.wypozyczalnia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * KLASA ENTITY - GATUNEK
 * 
 * Reprezentuje gatunek filmowy (Akcja, Komedia, Horror, itp.)
 * Prosty obiekt - tylko ID i nazwa.
 */
@Entity
@Table(name = "gatunek")
public class Gatunek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gatunek_id")
    private Long gatunekId;

    @NotBlank(message = "Nazwa gatunku jest wymagana")
    @Column(unique = true, nullable = false, length = 100)
    private String nazwa;

    // ============================================
    // KONSTRUKTORY
    // ============================================

    public Gatunek() {
    }

    public Gatunek(String nazwa) {
        this.nazwa = nazwa;
    }

    // ============================================
    // GETTERY I SETTERY
    // ============================================

    public Long getGatunekId() {
        return gatunekId;
    }

    public void setGatunekId(Long gatunekId) {
        this.gatunekId = gatunekId;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}
