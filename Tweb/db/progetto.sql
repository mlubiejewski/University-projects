-- phpMyAdmin SQL Dump
-- version 4.0.10.12
-- http://www.phpmyadmin.net
--
-- Host: 127.12.177.2:3306
-- Generato il: Nov 07, 2016 alle 17:08
-- Versione del server: 5.5.52
-- Versione PHP: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `progetto`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Birre`
--

CREATE TABLE IF NOT EXISTS `Birre` (
  `Nome` varchar(20) NOT NULL,
  `Tipo` varchar(10) NOT NULL,
  `Paese` varchar(20) NOT NULL,
  `Gradi` float NOT NULL,
  `Descrizione` varchar(1000) NOT NULL,
  PRIMARY KEY (`Nome`)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

--
-- Dump dei dati per la tabella `Birre`
--

INSERT INTO `Birre` (`Nome`, `Tipo`, `Paese`, `Gradi`, `Descrizione`) VALUES
('8.6 Red', 'Rossa', 'Olanda', 7.9, 'Una tinta fuori del comune per una birra delicata ma potente.\r\nUn colore sublime e intenso con varie sfumature di rosso cangianti secondo l''illuminazione per un aspetto in costante evoluzione e sempre unico!\r\nUn bouquet profumato che rivela saporiti aromi fin dalle prime note con un tocco di caramello.\r\nUna birra ricca e grande con note di malto e caramello per una sensazione ambivalente ma durevole, robusta e delicata, con un leggerissimo gusto amaro. Il sapore di un''esperienza nuova?'),
('Becks', 'Bionda', 'Germania', 5, 'La Beck''s, tedesca della Brauerei Beck & Co., e'' una Premium Pilsner biondo limpido, bassa fermentazione, effervescenza intensa, profumo molto luppolato, corpo ben strutturato, aroma e schiuma fine. Ma la Beck''s e'' una birra che non conosce vie di mezzo e o la si ama o la si odia - ed e'' il sapore che lega il suo destino: cio'' che la contraddistingue da ogni altra e'' infatti un equilibrato gusto amaro accompagnato da un retrogusto amarognolo piuttosto persistente. E'' proprio l''elevata presenza del luppolo a conferirgli questo sapore particolare. Nonche'' la selezione di un luppolo di altissima qualita'', quello che proviene da Hallertau, la zona di coltivazione di questa pianta piu'' famosa al mondo.'),
('Carlsberg', 'Bionda', 'Danimarca', 5, 'Famosissima Pils danese chiara, a bassa fermentazione, caratterizzata dal tipico aroma di luppolo. Una birra che ha aperto un capitolo nella storia della produzione, scoprendo nel 1880 un sistema di fermentazione ancor oggi all''avanguardia.'),
('Corona', 'Bionda', 'Messico', 4.6, 'Birra bionda messicana da bere con una fetta di lime o limone per moltiplicarne l''effetto dissetante'),
('Dark Depths', 'Scura', 'USA', 7.6, 'Across the cold and brackish waters of the Baltic, the English porter was transformed, from a mild ale to a dark and complex lager that confounds definition. Immersed in dark, roasted malts and a bold citrus hop character, these big and contrasting flavors are brought together with the smoothness of a lager for a brew that''s rugged, mysterious, and full of flavor.'),
('Daughter Of Autumn', 'Rossa', 'Scozia', 7.5, 'Strong scotch ale corposa e ben strutturata, di colore bruno, caratterizzata dal malto torbato, la cui affumicatura rimane morbida. Ha grado alcolico sostenuto e buon equilibrio grazie ai luppoli usati con parsimonia. Perfetta in abbinamento\r\na formaggi erborinati.\r\nOttima compagna per le sere piovose.'),
('Forst Sixtus', 'Rossa', 'Italia', 6.5, 'Birra rossa doppio malto, nasce nel rispetto della originale tradizione degli antichi monasteri europei. Si accompagna bene a formaggi e dessert'),
('Guiness Original', 'Scura', 'Irlanda', 4.2, 'Il colore e'' molto scuro, con una schiuma beige, e sapori e profumi sono dominati un aroma intensamente tostato cui si accostano caffe'' e cacao. La presenza di una buona luppolatura e'' nettamente percepibile, e il finale risulta secco e amarognolo.'),
('Heineken', 'Bionda', 'Olanda', 5, 'Ovunque nel mondo, e'' bello riconoscere qualcosa di rinfrescante. Quella bottiglia verde, la stella rossa, la "e" sorridente...come un improvviso benvenuto da parte di una vecchia amica. La birra Heineken fresca e di alta qualita''. Apprezzata ovunque dal 1873.\n'),
('Murrays Dark Knight', 'Scura', 'Australia', 4.5, 'Murray''s Dark Knight is made in the traditional Porter style from six specialty malts. It is full bodied, rich and complex. The beer is a deep ruby colour, with a creamy, off-white head.Murray''s Dark Knight has an aroma of caramel and bittersweet chocolate and finishes with a balanced bitterness.'),
('Stella Artois', 'Bionda', 'Belgio', 5.2, 'Stella Artois, uno dei brand internazionali piu'' affermati, che rappresenta il sinonimo della birra di ottima qualita'' e valore nel mondo, dal settembre 2006, grazie alla birreria di Apatin, e'' presente anche sul nostro mercato. \nDai suoi inizi, dal lontano 1366 fino ad oggi, la birra Stella Artois e'' diventata icona mondiale tra le birre, con il suo imballaggio caratteristico e il bicchiere in vetro.'),
('Tennets Super', 'Bionda', 'Scozia', 9, 'La Tennent''s Super viene considerata l''antesignana delle strong lager scozzesi, caratterizzate appunto dal grado alcolico elevato. Si presenta di colore giallo ramato e produce una schiuma scarsa e poco persistente. L''aroma e'' intenso e dominato dal profumo di malto e di mela. Assaporandola si coglie un gusto iniziale dolce che si stempera subito nell''amaro e quindi nell''intensita'' alcolica (9%), per ritornare nel finale alle note zuccherine del caramello.'),
('Tuborg', 'Bionda', 'Danimarca', 5, 'Tuborg e'' una premium lager presente dal 1873 in piu'' di 70 paesi. In tutto il mondo e'' riconosciuta per essere una birra dissetante con un gusto secco, amaro ridotto e moderato grado alcolico (5%).');

-- --------------------------------------------------------

--
-- Struttura della tabella `Preferiti`
--

CREATE TABLE IF NOT EXISTS `Preferiti` (
  `Utente` varchar(30) NOT NULL,
  `Birra` varchar(20) NOT NULL,
  PRIMARY KEY (`Utente`,`Birra`),
  KEY `Birra` (`Birra`)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

--
-- Dump dei dati per la tabella `Preferiti`
--

INSERT INTO `Preferiti` (`Utente`, `Birra`) VALUES
('Mat2', '8.6 Red'),
('mat5', '8.6 Red'),
('giaruffo', 'Becks'),
('mat5', 'Becks'),
('Mat', 'Carlsberg'),
('Mat20', 'Carlsberg'),
('mat5', 'Carlsberg'),
('test', 'Carlsberg'),
('giaruffo', 'Corona'),
('James', 'Corona'),
('Mat', 'Corona'),
('Mat2', 'Corona'),
('Mat20', 'Corona'),
('mat5', 'Corona'),
('Prova', 'Corona'),
('test', 'Corona'),
('Mat2', 'Daughter Of Autumn'),
('mat5', 'Daughter Of Autumn'),
('test', 'Daughter Of Autumn'),
('mat5', 'Forst Sixtus'),
('Mat2', 'Guiness Original'),
('Mat2', 'Heineken'),
('mat5', 'Heineken'),
('giaruffo', 'Murrays Dark Knight'),
('Mat2', 'Murrays Dark Knight'),
('mat5', 'Murrays Dark Knight'),
('pino', 'Murrays Dark Knight'),
('stefano', 'Murrays Dark Knight'),
('giaruffo', 'Stella Artois'),
('Mat2', 'Stella Artois'),
('mat5', 'Stella Artois'),
('rammar', 'Stella Artois'),
('giaruffo', 'Tennets Super'),
('Mat2', 'Tennets Super'),
('Mat20', 'Tennets Super'),
('mat5', 'Tennets Super'),
('mat5', 'Tuborg');

-- --------------------------------------------------------

--
-- Struttura della tabella `Recensioni`
--

CREATE TABLE IF NOT EXISTS `Recensioni` (
  `Utente` varchar(30) NOT NULL,
  `Birra` varchar(20) NOT NULL,
  `Testo` varchar(300) NOT NULL,
  `Voto` int(11) NOT NULL,
  PRIMARY KEY (`Utente`,`Birra`),
  KEY `Birra` (`Birra`)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

--
-- Dump dei dati per la tabella `Recensioni`
--

INSERT INTO `Recensioni` (`Utente`, `Birra`, `Testo`, `Voto`) VALUES
('giaruffo', 'Becks', 'fasdfds', 1),
('Mat', 'Becks', 'OOK', 7),
('Mat', 'Corona', 'Buona', 8),
('Mat', 'Daughter Of Autumn', '', 3),
('Mat', 'Forst Sixtus', '', 10),
('Mat', 'Murrays Dark Knight', 'OK', 8),
('Mat', 'Stella Artois', '', 1),
('Mat', 'Tennets Super', 'yuiasqjds', 1),
('Mat', 'Tuborg', 'Ottima birra per ogni occasione', 10),
('Mat2', 'Carlsberg', 'Molto buona', 9),
('Mat2', 'Daughter Of Autumn', 'Bisognerebbe avere un po'' di eseprienza nelle recensioni', 7),
('mat5', 'Murrays Dark Knight', 'OK', 6),
('pino', 'Murrays Dark Knight', 'Not bad', 6),
('Prova', 'Corona', 'Non male', 6),
('test', 'Carlsberg', 'OK', 6);

-- --------------------------------------------------------

--
-- Struttura della tabella `Utenti`
--

CREATE TABLE IF NOT EXISTS `Utenti` (
  `Username` varchar(30) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Email` varchar(30) NOT NULL,
  `Eta` int(11) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

