package by.itechart.BookLibrary.model.dao;

import by.itechart.BookLibrary.model.entity.Book;
import org.apache.commons.collections4.BidiMap;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BookDao {
    boolean add(Connection connection, Book book);
    boolean selectRepeatingFields(Connection connection, StringBuilder sb, Set<String> newFields,
                                  boolean isForGenres, Map<Short, String> fieldsFromDb);
    boolean insertNewFields(Connection connection, StringBuilder sb, Set<String> newFields, Map<Short,
            String> fieldsFromDb, boolean isForUpdate);
    boolean insertBookFields(Connection connection, StringBuilder sb, Set<Short> newFieldIds, short bookIdFk);
    boolean update(Connection connection, Book book);
    boolean selectOldBookFields(Connection connection, BidiMap<Short, String> oldBookFields, boolean isForGenres,
                                     String query, short bookId);
    void deleteOldBookFields(Connection connection, BidiMap<Short, String> oldBookFields, Set<String> newBookFields,
                                     StringBuilder sb, short bookId);

    List<Book> loadBooks(int offset, int recordsPerPage, Optional<String> filterMode);
    int getBookCount();
    Optional<Book> findById(short bookId);
    boolean changeBookCover(short bookId, String path);
    boolean isTitleAvailable(String title);
}
