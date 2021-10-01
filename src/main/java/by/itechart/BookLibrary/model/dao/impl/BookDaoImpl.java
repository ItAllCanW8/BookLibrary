package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    private static final String SELECT_BOOKS_LIST = "SELECT book_id, title, authors, publish_date, remaining_amount " +
            "FROM books ORDER BY remaining_amount ASC;";

    private static final String FIND_BOOK_BY_ID = "SELECT * FROM books WHERE book_id = ?;";

    public static final String UPDATE_BOOK_COVER = "UPDATE books SET cover = ? WHERE book_id = ?";

    private static final String bookIdCol = "book_id";
    private static final String bookCoverCol = "cover";
    private static final String bookTitleCol = "title";
    private static final String bookAuthorsCol = "authors";
    private static final String bookPublisherCol = "publisher";
    private static final String bookPublishDateCol = "publish_date";
    private static final String bookGenresCol = "genres";
    private static final String bookPageCountCol = "page_count";
    private static final String bookIsbnCol = "isbn";
    private static final String bookDescCol = "description";
    private static final String bookTotalAmountCol = "total_amount";
    private static final String bookRemainingAmountCol = "remaining_amount";
    private static final String bookStatusCol = "status";

    BookDaoImpl(){}

    @Override
    public List<Book> loadBookList() throws DaoException {
        List<Book> books = new ArrayList<>();

        try(Connection connection = DataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_BOOKS_LIST);

            while (resultSet.next()) {
                books.add(createBookFromResultSet(resultSet, false));
            }

        } catch (SQLException e) {
            throw new DaoException("Error loading books.", e);
        }

        return books;
    }

    @Override
    public Optional<Book> findById(long bookId) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BOOK_BY_ID)) {
            statement.setLong(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            return (resultSet.next() ? Optional.of(createBookFromResultSet(resultSet, true))
                    : Optional.empty());
        } catch (SQLException e) {
            throw new DaoException("Error finding book by id.", e);
        }
    }

    @Override
    public boolean changeBookCover(long bookId, String path) throws DaoException {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_COVER)) {
            statement.setString(1, path);
            statement.setLong(2, bookId);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Error updating book's cover", e);
        }
    }

    private Book createBookFromResultSet(ResultSet resultSet, boolean areAllFieldsPresent) throws SQLException {
        short id = resultSet.getShort(bookIdCol);
//        String cover = resultSet.getString(bookCoverCol);
        String title = resultSet.getString(bookTitleCol);
        String authors = resultSet.getString(bookAuthorsCol);
//        String publisher = resultSet.getString(bookAuthorCol);
        String publishDate = resultSet.getString(bookPublishDateCol);
        short remainingAmount = resultSet.getShort(bookRemainingAmountCol);

        return new Book(id, title, authors, publishDate, remainingAmount);
    }
}
