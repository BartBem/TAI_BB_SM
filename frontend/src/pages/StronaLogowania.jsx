import { useState } from 'react';
import { Container, Form, Button, Alert, Card, Row, Col } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import { uzyjAutoryzacji } from '../context/KontekstAutoryzacji';

const StronaLogowania = () => {
    const [dane, setDane] = useState({ email: '', haslo: '' });
    const [blad, setBlad] = useState('');
    const [ladowanie, setLadowanie] = useState(false);
    const { zaloguj } = uzyjAutoryzacji();
    const nawiguj = useNavigate();

    const zmienDane = (e) => {
        setDane({ ...dane, [e.target.name]: e.target.value });
    };

    const wyslijFormularz = async (e) => {
        e.preventDefault();
        setBlad('');
        setLadowanie(true);
        try {
            await zaloguj(dane);
            nawiguj('/');
        } catch (error) {
            setBlad('Błędny login lub hasło.');
        } finally {
            setLadowanie(false);
        }
    };

    return (
        <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <Card style={{ width: '400px' }} className="shadow-lg">
                <Card.Body>
                    <h2 className="text-center mb-4">Logowanie</h2>
                    {blad && <Alert variant="danger">{blad}</Alert>}
                    <Form onSubmit={wyslijFormularz}>
                        <Form.Group className="mb-3" controlId="email">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                placeholder="Wpisz email"
                                value={dane.email}
                                onChange={zmienDane}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="haslo">
                            <Form.Label>Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                name="haslo"
                                placeholder="Wpisz hasło"
                                value={dane.haslo}
                                onChange={zmienDane}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="w-100" disabled={ladowanie}>
                            {ladowanie ? 'Logowanie...' : 'Zaloguj się'}
                        </Button>
                    </Form>
                    <div className="w-100 text-center mt-3">
                        Nie masz konta? <Link to="/rejestracja">Zarejestruj się</Link>
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default StronaLogowania;
