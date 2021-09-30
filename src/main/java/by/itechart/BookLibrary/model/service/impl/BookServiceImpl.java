package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {
    private static final BookDao bookDao = DaoFactory.getInstance().getBookDao();

    BookServiceImpl(){}

    @Override
    public List<Book> loadBooks() throws ServiceException {
        try {
            return bookDao.loadBooks();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
