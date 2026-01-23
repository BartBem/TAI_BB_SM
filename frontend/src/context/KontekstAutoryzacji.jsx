import { createContext, useState, useEffect, useContext } from 'react';
import api from '../api/axios'; // Używamy skonfigurowanego axios

const KontekstAutoryzacji = createContext(null);

export const DostawcaAutoryzacji = ({ children }) => {
    const [uzytkownik, setUzytkownik] = useState(null);
    const [ladowanie, setLadowanie] = useState(true);

    const sprawdzAutoryzacje = async () => {
        try {
            const odpowiedz = await api.get('/autoryzacja/ja');
            setUzytkownik(odpowiedz.data);
        } catch (error) {
            setUzytkownik(null);
        } finally {
            setLadowanie(false);
        }
    };

    useEffect(() => {
        sprawdzAutoryzacje();
    }, []);

    const zaloguj = async (daneLogowania) => {
        await api.post('/autoryzacja/logowanie', daneLogowania);
        await sprawdzAutoryzacje();
    };

    const wyloguj = async () => {
        await api.post('/autoryzacja/wyloguj');
        setUzytkownik(null);
    };

    return (
        <KontekstAutoryzacji.Provider value={{ uzytkownik, zaloguj, wyloguj, sprawdzAutoryzacje, ladowanie }}>
            {children}
        </KontekstAutoryzacji.Provider>
    );
};

export const uzyjAutoryzacji = () => useContext(KontekstAutoryzacji);

// Eksportuj też sam kontekst dla komponentów używających useContext bezpośrednio
export { KontekstAutoryzacji };

