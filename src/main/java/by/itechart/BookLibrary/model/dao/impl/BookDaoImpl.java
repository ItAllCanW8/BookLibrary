package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    static final String SELECT_BOOKS = "SELECT * FROM books;";

    BookDaoImpl(){}

    @Override
    public List<Book> loadBooks() throws DaoException {
        List<Book> books = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_library",
                "labmaker", "qqqq");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_BOOKS);

            while (resultSet.next()) {
//                books.add(createBookFromResultSet(resultSet, false));
                System.out.println(resultSet.getString("title"));
            }

        } catch (SQLException e) {
            throw new DaoException("Error loading books.", e);
        }

        return books;
    }
}
