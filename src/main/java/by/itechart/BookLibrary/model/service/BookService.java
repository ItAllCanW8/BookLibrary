package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> loadBookList() throws ServiceException;
    Optional<Book> findById(long bookId) throws ServiceException;
    boolean changeBookCover(long bookId, String path) throws ServiceException;
}
