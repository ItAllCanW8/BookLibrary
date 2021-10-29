package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.model.entity.Reader;

import java.util.List;

public interface ReaderDao {
    List<Reader> loadReaders();
}
