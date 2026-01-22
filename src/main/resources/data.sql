-- ============================================
-- DANE TESTOWE - WYPOŻYCZALNIA FILMÓW
-- ============================================

-- 1. UŻYTKOWNICY (Hasło "admin123" zahashowane bcryptem - przykładowy hash, tu używamy plain text dla uproszczenia w testach lub hash jeśli BCrypt działa)
-- Uwaga: W prawdziwej aplikacji hasła muszą być zahashowane. Tutaj wpisujemy "na sztywno" hashe lub dummy wartości.
-- Przyjmijmy testowy hash dla "test": $2a$10$NotRealHashButExample... (użyjemy placeholders)
-- Dla uproszczenia testów API bez logowania SecurityConfig wyłączyliśmy, więc hasło nie jest sprawdzane.

INSERT INTO uzytkownik (email, nick, haslo_hash, imie, nazwisko, status) VALUES
('jan.kowalski@example.com', 'jankowal', '$2a$10$test', 'Jan', 'Kowalski', 'aktywny'),
('anna.nowak@example.com', 'annan', '$2a$10$test', 'Anna', 'Nowak', 'aktywny'),
('tom.hanks@example.com', 'tomh', '$2a$10$test', 'Tom', 'Hanks', 'aktywny'),
('admin@wypozyczalnia.pl', 'admin', '$2a$10$test', 'Admin', 'System', 'aktywny'),
('zablokowany@user.pl', 'badguy', '$2a$10$test', 'Zły', 'Użytkownik', 'zablokowany')
ON CONFLICT DO NOTHING;

-- 2. GATUNKI
INSERT INTO gatunek (nazwa) VALUES
('Akcja'),
('Komedia'),
('Dramat'),
('Sci-Fi'),
('Horror')
ON CONFLICT DO NOTHING;

-- 3. AKTORZY
INSERT INTO aktor (imie, nazwisko) VALUES
('Leonardo', 'DiCaprio'),
('Brad', 'Pitt'),
('Margot', 'Robbie'),
('Robert', 'De Niro'),
('Morgan', 'Freeman')
ON CONFLICT DO NOTHING;

-- 4. FILMY
INSERT INTO film (tytul, opis, rok_produkcji, czas_trwania_min, cena_wypozyczenia, plakat_url) VALUES
('Incepcja', 'Czasy, gdy technologia pozwala na wchodzenie w sny innych ludzi.', 2010, 148, 15.00, 'https://example.com/incepcja.jpg'),
('Wilk z Wall Street', 'Historia brokera, który żyje jak król dzięki oszustwom.', 2013, 180, 14.50, 'https://example.com/wilk.jpg'),
('Pewnego razu... w Hollywood', 'Aktor i jego dubler próbują odnaleźć się w Hollywood lat 60.', 2019, 161, 16.00, 'https://example.com/hollywood.jpg'),
('Siedem', 'Dwóch detektywów tropi seryjnego mordercę.', 1995, 127, 10.00, 'https://example.com/siedem.jpg'),
('Skazani na Shawshank', 'Bankier niesłusznie skazany na dożywocie.', 1994, 142, 12.00, 'https://example.com/shawshank.jpg')
ON CONFLICT DO NOTHING;

-- 5. RELACJE FILM-GATUNEK
-- Incepcja (1) -> Akcja(1), Sci-Fi(4)
INSERT INTO film_gatunek (film_id, gatunek_id) VALUES
(1, 1), (1, 4),
(2, 2), (2, 3), -- Wilk -> Komedia, Dramat
(3, 2), (3, 3), -- Pewnego razu -> Komedia, Dramat
(4, 3), (4, 1), -- Siedem -> Dramat, Akcja (Thriller)
(5, 3)          -- Shawshank -> Dramat
ON CONFLICT DO NOTHING;

-- 6. RELACJE FILM-AKTOR
INSERT INTO film_aktor (film_id, aktor_id) VALUES
(1, 1), -- Incepcja - Leo
(2, 1), (2, 3), -- Wilk - Leo, Margot
(3, 1), (3, 2), (3, 3), -- Pewnego razu - Leo, Brad, Margot
(4, 2), (4, 5), -- Siedem - Brad, Morgan
(5, 5) -- Shawshank - Morgan
ON CONFLICT DO NOTHING;

-- 7. WYPOŻYCZENIA
-- Jan (1) wypożycza Incepcję (1)
INSERT INTO wypozyczenie (uzytkownik_id, film_id, data_startu, data_konca, status) VALUES
(1, 1, NOW(), NOW() + INTERVAL '2 days', 'aktywne'),
(1, 2, NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days', 'wygasle'),
(2, 3, NOW(), NOW() + INTERVAL '2 days', 'oczekuje_oplacenia'),
(3, 4, NOW(), NOW() + INTERVAL '2 days', 'aktywne'),
(2, 5, NOW(), NOW() + INTERVAL '2 days', 'anulowane')
ON CONFLICT DO NOTHING;

-- 8. PŁATNOŚCI
-- Płatność dla wypożyczenia 1 (aktywne, opłacone)
INSERT INTO platnosc (wypozyczenie_id, kwota, metoda, status, data_oplacenia) VALUES
(1, 15.00, 'karta', 'oplacona', NOW()),
(2, 14.50, 'blik', 'oplacona', NOW() - INTERVAL '5 days'),
(3, 16.00, 'przelew', 'oczekuje', NULL),
(4, 10.00, 'karta', 'oplacona', NOW()),
(5, 12.00, NULL, 'odrzucona', NULL)
ON CONFLICT DO NOTHING;

-- 9. OCENY
INSERT INTO ocena (uzytkownik_id, film_id, wartosc) VALUES
(1, 1, 5),
(1, 2, 4),
(2, 3, 5),
(3, 4, 5),
(2, 5, 5)
ON CONFLICT DO NOTHING;

-- 10. KOMENTARZE
INSERT INTO komentarz (uzytkownik_id, film_id, tresc) VALUES
(1, 1, 'Niesamowity film, polecam każdemu!'),
(1, 2, 'Trochę za długi, ale świetna gra aktorska.'),
(2, 3, 'Tarantino w formie!'),
(3, 4, 'Klasyk kina, trzeba zobaczyć.'),
(2, 5, 'Najlepszy film w historii.')
ON CONFLICT DO NOTHING;
