import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { uzyjAutoryzacji } from '../context/KontekstAutoryzacji';

const Nawigacja = () => {
    const { uzytkownik, wyloguj } = uzyjAutoryzacji();
    const nawiguj = useNavigate();

    const obsluzWylogowanie = async () => {
        await wyloguj();
        nawiguj('/');
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
            <Container>
                <Navbar.Brand as={Link} to="/">Wypożyczalnia Filmów</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">Strona Główna</Nav.Link>
                        {uzytkownik && <Nav.Link as={Link} to="/moje-wypozyczenia">Moje Wypożyczenia</Nav.Link>}
                    </Nav>
                    <Nav>
                        {uzytkownik ? (
                            <>
                                <Navbar.Text className="me-3">
                                    Zalogowany jako: <strong>{uzytkownik.nick || uzytkownik.email}</strong>
                                </Navbar.Text>
                                <Button variant="outline-light" size="sm" onClick={obsluzWylogowanie}>
                                    Wyloguj
                                </Button>
                            </>
                        ) : (
                            <>
                                <Nav.Link as={Link} to="/logowanie">Logowanie</Nav.Link>
                                <Nav.Link as={Link} to="/rejestracja">Rejestracja</Nav.Link>
                            </>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default Nawigacja;
