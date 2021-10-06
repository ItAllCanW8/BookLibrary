package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    boolean add(Book book) throws DaoException;
    boolean update(Book book) throws DaoException;
    List<Book> loadBookList(int offset, int recordsPerPage) throws DaoException;
    int getBookCount() throws DaoException;
    Optional<Book> findById(short bookId) throws DaoException;
    boolean changeBookCover(short bookId, String path) throws DaoException;
    boolean isTitleAvailable(String title) throws DaoException;
}
