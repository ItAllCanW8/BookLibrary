package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.util.List;

public interface BorrowRecordService {
    boolean add(List<BorrowRecord> borrowRecords);
    boolean update(List<BorrowRecord> borrowRecords);
    List<BorrowRecord> loadBorrowRecords(short bookId);
}
