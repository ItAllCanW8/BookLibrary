package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.model.entity.Reader;

import java.sql.Connection;
import java.util.List;

public interface ReaderDao {
    boolean add(Connection connection, List<Reader> readers);
    List<Reader> loadReaders();
}
