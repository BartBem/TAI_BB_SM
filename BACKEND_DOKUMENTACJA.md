# 📚 Dokumentacja Backendu - Wypożyczalnia Filmów

> **Cel tego pliku:** Wyjaśnić krok po kroku jak działa backend aplikacji.
> Każda sekcja opisuje CO robi dany element i PO CO go potrzebujemy.

---

## 🏗️ Architektura Aplikacji

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (React)                         │
│                   localhost:5173                            │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP (REST API)
┌─────────────────────────▼───────────────────────────────────┐
│                    BACKEND (Spring Boot)                    │
│                    localhost:8080                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │ Kontrolery  │──│  Serwisy    │──│ Repozytoria │         │
│  │ (REST API)  │  │ (Logika)    │  │ (Baza)      │         │
│  └─────────────┘  └─────────────┘  └──────┬──────┘         │
└────────────────────────────────────────────┼────────────────┘
                                             │ SQL
┌────────────────────────────────────────────▼────────────────┐
│                    BAZA DANYCH (PostgreSQL)                 │
│                    localhost:5432                           │
└─────────────────────────────────────────────────────────────┘
```

**Przepływ danych:**
1. Użytkownik klika przycisk na stronie (Frontend)
2. Frontend wysyła żądanie HTTP do Kontrolera
3. Kontroler wywołuje Serwis (logika biznesowa)
4. Serwis używa Repozytorium do operacji na bazie
5. Dane wracają tą samą drogą do użytkownika

---

## 📁 Struktura Plików Backendu

```
src/main/java/org/example/wypozyczalnia/
├── WypozyczalniaApplication.java    # Punkt startowy aplikacji
├── config/
│   └── KonfiguracjaBezpieczenstwa.java  # Ustawienia bezpieczeństwa
├── controller/                      # Endpointy REST API
│   ├── AutoryzacjaController.java   # Logowanie, rejestracja
│   ├── FilmController.java          # Operacje na filmach
│   ├── InterakcjeController.java    # Oceny, komentarze
│   ├── PlatnoscController.java      # Płatności Stripe
│   └── WypozyczenieController.java  # Wypożyczenia
├── dto/                             # Obiekty transferu danych
│   ├── DaneLogowania.java           # Dane formularza logowania
│   ├── DaneRejestracji.java         # Dane formularza rejestracji
│   ├── DodajKomentarzRequest.java   # Żądanie dodania komentarza
│   ├── DodajOceneRequest.java       # Żądanie dodania oceny
│   ├── DodajOpinieRequest.java      # Ocena + komentarz razem
│   └── OpiniaResponse.java          # Odpowiedź z opinią
├── entity/                          # Klasy reprezentujące tabele
│   ├── Uzytkownik.java              # Tabela: uzytkownik
│   ├── Film.java                    # Tabela: film
│   ├── Gatunek.java                 # Tabela: gatunek
│   ├── Aktor.java                   # Tabela: aktor
│   ├── Wypozyczenie.java            # Tabela: wypozyczenie
│   ├── Platnosc.java                # Tabela: platnosc
│   ├── Ocena.java                   # Tabela: ocena
│   └── Komentarz.java               # Tabela: komentarz
├── repository/                      # Interfejsy dostępu do bazy
│   ├── UzytkownikRepository.java
│   ├── FilmRepository.java
│   ├── GatunekRepository.java
│   ├── AktorRepository.java
│   ├── WypozyczenieRepository.java
│   ├── PlatnoscRepository.java
│   ├── OcenaRepository.java
│   └── KomentarzRepository.java
├── service/                         # Logika biznesowa
│   ├── FilmService.java             # Wyszukiwanie filmów
│   ├── WypozyczenieService.java     # Proces wypożyczania
│   ├── PlatnoscService.java         # Integracja Stripe
│   └── InterakcjeService.java       # Oceny i komentarze
├── security/
│   └── SerwisAutoryzacji.java       # Weryfikacja użytkowników
└── exception/
    └── GlobalnyObslugiwaczBledow.java  # Obsługa błędów
