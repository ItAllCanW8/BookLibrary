package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {
    boolean add(Map<String, String> fields) throws ServiceException;
    boolean update(short bookId, Map<String, String> newFields) throws ServiceException;
    List<Book> loadBookList(int offset, int recordsPerPage) throws ServiceException;
    int getBookCount() throws ServiceException;
    Optional<Book> findById(short bookId) throws ServiceException;
    boolean changeBookCover(short bookId, String path) throws ServiceException;
}
