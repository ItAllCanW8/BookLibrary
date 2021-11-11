package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.ReaderDao;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.entity.Reader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.*;
import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.COMMA;

public class ReaderDaoImpl implements ReaderDao {
    public static final String SELECT_READERS = "SELECT email, name FROM readers;";
    public static final String INSERT = "INSERT INTO readers(email, name) VALUES ";

    private static final String readerNameCol = "name";
    private static final String readerEmailCol = "email";

    @Override
    public boolean add(Connection connection, List<Reader> readers) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuerySB = new StringBuilder(INSERT);

            byte counter = (byte) readers.size();
            for (Reader reader : readers) {
                insertQuerySB.append(LEFT_PARENTHESIS).append(APOSTROPHE).append(reader.getEmail()).append(APOSTROPHE)
                        .append(COMMA).append(APOSTROPHE).append(reader.getName()).append(APOSTROPHE).append(RIGHT_PARENTHESIS);

                if (--counter != 0) {
                    insertQuerySB.append(COMMA);
                }
            }

            return statement.executeUpdate(insertQuerySB.toString()) > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

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
