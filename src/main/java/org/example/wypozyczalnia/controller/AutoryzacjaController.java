package org.example.wypozyczalnia.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.wypozyczalnia.dto.DaneLogowania;
import org.example.wypozyczalnia.dto.DaneRejestracji;
import org.example.wypozyczalnia.entity.Uzytkownik;
import org.example.wypozyczalnia.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

/**
 * KONTROLER - AutoryzacjaController (Dawniej AuthController)
 * 
 * Obsługuje rejestrację, logowanie i sprawdzanie statusu (po polsku).
 */
@RestController
@RequestMapping("/api/autoryzacja") // Zmieniono z /api/auth
public class AutoryzacjaController {

    private final AuthenticationManager authenticationManager;
    private final UzytkownikRepository uzytkownikRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AutoryzacjaController(AuthenticationManager authenticationManager,
            UzytkownikRepository uzytkownikRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.uzytkownikRepository = uzytkownikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================
    // POST /api/autoryzacja/rejestracja
    // Rejestracja nowego użytkownika
    // ============================================
    @PostMapping("/rejestracja")
    public org.springframework.http.ResponseEntity<?> zarejestruj(
            @jakarta.validation.Valid @RequestBody DaneRejestracji req) {
        if (uzytkownikRepository.findByEmail(req.getEmail()).isPresent()) {
            return org.springframework.http.ResponseEntity.badRequest().body("Email zajęty!");
        }
        if (uzytkownikRepository.findByNick(req.getNick()).isPresent()) {
            return org.springframework.http.ResponseEntity.badRequest().body("Nick zajęty!");
        }

        Uzytkownik user = new Uzytkownik();
        user.setEmail(req.getEmail());
        user.setNick(req.getNick());
        user.setImie(req.getImie());
        user.setNazwisko(req.getNazwisko());
        user.setHasloHash(passwordEncoder.encode(req.getHaslo()));

        uzytkownikRepository.save(user);

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "Rejestracja udana! Możesz się zalogować.");
        return org.springframework.http.ResponseEntity.ok(response);
    }

    // ============================================
    // POST /api/autoryzacja/logowanie
    // Logowanie (tworzy sesję JSESSIONID)
    // ============================================
    @PostMapping("/logowanie") // Zmieniono z /login
    public String zaloguj(@RequestBody DaneLogowania req, HttpServletRequest request, HttpServletResponse response) {
        // 1. Uwierzytelnij (sprawdź login i hasło)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getHaslo()));

        // 2. Jeśli OK, utwórz Context Security
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // 3. Zapisz Context w sesji (to kluczowe dla Session-Based Auth!)
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return "Zalogowano pomyślnie! Twoje ID sesji to: " + session.getId();
    }

    // ============================================
    // GET /api/autoryzacja/ja
    // Zwraca dane zalogowanego użytkownika (dla frontendu)
    // ============================================
    @GetMapping("/ja")
    public org.springframework.http.ResponseEntity<?> sprawdzStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            return org.springframework.http.ResponseEntity.status(401).body("Niezalogowany");
        }

        // Pobierz użytkownika z bazy
        java.util.Optional<Uzytkownik> userOpt = uzytkownikRepository.findByEmail(auth.getName());

        if (userOpt.isPresent()) {
            Uzytkownik u = userOpt.get();
            // Zwracamy tylko potrzebne dane (bez hasła!)
            java.util.Map<String, Object> dane = new java.util.HashMap<>();
            dane.put("uzytkownikId", u.getUzytkownikId());
            dane.put("email", u.getEmail());
            dane.put("nick", u.getNick());
            dane.put("imie", u.getImie());
            dane.put("nazwisko", u.getNazwisko());
            return org.springframework.http.ResponseEntity.ok(dane);
        } else {
            return org.springframework.http.ResponseEntity.status(404).body("Użytkownik nie znaleziony");
        }
    }
}
