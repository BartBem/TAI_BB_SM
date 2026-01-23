-- ============================================
-- DANE TESTOWE - WYPOŻYCZALNIA FILMÓW
-- ============================================

-- 1. UŻYTKOWNICY
INSERT INTO uzytkownik (email, nick, haslo_hash, imie, nazwisko, status) VALUES
('jan.kowalski@example.com', 'jankowal', '$2a$10$test', 'Jan', 'Kowalski', 'aktywny'),
('anna.nowak@example.com', 'annan', '$2a$10$test', 'Anna', 'Nowak', 'aktywny'),
('tom.hanks@example.com', 'tomh', '$2a$10$test', 'Tom', 'Hanks', 'aktywny'),
('admin@wypozyczalnia.pl', 'admin', '$2a$10$test', 'Admin', 'System', 'aktywny'),
('zablokowany@user.pl', 'badguy', '$2a$10$test', 'Zły', 'Użytkownik', 'zablokowany')
ON CONFLICT DO NOTHING;

-- 2. GATUNKI
INSERT INTO gatunek (nazwa) VALUES
('Akcja'),
('Komedia'),
('Dramat'),
('Sci-Fi'),
('Horror')
ON CONFLICT DO NOTHING;

-- 3. AKTORZY
INSERT INTO aktor (imie, nazwisko) VALUES
('Leonardo', 'DiCaprio'),
('Brad', 'Pitt'),
('Margot', 'Robbie'),
('Robert', 'De Niro'),
('Morgan', 'Freeman')
ON CONFLICT DO NOTHING;

-- 4. FILMY (Pierwsza 5-tka)
INSERT INTO film (tytul, opis, rok_produkcji, czas_trwania_min, cena_wypozyczenia, plakat_url) VALUES
('Incepcja', 'Czasy, gdy technologia pozwala na wchodzenie w sny innych ludzi.', 2010, 148, 15.00, 'https://upload.wikimedia.org/wikipedia/en/2/2e/Inception_%282010%29_theatrical_poster.jpg'),
('Wilk z Wall Street', 'Historia brokera, który żyje jak król dzięki oszustwom.', 2013, 180, 14.50, 'https://upload.wikimedia.org/wikipedia/en/1/1f/WallStreet2013poster.jpg'),
('Pewnego razu... w Hollywood', 'Aktor i jego dubler próbują odnaleźć się w Hollywood lat 60.', 2019, 161, 16.00, 'https://upload.wikimedia.org/wikipedia/en/a/a4/Once_Upon_a_Time_in_Hollywood_poster.jpg'),
('Siedem', 'Dwóch detektywów tropi seryjnego mordercę.', 1995, 127, 10.00, 'https://upload.wikimedia.org/wikipedia/en/6/68/Seven_%28movie%29_poster.jpg'),
('Skazani na Shawshank', 'Bankier niesłusznie skazany na dożywocie.', 1994, 142, 12.00, 'https://upload.wikimedia.org/wikipedia/en/8/81/ShawshankRedemptionMoviePoster.jpg')
ON CONFLICT DO NOTHING;

-- 5. RELACJE FILM-GATUNEK
INSERT INTO film_gatunek (film_id, gatunek_id) VALUES
(1, 1), (1, 4),
(2, 2), (2, 3), 
(3, 2), (3, 3), 
(4, 3), (4, 1), 
(5, 3)          
ON CONFLICT DO NOTHING;

-- 6. RELACJE FILM-AKTOR
INSERT INTO film_aktor (film_id, aktor_id) VALUES
(1, 1), 
(2, 1), (2, 3), 
(3, 1), (3, 2), (3, 3), 
(4, 2), (4, 5), 
(5, 5) 
ON CONFLICT DO NOTHING;

