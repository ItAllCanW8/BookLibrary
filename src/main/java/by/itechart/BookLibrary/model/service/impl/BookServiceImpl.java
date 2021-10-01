package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.service.BookService;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private static final BookDao bookDao = DaoFactory.getInstance().getBookDao();

    BookServiceImpl(){}

    @Override
    public List<Book> loadBookList() throws ServiceException {
        try {
            return bookDao.loadBookList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<Book> findById(long bookId) throws ServiceException {
        try {
            return bookDao.findById(bookId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeBookCover(long bookId, String path) throws ServiceException {
        try {
            return bookDao.changeBookCover(bookId, path);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
