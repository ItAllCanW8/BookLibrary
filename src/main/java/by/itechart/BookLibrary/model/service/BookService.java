package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    boolean add(Map<String, String> fields);
    boolean update(short bookId, Map<String, String> newFields);
    boolean delete(Set<Short> bookIds);
    List<Book> loadBooks(int offset, int recordsPerPage, Optional<String> filterMode);
//    List<Book> search(Map<String, String> searchFields, int offset, int recordsPerPage, Optional<String> filterMode);
    List<Book> searchBooks(Map<String, String> searchFields);
    int getBookCount();
    Optional<Book> findById(short bookId);
    boolean changeBookCover(short bookId, String path);
}
