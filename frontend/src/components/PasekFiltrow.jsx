import { useState, useEffect } from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import api from '../api/axios';

const PasekFiltrow = ({ onFiltruj }) => {
    const [filtry, setFiltry] = useState({
        tytul: '',
        gatunek: '',
        rok: '',
        cenaMax: '',
        minOcena: ''
    });

    const [gatunki, setGatunki] = useState([]);

    useEffect(() => {
        const pobierzGatunki = async () => {
            try {
                const odpowiedz = await api.get('/filmy/gatunki');
                setGatunki(odpowiedz.data);
            } catch (error) {
                console.error("Błąd pobierania gatunków:", error);
            }
        };
        pobierzGatunki();
    }, []);

    const zmienFiltr = (e) => {
        const { name, value } = e.target;
        setFiltry(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const wyslijFormularz = (e) => {
        e.preventDefault();
        onFiltruj(filtry);
    };

    const wyczyscFiltry = () => {
        const resetFiltry = {
            tytul: '',
            gatunek: '',
            rok: '',
            cenaMax: '',
            minOcena: ''
        };
        setFiltry(resetFiltry);
        onFiltruj(resetFiltry);
    };

    return (
        <Form onSubmit={wyslijFormularz} className="mb-4 p-3 bg-light rounded shadow-sm">
            <Row className="g-3">
                <Col md={3}>
                    <Form.Control
                        type="text"
                        placeholder="Szukaj tytułu..."
                        name="tytul"
                        value={filtry.tytul}
                        onChange={zmienFiltr}
                    />
                </Col>
                <Col md={2}>
                    <Form.Select name="gatunek" value={filtry.gatunek} onChange={zmienFiltr}>
                        <option value="">Wszystkie gatunki</option>
                        {gatunki.map(g => (
                            <option key={g.gatunekId} value={g.nazwa}>{g.nazwa}</option>
                        ))}
                    </Form.Select>
                </Col>
                <Col md={2}>
                    <Form.Control
                        type="number"
                        placeholder="Rok"
                        name="rok"
                        value={filtry.rok}
                        onChange={zmienFiltr}
                    />
                </Col>
                <Col md={3}>
                    <InputGroup>
                        <InputGroup.Text>Max Cena</InputGroup.Text>
                        <Form.Control
                            type="number"
                            placeholder="np. 20"
                            name="cenaMax"
                            value={filtry.cenaMax}
                            onChange={zmienFiltr}
                        />
                        <InputGroup.Text>zł</InputGroup.Text>
                    </InputGroup>
                </Col>
                <Col md={2}>
                    <Form.Select name="minOcena" value={filtry.minOcena} onChange={zmienFiltr}>
                        <option value="">Min. Ocena</option>
                        <option value="1">1+ ⭐</option>
                        <option value="2">2+ ⭐</option>
                        <option value="3">3+ ⭐</option>
                        <option value="4">4+ ⭐</option>
                        <option value="5">5 ⭐</option>
                    </Form.Select>
                </Col>
                <Col md={12} className="d-flex justify-content-end gap-2">
                    <Button variant="secondary" onClick={wyczyscFiltry}>Wyczyść</Button>
                    <Button variant="primary" type="submit">Filtruj</Button>
                </Col>
            </Row>
        </Form>
    );
};

export default PasekFiltrow;
