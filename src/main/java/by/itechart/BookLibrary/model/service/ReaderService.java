package by.itechart.BookLibrary.model.service;

import by.itechart.BookLibrary.model.entity.Reader;

import java.util.List;

public interface ReaderService {
    boolean add(List<Reader> readers);
    List<Reader> loadReaders();
}
