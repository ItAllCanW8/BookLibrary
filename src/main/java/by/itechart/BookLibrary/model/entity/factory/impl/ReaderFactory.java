package by.itechart.BookLibrary.model.entity.factory.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.entity.Reader;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;

import java.util.Map;
import java.util.Optional;

public class ReaderFactory implements EntityFactory<Reader> {
    private static class Holder {
        static final EntityFactory<Reader> INSTANCE = new ReaderFactory();
    }

    public static EntityFactory<Reader> getInstance() {
        return ReaderFactory.Holder.INSTANCE;
    }

    private ReaderFactory() {
    }

    @Override
    public Optional<Reader> create(Map<String, String> fields) {
        return Optional.of(new Reader(
                fields.get(RequestParameter.READER_NAME),
                fields.get(RequestParameter.READER_EMAIL)));
    }

}
