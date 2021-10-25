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

    public static final String LOAD_OLD_BOOK_AUTHORS = "SELECT book_id_fk, author_id, author FROM book_authors" +
            " JOIN authors ON author_id = author_id_fk WHERE book_id_fk = ?;";
    public static final String DELETE_OLD_BOOK_AUTHORS = "DELETE from book_authors WHERE book_id_fk = ?" +
            " AND author_id_fk IN";

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

                if (insertGenres(connection, book, Optional.empty()) && insertAuthors(connection, book, Optional.empty())) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                }
            }

            return false;
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
                if (updateGenres(connection, book) && updateAuthors(connection, book)) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
            }

            return false;
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

    private void selectOldBookFields(Connection connection, BidiMap<Short, String> oldBookFields, boolean isForGenres,
                                     String query, short bookId) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setShort(1, bookId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                if (isForGenres) {
                    oldBookFields.put(rs.getShort(genreIdCol), rs.getString(genreCol));
                } else {
                    oldBookFields.put(rs.getShort(authorIdCol), rs.getString(authorCol));
                }
            }
        }
    }

    private void deleteOldBookFields(Connection connection, BidiMap<Short, String> oldBookFields, Set<String> newBookFields,
                                     StringBuilder sb, short bookId) throws SQLException {
        Set<Short> fieldIdsToDelete = new HashSet<>();

        for (String field : oldBookFields.values()) {
            if (!newBookFields.contains(field)) {
                fieldIdsToDelete.add(oldBookFields.getKey(field));
            }
        }

        byte genreIdsCounter = (byte) fieldIdsToDelete.size();

        for (Short genreIdFk : fieldIdsToDelete) {
            sb.append(genreIdFk);

            if (--genreIdsCounter != 0) {
                sb.append(COMMA);
            } else {
                sb.append(RIGHT_PARENTHESIS);
            }
        }

        try (PreparedStatement deleteOldBookGenresSt = connection.prepareStatement(sb.toString())) {
            deleteOldBookGenresSt.setShort(1, bookId);
            deleteOldBookGenresSt.executeUpdate();
        }
    }

    private boolean updateGenres(Connection connection, Book book) throws SQLException {
        Set<String> newBookGenres = book.getGenres();
        BidiMap<Short, String> oldBookGenres = new DualHashBidiMap<>();

        selectOldBookFields(connection, oldBookGenres, true, LOAD_OLD_BOOK_GENRES, book.getId());

        boolean result = true;

        if (!newBookGenres.equals(oldBookGenres.values())) {
            deleteOldBookFields(connection, oldBookGenres, newBookGenres,
                    new StringBuilder(DELETE_OLD_BOOK_GENRES + LEFT_PARENTHESIS), book.getId());


            Set<String> genresToInsertForUpd = new HashSet<>();
            for (String genre : newBookGenres) {
                if (!oldBookGenres.containsValue(genre)) {
                    genresToInsertForUpd.add(genre);
                }
            }

            result = insertGenres(connection, book, Optional.of(genresToInsertForUpd));
        }

        return result;
    }

    private boolean updateAuthors(Connection connection, Book book) throws SQLException {
        Set<String> newBookAuthors = book.getAuthors();
        BidiMap<Short, String> oldBookAuthors = new DualHashBidiMap<>();

        selectOldBookFields(connection, oldBookAuthors, false, LOAD_OLD_BOOK_AUTHORS, book.getId());

        boolean result = true;

        if (!newBookAuthors.equals(oldBookAuthors.values())) {
            deleteOldBookFields(connection, oldBookAuthors, newBookAuthors,
                    new StringBuilder(DELETE_OLD_BOOK_AUTHORS + LEFT_PARENTHESIS), book.getId());


            Set<String> authorsToInsertForUpd = new HashSet<>();
            for (String genre : newBookAuthors) {
                if (!oldBookAuthors.containsValue(genre)) {
                    authorsToInsertForUpd.add(genre);
                }
            }

            result = insertAuthors(connection, book, Optional.of(authorsToInsertForUpd));
        }

        return result;
    }

    private void selectRepeatingFieldsFromDb(Connection connection, StringBuilder sb, Set<String> newFields,
                                             boolean isForGenres, Map<Short, String> fieldsFromDb) throws SQLException {
        byte counter = (byte) newFields.size();

        for (String field : newFields) {
            sb.append("'").append(field).append("'");

            if (--counter != 0) {
                sb.append(WHITESPACE);
                if (isForGenres) {
                    sb.append("OR genre LIKE");
                } else {
                    sb.append("OR author LIKE");
                }
                sb.append(WHITESPACE);
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(sb.toString())) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if (isForGenres) {
                    fieldsFromDb.put(rs.getShort(genreIdCol),
                            rs.getString(genreCol));
                } else {
                    fieldsFromDb.put(rs.getShort(authorIdCol),
                            rs.getString(authorCol));
                }
            }
        }
    }

    private void insertNewFields(Connection connection, StringBuilder sb, Set<String> newFields, Map<Short,
            String> fieldsFromDb, boolean isForUpdate) throws SQLException {
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

        if (isForUpdate) {
            fieldsFromDb.clear();
        }

        try (Statement insertNewFieldsSt = connection.createStatement()) {
            if (insertNewFieldsSt.executeUpdate(sb.toString(), Statement.RETURN_GENERATED_KEYS) > 0) {
                ResultSet rsWithNewFieldIds = insertNewFieldsSt.getGeneratedKeys();

                if (!rsWithNewFieldIds.next()) {
                    throw new SQLException("Adding book failed, no ids obtained for new genres or authors.");
                }

                do {
                    //save the IDs of the newly inserted fields for further insertion into
                    // book_genres(book_id_fk, genre_id_fk) || book_authors(book_id_fk, author_id_fk)
                    Short newFieldId = rsWithNewFieldIds.getShort(1);
                    fieldsFromDb.put(newFieldId, "");
                } while (rsWithNewFieldIds.next());
            }
        }
    }

    private boolean insertBookFields(Connection connection, StringBuilder sb, Set<Short> newFieldIds, short bookIdFk)
            throws SQLException {
        try (Statement insertAllBookGenresSt = connection.createStatement()) {
            String query = prepareQueryFromSB(newFieldIds, sb, bookIdFk);

            return insertAllBookGenresSt.executeUpdate(query) > 0;
        }
    }

    private boolean insertGenres(Connection connection, Book book, Optional<Set<String>> genresToInsForUpdOpt) throws SQLException {
        StringBuilder loadGenresSB = new StringBuilder(LOAD_GENRES);
        Set<String> newGenres = book.getGenres();
        Map<Short, String> genresFromDb = new HashMap<>();

        selectRepeatingFieldsFromDb(connection, loadGenresSB, newGenres, true, genresFromDb);

        boolean isForUpdate = genresToInsForUpdOpt.isPresent();
        Set<Short> genreIdsToInsert = new HashSet<>();

        if (isForUpdate) {
            System.out.println("GEnres to insert " + genresToInsForUpdOpt.get());
            Set<String> genreToInsForUpd = genresToInsForUpdOpt.get();

            for (String genre : genresFromDb.values()) {
                if (genreToInsForUpd.contains(genre)) {
                    genreIdsToInsert.add(genresFromDb
                            .entrySet()
                            .stream()
                            .filter(entry -> Objects.equals(entry.getValue(), genre))
                            .map(Map.Entry::getKey)
                            .findFirst().get());
                }
            }
        }

        byte genresSize = (byte) newGenres.size();
        byte genresFromDbSize = (byte) genresFromDb.size();

        if (genresSize > genresFromDbSize || isForUpdate) {
            StringBuilder insertNewGenresSB = new StringBuilder(INSERT_GENRES);
            insertNewFields(connection, insertNewGenresSB, newGenres, genresFromDb, isForUpdate);
        }

        genreIdsToInsert.addAll(genresFromDb.keySet());

        return insertBookFields(connection, new StringBuilder(INSERT_BOOK_GENRES), genreIdsToInsert, book.getId());
    }

    private String prepareQueryFromSB(Set<Short> ids, StringBuilder stringBuilder, short bookIdFk) {
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

    private boolean insertAuthors(Connection connection, Book book, Optional<Set<String>> authorsToInsForUpdOpt) throws SQLException {
        StringBuilder loadAuthorsSB = new StringBuilder(LOAD_AUTHORS);
        Set<String> authors = book.getAuthors();
        Map<Short, String> authorsFromDb = new HashMap<>();

        selectRepeatingFieldsFromDb(connection, loadAuthorsSB, authors, false, authorsFromDb);

        boolean isForUpdate = authorsToInsForUpdOpt.isPresent();
        Set<Short> authorIdsToInsert = new HashSet<>();

        if (isForUpdate) {
            System.out.println("Authors to insert " + authorsToInsForUpdOpt.get());
            Set<String> authorsToInsForUpd = authorsToInsForUpdOpt.get();

            for (String genre : authorsFromDb.values()) {
                if (authorsToInsForUpd.contains(genre)) {
                    authorIdsToInsert.add(authorsFromDb
                            .entrySet()
                            .stream()
                            .filter(entry -> Objects.equals(entry.getValue(), genre))
                            .map(Map.Entry::getKey)
                            .findFirst().get());
                }
            }
        }

        byte authorsSize = (byte) authors.size();
        byte authorsFromDbSize = (byte) authorsFromDb.size();

        if (authorsSize > authorsFromDbSize || isForUpdate) {
            StringBuilder insertNewAuthorsSB = new StringBuilder(INSERT_AUTHORS);
            insertNewFields(connection, insertNewAuthorsSB, authors, authorsFromDb, isForUpdate);
        }

        authorIdsToInsert.addAll(authorsFromDb.keySet());

        return insertBookFields(connection, new StringBuilder(INSERT_BOOK_AUTHORS), authorIdsToInsert, book.getId());
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
