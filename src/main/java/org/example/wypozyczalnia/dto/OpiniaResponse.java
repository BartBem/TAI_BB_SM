package org.example.wypozyczalnia.dto;

import java.time.LocalDateTime;

public class OpiniaResponse {
    private String nick;
    private Integer ocena;
    private String tresc;
    private LocalDateTime data;

    public OpiniaResponse(String nick, Integer ocena, String tresc, LocalDateTime data) {
        this.nick = nick;
        this.ocena = ocena;
        this.tresc = tresc;
        this.data = data;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
