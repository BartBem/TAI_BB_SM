import { useState, useEffect } from 'react';
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import api from '../api/axios';
import KartaFilmu from '../components/KartaFilmu';
import Nawigacja from '../components/Nawigacja';
import PasekFiltrow from '../components/PasekFiltrow';

const StronaGlowna = () => {
    const [filmy, setFilmy] = useState([]);
    const [ladowanie, setLadowanie] = useState(true);
    const [blad, setBlad] = useState(null);

    const pobierzFilmy = async (filtry = {}) => {
        setLadowanie(true);
        try {
            // Zamiana pustych wartości na undefined/null
            const endpoint = Object.values(filtry).some(v => v) ? '/filmy/szukaj' : '/filmy';

            const odpowiedz = await api.get(endpoint, { params: filtry });
            setFilmy(odpowiedz.data);
            setBlad(null);
        } catch (err) {
            console.error(err);
            if (err.code === "ERR_NETWORK") {
                setBlad("Nie można połączyć się z serwerem. Upewnij się, że backend (Java) działa na porcie 8080.");
            } else {
                setBlad("Nie udało się pobrać listy filmów.");
            }
        } finally {
            setLadowanie(false);
        }
    };

    useEffect(() => {
        pobierzFilmy();
    }, []);

    const obsluzFiltrowanie = (filtry) => {
        pobierzFilmy(filtry);
    };

    const obsluzWypozyczenie = (film) => {
        // TODO: Logika wypożyczania (modal)
        alert(`Wybrano film: ${film.tytul}. Funkcja w przygotowaniu.`);
    };

    return (
        <>
            <Nawigacja />
            <Container>
                <h2 className="mb-4">Dostępne Filmy</h2>

                <PasekFiltrow onFiltruj={obsluzFiltrowanie} />

                {blad && <Alert variant="danger">{blad}</Alert>}

                {ladowanie ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" role="status">
                            <span className="visually-hidden">Ładowanie...</span>
                        </Spinner>
                    </div>
                ) : (
                    <Row xs={1} md={2} lg={4} className="g-4">
                        {filmy.length > 0 ? (
                            filmy.map(film => (
                                <Col key={film.filmId}>
                                    <KartaFilmu film={film} onWypozycz={obsluzWypozyczenie} />
                                </Col>
                            ))
                        ) : (
                            !blad && (
                                <Col xs={12}>
                                    <Alert variant="info">Nie znaleziono filmów spełniających kryteria.</Alert>
                                </Col>
                            )
                        )}
                    </Row>
                )}
            </Container>
        </>
    );
};

export default StronaGlowna;
