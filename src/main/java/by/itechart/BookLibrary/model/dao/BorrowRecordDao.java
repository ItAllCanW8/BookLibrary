package by.itechart.BookLibrary.model.dao;


import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.sql.Connection;
import java.util.List;

public interface BorrowRecordDao {
    boolean add(Connection connection, List<BorrowRecord> borrowRecords);
    List<BorrowRecord> loadBorrowRecords(short bookId);
}
