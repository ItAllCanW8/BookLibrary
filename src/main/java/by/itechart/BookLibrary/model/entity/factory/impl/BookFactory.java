package by.itechart.BookLibrary.model.entity.factory.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;
import by.itechart.BookLibrary.model.service.validation.BookValidator;

import java.util.Map;
import java.util.Optional;

public class BookFactory implements EntityFactory<Book> {
    public static final String DEFAULT_COVER = "default_book_cover.png";

    private static class Holder {
        static final EntityFactory<Book> INSTANCE = new BookFactory();
    }
    public static EntityFactory<Book> getInstance() {
        return BookFactory.Holder.INSTANCE;
    }
    private BookFactory() {}
    @Override
    public Optional<Book> create(Map<String, String> fields) {
        Optional<Book> result = Optional.empty();

//        if (BookValidator.isBookFormValid(fields)) {
//            String title = fields.get(RequestParameter.BOOK_TITLE);
//            String authors = fields.get(RequestParameter.BOOK_AUTHORS);
//            String publisher = fields.get(RequestParameter.BOOK_PUBLISHER);
//            String publishDate = fields.get(RequestParameter.BOOK_PUBLISH_DATE);
//            String genres = fields.get(RequestParameter.BOOK_GENRES);
//            short pageCount = Short.parseShort(fields.get(RequestParameter.BOOK_PAGE_COUNT));
//            String isbn = fields.get(RequestParameter.BOOK_ISBN);
//            short totalAmount = Short.parseShort(fields.get(RequestParameter.BOOK_TOTAL_AMOUNT));
//            String description = fields.get(RequestParameter.BOOK_DESCRIPTION);
//            String status = "Available (" + totalAmount + " out of " + totalAmount + ")";
//
//            result = Optional.of(new Book(DEFAULT_COVER, title, authors, publisher, publishDate, genres, pageCount, isbn,
//                    description, totalAmount, totalAmount, status));
//        }
        return result;
    }
}
