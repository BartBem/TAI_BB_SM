package org.example.wypozyczalnia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * GLOBALNY OBSŁUGIWACZ BŁĘDÓW (ControllerAdvice)
 * 
 * Przechwytuje wyjątki z całej aplikacji i zamienia je na ładne odpowiedzi
 * HTTP.
 */
@ControllerAdvice
public class GlobalnyObslugiwaczBledow {

    // 1. Obsługa błędów walidacji (@Valid - np. za krótkie hasło)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> obsluzBledyWalidacji(MethodArgumentNotValidException ex) {
        Map<String, String> bledy = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String pole = ((FieldError) error).getField();
            String komunikat = error.getDefaultMessage();
            bledy.put(pole, komunikat);
        });

        return new ResponseEntity<>(bledy, HttpStatus.BAD_REQUEST);
    }

    // 2. Obsługa ogólnych wyjątków biznesowych (np. "Email zajęty")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> obsluzWyjatkiBiznesowe(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
