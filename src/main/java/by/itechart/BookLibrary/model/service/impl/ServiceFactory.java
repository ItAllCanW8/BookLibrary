package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.BorrowRecordService;
import by.itechart.BookLibrary.model.service.ReaderService;

public class ServiceFactory {
    private final BookService bookService = new BookServiceImpl();
    private final ReaderService readerService = new ReaderServiceImpl();
    private final BorrowRecordService borrowRecordService = new BorrowRecordServiceImpl();

    private ServiceFactory() {
    }

    private static class Holder {
        static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return Holder.INSTANCE;
    }

    public BorrowRecordService getBorrowRecordService() { return borrowRecordService; }

    public BookService getBookService() {
        return bookService;
    }

    public ReaderService getReaderService() {
        return readerService;
    }
}
