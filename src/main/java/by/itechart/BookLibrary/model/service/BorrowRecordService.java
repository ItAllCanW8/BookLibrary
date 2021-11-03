package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.util.List;

public interface BorrowRecordService {
    List<BorrowRecord> loadBorrowRecords(short bookId);
}
