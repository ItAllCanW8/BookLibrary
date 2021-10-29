package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.ReaderDao;
import by.itechart.BookLibrary.model.entity.Reader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReaderDaoImpl implements ReaderDao {
    public static final String SELECT_READERS = "SELECT email, name FROM readers;";

    private static final String readerNameCol = "name";
    private static final String readerEmailCol = "email";

    @Override
    public List<Reader> loadReaders() {
        List<Reader> readers = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_READERS);

            while (rs.next()) {
                Reader reader = createReaderFromRS(rs, false);
                readers.add(reader);
            }

        } catch (SQLException e) {
            throw new DaoException("Error loading readers.", e);
        }

        return readers;
    }

    private Reader createReaderFromRS(ResultSet rs, boolean allFieldsPresented) throws SQLException {
        Reader reader = new Reader(
                rs.getString(readerNameCol),
                rs.getString(readerEmailCol));

        return reader;
    }
}
