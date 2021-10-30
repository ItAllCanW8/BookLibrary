use book_library;

SELECT COUNT(*) from readers;

SELECT * FROM books where status like 'unavailable%';
SELECT book_id, title, authors, publish_date, remaining_amount FROM books where status like 'available%' ORDER BY remaining_amount ASC LIMIT 0,3;

insert into book_library.authors(book_id, author) values(1, 'Tolstoy');
insert into book_library.authors(book_id, author) values(1, 'Lermontov');

use book_library;

SELECT book_id, title, author, publish_date, remaining_amount FROM book_authors JOIN books ON book_id_fk = book_id JOIN authors ON author_id_fk = author_id ORDER BY remaining_amount ASC LIMIT 100 OFFSET 0;
 
SELECT book_id_fk, author, author_id FROM book_authors JOIN authors ON author_id = author_id_fk WHERE book_id_fk = 14;
DELETE from book_authors WHERE book_id_fk = 13 AND author_id_fk IN(3, 5,7);
INSERT INTO book_authors(book_id_fk, author_id_fk) VALUES (13, 3); 

SELECT book_id_fk, genre, genre_id FROM book_genres JOIN genres ON genre_id = genre_id_fk  WHERE book_id_fk = 14; 
DELETE from book_genres WHERE book_id_fk = 13 AND genre_id_fk IN(7, 8, 9);
INSERT INTO book_genres(book_id_fk, genre_id_fk) VALUES (13, 9);
 
select genre_id, genre from genres WHERE genre LIKE 'action' OR genre LIKE 'qqq' OR genre LIKE 'quis';
 
SELECT book_id, title, author, publish_date, remaining_amount FROM books
 JOIN book_authors ON book_id_fk = book_id
 JOIN authors ON author_id_fk = author_id
 ORDER BY remaining_amount ASC
 LIMIT 0,5;
 
SELECT book_id, title, publish_date, remaining_amount FROM books ORDER BY remaining_amount ASC LIMIT 0,5;

SELECT book_id, title, publish_date, remaining_amount FROM books WHERE status LIKE 'unavailable%' ORDER BY remaining_amount ASC LIMIT 0,5;

SELECT book_id_fk, author FROM book_authors JOIN authors ON author_id_fk = author_id WHERE book_id_fk IN (11, 12, 13, 14, 15);

SELECT book_id_fk, author FROM book_authors JOIN authors ON author_id_fk = author_id WHERE book_id_fk = 14 and book_id_fk = 15;
 
select * from books;

INSERT INTO book_genres(book_id_fk, genre_id_fk) VALUES(14,3), (14,8);

INSERT INTO genres(genre) VALUES ('action'), ('adventure'), ('triller');

select genre_id, genre from genres WHERE genre LIKE "s%" OR genre LIKE "a%" OR genre LIKE "%A%"; 

select * from genres WHERE genre LIKE 'accumsAn' OR genre LIKE 'fds';

SELECT count(book_id_fk), genre from book_genres join genres on genre_id_fk = genre_id group by genre HAVING count(book_id_fk) >1;

SELECT book_id, title, author, publish_date, remaining_amount FROM books LEFT JOIN authors ON books.book_id = authors.book_id_fk; 

select author from books JOIN authors ON books.book_id = authors.book_id_fk where books.book_id = 15;
select genre from books JOIN genres ON books.book_id = genres.book_id_fk where books.book_id = 15;

SELECT title,author,genre FROM books JOIN authors ON books.book_id = authors.book_id_fk
            JOIN genres ON books.book_id = genres.book_id_fk;
            
SELECT DISTINCT title, genre FROM book_genres JOIN books ON book_id_fk = book_id JOIN genres ON genre_id_fk = genre_id WHERE book_id = 11
UNION
SELECT DISTINCT title, author FROM book_authors JOIN books ON book_id_fk = book_id JOIN authors ON author_id_fk = author_id WHERE book_id = 11;
         SELECT title, genre FROM book_genres JOIN books ON book_id_fk = book_id JOIN genres ON genre_id_fk = genre_id WHERE book_id = 11;
WITH fields_and_genres AS (
  SELECT book_id, cover, title, publisher, publish_date, genre, page_count, isbn, description, total_amount, remaining_amount, status FROM book_genres
  JOIN books ON book_id_fk = book_id
  JOIN genres ON genre_id_fk = genre_id WHERE book_id = 21
),
authors AS (
  SELECT DISTINCT author FROM book_authors JOIN books ON book_id_fk = book_id JOIN authors ON author_id_fk = author_id WHERE book_id = 21
)
SELECT * FROM fields_and_genres JOIN authors;
       
WITH fields_and_authors AS 
            (SELECT book_id, cover, title, publisher, publish_date, author, page_count, isbn, description, total_amount,
             remaining_amount, status FROM book_authors
             JOIN books ON book_id_fk = book_id
             JOIN authors ON author_id_fk = author_id WHERE book_id = 13),
             genres AS 
            (SELECT genre FROM book_genres
             JOIN books ON book_id_fk = book_id
             JOIN genres ON genre_id_fk = genre_id WHERE book_id = 13)
             SELECT * FROM fields_and_authors JOIN genres;
       
DELETE FROM books WHERE book_id IN(1,2,3);


