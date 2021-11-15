-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema book_library
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema book_library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `book_library` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `book_library` ;

-- -----------------------------------------------------
-- Table `book_library`.`authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`authors` (
  `author_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `author` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`author_id`),
  UNIQUE INDEX `author_UNIQUE` (`author` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 66
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`books` (
  `book_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `cover` VARCHAR(45) NULL DEFAULT NULL,
  `title` VARCHAR(255) NOT NULL,
  `publisher` VARCHAR(45) NOT NULL,
  `publish_date` DATE NOT NULL,
  `page_count` SMALLINT NOT NULL,
  `isbn` VARCHAR(45) NOT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `total_amount` SMALLINT NOT NULL,
  `remaining_amount` SMALLINT NOT NULL,
  `status` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE INDEX `book_id_UNIQUE` (`book_id` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 99
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`book_authors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`book_authors` (
  `id` SMALLINT NOT NULL AUTO_INCREMENT,
  `book_id_fk` SMALLINT NOT NULL,
  `author_id_fk` SMALLINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_book_authors` (`book_id_fk` ASC, `author_id_fk` ASC) VISIBLE,
  INDEX `fk_book_authors_books_idx` (`book_id_fk` ASC) VISIBLE,
  INDEX `fk_book_authors_authors_idx` (`author_id_fk` ASC) VISIBLE,
  CONSTRAINT `fk_book_authors_authors`
    FOREIGN KEY (`author_id_fk`)
    REFERENCES `book_library`.`authors` (`author_id`)
    ON DELETE RESTRICT,
  CONSTRAINT `fk_book_authors_books`
    FOREIGN KEY (`book_id_fk`)
    REFERENCES `book_library`.`books` (`book_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 120
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`genres`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`genres` (
  `genre_id` SMALLINT NOT NULL AUTO_INCREMENT,
  `genre` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE INDEX `genre_UNIQUE` (`genre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 114
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`book_genres`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`book_genres` (
  `id` SMALLINT NOT NULL AUTO_INCREMENT,
  `book_id_fk` SMALLINT NOT NULL,
  `genre_id_fk` SMALLINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_book_genres` (`book_id_fk` ASC, `genre_id_fk` ASC) VISIBLE,
  INDEX `fk_books_idx` (`book_id_fk` ASC) VISIBLE,
  INDEX `fk_genres_idx` (`genre_id_fk` ASC) VISIBLE,
  CONSTRAINT `fk_book_genres_books`
    FOREIGN KEY (`book_id_fk`)
    REFERENCES `book_library`.`books` (`book_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_book_genres_genres`
    FOREIGN KEY (`genre_id_fk`)
    REFERENCES `book_library`.`genres` (`genre_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 286
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`readers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`readers` (
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `book_library`.`borrow_records`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `book_library`.`borrow_records` (
  `borrow_record_id` BIGINT NOT NULL AUTO_INCREMENT,
  `borrow_date` DATETIME NOT NULL,
  `due_date` DATETIME NOT NULL,
  `return_date` DATETIME NULL DEFAULT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  `comment` VARCHAR(255) NULL DEFAULT NULL,
  `book_id_fk` SMALLINT NOT NULL,
  `reader_email_fk` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`borrow_record_id`),
  UNIQUE INDEX `borrow_record_id_UNIQUE` (`borrow_record_id` ASC) VISIBLE,
  INDEX `fk_borrow_records_books_idx` (`book_id_fk` ASC) VISIBLE,
  INDEX `fk_borrow_records_readers` (`reader_email_fk` ASC) VISIBLE,
  CONSTRAINT `fk_borrow_records_books`
    FOREIGN KEY (`book_id_fk`)
    REFERENCES `book_library`.`books` (`book_id`),
  CONSTRAINT `fk_borrow_records_readers`
    FOREIGN KEY (`reader_email_fk`)
    REFERENCES `book_library`.`readers` (`email`))
ENGINE = InnoDB
AUTO_INCREMENT = 78
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

insert into readers (email, name) values ('amays0@github.com', 'Adelbert');
insert into readers (email, name) values ('mchartman1@github.io', 'Maxy');
insert into readers (email, name) values ('smidghall2@networkadvertising.org', 'Sib');
insert into readers (email, name) values ('bbutteris3@google.com.hk', 'Bernette');
insert into readers (email, name) values ('areeson4@wikispaces.com', 'Arabel');
insert into readers (email, name) values ('rlowten5@si.edu', 'Reginauld');
insert into readers (email, name) values ('wdessant6@ifeng.com', 'Walsh');
insert into readers (email, name) values ('gbosley7@slashdot.org', 'Gerianna');
insert into readers (email, name) values ('hmaffini8@adobe.com', 'Hilary');
insert into readers (email, name) values ('jwithrington9@fda.gov', 'Joellyn');
insert into readers (email, name) values ('ahundya@netscape.com', 'Adriena');
insert into readers (email, name) values ('lspeakmanb@reuters.com', 'Linda');
insert into readers (email, name) values ('rgreensetc@comcast.net', 'Rachel');
insert into readers (email, name) values ('glapishd@zimbio.com', 'Garnette');
insert into readers (email, name) values ('mbovairde@umich.edu', 'Maridel');
insert into readers (email, name) values ('pconahyf@livejournal.com', 'Panchito');
insert into readers (email, name) values ('boakdeng@thetimes.co.uk', 'Bernhard');
insert into readers (email, name) values ('odebeauchamph@meetup.com', 'Osmund');
insert into readers (email, name) values ('jmacnamarai@mapy.cz', 'Janene');
insert into readers (email, name) values ('fsirmanj@usnews.com', 'Faye');
insert into readers (email, name) values ('snewdickk@latimes.com', 'Sib');
insert into readers (email, name) values ('tstammirsl@google.co.uk', 'Tonya');
insert into readers (email, name) values ('tmottonm@boston.com', 'Tamma');
insert into readers (email, name) values ('jhaversumn@bing.com', 'Jessamyn');
insert into readers (email, name) values ('mbanburyo@meetup.com', 'Maisie');
insert into readers (email, name) values ('atimsp@ihg.com', 'Amber');
insert into readers (email, name) values ('mpoloq@deliciousdays.com', 'Martelle');
insert into readers (email, name) values ('bmonkleighr@bizjournals.com', 'Benjie');
insert into readers (email, name) values ('cphysics@xrea.com', 'Clemmy');
insert into readers (email, name) values ('mbenjamint@t-online.de', 'Marilyn');
insert into readers (email, name) values ('ameggisonu@bizjournals.com', 'Arlana');
insert into readers (email, name) values ('tmalimv@army.mil', 'Trish');
insert into readers (email, name) values ('rcraigheadw@liveinternet.ru', 'Rubin');
insert into readers (email, name) values ('rfeehelyx@github.com', 'Riobard');
insert into readers (email, name) values ('bbennallcky@aol.com', 'Beret');
insert into readers (email, name) values ('lannicez@gizmodo.com', 'Lillian');
insert into readers (email, name) values ('etitcumb10@de.vu', 'Ealasaid');
insert into readers (email, name) values ('cdyerson11@google.com.br', 'Claribel');
insert into readers (email, name) values ('rwonter12@sogou.com', 'Roselia');
insert into readers (email, name) values ('spickles13@loc.gov', 'Shelba');
insert into readers (email, name) values ('gmclaughlan14@disqus.com', 'Glenn');
insert into readers (email, name) values ('apollie15@msu.edu', 'Alexandro');
insert into readers (email, name) values ('ckneesha16@newsvine.com', 'Cletus');
insert into readers (email, name) values ('dlindmark17@bluehost.com', 'Donica');
insert into readers (email, name) values ('hdenley18@cargocollective.com', 'Helene');
insert into readers (email, name) values ('tzohrer19@123-reg.co.uk', 'Tallou');
insert into readers (email, name) values ('hbrownhall1a@joomla.org', 'Hedvig');
insert into readers (email, name) values ('ttocknell1b@symantec.com', 'Trumaine');
insert into readers (email, name) values ('cjenkerson1c@gravatar.com', 'Correy');
insert into readers (email, name) values ('thaws1d@goo.gl', 'Terrance');

insert into genres (genre_id, genre) values (1, 'Action and Adventure');
insert into genres (genre_id, genre) values (2, 'Graphic Novel');
insert into genres (genre_id, genre) values (3, 'Detective');
insert into genres (genre_id, genre) values (4, 'Fantasy');
insert into genres (genre_id, genre) values (5, 'Historical Fiction');
insert into genres (genre_id, genre) values (6, 'Horror');
insert into genres (genre_id, genre) values (7, 'Romance');
insert into genres (genre_id, genre) values (8, 'Science Fiction');
insert into genres (genre_id, genre) values (9, 'Thriller');
insert into genres (genre_id, genre) values (10, 'History');
insert into genres (genre_id, genre) values (11, 'Self-Help');
insert into genres (genre_id, genre) values (12, 'Novel');

insert into authors (author_id, author) values (1, 'Stephen King');
insert into authors (author_id, author) values (2, 'JK Rowling');
insert into authors (author_id, author) values (3, 'Leo Tolstoy');
insert into authors (author_id, author) values (4, 'JRR Tolkien');
insert into authors (author_id, author) values (5, 'Fyodor Dostoevsky');
insert into authors (author_id, author) values (6, 'Agatha Christie');
insert into authors (author_id, author) values (7, 'Eckhart Tolle');
insert into authors (author_id, author) values (8, 'Anton Chekhov');
insert into authors (author_id, author) values (9, 'Erich Maria Remarque');
insert into authors (author_id, author) values (10, 'Nikolai Gogol');
insert into authors (author_id, author) values (11, 'Plutarch');

INSERT INTO books(book_id, cover, title, publisher, publish_date, page_count,isbn,total_amount, remaining_amount, status) 
VALUES (1, 'default_cover.png', 'Under the Dome', 'Scribner', '2009.11.10.', 1074, '978-1-4391-4850-1', '', 30, 30, 'Available (30 ouf of 30)'),
 (2, 'default_cover.png', 'It', 'Viking', '1986.09.15.', 1138, '0-670-81302-8', '', 30, 30, 'Available (30 ouf of 30)'),
 (3, 'default_cover.png', 'Harry Potter and the Philosophers Stone', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-3269-9', 30, 30, 'Available (30 ouf of 30)'),
(4, 'default_cover.png', 'Harry Potter and the Prisoner of Azkaban', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-4215-5', 30, 30, 'Available (30 ouf of 30)'),
 (6, 'default_cover.png', 'Harry Potter and the Order of the Phoenix', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-3269-9', 30, 30, 'Available (30 ouf of 30)'),
 (7, 'default_cover.png', 'Harry Potter and the Half-Blood Prince', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-3269-9', 30, 30, 'Available (30 ouf of 30)'),
 (8, 'default_cover.png', 'The Death of Ivan Ilyich', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-4215-5', 30, 30, 'Available (30 ouf of 30)'),
 (9, 'default_cover.png', 'Crime and Punishment', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-4215-5', 30, 30, 'Available (30 ouf of 30)'),
 (10, 'default_cover.png', 'The Brothers Karamazov', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-4215-5', 30, 30, 'Available (30 ouf of 30)'),
 (11, 'default_cover.png', 'Demons', 'Izdatelstvo', '1986.09.15.', 124, '0-7475-4215-5', 30, 30, 'Available (30 ouf of 30)');
 
 insert into book_authors(book_id_fk, author_id_fk) values(1,1);
insert into book_authors(book_id_fk, author_id_fk) values(2,1);
insert into book_authors(book_id_fk, author_id_fk) values(3,2);
insert into book_authors(book_id_fk, author_id_fk) values(4,2);
insert into book_authors(book_id_fk, author_id_fk) values(5,2);
insert into book_authors(book_id_fk, author_id_fk) values(6,2);
insert into book_authors(book_id_fk, author_id_fk) values(7,2);
insert into book_authors(book_id_fk, author_id_fk) values(8,3);
insert into book_authors(book_id_fk, author_id_fk) values(9,5);
insert into book_authors(book_id_fk, author_id_fk) values(10,5);
insert into book_authors(book_id_fk, author_id_fk) values(11,5);
 
insert into book_genres(book_id_fk, genre_id_fk) values (1,6), (1, 9);
insert into book_genres(book_id_fk, genre_id_fk) values (2,6), (2, 9), (2, 12);
insert into book_genres(book_id_fk, genre_id_fk) values (3,4), (3, 1), (3, 6);
insert into book_genres(book_id_fk, genre_id_fk) values (4,4), (4, 1), (4, 6);
insert into book_genres(book_id_fk, genre_id_fk) values (5,4), (5, 1), (5, 6);
insert into book_genres(book_id_fk, genre_id_fk) values (6,4), (6, 1), (6, 6);
insert into book_genres(book_id_fk, genre_id_fk) values (7,4), (7, 1), (7, 6);
insert into book_genres(book_id_fk, genre_id_fk) values (8,12);
insert into book_genres(book_id_fk, genre_id_fk) values (9,12), (9, 3), (9, 9);
insert into book_genres(book_id_fk, genre_id_fk) values (10,12), (10, 3), (10, 9);
insert into book_genres(book_id_fk, genre_id_fk) values (11,12), (11, 3), (11, 9);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
