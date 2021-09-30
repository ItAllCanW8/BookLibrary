package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.model.dao.BookDao;

public class DaoFactory {
    private final BookDao bookDao = new BookDaoImpl();

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