SELECT q1.title,q1.genre, q2.author FROM (SELECT DISTINCT title, genre FROM book_genres JOIN books ON book_id_fk = book_id JOIN genres ON genre_id_fk = genre_id) as q1, (SELECT DISTINCT author FROM book_authors JOIN books ON book_id_fk = book_id JOIN authors ON author_id_fk = author_id) as q2;       

SELECT DISTINCT title, genre, author FROM book_genres, book_authors JOIN books ON book_id_fk = book_id JOIN genres ON genre_id_fk = genre_id JOIN authors ON author_id_fk = author_id;
            
SELECT * FROM books JOIN authors ON books.book_id = authors.book_id_fk
            JOIN genres ON books.book_id = genres.book_id_fk WHERE book_id = 15;
            
SELECT author FROM authors WHERE author LIKE 'Van Butson' OR author LIKE '%mill';

INSERT INTO `authors` (`book_id_fk`,`author`)
VALUES
  (14,"Aiko Malone"),
  (14,"Post Malone");


INSERT INTO `authors` (`book_id_fk`,`author`)
VALUES
  (13,"Aiko Malone"),
  (12,"Oliver Bauer"),
  (12,"Mariko Randall"),
  (14,"Xantha Barlow"),
  (15,"James Davidson"),
  (15,"Berk Brooks"),
  (11,"Freya Webster"),
  (13,"Nicholas Reid"),
  (13,"Carol Brooks"),
  (11,"Marny O'donnell");

INSERT INTO `books` (`title`,`publisher`,`publish_date`,`page_count`,`isbn`,`description`,`total_amount`,`remaining_amount`,`status`)
VALUES
  ("sodales nisi","Ellis","2020-07-20",500,"40581667-9","in faucibus orci luctus et ultrices posuere cubilia Curae Donec tincidunt.",20,5,"Available (6 out of 14)"),
  ("arcu vel","Benson","2020-01-20",268,"4513980-8","pellentesque, tellus sem mollis dui, in sodales elit erat vitae risus. Duis a mi fringilla mi lacinia",12,9,"Available (4 out of 19)"),
  ("sapien. Nunc","Velazquez","2020-03-20",876,"18921774-9","lorem, sit amet ultricies sem magna nec quam. Curabitur vel",12,9,"Available (7 out of 19)"),
  ("sit amet","Sosa","2020-04-20",866,"49718262-k","risus varius orci, in consequat enim diam vel arcu. Curabitur ut odio vel est tempor bibendum. Donec felis orci,",16,4,"Available (5 out of 16)"),
  ("eget tincidunt","Bolton","2019-12-19",897,"29425468-4","Curabitur sed tortor. Integer aliquam adipiscing lacus. Ut nec urna et arcu imperdiet ullamcorper. Duis at lacus. Quisque purus",17,3,"Available (2 out of 16)");
  
INSERT INTO `genres` (`book_id_fk`,`genre`)
VALUES
  (12,"sit"),
  (15,"aliquet"),
  (12,"nascetur"),
  (13,"Pellentesque"),
  (15,"enim."),
  (11,"est,"),
  (13,"at"),
  (14,"neque"),
  (12,"diam."),
  (14,"elementum");
  INSERT INTO `genres` (`book_id_fk`,`genre`)
VALUES
  (11,"edhgfh");
  
INSERT INTO `genres` (`book_id_fk`,`genre`)
VALUES
  (13,"eu"),
  (11,"augue,"),
  (13,"amet,"),
  (13,"nisi"),
  (14,"semper");


SELECT book_id, title, publish_date, remaining_amount FROM books WHERE title LIKE ? OR author_pseudo LIKE ? 
            OR isbn LIKE ? OR genre LIKE ? OR available_quantity LIKE ?;
            
            SELECT book_id_fk, author FROM book_authors
            JOIN authors ON author_id_fk = author_id
             WHERE book_id_fk = book_id;
/*            
SELECT book_id, title, publish_date, remaining_amount, genre FROM books
JOIN book_authors ON book_id_fk = book_id 
JOIN authors ON author_id_fk = author_id
JOIN book_genres ON book_id_fk = book_id 
JOIN genres ON genre_id_fk = genre_id
WHERE title LIKE "a" OR (genre LIKE "a%" OR genre LIKE "p%");
*/

SELECT book_id, title, publish_date, remaining_amount, genre, author FROM books
JOIN book_authors ON book_authors.book_id_fk = book_id 
JOIN authors ON author_id_fk = author_id
JOIN book_genres ON book_genres.book_id_fk = book_id 
JOIN genres ON genre_id_fk = genre_id
WHERE title LIKE "a%" OR (genre LIKE "z%" OR genre LIKE "t%") OR (author LIKE "f%" OR author LIKE "%1") OR description LIKE "y%";
            
            WITH fields_and_authors AS 
            (SELECT book_id, title, publish_date, remaining_amount, author, description
            FROM book_authors
             JOIN books ON book_id_fk = book_id
             JOIN authors ON author_id_fk = author_id),
             genres AS 
            (SELECT genre FROM book_genres
             JOIN books ON book_id_fk = book_id
             JOIN genres ON genre_id_fk = genre_id)
             SELECT DISTINCT * FROM fields_and_authors JOIN genres WHERE title LIKE "A%";
             
SELECT email, name FROM readers;

