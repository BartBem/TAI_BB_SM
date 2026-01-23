import { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Badge, Button, Spinner, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { KontekstAutoryzacji } from '../context/KontekstAutoryzacji';
import axios from '../api/axios';

/**
 * STRONA MOJE WYPOŻYCZENIA
 * 
 * Wyświetla listę wypożyczeń zalogowanego użytkownika.
 * Pokazuje status, pozostały czas i umożliwia akcje.
 */
function StronaMojeWypozyczenia() {
    const { uzytkownik } = useContext(KontekstAutoryzacji);
    const [wypozyczenia, ustawWypozyczenia] = useState([]);
    const [ladowanie, ustawLadowanie] = useState(true);
    const [blad, ustawBlad] = useState('');

    // Pobierz wypożyczenia użytkownika
    useEffect(() => {
        const pobierzWypozyczenia = async () => {
            try {
                const odpowiedz = await axios.get('/wypozyczenia/moje');
                ustawWypozyczenia(odpowiedz.data);
            } catch (err) {
                console.error('Błąd pobierania wypożyczeń:', err);
                ustawBlad('Nie udało się pobrać wypożyczeń');
            } finally {
                ustawLadowanie(false);
            }
        };

        if (uzytkownik) {
            pobierzWypozyczenia();
        } else {
            ustawLadowanie(false);
        }
    }, [uzytkownik]);

    // Komponent licznika czasu
    const LicznikCzasu = ({ dataKonca }) => {
        const [pozostalyCzas, ustawPozostalyCzas] = useState('');

        useEffect(() => {
            const obliczCzas = () => {
                const teraz = new Date();
                const koniec = new Date(dataKonca);
                const roznica = koniec - teraz;

                if (roznica <= 0) {
                    ustawPozostalyCzas('Wygasło');
                    return;
                }

                const dni = Math.floor(roznica / (1000 * 60 * 60 * 24));
                const godziny = Math.floor((roznica % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                const minuty = Math.floor((roznica % (1000 * 60 * 60)) / (1000 * 60));
                const sekundy = Math.floor((roznica % (1000 * 60)) / 1000);

                if (dni > 0) {
                    ustawPozostalyCzas(`${dni}d ${godziny}h ${minuty}m`);
                } else {
                    ustawPozostalyCzas(`${godziny}h ${minuty}m ${sekundy}s`);
                }
            };

            obliczCzas();
            const interwał = setInterval(obliczCzas, 1000);
            return () => clearInterval(interwał);
        }, [dataKonca]);

        return <span className="fw-bold text-primary">{pozostalyCzas}</span>;
    };

    // Mapowanie statusów na Badge
    const pobierzBadgeStatusu = (status) => {
        const mapaStatusow = {
            'aktywne': { bg: 'success', tekst: '✅ Aktywne' },
            'oczekuje_oplacenia': { bg: 'warning', tekst: '⏳ Oczekuje płatności' },
            'wygasle': { bg: 'secondary', tekst: '⏰ Wygasłe' },
            'anulowane': { bg: 'danger', tekst: '❌ Anulowane' }
        };

        const dane = mapaStatusow[status] || { bg: 'secondary', tekst: status };
        return <Badge bg={dane.bg}>{dane.tekst}</Badge>;
    };

    // Ekran logowania wymagany
    if (!uzytkownik) {
        return (
            <Container className="mt-5 text-center">
                <Alert variant="warning">
                    ⚠️ Musisz być zalogowany, aby zobaczyć swoje wypożyczenia.
                </Alert>
            </Container>
        );
    }

    if (ladowanie) {
        return (
            <Container className="mt-5 text-center">
                <Spinner animation="border" />
                <p>Ładowanie wypożyczeń...</p>
            </Container>
        );
    }

    return (
        <Container className="mt-4">
            <Link to="/" className="btn btn-outline-secondary mb-3">
                ← Wróć do strony głównej
            </Link>

            <h2 className="mb-4">🎬 Moje Wypożyczenia</h2>

            {blad && <Alert variant="danger">{blad}</Alert>}

            {wypozyczenia.length === 0 ? (
                <Alert variant="info">
                    Nie masz jeszcze żadnych wypożyczeń. <Link to="/">Przejdź do katalogu filmów!</Link>
                </Alert>
            ) : (
                <Row>
                    {wypozyczenia.map((w) => (
                        <Col md={6} lg={4} key={w.wypozyczenieId} className="mb-4">
                            <Card className="h-100 shadow-sm">
                                <Card.Body>
                                    <Card.Title>{w.film?.tytul || 'Film'}</Card.Title>

                                    <div className="mb-2">
                                        {pobierzBadgeStatusu(w.status)}
                                    </div>

                                    <Card.Text className="small text-muted">
                                        <strong>Start:</strong> {new Date(w.dataStartu).toLocaleString('pl-PL')}<br />
                                        <strong>Koniec:</strong> {new Date(w.dataKonca).toLocaleString('pl-PL')}
                                    </Card.Text>

                                    {w.status === 'aktywne' && (
                                        <div className="mt-2 p-2 bg-light rounded">
                                            ⏱️ Pozostało: <LicznikCzasu dataKonca={w.dataKonca} />
                                        </div>
                                    )}

                                    {['oczekuje_oplacenia', 'OCZEKUJE_OPLACENIA'].includes(w.status) && (
                                        <Button
                                            variant="success"
                                            size="sm"
                                            className="mt-2 w-100"
                                            onClick={async () => {
                                                try {
                                                    const odp = await axios.post(`/platnosci/zaplac?wypozyczenieId=${w.wypozyczenieId}`);
                                                    if (odp.data && typeof odp.data === 'string' && odp.data.startsWith('http')) {
                                                        window.location.href = odp.data;
                                                    } else {
                                                        alert("Błąd: Nie udało się wygenerować linku płatności.");
                                                    }
                                                } catch (err) {
                                                    console.error(err);
                                                    alert("Błąd połączenia z serwerem płatności.");
                                                }
                                            }}
                                        >
                                            💳 Dokończ płatność
                                        </Button>
                                    )}
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    );
}

export default StronaMojeWypozyczenia;
