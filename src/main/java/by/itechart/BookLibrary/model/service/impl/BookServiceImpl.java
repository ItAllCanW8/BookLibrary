package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;
import by.itechart.BookLibrary.model.entity.factory.impl.BookFactory;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.validation.BookValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private static final BookDao bookDao = DaoFactory.getInstance().getBookDao();
    private static final EntityFactory<Book> bookFactory = BookFactory.getInstance();

    BookServiceImpl(){}

    @Override
    public boolean add(Map<String, String> fields) throws ServiceException {
        try {
            Optional<Book> bookOptional = bookFactory.create(fields);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                if(bookDao.isTitleAvailable(fields.get(RequestParameter.BOOK_TITLE)))
                    return (bookDao.add(book));
            }
        } catch (DaoException | NumberFormatException e) {
            throw new ServiceException(e);
        }
        return false;
    }

    @Override
    public boolean update(short bookId, Map<String, String> newFields) throws ServiceException {
        try {
            if (BookValidator.isBookFormValid(newFields)) {
                Optional<Book> bookOptional = bookDao.findById(bookId);
                if (bookOptional.isPresent()) {
                    Book book = bookOptional.get();

                    if(book.getTitle().equals(newFields.get(RequestParameter.BOOK_TITLE)) ||
                            bookDao.isTitleAvailable(newFields.get(RequestParameter.BOOK_TITLE))){
                        updateBookInfo(book, newFields);
                        return (bookDao.update(book));
                    }
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return false;
    }

    @Override
    public List<Book> loadBookList() throws ServiceException {
        try {
            return bookDao.loadBookList();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<Book> findById(short bookId) throws ServiceException {
        try {
            return bookDao.findById(bookId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeBookCover(short bookId, String path) throws ServiceException {
        try {
            return bookDao.changeBookCover(bookId, path);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void updateBookInfo(Book book, Map<String, String> fields) {
        String newTitle = fields.get(RequestParameter.BOOK_TITLE);
        String newAuthor = fields.get(RequestParameter.BOOK_AUTHOR);
        String newIsbn = fields.get(RequestParameter.BOOK_ISBN);
        short newQuantity = Short.parseShort(fields.get(RequestParameter.BOOK_QUANTITY));
        String newGenre = fields.get(RequestParameter.BOOK_GENRE);
        String newDescription = fields.get(RequestParameter.BOOK_DESCRIPTION);

        book.setTitle(newTitle);
        book.setAuthorPseudo(newAuthor);
        book.setIsbn(newIsbn);
        book.setAvailableQuantity(newQuantity);
        book.setGenre(newGenre);
        book.setShortDescription(newDescription);
    }
}
