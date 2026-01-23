package org.example.wypozyczalnia.service;

import org.example.wypozyczalnia.dto.DodajKomentarzRequest;
import org.example.wypozyczalnia.dto.DodajOceneRequest;
import org.example.wypozyczalnia.entity.Film;
import org.example.wypozyczalnia.entity.Komentarz;
import org.example.wypozyczalnia.entity.Ocena;
import org.example.wypozyczalnia.entity.Uzytkownik;
import org.example.wypozyczalnia.repository.FilmRepository;
import org.example.wypozyczalnia.repository.KomentarzRepository;
import org.example.wypozyczalnia.repository.OcenaRepository;
import org.example.wypozyczalnia.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InterakcjeService {

        private final OcenaRepository ocenaRepository;
        private final KomentarzRepository komentarzRepository;
        private final UzytkownikRepository uzytkownikRepository;
        private final FilmRepository filmRepository;

        @Autowired
        public InterakcjeService(OcenaRepository ocenaRepository,
                        KomentarzRepository komentarzRepository,
                        UzytkownikRepository uzytkownikRepository,
                        FilmRepository filmRepository) {
                this.ocenaRepository = ocenaRepository;
                this.komentarzRepository = komentarzRepository;
                this.uzytkownikRepository = uzytkownikRepository;
                this.filmRepository = filmRepository;
        }

        @Transactional
        public Ocena dodajOcene(String email, DodajOceneRequest req) {
                Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

                Film film = filmRepository.findById(req.getFilmId())
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono filmu"));

                // Sprawdź czy już nie ocenił
                Optional<Ocena> istniejaca = ocenaRepository
                                .findByUzytkownik_UzytkownikIdAndFilm_FilmId(uzytkownik.getUzytkownikId(),
                                                film.getFilmId());
                if (istniejaca.isPresent()) {
                        throw new RuntimeException("Już oceniłeś ten film!");
                }

                Ocena ocena = new Ocena();
                ocena.setUzytkownik(uzytkownik);
                ocena.setFilm(film);
                ocena.setWartosc(req.getWartosc());
                ocena.setDataOceny(LocalDateTime.now());

                return ocenaRepository.save(ocena);
        }

        @Transactional
        public Komentarz dodajKomentarz(String email, DodajKomentarzRequest req) {
                Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

                Film film = filmRepository.findById(req.getFilmId())
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono filmu"));

                Komentarz komentarz = new Komentarz();
                komentarz.setUzytkownik(uzytkownik);
                komentarz.setFilm(film);
                komentarz.setTresc(req.getTresc());
                komentarz.setDataDodania(LocalDateTime.now());

                return komentarzRepository.save(komentarz);
        }

        // ============================================
        // NOWA LOGIKA OPINII (OCENA + KOMENTARZ)
        // ============================================

        @Transactional
        public void dodajOpinie(String email, org.example.wypozyczalnia.dto.DodajOpinieRequest req) {
                Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

                Film film = filmRepository.findById(req.getFilmId())
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono filmu"));

                // 1. Dodaj/Zaktualizuj Ocenę
                Optional<Ocena> istniejacaOcena = ocenaRepository
                                .findByUzytkownik_UzytkownikIdAndFilm_FilmId(uzytkownik.getUzytkownikId(),
                                                film.getFilmId());

                Ocena ocena = istniejacaOcena.orElse(new Ocena());
                ocena.setUzytkownik(uzytkownik);
                ocena.setFilm(film);
                ocena.setWartosc(req.getWartosc());
                ocena.setDataOceny(LocalDateTime.now());
                ocenaRepository.save(ocena);

                // 2. Dodaj/Zaktualizuj Komentarz
                List<Komentarz> istniejaceKomentarze = komentarzRepository
                                .findByUzytkownik_UzytkownikIdAndFilm_FilmId(uzytkownik.getUzytkownikId(),
                                                film.getFilmId());

                Komentarz komentarz;
                if (!istniejaceKomentarze.isEmpty()) {
                        komentarz = istniejaceKomentarze.get(0);
                        // Usuń duplikaty jeśli istnieją (sprzątanie po starym kodzie)
                        if (istniejaceKomentarze.size() > 1) {
                                for (int i = 1; i < istniejaceKomentarze.size(); i++) {
                                        komentarzRepository.delete(istniejaceKomentarze.get(i));
                                }
                        }
                } else {
                        komentarz = new Komentarz();
                }

                komentarz.setUzytkownik(uzytkownik);
                komentarz.setFilm(film);
                komentarz.setTresc(req.getTresc());
                komentarz.setDataDodania(LocalDateTime.now());
                komentarzRepository.save(komentarz);
        }

        @Transactional
        public void usunOpinie(String email, Long filmId) {
                Uzytkownik uzytkownik = uzytkownikRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

                // Usun ocene
                ocenaRepository.findByUzytkownik_UzytkownikIdAndFilm_FilmId(uzytkownik.getUzytkownikId(), filmId)
                                .ifPresent(ocenaRepository::delete);

                // Usun komentarz(e)
                List<Komentarz> komentarze = komentarzRepository
                                .findByUzytkownik_UzytkownikIdAndFilm_FilmId(uzytkownik.getUzytkownikId(), filmId);
                komentarzRepository.deleteAll(komentarze);
        }

        public List<org.example.wypozyczalnia.dto.OpiniaResponse> pobierzOpinieDoFilmu(Long filmId) {
                // 1. Pobierz wszystkie komentarze (posortowane)
                List<Komentarz> komentarze = komentarzRepository.findByFilm_FilmIdOrderByDataDodaniaDesc(filmId);

                // 2. Pobierz wszystkie oceny do tego filmu
                List<Ocena> oceny = ocenaRepository.findByFilm_FilmId(filmId);

                // 3. Połącz w DTO
                return komentarze.stream().map(k -> {
                        Integer ocenaWartosc = oceny.stream()
                                        .filter(o -> o.getUzytkownik().getUzytkownikId()
                                                        .equals(k.getUzytkownik().getUzytkownikId()))
                                        .findFirst()
                                        .map(Ocena::getWartosc)
                                        .orElse(0); // 0 jeśli brak oceny (ale powinno być, bo wymuszamy razem)

                        return new org.example.wypozyczalnia.dto.OpiniaResponse(
                                        k.getUzytkownik().getNick(),
                                        ocenaWartosc,
                                        k.getTresc(),
                                        k.getDataDodania());
                }).toList();
        }

        // STARE METODY (Można zostawić dla kompatybilności lub usunąć, ale w
        // kontrolerze są używane stare endpointy,
        // więc zostawiam je, choć frontend przejdzie na nowe).
        public List<Komentarz> pobierzKomentarzeDoFilmu(Long filmId) {
                return komentarzRepository.findByFilm_FilmIdOrderByDataDodaniaDesc(filmId);
        }
}
