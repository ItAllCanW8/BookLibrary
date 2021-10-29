package by.itechart.BookLibrary.model.entity.factory.impl;

import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.entity.factory.EntityFactory;

import java.util.Map;
import java.util.Optional;

public class BorrowRecordFactory implements EntityFactory<BorrowRecord> {
    @Override
    public Optional<BorrowRecord> create(Map<String, String> fields) {
        return Optional.empty();
    }
}
