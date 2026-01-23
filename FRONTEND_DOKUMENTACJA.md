# 📚 Dokumentacja Frontendu - Wypożyczalnia Filmów (React)

> **Cel tego pliku:** Wyjaśnić krok po kroku jak działa frontend aplikacji.
> Frontend to to, co widzi użytkownik w przeglądarce.

---

## 🏗️ Technologie

| Technologia | Wersja | Po co |
|-------------|--------|-------|
| **React** | 18+ | Biblioteka do budowania interfejsów użytkownika |
| **Vite** | 5+ | Szybki serwer deweloperski i bundler |
| **React Router** | 6+ | Nawigacja między stronami (bez przeładowania) |
| **Axios** | 1+ | Wysyłanie żądań HTTP do backendu |
| **Bootstrap** | 5+ | Gotowe style CSS (przyciski, karty, formularze) |
| **React Bootstrap** | 2+ | Komponenty Bootstrap jako komponenty React |

---

## 📁 Struktura Plików Frontendu

```
frontend/src/
├── main.jsx                    # Punkt startowy aplikacji React
├── App.jsx                     # Konfiguracja routingu (ścieżki URL)
├── index.css                   # Globalne style CSS
├── api/
│   └── axios.js                # Skonfigurowany klient HTTP
├── context/
│   └── KontekstAutoryzacji.jsx # Stan "czy użytkownik jest zalogowany"
├── components/                 # Małe, wielokrotnie używane elementy
│   ├── Nawigacja.jsx           # Pasek nawigacji (góra strony)
│   ├── KartaFilmu.jsx          # Pojedyncza karta filmu
│   └── PasekFiltrow.jsx        # Filtry wyszukiwania
└── pages/                      # Całe strony (widoki)
    ├── StronaGlowna.jsx        # Lista filmów z filtrami
    ├── StronaSzczegolyFilmu.jsx # Szczegóły filmu + opinie + wypożyczanie
    ├── StronaLogowania.jsx     # Formularz logowania
    ├── StronaRejestracji.jsx   # Formularz rejestracji
    └── StronaMojeWypozyczenia.jsx # Lista wypożyczeń użytkownika
```

---

## 🔧 Konfiguracja

### main.jsx - Punkt Startowy
```jsx
// To jest pierwszy plik który React uruchamia
// Ustawia:
// 1. BrowserRouter - obsługę URL-i
// 2. DostawcaAutoryzacji - kontekst zalogowanego użytkownika
// 3. Bootstrap CSS - style

import { BrowserRouter } from 'react-router-dom';
import { DostawcaAutoryzacji } from './context/KontekstAutoryzacji';
import 'bootstrap/dist/css/bootstrap.min.css';
import App from './App';

ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <DostawcaAutoryzacji>
            <App />
        </DostawcaAutoryzacji>
    </BrowserRouter>
);
```

### axios.js - Klient HTTP
```jsx
// Tworzy skonfigurowany obiekt do komunikacji z backendem
// baseURL - wszystkie żądania idą do http://localhost:8080/api
// withCredentials - wysyła ciasteczko sesji (JSESSIONID)

import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true  // ← To jest KLUCZOWE dla sesji!
});

export default api;
```

### KontekstAutoryzacji.jsx - Stan Zalogowania
```jsx
// React Context - globalny stan dostępny w całej aplikacji
// Przechowuje:
// - uzytkownik - dane zalogowanego (lub null)
// - zaloguj() - funkcja logowania
// - wyloguj() - funkcja wylogowania

// Użycie w komponentach:
const { uzytkownik, zaloguj, wyloguj } = uzyjAutoryzacji();

if (uzytkownik) {
    // Użytkownik jest zalogowany
} else {
    // Użytkownik niezalogowany
}
```

---

## 📄 Strony (Pages)

### StronaGlowna.jsx
**URL:** `/`
**Co robi:**
1. Pobiera listę filmów z backendu (`GET /api/filmy`)
2. Wyświetla filtry (tytuł, gatunek, rok, cena, ocena)
3. Renderuje siatkę kart filmów
4. Kliknięcie w film → przejście do szczegółów

**Kluczowe elementy:**
```jsx
// Pobieranie filmów przy starcie strony
useEffect(() => {
    pobierzFilmy();
}, []);

// Funkcja pobierająca filmy
const pobierzFilmy = async (filtry = {}) => {
    const odpowiedz = await api.get('/filmy/szukaj', { params: filtry });
    setFilmy(odpowiedz.data);
};
```

---

### StronaSzczegolyFilmu.jsx
**URL:** `/film/:id` (np. `/film/1`)
**Co robi:**
1. Pobiera szczegóły filmu (`GET /api/filmy/{id}`)
2. Pobiera opinie (`GET /api/interakcje/film/{id}/opinie`)
3. Wyświetla tytuł, opis, gatunki, aktorów
4. Pokazuje listę opinii (gwiazdki + komentarze)
5. Formularz dodawania opinii (dla zalogowanych)
6. Przycisk "Wypożycz teraz" → płatność Stripe

**Kluczowe elementy:**
```jsx
// Wypożyczanie filmu
3. Wyświetla karty z:
   - Tytułem filmu
   - Statusem (aktywne, wygasłe, oczekuje płatności)
   - Licznikiem czasu (ile zostało do końca)
4. Dla nieopłaconych (status "oczekuje_oplacenia") → przycisk "💳 Dokończ płatność"
   - Pozwala wygenerować nowy link do Stripe, jeśli poprzedni wygasł lub użytkownik zamknął kartę.
```

---

