package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.model.service.BookService;

public class ServiceFactory {
    private final BookService bookService = new BookServiceImpl();

    private ServiceFactory() {
    }

    private static class Holder {
        static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return Holder.INSTANCE;
    }

    public BookService getBookService() {
        return bookService;
    }
}
