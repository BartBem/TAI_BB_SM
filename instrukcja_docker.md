# 🐳 Instrukcja obsługi Dockera - Wypożyczalnia Filmów

Ten dokument opisuje jak zarządzać kontenerami w projekcie, jak aktualizować aplikację i jak rozwiązywać typowe problemy.

---

## 🚀 1. Codzienna praca (Development)

Gdy pracujesz nad kodem w IntelliJ, potrzebujesz w Dockerze **tylko bazy danych**. Aplikację uruchamiasz przyciskiem "Run" w IntelliJ (lub `mvnw spring-boot:run`).

### Uruchomienie samej bazy:
```bash
docker-compose up -d db pgadmin
```
*(Flaga `-d` oznacza "detached" - kontenery działają w tle, nie blokują terminala)*

### Zatrzymanie bazy:
```bash
docker-compose stop
```

---

## 📦 2. Udostępnianie projektu (Całość w Dockerze)

Gdy chcesz wysłać projekt komuś, kto nie ma Javy/IntelliJ, lub chcesz przetestować "wersję pudełkową".

### Uruchomienie wszystkiego (Baza + Aplikacja):
```bash
docker-compose up -d
```
Wtedy aplikacja backendowa działa w kontenerze na porcie `8080`.

---

## 🔄 3. Aktualizacja kodu w Dockerze (WAŻNE!)

Jeśli zmieniłeś coś w kodzie (np. w `FilmController.java`) i chcesz, żeby kontener `backend` to widział, musisz go **PRZEBUDOWAĆ**. Zwykły restart nie wystarczy.

### Krok po kroku:

1. **Zapisz zmiany** w IntelliJ.
2. W terminalu wpisz:
   ```bash
   docker-compose up -d --build backend
   ```
   *(Flaga `--build` wymusza ponowną kompilację i stworzenie nowego obrazu)*

Alternatywnie dla całości:
```bash
docker-compose up -d --build
```

---

## 🛠️ 4. Typowe problemy

### 🔴 Błąd: "Port 8080 is already in use"

Oznacza to, że próbujesz uruchomić aplikację w IntelliJ, ale kontener `backend` wciąż działa w tle i zajmuje port.

**Rozwiązanie:** Zatrzymaj kontener backendu.
```bash
docker stop backend_wypozyczalnia
```
Teraz możesz uruchomić aplikację w IntelliJ.

### 🔴 Baza danych nie ma aktualnych danych

Jeśli usunąłeś kontenery (`down`), baza może być pusta.
**Rozwiązanie:** Restart aplikacji Java (czy to w IntelliJ, czy w Dockerze) automatycznie załaduje dane z pliku `data.sql` (dzięki naszej konfiguracji).

---

## 📝 5. Ściąga z komend

| Komenda | Co robi | Kiedy używać |
|---------|---------|--------------|
| `docker-compose up -d` | Uruchamia wszystko w tle | Chcesz uruchomić system |
| `docker-compose up -d --build` | Buduje i uruchamia (aktualizuje kod) | Po zmianach w kodzie |
| `docker-compose down` | Usuwa kontenery i sieci | Chcesz posprzątać ("format") |
| `docker-compose stop` | Zatrzymuje kontenery (nie usuwa danych) | Koniec pracy na dziś |
| `docker-compose start` | Wznawia zatrzymane kontenery | Powrót do pracy |
| `docker ps` | Pokazuje co działa | Sprawdzanie statusu |
| `docker logs -f backend_wypozyczalnia` | Pokazuje logi aplikacji | Debugowanie błędów |
