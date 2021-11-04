package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.dao.impl.BookDaoImpl;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;
import by.itechart.BookLibrary.model.entity.factory.impl.BookFactory;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.validation.BookValidator;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.LEFT_PARENTHESIS;

public class BookServiceImpl implements BookService {
    private static final BookDao bookDao = DaoFactory.getInstance().getBookDao();
    private static final EntityFactory<Book> bookFactory = BookFactory.getInstance();

    BookServiceImpl() {
    }

    @Override
    public boolean add(Map<String, String> fields) {
        boolean result = false;

//        if (BookValidator.isBookFormValid(fields)) {
            Optional<Book> bookOptional = bookFactory.create(fields);

            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();

                if (bookDao.isTitleAvailable(fields.get(RequestParameter.BOOK_TITLE))) {
                    try (Connection connection = DataSource.getConnection()) {
                        connection.setAutoCommit(false);

                        if (bookDao.add(connection, book)
                                && insertFields(connection, book, Optional.empty(), true)
                                && insertFields(connection, book, Optional.empty(), false)) {

                            connection.commit();
                            result = true;
                        } else {
                            connection.rollback();
                        }
                    } catch (DaoException | NumberFormatException | SQLException e) {
                        throw new ServiceException(e);
                    }
                }
            }
//        }
        return result;
    }

    @Override
    public boolean update(short bookId, Map<String, String> newFields) {
//            if (BookValidator.isBookFormValid(newFields)) {
        boolean result = false;
        Optional<Book> bookOptional = bookDao.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();

            if (book.getTitle().equals(newFields.get(RequestParameter.BOOK_TITLE)) ||
                    bookDao.isTitleAvailable(newFields.get(RequestParameter.BOOK_TITLE))) {

                updateBookInfo(book, newFields);
                try (Connection connection = DataSource.getConnection()) {
                    connection.setAutoCommit(false);

                    if (bookDao.update(connection, book)
                            && updateFields(connection, book, true)
                            && updateFields(connection, book, false)) {
                        connection.commit();
                        result = true;
                    } else {
                        connection.rollback();
                    }
                } catch (DaoException | SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
//    }

        return result;
    }

    @Override
    public boolean delete(Set<Short> bookIds) {
        try(Connection connection = DataSource.getConnection()){
            return bookDao.delete(connection, bookIds);
        } catch (DaoException | SQLException e){
            throw new ServiceException(e);
        }
    }

    private boolean insertFields(Connection connection, Book book, Optional<Set<String>> fieldsToInsForUpdOpt,
                                 boolean isForGenres) throws SQLException {
        Set<String> newFields;
        String loadFieldsQuery;
        String insertFieldsQuery;
        String insertBookFieldsQuery;

        if (isForGenres) {
            newFields = book.getGenres();
            loadFieldsQuery = BookDaoImpl.LOAD_GENRES;
            insertFieldsQuery = BookDaoImpl.INSERT_GENRES;
            insertBookFieldsQuery = BookDaoImpl.INSERT_BOOK_GENRES;
        } else {
            newFields = book.getAuthors();
            loadFieldsQuery = BookDaoImpl.LOAD_AUTHORS;
            insertFieldsQuery = BookDaoImpl.INSERT_AUTHORS;
            insertBookFieldsQuery = BookDaoImpl.INSERT_BOOK_AUTHORS;
        }

        Map<Short, String> fieldsFromDb = new HashMap<>();

        bookDao.selectRepeatingFields(connection, new StringBuilder(loadFieldsQuery), newFields, isForGenres, fieldsFromDb);

        boolean isForUpdate = fieldsToInsForUpdOpt.isPresent();
        Set<Short> fieldIdsToInsert = new HashSet<>();

        if (isForUpdate) {
            Set<String> fieldsToInsForUpd = fieldsToInsForUpdOpt.get();

            for (String field : fieldsFromDb.values()) {
                if (fieldsToInsForUpd.contains(field)) {
                    fieldIdsToInsert.add(fieldsFromDb
                            .entrySet()
                            .stream()
                            .filter(entry -> Objects.equals(entry.getValue(), field))
                            .map(Map.Entry::getKey)
                            .findFirst().get());
                }
            }
        } else {
            fieldIdsToInsert.addAll(fieldsFromDb.keySet());
        }

        if (newFields.size() > fieldsFromDb.size() || isForUpdate) {
            if (bookDao.insertNewFields(connection, new StringBuilder(insertFieldsQuery), newFields, fieldsFromDb, isForUpdate)) {
                fieldIdsToInsert.addAll(fieldsFromDb.keySet());
            }
        }

        return bookDao.insertBookFields(connection, new StringBuilder(insertBookFieldsQuery), fieldIdsToInsert, book.getId());
    }

    private boolean updateFields(Connection connection, Book book, boolean isForGenres) throws SQLException {
        Set<String> newBookFields;
        BidiMap<Short, String> oldBookFields = new DualHashBidiMap<>();
        String loadFieldsQuery;
        String deleteFieldsQuery;

        if (isForGenres) {
            newBookFields = book.getGenres();
            loadFieldsQuery = BookDaoImpl.LOAD_OLD_BOOK_GENRES;
            deleteFieldsQuery = BookDaoImpl.DELETE_OLD_BOOK_GENRES;
        } else {
            newBookFields = book.getAuthors();
            loadFieldsQuery = BookDaoImpl.LOAD_OLD_BOOK_AUTHORS;
            deleteFieldsQuery = BookDaoImpl.DELETE_OLD_BOOK_AUTHORS;
        }

        boolean result = bookDao.selectOldBookFields(connection, oldBookFields, isForGenres, loadFieldsQuery, book.getId());

        if (!newBookFields.equals(oldBookFields.values())) {
            bookDao.deleteOldBookFields(connection, oldBookFields, newBookFields,
                    new StringBuilder(deleteFieldsQuery + LEFT_PARENTHESIS), book.getId());

            Set<String> fieldsToInsertForUpd = new HashSet<>();
            for (String field : newBookFields) {
                if (!oldBookFields.containsValue(field)) {
                    fieldsToInsertForUpd.add(field.toLowerCase());
                }
            }

            if (fieldsToInsertForUpd.size() > 0) {
                result = isForGenres ? insertFields(connection, book, Optional.of(fieldsToInsertForUpd), true) :
                        insertFields(connection, book, Optional.of(fieldsToInsertForUpd), false);
            }
        }

        return result;
    }

    @Override
    public List<Book> loadBooks(int offset, int recordsPerPage, Optional<String> filterMode) {
        try {
            return bookDao.loadBooks(offset, recordsPerPage, filterMode);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Book> searchBooks(Map<String, String> searchFields) {
        try{
            Set<String> authors = new HashSet<>();
            Set<String> genres = new HashSet<>();

            String authorsStr = searchFields.get(RequestParameter.BOOK_AUTHORS);
            String genresStr = searchFields.get(RequestParameter.BOOK_GENRES);

            if(!authorsStr.equals("")){
                authors = Arrays.stream(authorsStr
                        .split(", "))
                        .collect(Collectors.toSet());
                System.out.println(authors);
            }
            if(!genresStr.equals("")){
                genres = Arrays.stream(genresStr
                        .split(", "))
                        .collect(Collectors.toSet());
                System.out.println(genres);
            }

            return bookDao.searchBooks(searchFields.get(RequestParameter.BOOK_TITLE),
                    searchFields.get(RequestParameter.BOOK_DESCRIPTION), authors, genres);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    public int getBookCount() {
        try {
            return bookDao.getBookCount();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<Book> findById(short bookId) {
        try {
            return bookDao.findById(bookId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean changeBookCover(short bookId, String path) {
        try {
            return bookDao.changeBookCover(bookId, path);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void updateBookInfo(Book book, Map<String, String> fields) {
        String newTitle = fields.get(RequestParameter.BOOK_TITLE);

        String newAuthorsStr = fields.get(RequestParameter.BOOK_AUTHORS);
        Set<String> newAuthors = Arrays.stream(newAuthorsStr.split(", ")).collect(Collectors.toSet());

        String newPublisher = fields.get(RequestParameter.BOOK_PUBLISHER);
        String newPublishDate = fields.get(RequestParameter.BOOK_PUBLISH_DATE);

        String newGenresStr = fields.get(RequestParameter.BOOK_GENRES);
        Set<String> newGenres = Arrays.stream(newGenresStr.split(", ")).collect(Collectors.toSet());

        short newPageCount = Short.parseShort(fields.get(RequestParameter.BOOK_PAGE_COUNT));
        String newIsbn = fields.get(RequestParameter.BOOK_ISBN);
        short newTotalAmount = Short.parseShort(fields.get(RequestParameter.BOOK_TOTAL_AMOUNT));
        String newDescription = fields.get(RequestParameter.BOOK_DESCRIPTION);

        short remainingAmount = book.getRemainingAmount();
        String statusPrefix = remainingAmount > 0 ? "Available (" : "Unavailable (";

        if (remainingAmount > newTotalAmount) {
            book.setRemainingAmount(newTotalAmount);
        } else if (remainingAmount < newTotalAmount) {
            short oldTotalAmount = book.getTotalAmount();
            book.setRemainingAmount((short) (remainingAmount + (newTotalAmount - oldTotalAmount)));
        }

        String newStatus = statusPrefix + book.getRemainingAmount() + " out of " + newTotalAmount + ")";

        book.setTitle(newTitle);
        book.setAuthors(newAuthors);
        book.setPublisher(newPublisher);
        book.setPublishDate(newPublishDate);
        book.setGenres(newGenres);
        book.setPageCount(newPageCount);
        book.setIsbn(newIsbn);
        book.setTotalAmount(newTotalAmount);
        book.setDescription(newDescription);
        book.setStatus(newStatus);
    }
}
