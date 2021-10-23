package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.entity.Book;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.*;

import java.sql.*;
import java.util.*;

public class BookDaoImpl implements BookDao {
    public static final String INSERT = "INSERT INTO books(cover, title, publisher, publish_date," +
            " page_count, isbn, description, total_amount, remaining_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ? );";

    public static final String LOAD_GENRES = "select genre_id, genre from genres WHERE genre LIKE ";
    public static final String LOAD_AUTHORS = "select author_id, author from authors WHERE author LIKE ";

    public static final String INSERT_GENRES = "INSERT INTO genres(genre) VALUES";
    public static final String INSERT_AUTHORS = "INSERT INTO authors(author) VALUES";

    public static final String INSERT_BOOK_GENRES = "INSERT INTO book_genres(book_id_fk, genre_id_fk) VALUES";
    public static final String INSERT_BOOK_AUTHORS = "INSERT INTO book_authors(book_id_fk, author_id_fk) VALUES";

    public static final String UPDATE = "UPDATE books SET title = ?, publisher = ?, publish_date = ?, page_count = ?," +
            " isbn = ?, description = ?, total_amount = ?, remaining_amount = ?, status = ? WHERE book_id = ?;";

    public static final String LOAD_OLD_BOOK_GENRES = "SELECT book_id_fk, genre_id, genre FROM book_genres" +
            " JOIN genres ON genre_id = genre_id_fk WHERE book_id_fk = ?;";
    public static final String DELETE_OLD_BOOK_GENRES = "DELETE from book_genres WHERE book_id_fk = ?" +
            " AND genre_id_fk IN";

    private static final String SELECT_LIST = "SELECT book_id, title, publish_date, remaining_amount FROM books ";

    private static final String LOAD_BOOK_AUTHORS = "SELECT book_id_fk, author FROM book_authors " +
            "JOIN authors ON author_id_fk = author_id" +
            " WHERE book_id_fk IN (";

    private static final String SELECT_COUNT = "SELECT COUNT(book_id) from books;";

    private static final String FIND_BY_ID = "WITH fields_and_authors AS " +
            "(SELECT book_id, cover, title, publisher, publish_date, author, page_count, isbn, description, total_amount," +
            " remaining_amount, status FROM book_authors" +
            " JOIN books ON book_id_fk = book_id" +
            " JOIN authors ON author_id_fk = author_id WHERE book_id = ?)," +
            " genres AS " +
            "(SELECT genre FROM book_genres" +
            " JOIN books ON book_id_fk = book_id" +
            " JOIN genres ON genre_id_fk = genre_id WHERE book_id = ?)" +
            " SELECT * FROM fields_and_authors JOIN genres;";

    public static final String UPDATE_COVER = "UPDATE books SET cover = ? WHERE book_id = ?";

    public static final String CHECK_TITLE_FOR_EXISTENCE = "SELECT title FROM books WHERE title = ?;";

    private static final String bookIdCol = "book_id";
    private static final String bookCoverCol = "cover";
    private static final String bookTitleCol = "title";
    private static final String bookAuthorCol = "author";
    private static final String bookPublisherCol = "publisher";
    private static final String bookPublishDateCol = "publish_date";
    private static final String bookGenreCol = "genre";
    private static final String bookPageCountCol = "page_count";
    private static final String bookIsbnCol = "isbn";
    private static final String bookDescCol = "description";
    private static final String bookTotalAmountCol = "total_amount";
    private static final String bookRemainingAmountCol = "remaining_amount";
    private static final String bookStatusCol = "status";
    private static final String bookIdFkCol = "book_id_fk";

    private static final String genreIdCol = "genre_id";
    private static final String genreCol = "genre";
    private static final String authorIdCol = "author_id";
    private static final String authorCol = "author";

    BookDaoImpl() {
    }

    @Override
    public boolean add(Book book) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement insertBookSt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            insertBookSt.setString(1, book.getCover());
            insertBookSt.setString(2, book.getTitle());
            insertBookSt.setString(3, book.getPublisher());
            insertBookSt.setString(4, book.getPublishDate());
            insertBookSt.setShort(5, book.getPageCount());
            insertBookSt.setString(6, book.getIsbn());
            insertBookSt.setString(7, book.getDescription());
            insertBookSt.setShort(8, book.getTotalAmount());
            insertBookSt.setShort(9, book.getRemainingAmount());
            insertBookSt.setString(10, book.getStatus());

