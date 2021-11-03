package by.itechart.BookLibrary.model.dao;


import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.util.List;

public interface BorrowRecordDao {
    List<BorrowRecord> loadBorrowRecords(short bookId);
}
