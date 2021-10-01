package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    List<Book> loadBookList() throws DaoException;
    Optional<Book> findById(long bookId) throws DaoException;
    boolean changeBookCover(long bookId, String path) throws DaoException;
}