            if (insertBookSt.executeUpdate() == 1) {
                ResultSet rsWithBookId = insertBookSt.getGeneratedKeys();

                if (!rsWithBookId.next()) {
                    throw new DaoException("Adding book failed, no ID obtained.");
                }

                book.setId(rsWithBookId.getShort(1));

                if (insertGenres(connection, book, false) && insertAuthors(connection, book)) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }

            return true;
        } catch (SQLException e) {
            throw new DaoException("Error adding a book.", e);
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement updateBookSt = connection.prepareStatement(UPDATE)) {
            connection.setAutoCommit(false);

            updateBookSt.setString(1, book.getTitle());
            updateBookSt.setString(2, book.getPublisher());
            updateBookSt.setString(3, book.getPublishDate());
            updateBookSt.setShort(4, book.getPageCount());
            updateBookSt.setString(5, book.getIsbn());
            updateBookSt.setString(6, book.getDescription());
            updateBookSt.setShort(7, book.getTotalAmount());
            updateBookSt.setShort(8, book.getRemainingAmount());
            updateBookSt.setString(9, book.getStatus());
            updateBookSt.setShort(10, book.getId());

            if (updateBookSt.executeUpdate() == 1) {
                updateGenres(connection, book);
            }

            return true;
        } catch (SQLException e) {
            throw new DaoException("Error updating book.", e);
        }
    }

    @Override
    public List<Book> loadBookList(int offset, int recordsPerPage, Optional<String> filterMode) throws DaoException {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String loadBookListQuery = SELECT_LIST;

            if (filterMode.isPresent()) {
                loadBookListQuery += "WHERE status LIKE '" + filterMode.get() + "%' ";
            }

            loadBookListQuery += "ORDER BY remaining_amount ASC LIMIT " + offset + COMMA + recordsPerPage;

            ResultSet rs = statement.executeQuery(loadBookListQuery);

            if (rs.isBeforeFirst()) {
                StringBuilder loadBookAuthorsSB = new StringBuilder(LOAD_BOOK_AUTHORS);

                while (rs.next()) {
                    Book book = createBookFromRS(rs, false);
                    books.add(book);

                    loadBookAuthorsSB.append(book.getId());

                    if (!rs.isLast()) {
                        loadBookAuthorsSB.append(COMMA);
                    } else {
                        loadBookAuthorsSB.append(RIGHT_PARENTHESIS);
                    }
                }

                rs = statement.executeQuery(loadBookAuthorsSB.toString());
                joinBooksWithAuthors(rs, books);
            }
        } catch (SQLException e) {
            throw new DaoException("Error loading books.", e);
        }

        return books;
    }

    @Override
    public int getBookCount() throws DaoException {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_COUNT);

            return (resultSet.next() ? resultSet.getInt(1) : 0);
        } catch (SQLException | NumberFormatException e) {
            throw new DaoException("Error getting books count.", e);
        }
    }

    @Override
    public Optional<Book> findById(short bookId) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID, ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            statement.setShort(1, bookId);
            statement.setShort(2, bookId);
            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next() ? Optional.of(createBookFromRS(resultSet, true))
                    : Optional.empty());
        } catch (SQLException e) {
            throw new DaoException("Error finding book by id.", e);
        }
    }

    @Override
    public boolean changeBookCover(short bookId, String path) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_COVER)) {
            statement.setString(1, path);
            statement.setShort(2, bookId);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Error updating book's cover", e);
        }
    }

    @Override
    public boolean isTitleAvailable(String title) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_TITLE_FOR_EXISTENCE)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            return !resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Error checking if book title is available.", e);
        }
    }

    private void joinBooksWithAuthors(ResultSet rs, List<Book> books) throws SQLException {
        MultiValuedMap<Short, String> bookAuthors = new HashSetValuedHashMap<>();

        while (rs.next()) {
            Short bookId = rs.getShort(bookIdFkCol);
            String author = rs.getString(bookAuthorCol);

            bookAuthors.put(bookId, author);
        }

        for (Book book : books) {
            book.setAuthors((Set<String>) bookAuthors.get(book.getId()));
        }
    }

    private boolean updateGenres(Connection connection, Book book) throws SQLException {
        Set<String> newBookGenres = book.getGenres();
        BidiMap<Short, String> oldBookGenres = new DualHashBidiMap<>();

        try (PreparedStatement loadOldBookGenresSt = connection.prepareStatement(LOAD_OLD_BOOK_GENRES)) {
            loadOldBookGenresSt.setShort(1, book.getId());
            ResultSet rsWithOldBookGenres = loadOldBookGenresSt.executeQuery();

            while (rsWithOldBookGenres.next()) {
                oldBookGenres.put(rsWithOldBookGenres.getShort(genreIdCol), rsWithOldBookGenres.getString(genreCol));
            }
        }

//        if (!newBookGenres.equals(oldBookGenres.values())) {
        System.out.println("old genres " + oldBookGenres);
        System.out.println("new genres " + newBookGenres);

        insertGenres(connection, book, true);

        byte newBookGenresSize = (byte) newBookGenres.size();
        byte oldBookGenresSize = (byte) oldBookGenres.size();

        if (newBookGenresSize < oldBookGenresSize) {
            Set<Short> genresToDelete = new HashSet<>();

            for (String genre : oldBookGenres.values()) {
                if (!newBookGenres.contains(genre)) {
                    genresToDelete.add(oldBookGenres.getKey(genre));
                }
            }

            StringBuilder deleteOldBookGenresSB = new StringBuilder(DELETE_OLD_BOOK_GENRES + LEFT_PARENTHESIS);
            byte genreIdsCounter = (byte) genresToDelete.size();

            for (Short genreIdFk : genresToDelete) {
                deleteOldBookGenresSB.append(genreIdFk);

                if (--genreIdsCounter != 0) {
                    deleteOldBookGenresSB.append(COMMA);
                } else {
                    deleteOldBookGenresSB.append(RIGHT_PARENTHESIS);
                }
            }

            try (PreparedStatement deleteOldBookGenresSt = connection.prepareStatement(deleteOldBookGenresSB.toString())) {
                deleteOldBookGenresSt.setShort(1, book.getId());
                deleteOldBookGenresSt.executeUpdate();
            }
        }
//        }

        return true;
    }

    private void insertHelp1(Connection connection, StringBuilder sb, Set<String> newFields, boolean isForGenres,
                             Map<Short, String> fieldsFromDb) throws SQLException{
        byte counter = (byte) newFields.size();

        for (String field : newFields) {
            sb.append("'").append(field).append("'");

            if (--counter != 0) {
//                sb.append(WHITESPACE).append("OR genre LIKE").append(WHITESPACE);
                sb.append(WHITESPACE);
                if(isForGenres){
                    sb.append("OR genre LIKE");
                } else {
                    sb.append("OR author LIKE");
                }
                sb.append(WHITESPACE);
            }
        }

        ResultSet rs;

        try (PreparedStatement statement = connection.prepareStatement(sb.toString())) {
            rs = statement.executeQuery();

            while (rs.next()) {
                if(isForGenres){
                    fieldsFromDb.put(rs.getShort(genreIdCol),
                            rs.getString(genreCol));
                } else {
                    fieldsFromDb.put(rs.getShort(authorIdCol),
                            rs.getString(authorCol));
                }
            }
        }
    }

    private void insertHelp2(Connection connection, StringBuilder sb, Set<String> newFields, Map<Short,
            String> fieldsFromDb) throws SQLException {
        byte counter = (byte) (newFields.size() - fieldsFromDb.size());

        for (String field : newFields) {
            if (!fieldsFromDb.containsValue(field)) {
                sb.append(WHITESPACE).append(LEFT_PARENTHESIS).append(APOSTROPHE)
                        .append(field).append(APOSTROPHE).append(RIGHT_PARENTHESIS);

                if (--counter != 0) {
                    sb.append(COMMA);
                }
            }
        }

        System.out.println("INSERT NEW GENRES SB " + sb);

        try (Statement insertNewGenresSt = connection.createStatement()) {
            if (insertNewGenresSt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS) > 0) {
                ResultSet rsWithNewGenreIds = insertNewGenresSt.getGeneratedKeys();

                if (!rsWithNewGenreIds.next()) {
                    throw new SQLException("Adding book failed, no ids obtained for new genres or authors.");
                }

                do {
                    //save the IDs of the newly inserted genres for further insertion into
                    // book_genres(book_id_fk, genre_id_fk)
                    Short newFieldId = rsWithNewGenreIds.getShort(1);
                    fieldsFromDb.put(newFieldId, "");
                } while (rsWithNewGenreIds.next());
            }
        }
    }

    private boolean insertHelp3(Connection connection,StringBuilder sb, Set<Short> newFieldIds, short bookIdFk) throws SQLException{
        try (Statement insertAllBookGenresSt = connection.createStatement()) {
            String query = prepareQueryFromSB(newFieldIds, sb, bookIdFk);

            System.out.println("QUERY "+query);

            return insertAllBookGenresSt.executeUpdate(query) > 0;
        }
    }

    private boolean insertGenres(Connection connection, Book book, boolean isForUpdate) throws SQLException {
        StringBuilder loadGenresSB = new StringBuilder(LOAD_GENRES);
        Set<String> genres = book.getGenres();
        Map<Short, String> genresFromDb = new HashMap<>();

        insertHelp1(connection,loadGenresSB, genres, true, genresFromDb);

//        byte genresSize = (byte) genres.size();
//        byte genreCounter = genresSize;
//
//        for (String genre : genres) {
//            loadGenresSB.append("'").append(genre).append("'");
//
//            if (--genreCounter != 0) {
//                loadGenresSB.append(WHITESPACE).append("OR genre LIKE").append(WHITESPACE);
//            }
//        }
//
//        ResultSet resultSetWithGenres;
//        Map<Short, String> genresFromDb = new HashMap<>();
//
//        System.out.println("LOAD GENRES SB: " + loadGenresSB.toString());
//
//        try (PreparedStatement loadGenresSt = connection.prepareStatement(loadGenresSB.toString())) {
//            resultSetWithGenres = loadGenresSt.executeQuery();
//
//            while (resultSetWithGenres.next()) {
//                genresFromDb.put(resultSetWithGenres.getShort(genreIdCol),
//                        resultSetWithGenres.getString(genreCol));
//            }
//        }

        System.out.println("Genres from db :" + genresFromDb);

        byte genresSize = (byte)genres.size();
        byte genresFromDbSize = (byte) genresFromDb.size();

//        Set<Short> newGenreIds = new HashSet<>();

//        if (genresSize > genresFromDbSize || isForUpdate) {
        if (genresSize > genresFromDbSize) {
            StringBuilder insertNewGenresSB = new StringBuilder(INSERT_GENRES);

            insertHelp2(connection, insertNewGenresSB, genres, genresFromDb);
//            byte counter = (byte) (genresSize - genresFromDbSize);
//
//            for (String genre : genres) {
//                if (!genresFromDb.containsValue(genre)) {
//                    insertNewGenresSB.append(WHITESPACE).append(LEFT_PARENTHESIS).append(APOSTROPHE)
//                            .append(genre).append(APOSTROPHE).append(RIGHT_PARENTHESIS);
//
//                    if (--counter != 0) {
//                        insertNewGenresSB.append(COMMA);
//                    }
//
////                    if(isForUpdate){
////                        newGenreIds.add(
////                                genresFromDb
////                                        .entrySet()
////                                        .stream()
////                                        .filter(entry -> Objects.equals(entry.getValue(), genre))
////                                        .map(Map.Entry::getKey)
////                                        .findFirst().get());
////                    }
//                }
//            }
//
//            System.out.println("INSERT NEW GENRES SB " + insertNewGenresSB);
//
//            try (Statement insertNewGenresSt = connection.createStatement()) {
//                if (insertNewGenresSt.executeUpdate(insertNewGenresSB.toString(), Statement.RETURN_GENERATED_KEYS) > 0) {
////                if (newGenreIds.size() > 0 && insertNewGenresSt.executeUpdate(insertNewGenresSB.toString(), Statement.RETURN_GENERATED_KEYS) > 0) {
//                    ResultSet rsWithNewGenreIds = insertNewGenresSt.getGeneratedKeys();
//
//                    do {
//                        //save the IDs of the newly inserted genres for further insertion into
//                        // book_genres(book_id_fk, genre_id_fk)
//                        Short newGenreId = rsWithNewGenreIds.getShort(1);
//                        genresFromDb.put(newGenreId, "");
//
////                        if(isForUpdate){
////                            newGenreIds.add(newGenreId);
////                        }
//                    } while (rsWithNewGenreIds.next());
//                }
//            }
        }

        return insertHelp3(connection, new StringBuilder(INSERT_BOOK_GENRES), genresFromDb.keySet(), book.getId());

//        try (Statement insertAllBookGenresSt = connection.createStatement()) {
//            StringBuilder insertBookGenresSB = new StringBuilder(INSERT_BOOK_GENRES);
//            short bookIdFk = book.getId();
//
////            byte counter = (byte) genresFromDb.size();
////
////            for (Short genreIdFk : genresFromDb.keySet()) {
////                insertBookGenresSB.append(LEFT_PARENTHESIS).append(bookIdFk).append(COMMA)
////                        .append(genreIdFk).append(RIGHT_PARENTHESIS);
////
////                if (--counter != 0) {
////                    insertBookGenresSB.append(COMMA);
////                }
////            }
//
////            String query = !isForUpdate ? prepareQueryFromSB(genresFromDb.keySet(), insertBookGenresSB, bookIdFk) :
////                    prepareQueryFromSB(newGenreIds, insertBookGenresSB, bookIdFk);
//
//            String query = prepareQueryFromSB(genresFromDb.keySet(), insertBookGenresSB, bookIdFk);
//
//            System.out.println("QUERY "+query);
//
//            return insertAllBookGenresSt.executeUpdate(query) > 0;
////            return insertAllBookGenresSt.executeUpdate(insertBookGenresSB.toString()) > 0;
//        }
    }

    private String prepareQueryFromSB(Set<Short> ids, StringBuilder stringBuilder, short bookIdFk){
        byte counter = (byte) ids.size();

        for (Short genreIdFk : ids) {
            stringBuilder.append(LEFT_PARENTHESIS).append(bookIdFk).append(COMMA)
                    .append(genreIdFk).append(RIGHT_PARENTHESIS);

            if (--counter != 0) {
                stringBuilder.append(COMMA);
            }
        }

        return stringBuilder.toString();
    }

    private boolean insertAuthors(Connection connection, Book book) throws SQLException {
        StringBuilder loadAuthorsSB = new StringBuilder(LOAD_AUTHORS);
        Set<String> authors = book.getAuthors();
        Map<Short, String> authorsFromDb = new HashMap<>();

        insertHelp1(connection,loadAuthorsSB, authors, false, authorsFromDb);

        byte authorsSize = (byte) authors.size();
        byte authorsFromDbSize = (byte) authorsFromDb.size();

        if (authorsSize > authorsFromDbSize) {
            StringBuilder insertNewAuthorsSB = new StringBuilder(INSERT_AUTHORS);
            insertHelp2(connection, insertNewAuthorsSB, authors, authorsFromDb);
        }

        return insertHelp3(connection,new StringBuilder(INSERT_BOOK_AUTHORS), authorsFromDb.keySet(), book.getId());
    }

    private Book createBookFromRS(ResultSet rs, boolean allFieldsPresented) throws SQLException {
        Book book = new Book(
                rs.getShort(bookIdCol),
                rs.getString(bookTitleCol),
                rs.getString(bookPublishDateCol),
                rs.getShort(bookRemainingAmountCol));

        if (allFieldsPresented) {
            book.setCover(rs.getString(bookCoverCol));
            book.setPublisher(rs.getString(bookPublisherCol));
            book.setPageCount(Short.parseShort(rs.getString(bookPageCountCol)));
            book.setIsbn(rs.getString(bookIsbnCol));
            book.setDescription(rs.getString(bookDescCol));
            book.setTotalAmount(Short.parseShort(rs.getString(bookTotalAmountCol)));
            book.setStatus(rs.getString(bookStatusCol));

            Set<String> authors = new HashSet<>();
            Set<String> genres = new HashSet<>();

            do {
                String genre = rs.getString(bookGenreCol);
                String author = rs.getString(bookAuthorCol);

                if (genre != null) {
                    genres.add(genre);
                }

                if (author != null) {
                    authors.add(author);
                }
            } while (rs.next());

            book.setAuthors(authors);
            book.setGenres(genres);
        }

        return book;
    }
}
