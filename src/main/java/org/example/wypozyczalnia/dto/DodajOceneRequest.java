package org.example.wypozyczalnia.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DodajOceneRequest {

    @NotNull(message = "ID filmu jest wymagane")
    private Long filmId;

    @NotNull(message = "Ocena jest wymagana")
    @Min(value = 1, message = "Minimalna ocena to 1")
    @Max(value = 5, message = "Maksymalna ocena to 5")
    private Integer wartosc;

    // Gettery i Settery
    public Long getFilmId() {
        return filmId;
    }

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public Integer getWartosc() {
        return wartosc;
    }

    public void setWartosc(Integer wartosc) {
        this.wartosc = wartosc;
    }
}
