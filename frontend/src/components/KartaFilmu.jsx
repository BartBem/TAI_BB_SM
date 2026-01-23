import { Card, Button, Badge } from 'react-bootstrap';
import { Link } from 'react-router-dom';

/**
 * KARTA FILMU
 * 
 * Wyświetla pojedynczy film w siatce na stronie głównej.
 * Kliknięcie w kartę lub "Zobacz więcej" prowadzi do szczegółów.
 */
const KartaFilmu = ({ film }) => {
    return (
        <Card className="h-100 shadow-sm border-0">
            {/* Plakat z rokiem produkcji */}
            <Link to={`/film/${film.filmId}`} style={{ textDecoration: 'none' }}>
                <div style={{ position: 'relative' }}>
                    <Card.Img
                        variant="top"
                        src={film.plakatUrl || "https://via.placeholder.com/300x450?text=Brak+Plakatu"}
                        style={{ height: '350px', objectFit: 'cover' }}
                    />
                    <Badge
                        bg="secondary"
                        style={{ position: 'absolute', top: '10px', right: '10px' }}>
                        {film.rokProdukcji}
                    </Badge>
                </div>
            </Link>

            <Card.Body className="d-flex flex-column">
                <Link to={`/film/${film.filmId}`} className="text-decoration-none text-dark">
                    <Card.Title>{film.tytul}</Card.Title>
                </Link>

                <Card.Text className="text-muted small mb-2">
                    {film.gatunki?.map(g => g.nazwa).join(', ') || 'Brak gatunku'}
                    {film.czasTrwaniaMin && ` | ${film.czasTrwaniaMin} min`}
                </Card.Text>

                <div className="mt-auto d-flex justify-content-between align-items-center">
                    <span className="h5 mb-0 text-success">
                        {film.cenaWypozyczenia?.toFixed(2)} zł
                    </span>
                    <Button
                        as={Link}
                        to={`/film/${film.filmId}`}
                        variant="primary"
                        size="sm"
                    >
                        Zobacz więcej
                    </Button>
                </div>
            </Card.Body>
        </Card>
    );
};

export default KartaFilmu;