--
-- Dump dei dati per la tabella `Utenti`
--

INSERT INTO `Utenti` (`Username`, `Password`, `Email`, `Eta`) VALUES
('abc', 'abc', 'abc', 20),
('abc1', 'abc1', 'abc1', 20),
('giaruffo', 'giaruffo', 'giaruffo@gmail.com', 44),
('James', 'Bond', 'bond', 35),
('Mario', 'mario', 'mario@gmail.com', 24),
('Mat', 'mat', 'mat@gmail.com', 22),
('Mat10', 'mat10', 'mat10', 22),
('Mat11', 'mat11', 'mat11', 22),
('Mat2', 'mat2', 'mat2', 22),
('Mat20', 'mat20', 'mat20', 22),
('Mat3', 'mat3', 'mat3', 22),
('Mat4', 'mat4', 'mat4', 22),
('Mat5', 'mat5', 'mat5', 18),
('Pino', 'pino', 'pino25@gmail.com', 25),
('Prova', 'Prova', 'prova@prova.com', 20),
('rammar', 'test', 'rammar@test.com', 21),
('Stefano', 'pippo76', 'S.gili@episrl.it', 18),
('test', 'test', 'test@test.com', 20);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `Preferiti`
--
ALTER TABLE `Preferiti`
  ADD CONSTRAINT `Preferiti_ibfk_1` FOREIGN KEY (`Utente`) REFERENCES `Utenti` (`Username`) ON DELETE CASCADE,
  ADD CONSTRAINT `Preferiti_ibfk_2` FOREIGN KEY (`Birra`) REFERENCES `Birre` (`Nome`) ON DELETE CASCADE;

--
-- Limiti per la tabella `Recensioni`
--
ALTER TABLE `Recensioni`
  ADD CONSTRAINT `Recensioni_ibfk_1` FOREIGN KEY (`Utente`) REFERENCES `Utenti` (`Username`) ON DELETE CASCADE,
  ADD CONSTRAINT `Recensioni_ibfk_2` FOREIGN KEY (`Birra`) REFERENCES `Birre` (`Nome`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