-- 7. WYPOŻYCZENIA
INSERT INTO wypozyczenie (uzytkownik_id, film_id, data_startu, data_konca, status) VALUES
(1, 1, NOW(), NOW() + INTERVAL '2 days', 'aktywne'),
(1, 2, NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days', 'wygasle'),
(2, 3, NOW(), NOW() + INTERVAL '2 days', 'oczekuje_oplacenia'),
(3, 4, NOW(), NOW() + INTERVAL '2 days', 'aktywne'),
(2, 5, NOW(), NOW() + INTERVAL '2 days', 'anulowane')
ON CONFLICT DO NOTHING;

-- 8. PŁATNOŚCI
INSERT INTO platnosc (wypozyczenie_id, kwota, metoda, status, data_oplacenia) VALUES
(1, 15.00, 'karta', 'oplacona', NOW()),
(2, 14.50, 'blik', 'oplacona', NOW() - INTERVAL '5 days'),
(3, 16.00, 'przelew', 'oczekuje', NULL),
(4, 10.00, 'karta', 'oplacona', NOW()),
(5, 12.00, NULL, 'odrzucona', NULL)
ON CONFLICT DO NOTHING;

-- 9. OCENY
INSERT INTO ocena (uzytkownik_id, film_id, wartosc) VALUES
(1, 1, 5),
(1, 2, 4),
(2, 3, 5),
(3, 4, 5),
(2, 5, 5)
ON CONFLICT DO NOTHING;

-- 10. KOMENTARZE
INSERT INTO komentarz (uzytkownik_id, film_id, tresc, data_dodania) VALUES
(1, 1, 'Niesamowity film, polecam każdemu!', '2025-05-15 14:30:00'),
(1, 2, 'Trochę za długi, ale świetna gra aktorska.', '2025-08-20 09:15:00'),
(2, 3, 'Tarantino w formie!', '2026-01-10 18:45:00'),
(3, 4, 'Klasyk kina, trzeba zobaczyć.', '2025-11-05 20:00:00'),
(2, 5, 'Najlepszy film w historii.', '2026-03-01 12:00:00')
ON CONFLICT DO NOTHING;

-- ============================================
-- DODATKOWE 50 FILMÓW - GENEROWANE AUTOMATYCZNIE
-- ============================================

INSERT INTO film (tytul, opis, rok_produkcji, czas_trwania_min, cena_wypozyczenia, plakat_url) VALUES
('Ojciec Chrzestny', 'Opowieść o rodzinie Corleone.', 1972, 175, 14.99, 'https://upload.wikimedia.org/wikipedia/en/1/1c/Godfather_ver1.jpg'),
('Mroczny Rycerz', 'Batman stawia czoła Jokerowi.', 2008, 152, 12.50, 'https://upload.wikimedia.org/wikipedia/commons/e/ea/The_Dark_Knight_Batman.jpg'),
('Dwunastu gniewnych ludzi', 'Ławnik próbuje przekonać pozostałych o niewinności oskarżonego.', 1957, 96, 9.99, 'https://upload.wikimedia.org/wikipedia/commons/b/b5/12_Angry_Men_%281957_film_poster%29.jpg'),
('Lista Schindlera', 'Prawdziwa historia Oskara Schindlera.', 1993, 195, 15.00, 'https://upload.wikimedia.org/wikipedia/en/3/38/Schindler%27s_List_movie.jpg'),
('Władca Pierścieni: Powrót króla', 'Ostateczna walka o Śródziemie.', 2003, 201, 16.99, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Wladca+Pierscieni+III'),
('Pulp Fiction', 'Przeplatające się losy przestępców.', 1994, 154, 13.00, 'https://upload.wikimedia.org/wikipedia/en/3/3b/Pulp_Fiction_%281994%29_poster.jpg'),
('Władca Pierścieni: Drużyna Pierścienia', 'Początek wyprawy Frodo.', 2001, 178, 15.99, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Wladca+Pierscieni+I'),
('Forrest Gump', 'Historia niezwykłego człowieka.', 1994, 142, 14.00, 'https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg'),
('Podziemny krąg', 'Znudzony pracownik biurowy zakłada klub walki.', 1999, 139, 12.99, 'https://upload.wikimedia.org/wikipedia/en/f/fc/Fight_Club_poster.jpg'),
('Dzień Świra', 'Adaś Miauczyński, sfrustrowany intelektualista, przeżywa kolejny dzień swojego życia.', 2002, 93, 11.00, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Dzien+Swira'),
('Władca Pierścieni: Dwie wieże', 'Kontynuacja podróży Frodo i Sama.', 2002, 179, 15.99, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Wladca+Pierscieni+II'),
('Gwiezdne wojny: Część V - Imperium kontratakuje', 'Luke Skywalker szkoli się na Jedi.', 1980, 124, 13.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Gwiezdne+Wojny+V'),
('Matrix', 'Haker odkrywa prawdę o rzeczywistości.', 1999, 136, 12.00, 'https://upload.wikimedia.org/wikipedia/commons/c/c5/The-matrix-logo.svg'),
('Chłopcy z ferajny', 'Historia gangstera Henryego Hilla.', 1990, 146, 13.00, 'https://upload.wikimedia.org/wikipedia/en/7/7b/Goodfellas.jpg'),
('Lot nad kukulczym gniazdem', 'Przestępca trafia do szpitala psychiatrycznego.', 1975, 133, 11.99, 'https://upload.wikimedia.org/wikipedia/en/2/26/One_Flew_Over_the_Cuckoo%27s_Nest_poster.jpg'),
('Siedmiu samurajów', 'Wioska wynajmuje samurajów do obrony.', 1954, 207, 10.00, 'https://upload.wikimedia.org/wikipedia/commons/b/ba/Seven_Samurai_poster.jpg'),
('Miasto Boga', 'Brutalne życie w fawelach Rio de Janeiro.', 2002, 130, 12.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Miasto+Boga'),
('Życie jest piękne', 'Ojciec chroni syna w obozie koncentracyjnym humor.', 1997, 116, 13.00, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Zycie+Jest+Piekne'),
('Milczenie owiec', 'Agentka FBI szuka pomocy u Hannibala Lectera.', 1991, 118, 14.00, 'https://upload.wikimedia.org/wikipedia/en/8/86/The_Silence_of_the_Lambs_poster.jpg'),
('Gwiezdne wojny: Część IV - Nowa nadzieja', 'Luke Skywalker dołącza do Rebelii.', 1977, 121, 13.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Gwiezdne+Wojny+IV'),
('Szeregowiec Ryan', 'Misja ratunkowa podczas II wojny światowej.', 1998, 169, 14.50, 'https://upload.wikimedia.org/wikipedia/en/a/ac/Saving_Private_Ryan_poster.jpg'),
('Kiler', 'Taksówkarz Jurek Kiler zostaje omyłkowo wzięty za płatnego zabójcę.', 1997, 104, 12.00, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Kiler'),
('Zielona mila', 'Niezwykły więzień w celi śmierci.', 1999, 189, 14.00, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Zielona+Mila'),
('Interstellar', 'Podróż przez tunel czasoprzestrzenny.', 2014, 169, 15.50, 'https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg'),
('Parazyt', 'Biedna rodzina infiltruje dom bogaczy.', 2019, 132, 16.00, 'https://upload.wikimedia.org/wikipedia/en/5/53/Parasite_%282019_film%29.png'),
('Leon zawodowiec', 'Płatny zabójca opiekuje się dziewczynką.', 1994, 110, 12.50, 'https://upload.wikimedia.org/wikipedia/en/0/03/Leon-poster.jpg'),
('Król Lew', 'Młody lew musi odzyskać tron.', 1994, 88, 15.00, 'https://upload.wikimedia.org/wikipedia/en/3/3d/The_Lion_King_poster.jpg'),
('Pianista', 'Losy pianisty w okupowanej Warszawie.', 2002, 150, 14.00, 'https://upload.wikimedia.org/wikipedia/en/a/a6/The_Pianist_movie.jpg'),
('Terminator 2: Dzień sądu', 'Cyborg chroni przyszłego przywódcę ludzi.', 1991, 137, 13.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Terminator+2'),
('Powrót do przyszłości', 'Nastolatek przenosi się w czasie do lat 50.', 1985, 116, 12.00, 'https://upload.wikimedia.org/wikipedia/en/d/d2/Back_to_the_Future.jpg'),
('Harry Potter i Kamień Filozoficzny', 'Chłopiec odkrywa, że jest czarodziejem.', 2001, 152, 14.99, 'https://upload.wikimedia.org/wikipedia/en/7/7a/Harry_Potter_and_the_Philosopher%27s_Stone_banner.jpg'),
('Gladiator', 'Generał rzymski staje się gladiatorem.', 2000, 155, 14.50, 'https://upload.wikimedia.org/wikipedia/en/f/fb/Gladiator_%282000_film_poster%29.png'),
('Memento', 'Mężczyzna z zanikiem pamięci krótkotrwałej szuka zabójcy.', 2000, 113, 12.50, 'https://upload.wikimedia.org/wikipedia/en/c/c7/Memento_poster.jpg'),
('Apokalipsa', 'Misja zabicia zbuntowanego pułkownika w Wietnamie.', 1979, 147, 13.00, 'https://upload.wikimedia.org/wikipedia/en/c/c2/Apocalypse_Now_poster.jpg'),
('Poszukiwacze zaginionej Arki', 'Indiana Jones szuka biblijnej Arki.', 1981, 115, 13.50, 'https://upload.wikimedia.org/wikipedia/en/4/4c/Raiders_of_the_Lost_Ark.jpg'),
('Django', 'Wyzwolony niewolnik ratuje żonę.', 2012, 165, 15.00, 'https://upload.wikimedia.org/wikipedia/en/8/8b/Django_Unchained_Poster.jpg'),
('WALL·E', 'Robot sprzątający wyrusza w kosmos.', 2008, 98, 13.00, 'https://upload.wikimedia.org/wikipedia/commons/e/e5/WALL%C2%B7E_Logo.svg'),
('Inni', 'Kobieta z dziećmi czeka na męża w nawiedzonym domu.', 2001, 101, 11.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Inni'),
('Joker', 'Komik popada w szaleństwo.', 2019, 122, 16.50, 'https://upload.wikimedia.org/wikipedia/en/e/e1/Joker_%282019_film%29_poster.jpg'),
('Lśnienie', 'Pisarz traci zmysły w odciętym hotelu.', 1980, 146, 12.50, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Lsnienie'),
('Avengers: Koniec gry', 'Ostateczne starcie z Thanosem.', 2019, 181, 17.00, 'https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg'),
('Obcy - 8. pasażer Nostromo', 'Załoga statku walczy z obcą formą życia.', 1979, 117, 13.00, 'https://upload.wikimedia.org/wikipedia/en/c/c3/Alien_movie_poster.jpg'),
('Spider-Man: Uniwersum', 'Miles Morales poznaje innych Spider-Manów.', 2018, 117, 15.50, 'https://upload.wikimedia.org/wikipedia/en/f/fa/Spider-Man_Into_the_Spider-Verse_poster.png'),
('Coco', 'Chłopiec trafia do Krainy Umarłych.', 2017, 105, 14.50, 'https://upload.wikimedia.org/wikipedia/en/9/98/Coco_%282017_film%29_poster.jpg'),
('Whiplash', 'Młody perkusista i jego wymagający nauczyciel.', 2014, 106, 13.50, 'https://upload.wikimedia.org/wikipedia/en/0/01/Whiplash_poster.jpg'),
('Amelia', 'Nieśmiała paryżanka zmienia życie innych.', 2001, 122, 12.00, 'https://upload.wikimedia.org/wikipedia/en/5/53/Amelie_poster.jpg'),
('Top Gun: Maverick', 'Pilot wraca do szkoły Top Gun.', 2022, 130, 18.00, 'https://upload.wikimedia.org/wikipedia/en/1/13/Top_Gun_Maverick_Poster.jpg'),
('Shrek', 'Ogr i osioł ratują księżniczkę.', 2001, 90, 13.00, 'https://placehold.co/400x600/1a1a1a/ffffff?text=Shrek'),
('Grand Budapest Hotel', 'Przygody konsjerża w luksusowym hotelu.', 2014, 99, 14.00, 'https://upload.wikimedia.org/wikipedia/commons/e/e5/The_Grand_Budapest_Hotel_movie_logo.png'),
('Truman Show', 'Mężczyzna odkrywa, że jego życie to reality show.', 1998, 103, 12.50, 'https://upload.wikimedia.org/wikipedia/commons/e/ec/The_Truman_Show.png')
ON CONFLICT DO NOTHING;

-- Przypisanie losowych gatunków
INSERT INTO film_gatunek (film_id, gatunek_id) VALUES
-- Ojciec Chrzestny (ID: 6) -> Dramat (3)
(6, 3), 
-- Mroczny Rycerz (ID: 7) -> Akcja (1)
(7, 1), 
-- 12 Gniewnych (ID: 8) -> Dramat (3)
(8, 3), 
-- Lista Schindlera (ID: 9) -> Dramat (3)
(9, 3), 
-- Wladca P. 3 (ID: 10) -> Sci-Fi (4)
(10, 4), 
-- Pulp Fiction (ID: 11) -> Dramat (3)
(11, 3),
-- Wladca P. 1 (ID: 12) -> Sci-Fi (4)
(12, 4), 
-- Forrest Gump (ID: 13) -> Komedia (2)
(13, 2), 
-- Fight Club (ID: 14) -> Dramat (3)
(14, 3), 
-- Dzień Świra (ID 15) -> Komedia (2), Dramat (3)
(15, 2), (15, 3),
-- Wladca P. 2 (ID: 16) -> Sci-Fi (4)
(16, 4), 
-- SW 5 (ID: 17) -> Sci-Fi (4)
(17, 4), 
-- Matrix (ID: 18) -> Sci-Fi (4)
(18, 4), 
-- Goodfellas (ID: 19) -> Dramat (3)
(19, 3), 
-- Lot nad... (ID: 20) -> Dramat (3)
(20, 3), 
-- Siedmiu Samurajow (ID: 21) -> Akcja (1)
(21, 1), 
-- Miasto Boga (ID: 22) -> Dramat (3)
(22, 3), 
-- Zycie jest piekne (ID: 23) -> Komedia (2)
(23, 2), 
-- Milczenie owiec (ID: 24) -> Horror (5)
(24, 5), 
-- SW 4 (ID: 25) -> Sci-Fi (4)
(25, 4), 
-- Ryan (ID: 26) -> Akcja (1)
(26, 1), 
-- Kiler (ZAMIAST SPIRITED AWAY - ID: 27) -> Komedia (2) + Akcja (1)
(27, 2), (27, 1),
-- Zielona Mila (ID: 28) -> Dramat (3)
(28, 3), 
-- Interstellar (ID: 29) -> Sci-Fi (4)
(29, 4), 
-- Parasite (ID: 30) -> Dramat (3)
(30, 3), 
-- Leon (ID: 31) -> Akcja (1)
(31, 1), 
-- Krol Lew (ID: 32) -> Komedia (2 - animacja)
(32, 2), 
-- Pianista (ID: 33) -> Dramat (3)
(33, 3), 
-- T2 (ID: 34) -> Sci-Fi (4)
(34, 4), 
-- BTTF (ID: 35) -> Sci-Fi (4)
(35, 4), 
-- Harry Potter 1 (ID: 36) -> Sci-Fi (4)
(36, 4), 
-- Gladiator (ID: 37) -> Akcja (1)
(37, 1), 
-- Memento (ID: 38) -> Dramat (3)
(38, 3), 
-- Apokalipsa (ID: 39) -> Dramat (3)
(39, 3), 
-- Indy Jones (ID: 40) -> Akcja (1)
(40, 1), 
-- Django (ID: 41) -> Akcja (1)
(41, 1), 
-- WALL-E (ID: 42) -> Sci-Fi (4)
(42, 4), 
-- Inni (ID: 43) -> Horror (5)
(43, 5), 
-- Joker (ID: 44) -> Dramat (3)
(44, 3), 
-- Lsnienie (ID: 45) -> Horror (5)
(45, 5), 
-- Avengers (ID: 46) -> Akcja (1)
(46, 1), 
-- Obcy (ID: 47) -> Horror (5)
(47, 5), 
-- Spider-Verse (ID: 48) -> Akcja (1)
(48, 1), 
-- Coco (ID: 49) -> Komedia (2)
(49, 2), 
-- Whiplash (ID: 50) -> Dramat (3)
(50, 3), 
-- Amelia (ID: 51) -> Komedia (2)
(51, 2), 
-- Maverick (ID: 52) -> Akcja (1)
(52, 1), 
-- Shrek (ID: 53) -> Komedia (2)
(53, 2), 
-- GBH (ID: 54) -> Komedia (2)
(54, 2), 
-- Truman Show (ID: 55) -> Dramat (3)
(55, 3)
ON CONFLICT DO NOTHING;

-- ============================================
-- AKTUALIZACJA GRAFIK DLA ISTNIEJĄCYCH FILMÓW (WYMUSZENIE NADPISANIA PLACEHOLDERÓW)
-- ============================================

UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/b/ba/Seven_Samurai_poster.jpg' WHERE tytul = 'Siedmiu samurajów';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/c/c5/The-matrix-logo.svg' WHERE tytul = 'Matrix';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/e/e5/WALL%C2%B7E_Logo.svg' WHERE tytul = 'WALL·E';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/e/ea/The_Dark_Knight_Batman.jpg' WHERE tytul = 'Mroczny Rycerz';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/e/e5/The_Grand_Budapest_Hotel_movie_logo.png' WHERE tytul = 'Grand Budapest Hotel';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/commons/e/ec/The_Truman_Show.png' WHERE tytul = 'Truman Show';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/en/2/2e/Inception_%282010%29_theatrical_poster.jpg' WHERE tytul = 'Incepcja';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/en/1/1f/WallStreet2013poster.jpg' WHERE tytul = 'Wilk z Wall Street';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/en/a/a4/Once_Upon_a_Time_in_Hollywood_poster.jpg' WHERE tytul = 'Pewnego razu... w Hollywood';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/en/6/68/Seven_%28movie%29_poster.jpg' WHERE tytul = 'Siedem';
UPDATE film SET plakat_url = 'https://upload.wikimedia.org/wikipedia/en/8/81/ShawshankRedemptionMoviePoster.jpg' WHERE tytul = 'Skazani na Shawshank';