### StronaLogowania.jsx
**URL:** `/logowanie`
**Co robi:**
1. Formularz z polami: email, hasło
2. Po submit wysyła `POST /api/autoryzacja/logowanie`
3. Przy sukcesie → przekierowanie do strony głównej
4. Przy błędzie → wyświetla komunikat

---

### StronaRejestracji.jsx
**URL:** `/rejestracja`
**Co robi:**
1. Formularz z polami: email, nick, imię, nazwisko, hasło, potwierdź hasło
2. Walidacja: hasła muszą być takie same, min 6 znaków
3. Wysyła `POST /api/autoryzacja/rejestracja`
4. Przy sukcesie → przekierowanie do logowania

---

### StronaMojeWypozyczenia.jsx
**URL:** `/moje-wypozyczenia`
**Co robi:**
1. Sprawdza czy użytkownik jest zalogowany
2. Pobiera listę wypożyczeń (`GET /api/wypozyczenia/moje`)
3. Wyświetla karty z:
   - Tytułem filmu
   - Statusem (aktywne, wygasłe, oczekuje płatności)
   - Licznikiem czasu (ile zostało do końca)
4. Dla nieopłaconych (status "oczekuje_oplacenia") → przycisk "💳 Dokończ płatność"
   - Pozwala wygenerować nowy link do Stripe, jeśli poprzedni wygasł lub użytkownik zamknął kartę.

**Licznik czasu:**
```jsx
// Komponent który co sekundę przelicza pozostały czas
const LicznikCzasu = ({ dataKonca }) => {
    const [pozostalyCzas, ustawPozostalyCzas] = useState('');
    
    useEffect(() => {
        const interwał = setInterval(() => {
            const roznica = new Date(dataKonca) - new Date();
            // Oblicz dni, godziny, minuty, sekundy...
        }, 1000);
        
        return () => clearInterval(interwał);
    }, [dataKonca]);
    
    return <span>{pozostalyCzas}</span>;
};
```

---

## 🧩 Komponenty (Components)

### Nawigacja.jsx
**Po co:** Pasek nawigacji na górze każdej strony
**Zawiera:**
- Logo/nazwa "Wypożyczalnia Filmów"
- Link do strony głównej
- Link "Moje Wypożyczenia" (tylko dla zalogowanych)
- Przyciski "Logowanie" / "Rejestracja" (dla niezalogowanych)
- Nazwa użytkownika + "Wyloguj" (dla zalogowanych)

---

### KartaFilmu.jsx
**Po co:** Pojedyncza karta filmu w siatce
**Zawiera:**
- Plakat (obrazek) - z obsługą błędów ładowania
- Rok produkcji (badge w rogu)
- Tytuł
- Gatunki
- Czas trwania
- Cena
- Przycisk "Zobacz więcej" → link do szczegółów

---

### PasekFiltrow.jsx
**Po co:** Filtry wyszukiwania filmów
**Zawiera:**
- Pole tekstowe: szukaj po tytule
- Dropdown: gatunek (pobierany z backendu)
- Pole liczbowe: rok
- Pole liczbowe: max cena
- Dropdown: minimalna ocena (1-5)
- Przyciski: "Filtruj", "Wyczyść"

---

## 🔄 Przepływ Danych

### Przykład: Logowanie

```
1. Użytkownik wpisuje email i hasło
   ↓
2. Klikuje "Zaloguj"
   ↓
3. React wywołuje: zaloguj({ email, haslo })
   ↓
4. axios wysyła POST /api/autoryzacja/logowanie
   ↓
5. Backend weryfikuje hasło
   ↓
6. Backend ustawia ciasteczko JSESSIONID
   ↓
7. Frontend pobiera dane użytkownika (GET /api/autoryzacja/ja)
   ↓
8. Stan uzytkownik = { email, nick, ... }
   ↓
9. Nawigacja pokazuje: "Zalogowany jako: nick"
```

### Przykład: Wypożyczanie

```
1. Użytkownik klika "Wypożycz teraz"
   ↓
2. POST /api/wypozyczenia { filmId: 1 }
   → Tworzy wypożyczenie w bazie
   ↓
3. POST /api/platnosci/utworz/{id}
   → Tworzy sesję Stripe
   → Zwraca URL do płatności
   ↓
4. window.location.href = stripeUrl
   → Przeglądarka otwiera Stripe Checkout
   ↓
5. Użytkownik płaci kartą (testową)
   ↓
6. Stripe przekierowuje do /api/platnosci/sukces
   ↓
7. Backend oznacza płatność jako opłaconą
   ↓
8. Użytkownik wraca na stronę główną
```

---

## 🚀 Uruchamianie Frontendu

```bash
# Wejdź do katalogu frontend
cd frontend

# Zainstaluj zależności (tylko za pierwszym razem)
npm install

# Uruchom serwer deweloperski
npm run dev
```

**Frontend będzie dostępny pod:** `http://localhost:5173`
(lub 5174 jeśli 5173 jest zajęty)

---

## ✅ Status Kompletności Frontendu

| Element | Status |
|---------|--------|
| Strona główna z filmami | ✅ |
| Filtry wyszukiwania | ✅ |
| Szczegóły filmu | ✅ |
| Opinie (oceny + komentarze) | ✅ |
| Logowanie | ✅ |
| Rejestracja | ✅ |
| Moje Wypożyczenia | ✅ |
| Licznik czasu | ✅ |
| Integracja Stripe | ✅ |
| Nawigacja dynamiczna | ✅ |

**Frontend jest kompletny!**

---

## 🔗 Powiązane Dokumenty

- [BACKEND_DOKUMENTACJA.md](./BACKEND_DOKUMENTACJA.md) - Dokumentacja backendu
