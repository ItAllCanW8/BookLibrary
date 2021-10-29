package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.model.dao.BookDao;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.dao.ReaderDao;

public class DaoFactory {
    private final BookDao bookDao = new BookDaoImpl();

    public ReaderDao getReaderDao() {
        return readerDao;
    }

    public BorrowRecordDao getBorrowRecordDao() {
        return borrowRecordDao;
    }

    private final ReaderDao readerDao = new ReaderDaoImpl();
    private final BorrowRecordDao borrowRecordDao = new BorrowRecordDaoImpl();

    private DaoFactory() {
    }

    private static class Holder {
        static final DaoFactory INSTANCE = new DaoFactory();
    }
    public static DaoFactory getInstance() {
        return DaoFactory.Holder.INSTANCE;
    }

    public BookDao getBookDao() {
        return bookDao;
    }
}
