package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.entity.Book;

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

    private static final String SELECT_LIST = "SELECT book_id, title, author, publish_date, remaining_amount " +
            "FROM books JOIN authors ON books.book_id = authors.book_id_fk ";

//    ORDER BY remaining_amount ASC LIMIT

    private static final String SELECT_COUNT = "SELECT COUNT(*) from books;";

    private static final String FIND_BY_ID = "SELECT * FROM books JOIN authors ON books.book_id = authors.book_id_fk" +
            " JOIN genres ON books.book_id = genres.book_id_fk WHERE book_id = ?;";

    public static final String UPDATE_COVER = "UPDATE books SET cover = ? WHERE book_id = ?";

    public static final String CHECK_FOR_EXISTENCE = "SELECT title FROM books WHERE title = ?;";

    private static final String bookIdCol = "book_id";
    private static final String bookCoverCol = "cover";
    private static final String bookTitleCol = "title";
    private static final String bookAuthorCol = "author";
    private static final String bookPublisherCol = "publisher";
    private static final String bookPublishDateCol = "publish_date";
    private static final String bookGenresCol = "genres";
    private static final String bookPageCountCol = "page_count";
    private static final String bookIsbnCol = "isbn";
    private static final String bookDescCol = "description";
    private static final String bookTotalAmountCol = "total_amount";
    private static final String bookRemainingAmountCol = "remaining_amount";
    private static final String bookStatusCol = "status";

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

            if(statement.executeUpdate() == 1){
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
        List<Book> books;

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String query = SELECT_LIST;

            if (filterMode.isPresent()) {
                query += "WHERE status LIKE '" + filterMode.get() + "%' ";
            }

            query += "ORDER BY remaining_amount ASC LIMIT ";
            ResultSet resultSet = statement.executeQuery(query + offset + "," + recordsPerPage);

            books = createBooksFromRS(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Error loading books.", e);
        }

        return books;
    }

    private List<Book> createBooksFromRS(ResultSet rs) {
        List<Book> books = new ArrayList<>();

        try {
            if (rs.next()) {
                while (!rs.isAfterLast()) {
                    short bookId = rs.getShort(bookIdCol);
                    String title = rs.getString(bookTitleCol);
                    String publishDate = rs.getString(bookPublishDateCol);
                    short remainingAmount = rs.getShort(bookRemainingAmountCol);

                    Set<String> authors = new HashSet<>();

                    do {
                        String author = rs.getString(bookAuthorCol);

                        if(author != null){
                            authors.add(author);
                        }
                    } while (rs.next() && rs.getShort(bookIdCol) == bookId);

                    Book book = new Book(bookId, title, authors, publishDate, remainingAmount);
                    books.add(book);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setShort(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next() ? Optional.of(createBookFromResultSet(resultSet, true))
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

    private Book createBookFromResultSet(ResultSet resultSet, boolean areAllFieldsPresent) throws SQLException {
        short id = resultSet.getShort(bookIdCol);
        String title = resultSet.getString(bookTitleCol);
        String author = resultSet.getString(bookAuthorCol);
        Set<String> authors = new HashSet<>();
        authors.add(author);

        String publishDate = resultSet.getString(bookPublishDateCol);
        short remainingAmount = resultSet.getShort(bookRemainingAmountCol);

        Book book = new Book(id, title, authors, publishDate, remainingAmount);

        System.out.println(book);

        if (areAllFieldsPresent) {
            book.setCover(resultSet.getString(bookCoverCol));
            book.setPublisher(resultSet.getString(bookPublisherCol));
//            book.setGenres(resultSet.getString(bookGenresCol));
            book.setPageCount(Short.parseShort(resultSet.getString(bookPageCountCol)));
            book.setIsbn(resultSet.getString(bookIsbnCol));
            book.setDescription(resultSet.getString(bookDescCol));
            book.setTotalAmount(Short.parseShort(resultSet.getString(bookTotalAmountCol)));
            book.setStatus(resultSet.getString(bookStatusCol));
        }

        return book;
    }
}
