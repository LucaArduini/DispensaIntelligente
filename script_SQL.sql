DROP SCHEMA IF EXISTS dispensaintelligente;
CREATE SCHEMA IF NOT EXISTS dispensaintelligente DEFAULT CHARACTER SET utf8 ;
USE dispensaintelligente ;

CREATE TABLE IF NOT EXISTS `Prodotti` (
    `Utente` VARCHAR(25) NOT NULL,
    `Nome` VARCHAR(25) NOT NULL,
    `Pezzi` VARCHAR(20), 
    `Categoria` VARCHAR(15), 
    `DataScadenza` DATE NOT NULL, 
    PRIMARY KEY(`Utente`, `Nome`, `DataScadenza`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO Prodotti VALUES ("luca", "Pesto"              , "1 barattolo", "Condimento", '2022-02-23');
INSERT INTO Prodotti VALUES ("luca", "Insalata iceberg"   , "400 g"      , "Verdura"   , '2022-02-25');
INSERT INTO Prodotti VALUES ("luca", "Hamburger di pollo" , "2"          , "Carne"     , '2022-02-27');
INSERT INTO Prodotti VALUES ("luca", "Patate da forno"    , "0,5 kg"     , "Contorno"  , '2022-03-08');
INSERT INTO Prodotti VALUES ("luca", "Sugo Pronto"        , "1 bottiglia", "Condimento", '2022-03-18');
INSERT INTO Prodotti VALUES ("luca", "Scatolette di tonno", "2"          , "Condimento", '2022-04-30');