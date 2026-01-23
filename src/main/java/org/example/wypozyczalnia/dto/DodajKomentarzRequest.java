package org.example.wypozyczalnia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DodajKomentarzRequest {

    @NotNull(message = "ID filmu jest wymagane")
    private Long filmId;

    @NotBlank(message = "Treść komentarza nie może być pusta")
    private String tresc;

    // Gettery i Settery
    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }
}