```

---

## 🧩 Encje (Entity) - Tabele w Bazie Danych

Encje to klasy Java, które reprezentują tabele w bazie danych.
Spring automatycznie tworzy tabele na podstawie tych klas.

### 1. Uzytkownik.java
**Tabela:** `uzytkownik`
**Po co:** Przechowuje dane zarejestrowanych użytkowników

| Pole | Typ | Opis |
|------|-----|------|
| uzytkownikId | Long | Unikalny identyfikator (klucz główny) |
| email | String | Adres email (unikalny, do logowania) |
| nick | String | Nazwa wyświetlana |
| hasloHash | String | Zahashowane hasło (BCrypt) |
| imie, nazwisko | String | Dane osobowe |
| status | String | "aktywny" lub "zablokowany" |
| dataRejestracji | LocalDateTime | Kiedy utworzono konto |

### 2. Film.java
**Tabela:** `film`
**Po co:** Katalog filmów do wypożyczenia

| Pole | Typ | Opis |
|------|-----|------|
| filmId | Long | Unikalny identyfikator |
| tytul | String | Tytuł filmu |
| opis | String | Opis/fabuła |
| rokProdukcji | Integer | Rok wydania |
| czasTrwaniaMin | Integer | Długość w minutach |
| cenaWypozyczenia | BigDecimal | Cena za 48h |
| plakatUrl | String | Link do obrazka |
| gatunki | Set<Gatunek> | Relacja wiele-do-wielu |
| aktorzy | Set<Aktor> | Relacja wiele-do-wielu |
| oceny | List<Ocena> | Oceny użytkowników |

### 3. Wypozyczenie.java
**Tabela:** `wypozyczenie`
**Po co:** Rejestruje kto, kiedy i co wypożyczył

| Pole | Typ | Opis |
|------|-----|------|
| wypozyczenieId | Long | Unikalny identyfikator |
| uzytkownik | Uzytkownik | Kto wypożyczył (FK) |
| film | Film | Co wypożyczył (FK) |
| dataStartu | LocalDateTime | Początek wypożyczenia |
| dataKonca | LocalDateTime | Koniec (+48h) |
| status | String | "aktywne", "wygasle", "oczekuje_oplacenia" |
| platnosc | Platnosc | Powiązana płatność |

### 4. Platnosc.java
**Tabela:** `platnosc`
**Po co:** Śledzi płatności przez Stripe

| Pole | Typ | Opis |
|------|-----|------|
| platnoscId | Long | Unikalny identyfikator |
| wypozyczenie | Wypozyczenie | Za co płatność (FK) |
| kwota | BigDecimal | Ile do zapłaty |
| status | String | "oczekuje", "oplacona", "odrzucona" |
| metoda | String | "karta", "blik", itp. |
| stripeSessionId | String | ID sesji Stripe |
| dataOplacenia | LocalDateTime | Kiedy opłacono |

### 5. Ocena.java
**Tabela:** `ocena`
**Po co:** Oceny filmów (gwiazdki 1-5)

| Pole | Typ | Opis |
|------|-----|------|
| ocenaId | Long | Unikalny identyfikator |
| uzytkownik | Uzytkownik | Kto ocenił (FK) |
| film | Film | Co ocenił (FK) |
| wartosc | Integer | Ocena 1-5 |
| dataOceny | LocalDateTime | Kiedy oceniono |

> **Ograniczenie:** Jeden użytkownik może dać tylko jedną ocenę danemu filmowi.

### 6. Komentarz.java
**Tabela:** `komentarz`
**Po co:** Tekstowe recenzje filmów

| Pole | Typ | Opis |
|------|-----|------|
| komentarzId | Long | Unikalny identyfikator |
| uzytkownik | Uzytkownik | Kto napisał (FK) |
| film | Film | Do jakiego filmu (FK) |
| tresc | String | Tekst komentarza |
| dataDodania | LocalDateTime | Kiedy dodano |

### 7. Gatunek.java i Aktor.java
**Tabele:** `gatunek`, `aktor`
**Po co:** Kategorie filmów i obsada aktorska

---

## 🎮 Kontrolery (REST API)

Kontrolery odbierają żądania HTTP i zwracają odpowiedzi JSON.

### AutoryzacjaController.java
**Ścieżka bazowa:** `/api/autoryzacja`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/logowanie` | Logowanie użytkownika |
| POST | `/rejestracja` | Tworzenie nowego konta |
| POST | `/wyloguj` | Wylogowanie (usuwa sesję) |
| GET | `/ja` | Pobiera dane zalogowanego użytkownika |

### FilmController.java
**Ścieżka bazowa:** `/api/filmy`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| GET | `/` | Lista wszystkich filmów |
| GET | `/{id}` | Szczegóły jednego filmu |
| GET | `/szukaj` | Wyszukiwanie z filtrami |
| GET | `/gatunki` | Lista gatunków (do filtrów) |

### WypozyczenieController.java
**Ścieżka bazowa:** `/api/wypozyczenia`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/` | Utwórz nowe wypożyczenie |
| GET | `/moje` | Lista wypożyczeń użytkownika |

### PlatnoscController.java
**Ścieżka bazowa:** `/api/platnosci`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/utworz/{wypozyczenieId}` | Generuje link płatności Stripe |
| GET | `/sukces` | Strona po udanej płatności |
| GET | `/anulowano` | Strona po anulowaniu |

