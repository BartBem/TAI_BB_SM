import { Routes, Route } from 'react-router-dom';
import StronaGlowna from './pages/StronaGlowna';
import StronaLogowania from './pages/StronaLogowania';
import StronaRejestracji from './pages/StronaRejestracji';
import StronaMojeWypozyczenia from './pages/StronaMojeWypozyczenia';
import StronaSzczegolyFilmu from './pages/StronaSzczegolyFilmu';

/**
 * GŁÓWNY KOMPONENT APLIKACJI
 * 
 * Konfiguruje routing (ścieżki URL).
 * Każda ścieżka wyświetla inny komponent strony.
 */
function App() {
  return (
    <Routes>
      {/* Strona główna - lista filmów z filtrami */}
      <Route path="/" element={<StronaGlowna />} />

      {/* Szczegóły filmu - opinie, wypożyczanie */}
      <Route path="/film/:id" element={<StronaSzczegolyFilmu />} />

      {/* Autoryzacja */}
      <Route path="/logowanie" element={<StronaLogowania />} />
      <Route path="/rejestracja" element={<StronaRejestracji />} />

      {/* Panel użytkownika */}
      <Route path="/moje-wypozyczenia" element={<StronaMojeWypozyczenia />} />
    </Routes>
  );
}

export default App;

