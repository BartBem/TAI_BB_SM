import { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Form, Alert, Spinner, Badge, ListGroup } from 'react-bootstrap';
import { KontekstAutoryzacji } from '../context/KontekstAutoryzacji';
import axios from '../api/axios';

/**
 * STRONA SZCZEGÓŁÓW FILMU
 * 
 * Wyświetla pełne informacje o filmie:
 * - Tytuł, opis, gatunki, aktorzy
 * - Średnia ocena
 * - Lista opinii (komentarze + oceny)
 * - Formularz dodawania opinii
 * - Przycisk wypożyczenia z płatnością Stripe
 */
function StronaSzczegolyFilmu() {
    const { id } = useParams();
    const { uzytkownik } = useContext(KontekstAutoryzacji);
    const nawiguj = useNavigate();

    // Stan filmu
    const [film, ustawFilm] = useState(null);
    const [opinie, ustawOpinie] = useState([]);
    const [ladowanie, ustawLadowanie] = useState(true);
    const [blad, ustawBlad] = useState('');

    // Stan formularza opinii
    const [nowaOcena, ustawNowaOcena] = useState(5);
    const [nowyKomentarz, ustawNowyKomentarz] = useState('');
    const [wysylanie, ustawWysylanie] = useState(false);
    const [bladOpinii, ustawBladOpinii] = useState('');
    const [sukcesOpinii, ustawSukcesOpinii] = useState('');

    // Stan wypożyczania
    const [wypozyczanie, ustawWypozyczanie] = useState(false);
    const [bladWypozyczenia, ustawBladWypozyczenia] = useState('');

    // Pobierz dane filmu i opinie
    useEffect(() => {
        const pobierzDane = async () => {
            try {
                // Pobierz film
                const odpFilm = await axios.get(`/filmy/${id}`);
                ustawFilm(odpFilm.data);

                // Pobierz opinie
                try {
                    const odpOpinie = await axios.get(`/interakcje/film/${id}/opinie`);
                    ustawOpinie(odpOpinie.data);
                } catch (err) {
                    console.log('Brak opinii lub błąd:', err);
                }
            } catch (err) {
                console.error('Błąd pobierania filmu:', err);
                ustawBlad('Nie udało się pobrać danych filmu');
            } finally {
                ustawLadowanie(false);
            }
        };

        pobierzDane();
    }, [id]);

    // Dodaj opinię
    const dodajOpinie = async (e) => {
        e.preventDefault();
        ustawBladOpinii('');
        ustawSukcesOpinii('');

        if (!uzytkownik) {
            ustawBladOpinii('Musisz być zalogowany, aby dodać opinię');
            return;
        }

        ustawWysylanie(true);

        try {
            await axios.post('/interakcje/opinia', {
                filmId: parseInt(id),
                wartosc: nowaOcena,  // Zmieniono z 'ocena'
                tresc: nowyKomentarz || "" // Zmieniono z 'komentarz', backend wymaga @NotBlank, więc pusty string zadziała lepiej niż null (chociaż zaraz to zmienimy w backendzie)
            });

            ustawSukcesOpinii('Opinia dodana!');
            ustawNowyKomentarz('');

            // Odśwież opinie
            const odpOpinie = await axios.get(`/interakcje/film/${id}/opinie`);
            ustawOpinie(odpOpinie.data);

        } catch (err) {
            console.error('Błąd dodawania opinii:', err);
            ustawBladOpinii(err.response?.data?.message || err.response?.data || 'Nie udało się dodać opinii');
        } finally {
            ustawWysylanie(false);
        }
    };

    // Usuń opinię
    const usunOpinie = async () => {
        if (!window.confirm("Czy na pewno chcesz usunąć swoją opinię?")) return;
        ustawBladOpinii('');
        ustawSukcesOpinii('');

        try {
            await axios.delete(`/interakcje/opinia?filmId=${id}`);
            ustawSukcesOpinii('Opinia usunięta.');

            // Odśwież opinie
            const odpOpinie = await axios.get(`/interakcje/film/${id}/opinie`);
            ustawOpinie(odpOpinie.data);

            // Zresetuj formularz
            ustawNowyKomentarz('');
            ustawNowaOcena(5);
        } catch (err) {
            console.error('Błąd usuwania opinii:', err);
            ustawBladOpinii('Nie udało się usunąć opinii.');
        }
    };

    // Wypożycz film (integracja ze Stripe)
    const wypozyczFilm = async () => {
        ustawBladWypozyczenia('');

        if (!uzytkownik) {
            nawiguj('/logowanie');
            return;
        }

        ustawWypozyczanie(true);

        try {
            // 1. Utwórz wypożyczenie
            const odpWypozyczenie = await axios.post('/wypozyczenia', null, {
                params: { filmId: parseInt(id) } // Backend WypozyczenieController używa @RequestParam filmId
            });

            const wypozyczenieId = odpWypozyczenie.data.wypozyczenieId;

            // 2. Pobierz link do płatności Stripe
            // Backend: POST /api/platnosci/zaplac?wypozyczenieId=... (zwraca String URL)
            const odpPlatnosc = await axios.post(`/platnosci/zaplac?wypozyczenieId=${wypozyczenieId}`);

            // 3. Przekieruj do Stripe Checkout
            // Odpowiedź to bezpośredni URL (String), nie JSON {url: ...}
            if (odpPlatnosc.data && typeof odpPlatnosc.data === 'string' && odpPlatnosc.data.startsWith('http')) {
                window.location.href = odpPlatnosc.data;
            } else {
                console.error("Nieprawidłowa odpowiedź płatności:", odpPlatnosc.data);
                ustawBladWypozyczenia('Błąd serwera płatności: Nie otrzymano linku.');
            }

        } catch (err) {
            console.error('Błąd wypożyczania:', err);
            if (err.response?.status === 401) {
                nawiguj('/logowanie');
            } else {
                ustawBladWypozyczenia(err.response?.data?.message || err.response?.data || 'Nie udało się wypożyczyć filmu');
            }
        } finally {
            ustawWypozyczanie(false);
        }
    };

    // Komponent gwiazdek
    const Gwiazdki = ({ ocena }) => {
        return (
            <span className="text-warning">
                {'★'.repeat(ocena)}{'☆'.repeat(5 - ocena)}
            </span>
        );
    };

    if (ladowanie) {
        return (
            <Container className="mt-5 text-center">
                <Spinner animation="border" />
                <p>Ładowanie filmu...</p>
            </Container>
        );
    }

    if (blad || !film) {
        return (
            <Container className="mt-5">
                <Alert variant="danger">{blad || 'Film nie znaleziony'}</Alert>
            </Container>
        );
    }

    // Oblicz średnią ocenę
    const sredniaOcena = opinie.length > 0
        ? (opinie.reduce((sum, o) => sum + o.ocena, 0) / opinie.length).toFixed(1)
        : null;

    return (
        <Container className="mt-4">
            <Link to="/" className="btn btn-outline-secondary mb-3">
                ← Wróć do strony głównej
            </Link>
            <Row>
                {/* Kolumna główna - informacje o filmie */}
                <Col md={8}>
                    <Card className="shadow-sm mb-4">
                        <Card.Body>
                            <Row>
                                <Col md={4} className="mb-3 mb-md-0">
                                    <div style={{ height: '400px', overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: '#000', borderRadius: '4px' }}>
                                        {film.plakatUrl ? (
                                            <img
                                                src={film.plakatUrl.startsWith('/') ? film.plakatUrl : film.plakatUrl}
                                                alt={film.tytul}
                                                style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                                onError={(e) => {
                                                    e.target.onerror = null;
                                                    e.target.src = `https://placehold.co/400x600/1a1a1a/ffffff?text=${encodeURIComponent(film.tytul)}`;
                                                }}
                                            />
                                        ) : (
                                            <div className="text-white text-center p-3">
                                                {film.tytul}
                                            </div>
                                        )}
                                    </div>
                                </Col>
                                <Col md={8}>
                                    <h1>{film.tytul}</h1>

                                    <div className="mb-3">
                                        {film.gatunki?.map((g, i) => (
                                            <Badge key={i} bg="secondary" className="me-1">
                                                {g.nazwa}
                                            </Badge>
                                        ))}
                                        <Badge bg="info" className="ms-2">
                                            📅 {film.rokProdukcji}
                                        </Badge>
                                        {film.czasTrwaniaMin && (
                                            <Badge bg="dark" className="ms-1">
                                                ⏱️ {film.czasTrwaniaMin} min
                                            </Badge>
                                        )}
                                    </div>

                                    {sredniaOcena && (
                                        <div className="mb-3">
                                            <Gwiazdki ocena={Math.round(sredniaOcena)} />
                                            <span className="ms-2 text-muted">
                                                ({sredniaOcena} / 5, {opinie.length} opinii)
                                            </span>
                                        </div>
                                    )}

                                    <p className="lead">{film.opis}</p>

                                    {film.aktorzy?.length > 0 && (
                                        <p>
                                            <strong>Obsada:</strong>{' '}
                                            {film.aktorzy.map(a => `${a.imie} ${a.nazwisko}`).join(', ')}
                                        </p>
                                    )}
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>

                    {/* Sekcja opinii */}
                    <Card className="shadow-sm mb-4">
                        <Card.Header>
                            <h5 className="mb-0">💬 Opinie ({opinie.length})</h5>
                        </Card.Header>
                        <Card.Body>
                            {opinie.length === 0 ? (
                                <p className="text-muted">Brak opinii. Bądź pierwszy!</p>
                            ) : (
                                <ListGroup variant="flush">
                                    {opinie.map((opinia, i) => (
                                        <ListGroup.Item key={i}>
                                            <div className="d-flex justify-content-between">
                                                <strong>{opinia.nick}</strong>
                                                <Gwiazdki ocena={opinia.ocena} />
                                            </div>
                                            {uzytkownik && uzytkownik.nick === opinia.nick && (
                                                <div className="text-end">
                                                    <Button
                                                        variant="outline-danger"
                                                        size="sm"
                                                        onClick={usunOpinie}
                                                        className="mt-1"
                                                    >
                                                        Usuń
                                                    </Button>
                                                </div>
                                            )}
                                            {opinia.tresc && (
                                                <p className="mb-0 mt-1 text-muted">
                                                    "{opinia.tresc}"
                                                </p>
                                            )}
                                            <small className="text-muted">
                                                {new Date(opinia.data).toLocaleDateString('pl-PL')}
                                            </small>
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            )}
                        </Card.Body>
                    </Card>

                    {/* Formularz dodawania opinii */}
                    {uzytkownik && (
                        <Card className="shadow-sm">
                            <Card.Header>
                                <h5 className="mb-0">✍️ Dodaj swoją opinię</h5>
                            </Card.Header>
                            <Card.Body>
                                {bladOpinii && <Alert variant="danger">{bladOpinii}</Alert>}
                                {sukcesOpinii && <Alert variant="success">{sukcesOpinii}</Alert>}

                                <Form onSubmit={dodajOpinie}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Twoja ocena</Form.Label>
                                        <Form.Select
                                            value={nowaOcena}
                                            onChange={(e) => ustawNowaOcena(parseInt(e.target.value))}
                                        >
                                            {[5, 4, 3, 2, 1].map(n => (
                                                <option key={n} value={n}>
                                                    {'★'.repeat(n)}{'☆'.repeat(5 - n)} ({n})
                                                </option>
                                            ))}
                                        </Form.Select>
                                    </Form.Group>

                                    <Form.Group className="mb-3">
                                        <Form.Label>Komentarz (opcjonalny)</Form.Label>
                                        <Form.Control
                                            as="textarea"
                                            rows={3}
                                            value={nowyKomentarz}
                                            onChange={(e) => ustawNowyKomentarz(e.target.value)}
                                            placeholder="Napisz co myślisz o tym filmie..."
                                        />
                                    </Form.Group>

                                    <Button
                                        variant="primary"
                                        type="submit"
                                        disabled={wysylanie}
                                    >
                                        {wysylanie ? 'Wysyłanie...' : 'Dodaj opinię'}
                                    </Button>
                                </Form>
                            </Card.Body>
                        </Card>
                    )}
                </Col>

                {/* Kolumna boczna - wypożyczenie */}
                <Col md={4}>
                    <Card className="shadow-sm sticky-top" style={{ top: '1rem' }}>
                        <Card.Body className="text-center">
                            <h3 className="text-success mb-3">
                                💰 {film.cenaWypozyczenia?.toFixed(2)} zł
                            </h3>
                            <p className="text-muted">Wypożyczenie na 48h</p>

                            {bladWypozyczenia && (
                                <Alert variant="danger" className="small">
                                    {bladWypozyczenia}
                                </Alert>
                            )}

                            <Button
                                variant="success"
                                size="lg"
                                className="w-100"
                                onClick={wypozyczFilm}
                                disabled={wypozyczanie}
                            >
                                {wypozyczanie ? (
                                    <>
                                        <Spinner size="sm" className="me-2" />
                                        Przetwarzanie...
                                    </>
                                ) : (
                                    '🎬 Wypożycz teraz'
                                )}
                            </Button>

                            {!uzytkownik && (
                                <p className="text-muted small mt-2">
                                    Musisz być zalogowany, aby wypożyczyć film
                                </p>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default StronaSzczegolyFilmu;
