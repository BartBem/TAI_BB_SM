-- ============================================
-- SKRYPT SQL - WYPOŻYCZALNIA FILMÓW
-- ============================================
-- Ten skrypt tworzy wszystkie tabele w bazie danych.
-- Spring Boot wykona go automatycznie przy starcie aplikacji.

-- ============================================
-- 1. TABELA UŻYTKOWNIKÓW
-- ============================================
-- Przechowuje dane zarejestrowanych użytkowników
CREATE TABLE IF NOT EXISTS uzytkownik (
    uzytkownik_id SERIAL PRIMARY KEY,           -- Klucz główny, auto-increment
    email VARCHAR(255) UNIQUE NOT NULL,         -- Email (unikalny, wymagany)
    nick VARCHAR(100) UNIQUE NOT NULL,          -- Nick (unikalny, wymagany)
    haslo_hash VARCHAR(255) NOT NULL,           -- Zahashowane hasło (BCrypt)
    imie VARCHAR(100),                          -- Imię (opcjonalne)
    nazwisko VARCHAR(100),                      -- Nazwisko (opcjonalne)
    data_rejestracji TIMESTAMP DEFAULT NOW(),   -- Data rejestracji
    status VARCHAR(20) DEFAULT 'aktywny'        -- Status: aktywny/zablokowany
);

-- ============================================
-- 2. TABELA FILMÓW
-- ============================================
-- Przechowuje informacje o filmach do wypożyczenia
CREATE TABLE IF NOT EXISTS film (
    film_id SERIAL PRIMARY KEY,                 -- Klucz główny
    tytul VARCHAR(255) NOT NULL,                -- Tytuł filmu (wymagany)
    opis TEXT,                                  -- Opis filmu (opcjonalny)
    rok_produkcji INTEGER,                      -- Rok produkcji
    czas_trwania_min INTEGER,                   -- Czas trwania w minutach
    plakat_url VARCHAR(500),                    -- URL do obrazka plakatu
    cena_wypozyczenia DECIMAL(10,2) NOT NULL    -- Cena za wypożyczenie
);

-- ============================================
-- 3. TABELA GATUNKÓW
-- ============================================
-- Przechowuje gatunki filmowe (Akcja, Komedia, Horror, itp.)
CREATE TABLE IF NOT EXISTS gatunek (
    gatunek_id SERIAL PRIMARY KEY,              -- Klucz główny
    nazwa VARCHAR(100) UNIQUE NOT NULL          -- Nazwa gatunku (unikalna)
);

-- ============================================
-- 4. TABELA ŁĄCZĄCA FILM-GATUNEK (M:N)
-- ============================================
-- Jeden film może mieć wiele gatunków,
-- jeden gatunek może być przypisany do wielu filmów
CREATE TABLE IF NOT EXISTS film_gatunek (
    film_id INTEGER NOT NULL,
    gatunek_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, gatunek_id),          -- Klucz złożony
    FOREIGN KEY (film_id) REFERENCES film(film_id) ON DELETE CASCADE,
    FOREIGN KEY (gatunek_id) REFERENCES gatunek(gatunek_id) ON DELETE CASCADE
);

-- ============================================
-- 5. TABELA AKTORÓW
-- ============================================
CREATE TABLE IF NOT EXISTS aktor (
    aktor_id SERIAL PRIMARY KEY,                -- Klucz główny
    imie VARCHAR(100) NOT NULL,                 -- Imię aktora
    nazwisko VARCHAR(100) NOT NULL              -- Nazwisko aktora
);

-- ============================================
-- 6. TABELA ŁĄCZĄCA FILM-AKTOR (M:N)
-- ============================================
-- Jeden film może mieć wielu aktorów,
-- jeden aktor może grać w wielu filmach
CREATE TABLE IF NOT EXISTS film_aktor (
    film_id INTEGER NOT NULL,
    aktor_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, aktor_id),            -- Klucz złożony
    FOREIGN KEY (film_id) REFERENCES film(film_id) ON DELETE CASCADE,
    FOREIGN KEY (aktor_id) REFERENCES aktor(aktor_id) ON DELETE CASCADE
);

