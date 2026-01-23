# 🎬 Wypożyczalnia Filmów - Instrukcja Uruchamiania

## 📋 Wymagania

Przed uruchomieniem upewnij się, że masz zainstalowane:
- **Java 17+** (do backendu)
- **Node.js 18+** (do frontendu)
- **PostgreSQL** (lub Docker)

---

## 🚀 Jak Uruchomić Aplikację

### KROK 1: Uruchom Bazę Danych (PostgreSQL)

**Opcja A: Przez Docker (zalecane)**
```bash
cd c:\Users\barto\IdeaProjects\wypozyczalnia
docker-compose up -d db
```

**Opcja B: Lokalna instalacja PostgreSQL**
- Baza musi działać na porcie `5432`
- Użytkownik: `admin`, Hasło: `admin123`
- Nazwa bazy: `wypozyczalnia`

---

### KROK 2: Uruchom Backend (Spring Boot)

**W IntelliJ IDEA:**
1. Otwórz projekt `wypozyczalnia`
2. Znajdź plik `WypozyczalniaApplication.java`
3. Kliknij prawym → **Run 'WypozyczalniaApplication'**
4. Poczekaj aż zobaczysz: `Started WypozyczalniaApplication`

**Backend działa na:** `http://localhost:8080`

---

### KROK 3: Uruchom Frontend (React)

**W terminalu (CMD lub PowerShell):**
```bash
cd c:\Users\barto\IdeaProjects\wypozyczalnia\frontend
npm install      # Tylko za pierwszym razem!
npm run dev
```

**Frontend działa na:** `http://localhost:5173`

---

## 🌐 Otwórz Aplikację

Po uruchomieniu wszystkich komponentów, otwórz przeglądarkę:

👉 **http://localhost:5173**

---

## 🔑 Testowe Konta

Aplikacja ładuje dane testowe z pliku `data.sql`. Możesz użyć:

| Email | Nick | Hasło |
|-------|------|-------|
| jan.kowalski@example.com | jankowal | (zahashowane) |
| admin@wypozyczalnia.pl | admin | (zahashowane) |

**Lub zarejestruj nowe konto** na stronie `/rejestracja`.

---

## 📊 Porty w Aplikacji

| Usługa | Port | URL |
|--------|------|-----|
| Frontend (React) | 5173 | http://localhost:5173 |
| Backend (Spring) | 8080 | http://localhost:8080/api |
| Baza danych (PostgreSQL) | 5432 | - |
| pgAdmin (opcjonalnie) | 5050 | http://localhost:5050 |

---

## ❌ Rozwiązywanie Problemów

### "Port 8080 already in use"
```bash
# Znajdź proces
netstat -ano | findstr :8080
# Zamknij go (zamień PID na numer z poprzedniej komendy)
taskkill /PID <PID> /F
```

### "Port 5173 is in use"
Zamknij wszystkie terminale i uruchom `npm run dev` ponownie.
Vite automatycznie przeskoczy na 5174 jeśli 5173 jest zajęty.

### "Błąd połączenia z serwerem" na frontendzie
1. Sprawdź czy backend działa (zobacz logi w IntelliJ)
2. Sprawdź czy CORS pozwala na połączenie z portu frontendu

### "password authentication failed for user"
Sprawdź hasło w `application.properties`:
```properties
spring.datasource.username=admin
spring.datasource.password=admin123
```

---

## 📁 Struktura Projektu

```
wypozyczalnia/
├── src/main/java/...           # Backend (Java/Spring Boot)
├── src/main/resources/
│   ├── application.properties  # Konfiguracja backendu
│   └── data.sql                # Dane testowe
├── frontend/                   # Frontend (React)
│   ├── src/
│   │   ├── pages/              # Strony aplikacji
│   │   ├── components/         # Komponenty wielokrotnego użytku
│   │   └── context/            # Stan globalny (autoryzacja)
│   └── package.json            # Zależności Node.js
├── docker-compose.yml          # Docker dla bazy danych
├── BACKEND_DOKUMENTACJA.md     # Dokumentacja backendu
└── FRONTEND_DOKUMENTACJA.md    # Dokumentacja frontendu
```

---

## 🎯 Funkcje Aplikacji

- ✅ Przeglądanie katalogu filmów
- ✅ Filtrowanie (tytuł, gatunek, rok, cena, ocena)
- ✅ Rejestracja i logowanie
- ✅ Wypożyczanie filmów (48h)
- ✅ Płatności online (Stripe)
- ✅ Oceny i komentarze
- ✅ Panel "Moje Wypożyczenia" z licznikiem czasu