### InterakcjeController.java
**Ścieżka bazowa:** `/api/interakcje`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/ocena` | Dodaj ocenę (1-5) |
| POST | `/komentarz` | Dodaj komentarz |
| POST | `/opinia` | Dodaj ocenę + komentarz razem |
| GET | `/film/{id}/opinie` | Pobierz opinie o filmie |

---

## ⚙️ Serwisy (Logika Biznesowa)

Serwisy zawierają logikę aplikacji - walidacje, obliczenia, reguły biznesowe.

### FilmService.java
- `pobierzWszystkieFilmy()` - zwraca listę wszystkich filmów
- `pobierzFilmPoId(id)` - zwraca jeden film
- `szukajFilmow(filtry)` - wyszukiwanie z parametrami
- `pobierzWszystkieGatunki()` - dla dropdowna w filtrach

### WypozyczenieService.java
- `utworzWypozyczenie(uzytkownikId, filmId)` - tworzy wypożyczenie na 48h
- `pobierzMojeWypozyczenia(uzytkownikId)` - lista wypożyczeń użytkownika

### PlatnoscService.java
- `utworzSesjePlatnosci(wypozyczenieId)` - generuje link do Stripe Checkout
- `oznaczJakoOplacona(sessionId)` - aktualizuje status po płatności

### InterakcjeService.java
- `dodajOcene(uzytkownikId, filmId, wartosc)` - dodaje lub aktualizuje ocenę
- `dodajKomentarz(uzytkownikId, filmId, tresc)` - dodaje komentarz
- `dodajOpinie(request)` - dodaje ocenę + komentarz razem
- `pobierzOpinieDoFilmu(filmId)` - łączy oceny z komentarzami

---

## 🔐 Bezpieczeństwo

### KonfiguracjaBezpieczenstwa.java
Konfiguruje Spring Security:

1. **CSRF wyłączone** - dla uproszczenia (w produkcji należy włączyć)
2. **CORS** - pozwala na żądania z `localhost:5173` i `localhost:5174`
3. **Publiczne endpointy:**
   - `/api/autoryzacja/**` - logowanie i rejestracja
   - `GET /api/filmy/**` - przeglądanie filmów
4. **Chronione** - wszystko inne wymaga zalogowania
5. **Sesje** - używamy JSESSIONID (ciasteczko)

### SerwisAutoryzacji.java
Implementuje `UserDetailsService` - Spring używa go do weryfikacji hasła podczas logowania.

---

## 📦 DTO (Data Transfer Objects)

DTO to obiekty używane do przesyłania danych między frontendem a backendem.
Oddzielamy je od encji, żeby nie eksponować wszystkich pól bazy danych.

| DTO | Po co |
|-----|-------|
| DaneLogowania | email + hasło (formularz logowania) |
| DaneRejestracji | email + nick + hasło + imię + nazwisko (formularz rejestracji) |
| DodajOpinieRequest | filmId + ocena + komentarz (formularz opinii) |
| OpiniaResponse | nickUzytkownika + ocena + komentarz + data (wyświetlanie opinii) |

---

## 🗄️ Pliki Konfiguracyjne

### application.properties
```properties
spring.application.name=wypozyczalnia

# Konfiguracja Bazy Danych (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/wypozyczalnia?characterEncoding=UTF-8&encoding=UTF-8
spring.datasource.username=admin
spring.datasource.password=admin123
spring.datasource.driver-class-name=org.postgresql.Driver

# Konfiguracja Hibernate / JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop  # ⚠️ Czyści bazę przy restarcie!
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true

# Inicjalizacja danych testowych
spring.sql.init.mode=always

# Kodowanie
server.encoding.charset=UTF-8
server.encoding.force=true

# Logowanie
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=WARN

# Stripe - płatności (plik w .gitignore, klucz tylko lokalnie)
stripe.api.key=WKLEJ_TUTAJ_SWOJ_KLUCZ_STRIPE
```

> **Ważne:** Plik `application.properties` jest w `.gitignore`. Użyj szablonu `application.properties.template` do konfiguracji.

### data.sql
Zawiera przykładowe dane testowe:
- 5 użytkowników
- 5 gatunków (Akcja, Komedia, Dramat, Sci-Fi, Horror)
- 5 aktorów
- **50+ filmów** (5 głównych + automatycznie generowane)
- Przykładowe wypożyczenia, płatności, oceny, komentarze

---

## ✅ Status Kompletności Backendu

| Element | Ilość | Status |
|---------|-------|--------|
| Encje | 8 | ✅ Kompletne |
| Kontrolery | 5 | ✅ Kompletne |
| Serwisy | 4 | ✅ Kompletne |
| Repozytoria | 8 | ✅ Kompletne |
| DTO | 6 | ✅ Kompletne |
| Bezpieczeństwo | ✓ | ✅ Skonfigurowane |
| Stripe | ✓ | ✅ Zintegrowane |
| CORS | ✓ | ✅ Dla React |

**Backend jest kompletny i gotowy do użycia!**

---

## 🔗 Następny Krok: Frontend

Dokumentacja frontendu React znajduje się w oddzielnym pliku:
→ [FRONTEND_DOKUMENTACJA.md](./FRONTEND_DOKUMENTACJA.md)
