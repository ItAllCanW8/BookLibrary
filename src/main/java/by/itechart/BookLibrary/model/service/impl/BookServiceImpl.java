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
    public List<Book> loadBookList(int offset, int recordsPerPage, Optional<String> filterMode) throws ServiceException {
        try {
            return bookDao.loadBookList(offset, recordsPerPage, filterMode);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getBookCount() throws ServiceException {
        try {
            return bookDao.getBookCount();
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
        String newAuthors = fields.get(RequestParameter.BOOK_AUTHORS);
        String newPublisher = fields.get(RequestParameter.BOOK_PUBLISHER);
        String newPublishDate = fields.get(RequestParameter.BOOK_PUBLISH_DATE);
        String newGenres = fields.get(RequestParameter.BOOK_GENRES);
        short newPageCount = Short.parseShort(fields.get(RequestParameter.BOOK_PAGE_COUNT));
        String newIsbn = fields.get(RequestParameter.BOOK_ISBN);
        short newTotalAmount = Short.parseShort(fields.get(RequestParameter.BOOK_TOTAL_AMOUNT));
        String newDescription = fields.get(RequestParameter.BOOK_DESCRIPTION);

        System.out.println(newDescription);

        short remainingAmount  = book.getRemainingAmount();
        String statusPrefix = remainingAmount > 0 ? "Available (" : "Unavailable (";

        if(remainingAmount > newTotalAmount){
            book.setRemainingAmount(newTotalAmount);
        } else if(remainingAmount < newTotalAmount){
            short oldTotalAmount = book.getTotalAmount();
            book.setRemainingAmount((short) (remainingAmount + (newTotalAmount - oldTotalAmount)));
        }

        String newStatus = statusPrefix + book.getRemainingAmount() + " out of " + newTotalAmount + ")";

        book.setTitle(newTitle);
//        book.setAuthors(newAuthors);
        book.setPublisher(newPublisher);
        book.setPublishDate(newPublishDate);
//        book.setGenres(newGenres);
        book.setPageCount(newPageCount);
        book.setIsbn(newIsbn);
        book.setTotalAmount(newTotalAmount);
        book.setDescription(newDescription);
        book.setStatus(newStatus);
    }
}