-- ============================================
-- 7. TABELA WYPOŻYCZEŃ
-- ============================================
-- Przechowuje historię wypożyczeń użytkowników
CREATE TABLE IF NOT EXISTS wypozyczenie (
    wypozyczenie_id SERIAL PRIMARY KEY,         -- Klucz główny
    uzytkownik_id INTEGER NOT NULL,             -- Kto wypożycza
    film_id INTEGER NOT NULL,                   -- Co wypożycza
    data_utworzenia TIMESTAMP DEFAULT NOW(),    -- Kiedy złożono zamówienie
    data_startu TIMESTAMP,                      -- Od kiedy dostęp do filmu
    data_konca TIMESTAMP,                       -- Do kiedy dostęp do filmu
    status VARCHAR(30) DEFAULT 'oczekuje_oplacenia', -- Status wypożyczenia
    FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(uzytkownik_id),
    FOREIGN KEY (film_id) REFERENCES film(film_id)
);
-- Statusy: oczekuje_oplacenia, aktywne, wygasle, anulowane

-- ============================================
-- 8. TABELA PŁATNOŚCI (1:1 z wypożyczeniem)
-- ============================================
CREATE TABLE IF NOT EXISTS platnosc (
    platnosc_id SERIAL PRIMARY KEY,             -- Klucz główny
    wypozyczenie_id INTEGER UNIQUE NOT NULL,    -- UNIQUE = relacja 1:1
    kwota DECIMAL(10,2) NOT NULL,               -- Kwota do zapłaty
    metoda VARCHAR(50),                         -- Metoda: karta/blik/przelew
    status VARCHAR(30) DEFAULT 'oczekuje',      -- Status płatności
    stripe_payment_intent_id VARCHAR(255),      -- ID transakcji Stripe
    stripe_session_id VARCHAR(255),             -- ID sesji Stripe
    data_utworzenia TIMESTAMP DEFAULT NOW(),    -- Kiedy utworzono płatność
    data_oplacenia TIMESTAMP,                   -- Kiedy opłacono
    FOREIGN KEY (wypozyczenie_id) REFERENCES wypozyczenie(wypozyczenie_id)
);
-- Statusy: oczekuje, oplacona, odrzucona, zwrot

-- ============================================
-- 9. TABELA OCEN
-- ============================================
-- Użytkownicy mogą oceniać filmy (1-5 gwiazdek)
CREATE TABLE IF NOT EXISTS ocena (
    ocena_id SERIAL PRIMARY KEY,                -- Klucz główny
    uzytkownik_id INTEGER NOT NULL,             -- Kto ocenia
    film_id INTEGER NOT NULL,                   -- Co ocenia
    wartosc INTEGER NOT NULL CHECK (wartosc >= 1 AND wartosc <= 5), -- 1-5
    data_oceny TIMESTAMP DEFAULT NOW(),         -- Kiedy oceniono
    UNIQUE (uzytkownik_id, film_id),            -- Jeden użytkownik = jedna ocena na film
    FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(uzytkownik_id),
    FOREIGN KEY (film_id) REFERENCES film(film_id)
);

-- ============================================
-- 10. TABELA KOMENTARZY
-- ============================================
-- Użytkownicy mogą pisać recenzje filmów
CREATE TABLE IF NOT EXISTS komentarz (
    komentarz_id SERIAL PRIMARY KEY,            -- Klucz główny
    uzytkownik_id INTEGER NOT NULL,             -- Kto komentuje
    film_id INTEGER NOT NULL,                   -- Pod jakim filmem
    tresc TEXT NOT NULL,                        -- Treść komentarza
    data_dodania TIMESTAMP DEFAULT NOW(),       -- Kiedy dodano
    FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(uzytkownik_id),
    FOREIGN KEY (film_id) REFERENCES film(film_id)
);
