package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.entity.Book;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.sql.*;
import java.util.*;

public class BookDaoImpl implements BookDao {
    public static final String INSERT = "INSERT INTO books(cover, title, publisher, publish_date," +
            " page_count, isbn, description, total_amount, remaining_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ? );";

    public static final String INSERT_AUTHORS = "INSERT INTO authors(book_id_fk, author) VALUES (?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ? );";

    public static final String UPDATE = "UPDATE books SET title = ?, authors = ?, publisher = ?, publish_date = ?, " +
            "genres = ?, page_count = ?, isbn = ?, description = ?, total_amount = ?, remaining_amount = ?, status = ?" +
            " WHERE book_id = ?;";

    private static final String SELECT_LIST = "SELECT book_id, title, publish_date, remaining_amount FROM books ";

    private static final String LOAD_BOOK_AUTHORS = "SELECT book_id_fk, author FROM book_authors " +
            "JOIN authors ON author_id_fk = author_id" +
            " WHERE book_id_fk IN (";

    private static final String SELECT_COUNT = "SELECT COUNT(*) from books;";

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

    public static final String CHECK_FOR_EXISTENCE = "SELECT title FROM books WHERE title = ?;";

    public static final String COMMA = ",";

    public static final String QUERY_END = ");";

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

    BookDaoImpl() {
    }

    @Override
    public boolean add(Book book) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, book.getCover());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getPublishDate());
            statement.setShort(5, book.getPageCount());
            statement.setString(6, book.getIsbn());
            statement.setString(7, book.getDescription());
            statement.setShort(8, book.getTotalAmount());
            statement.setShort(9, book.getRemainingAmount());
            statement.setString(10, book.getStatus());

            if (statement.executeUpdate() == 1) {
//                statement = connection.prepareStatement("");
            }

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Error adding a book.", e);
        }
    }

    @Override
    public boolean update(Book book) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, book.getTitle());
//            statement.setString(2, book.getAuthors());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getPublishDate());
//            statement.setString(5, book.getGenres());
            statement.setShort(6, book.getPageCount());
            statement.setString(7, book.getIsbn());
            statement.setString(8, book.getDescription());
            statement.setShort(9, book.getTotalAmount());
            statement.setShort(10, book.getRemainingAmount());
            statement.setString(11, book.getStatus());
            statement.setShort(12, book.getId());

            return statement.executeUpdate() == 1;
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
                System.out.println(filterMode.get());
                loadBookListQuery += "WHERE status LIKE '" + filterMode.get() + "%' ";
            }

            loadBookListQuery += "ORDER BY remaining_amount ASC LIMIT " + offset + COMMA + recordsPerPage;

            ResultSet rs = statement.executeQuery(loadBookListQuery);

            if(rs.isBeforeFirst()){
                StringBuilder loadBookAuthorsQuery = new StringBuilder(LOAD_BOOK_AUTHORS);

                while(rs.next()){
                    Book book = createBookFromRS(rs, false);
                    books.add(book);

                    loadBookAuthorsQuery.append(book.getId());

                    if(!rs.isLast()){
                        loadBookAuthorsQuery.append(COMMA);
                    } else {
                        loadBookAuthorsQuery.append(QUERY_END);
                    }
                }

                rs = statement.executeQuery(String.valueOf(loadBookAuthorsQuery));
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
             PreparedStatement statement = connection.prepareStatement(CHECK_FOR_EXISTENCE)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            return !resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Error checking if book title is available.", e);
        }
    }

    private void joinBooksWithAuthors(ResultSet rs, List<Book> books) throws SQLException {
        MultiValuedMap<Short, String> bookAuthors = new HashSetValuedHashMap<>();

        while(rs.next()){
            Short bookId = rs.getShort(bookIdFkCol);
            String author = rs.getString(bookAuthorCol);

            bookAuthors.put(bookId, author);
        }

        for (Book book: books) {
            book.setAuthors((Set<String>) bookAuthors.get(book.getId()));
        }
    }

    private Book createBookFromRS(ResultSet rs, boolean allFieldsPresented) throws SQLException {
        Book book = new Book(
                rs.getShort(bookIdCol),
                rs.getString(bookTitleCol),
                rs.getString(bookPublishDateCol),
                rs.getShort(bookRemainingAmountCol));

        if(allFieldsPresented){
            book.setCover(rs.getString(bookCoverCol));
            book.setPublisher(rs.getString(bookPublisherCol));
            book.setPageCount(Short.parseShort(rs.getString(bookPageCountCol)));
            book.setIsbn(rs.getString(bookIsbnCol));
            book.setDescription(rs.getString(bookDescCol));
            book.setTotalAmount(Short.parseShort(rs.getString(bookTotalAmountCol)));
            book.setStatus(rs.getString(bookStatusCol));

            Set<String> authors = new HashSet<>();
            Set<String> genres = new HashSet<>();

            String author = rs.getString(bookAuthorCol);

            if (author != null) {
                authors.add(author);
            }

            do {
                String genre = rs.getString(bookGenreCol);

                if (genre != null) {
                    genres.add(genre);
                }
            } while (rs.next() && rs.getString(bookAuthorCol).equals(author));

            if(genres.size() == 1){
                rs.first();
            }

            while (rs.next()) {
                String nextAuthor = rs.getString(bookAuthorCol);

                if(author != null && nextAuthor != null && !author.equals(nextAuthor)) {
                    author = nextAuthor;

                    authors.add(author);
                }
            }

            book.setAuthors(authors);
            book.setGenres(genres);
        }

        return book;
    }

//    private List<Book> createBooksFromRS(ResultSet rs) throws SQLException {
//        List<Book> books = new ArrayList<>();
//
//        if (rs.next()) {
//            while (!rs.isAfterLast()) {
//                short bookId = rs.getShort(bookIdCol);
//                String title = rs.getString(bookTitleCol);
//                String publishDate = rs.getString(bookPublishDateCol);
//                short remainingAmount = rs.getShort(bookRemainingAmountCol);
//
//                Set<String> authors = new HashSet<>();
//
//                do {
//                    String author = rs.getString(bookAuthorCol);
//
//                    if (author != null) {
//                        authors.add(author);
//                    }
//                } while (rs.next() && rs.getShort(bookIdCol) == bookId);
//
//                Book book = new Book(bookId, title, authors, publishDate, remainingAmount);
//                books.add(book);
//            }
//        }
//        return books;
//    }
}
