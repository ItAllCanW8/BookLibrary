package by.itechart.BookLibrary.model.entity.factory.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BookFactory implements EntityFactory<Book> {
    public static final String DEFAULT_COVER = "default_book_cover.png";

    private static class Holder {
        static final EntityFactory<Book> INSTANCE = new BookFactory();
    }

    public static EntityFactory<Book> getInstance() {
        return BookFactory.Holder.INSTANCE;
    }

    private BookFactory() {
    }

    @Override
    public Optional<Book> create(Map<String, String> fields) {
        String title = fields.get(RequestParameter.BOOK_TITLE);

        String authorsStr = fields.get(RequestParameter.BOOK_AUTHORS);
        Set<String> authors = Arrays.stream(authorsStr.split(", ")).collect(Collectors.toSet());

        String publisher = fields.get(RequestParameter.BOOK_PUBLISHER);
        String publishDate = fields.get(RequestParameter.BOOK_PUBLISH_DATE);

        String genresStr = fields.get(RequestParameter.BOOK_GENRES);
        Set<String> genres = Arrays.stream(genresStr.split(", ")).collect(Collectors.toSet());

        short pageCount = Short.parseShort(fields.get(RequestParameter.BOOK_PAGE_COUNT));
        String isbn = fields.get(RequestParameter.BOOK_ISBN);
        short totalAmount = Short.parseShort(fields.get(RequestParameter.BOOK_TOTAL_AMOUNT));
        String description = fields.get(RequestParameter.BOOK_DESCRIPTION);
        String status = "Available (" + totalAmount + " out of " + totalAmount + ")";

        return Optional.of(new Book(DEFAULT_COVER, title, authors, publisher, publishDate, genres,
                pageCount, isbn, description, totalAmount, totalAmount, status));
    }
}
