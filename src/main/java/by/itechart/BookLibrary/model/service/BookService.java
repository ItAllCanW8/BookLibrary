package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> loadBooks() throws ServiceException;
}
