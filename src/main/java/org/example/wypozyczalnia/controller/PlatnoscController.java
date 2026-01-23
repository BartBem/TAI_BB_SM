package org.example.wypozyczalnia.controller;

import org.example.wypozyczalnia.service.PlatnoscService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/platnosci")
public class PlatnoscController {

    private final PlatnoscService platnoscService;

    @Autowired
    public PlatnoscController(PlatnoscService platnoscService) {
        this.platnoscService = platnoscService;
    }

    // ============================================
    // POST /api/platnosci/zaplac?wypozyczenieId=1
    // Generuje link do płatności Stripe
    // ============================================
    @PostMapping("/zaplac")
    public String zaplac(@RequestParam Long wypozyczenieId) {
        try {
            return platnoscService.utworzLinkPlatnosci(wypozyczenieId);
        } catch (Exception e) {
            throw new RuntimeException("Błąd tworzenia płatności: " + e.getMessage());
        }
    }

    // ============================================
    // GET /api/platnosci/sukces
    // Powrót ze Stripe po udanej płatności
    // ============================================
    // ============================================
    // GET /api/platnosci/sukces
    // Powrót ze Stripe po udanej płatności
    // ============================================
    @GetMapping("/sukces")
    public void sukces(@RequestParam("session_id") String sessionId, HttpServletResponse response) throws IOException {
        try {
            platnoscService.potwierdzPlatnosc(sessionId);
            // Przekierowanie na Frontend (port 5173) na stronę Moje Wypożyczenia
            response.sendRedirect("http://localhost:5173/moje-wypozyczenia?status=sukces");
        } catch (Exception e) {
            response.sendRedirect("http://localhost:5173/?blad=" + e.getMessage());
        }
    }

    // ============================================
    // GET /api/platnosci/anulowano
    // Powrót po anulowaniu
    // ============================================
    @GetMapping("/anulowano")
    public void anulowano(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:5173/?status=anulowano");
    }
}
