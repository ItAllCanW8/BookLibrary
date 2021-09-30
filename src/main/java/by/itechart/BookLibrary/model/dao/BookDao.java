package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;

public interface BookDao {
    List<Book> loadBooks() throws DaoException;
}
