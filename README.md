# Wypożyczalnia Filmów - Backend

Aplikacja backendowa dla internetowej wypożyczalni filmów.

## 🚀 Szybki start (Docker)

**Wymagania:** Docker Desktop

```bash
# Uruchom wszystko jedną komendą:
docker-compose up --build

# Aplikacja dostępna pod:
# http://localhost:8080/api/filmy/test
```

## 🔧 Uruchomienie bez Dockera

**Wymagania:** Java 17, Docker (tylko dla bazy)

```bash
# 1. Uruchom bazę danych:
docker-compose up db -d

# 2. Uruchom aplikację:
./mvnw spring-boot:run
```

## 📡 API Endpoints

| Metoda | URL | Opis |
|--------|-----|------|
| GET | `/api/filmy` | Lista filmów |
| GET | `/api/filmy/{id}` | Film po ID |
| POST | `/api/filmy` | Dodaj film |
| DELETE | `/api/filmy/{id}` | Usuń film |

## 🗄️ Dostęp do bazy

- **pgAdmin:** http://localhost:5050
- **Login:** admin@wypozyczalnia.pl
- **Hasło:** admin123

## 🛠️ Technologie

- Java 17
- Spring Boot 4.0
- PostgreSQL 16
- Docker
