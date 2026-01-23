import { useState } from 'react';
import { Container, Form, Button, Alert, Card } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import axios from '../api/axios';

/**
 * STRONA REJESTRACJI
 * 
 * Formularz tworzenia nowego konta użytkownika.
 * Po udanej rejestracji przekierowuje do logowania.
 */
function StronaRejestracji() {
    // Stan formularza
    const [email, ustawEmail] = useState('');
    const [nick, ustawNick] = useState('');
    const [haslo, ustawHaslo] = useState('');
    const [potwierdzHaslo, ustawPotwierdzHaslo] = useState('');
    const [imie, ustawImie] = useState('');
    const [nazwisko, ustawNazwisko] = useState('');

    // Stan UI
    const [ladowanie, ustawLadowanie] = useState(false);
    const [blad, ustawBlad] = useState('');
    const [sukces, ustawSukces] = useState(false);

    const nawiguj = useNavigate();

    // Obsługa wysłania formularza
    const obsluzRejestracje = async (e) => {
        e.preventDefault();
        ustawBlad('');

        // Walidacja hasła
        if (haslo !== potwierdzHaslo) {
            ustawBlad('Hasła nie są identyczne!');
            return;
        }

        if (haslo.length < 6) {
            ustawBlad('Hasło musi mieć minimum 6 znaków!');
            return;
        }

        ustawLadowanie(true);

        try {
            await axios.post('/autoryzacja/rejestracja', {
                email,
                nick,
                haslo,
                imie,
                nazwisko
            });

            ustawSukces(true);
            // Po 2 sekundach przekieruj do logowania
            setTimeout(() => nawiguj('/logowanie'), 2000);

        } catch (err) {
            console.error('Błąd rejestracji:', err);

            if (err.code === "ERR_NETWORK") {
                ustawBlad("BRAK POŁĄCZENIA! \nUpewnij się, że:\n1. Backend działa (zielona strzałka w IntelliJ)\n2. Zrestartowałeś go po zmianach\n3. Nic nie blokuje portu 8080");
            } else if (err.response && err.response.data) {
                // Obsługa błędów walidacji (obiekt {pole: blad}) lub zwykły string
                const data = err.response.data;
                if (typeof data === 'object' && !data.message) {
                    // To są błędy walidacji (np. {imie: "Wymagane", nazwisko: "Wymagane"})
                    const listaBledow = Object.values(data).map((msg, idx) => <div key={idx}>• {msg}</div>);
                    ustawBlad(listaBledow);
                } else {
                    // To jest błąd z message lub surowy tekst
                    const msg = data.message || JSON.stringify(data);
                    ustawBlad(msg);
                }
            } else {
                ustawBlad("Wystąpił nieoczekiwany błąd: " + err.message);
            }
        } finally {
            ustawLadowanie(false);
        }
    };

    return (
        <Container className="mt-5" style={{ maxWidth: '500px' }}>
            <Card className="shadow">
                <Card.Body className="p-4">
                    <h2 className="text-center mb-4">📝 Rejestracja</h2>

                    {sukces && (
                        <Alert variant="success">
                            ✅ Konto utworzone! Przekierowanie do logowania...
                        </Alert>
                    )}

                    {blad && <Alert variant="danger">{blad}</Alert>}

                    <Form onSubmit={obsluzRejestracje}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email *</Form.Label>
                            <Form.Control
                                type="email"
                                value={email}
                                onChange={(e) => ustawEmail(e.target.value)}
                                placeholder="jan.kowalski@example.com"
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Nick *</Form.Label>
                            <Form.Control
                                type="text"
                                value={nick}
                                onChange={(e) => ustawNick(e.target.value)}
                                placeholder="jankowal"
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Imię</Form.Label>
                            <Form.Control
                                type="text"
                                value={imie}
                                onChange={(e) => ustawImie(e.target.value)}
                                placeholder="Jan"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Nazwisko</Form.Label>
                            <Form.Control
                                type="text"
                                value={nazwisko}
                                onChange={(e) => ustawNazwisko(e.target.value)}
                                placeholder="Kowalski"
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Hasło *</Form.Label>
                            <Form.Control
                                type="password"
                                value={haslo}
                                onChange={(e) => ustawHaslo(e.target.value)}
                                placeholder="Minimum 6 znaków"
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-4">
                            <Form.Label>Potwierdź hasło *</Form.Label>
                            <Form.Control
                                type="password"
                                value={potwierdzHaslo}
                                onChange={(e) => ustawPotwierdzHaslo(e.target.value)}
                                placeholder="Powtórz hasło"
                                required
                            />
                        </Form.Group>

                        <Button
                            variant="primary"
                            type="submit"
                            className="w-100"
                            disabled={ladowanie || sukces}
                        >
                            {ladowanie ? 'Rejestruję...' : 'Zarejestruj się'}
                        </Button>
                    </Form>

                    <div className="text-center mt-3">
                        <small>
                            Masz już konto? <Link to="/logowanie">Zaloguj się</Link>
                        </small>
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
}

export default StronaRejestracji;
