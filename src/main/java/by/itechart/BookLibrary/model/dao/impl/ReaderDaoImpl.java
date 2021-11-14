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
    private static final String CHECK_EMAILS_FOR_EXISTENCE = " SELECT email FROM readers WHERE ";

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
    public boolean areEmailsAvailable(List<String> emails) {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {

            StringBuilder querySB = new StringBuilder(CHECK_EMAILS_FOR_EXISTENCE);

            byte counter = (byte) emails.size();
            for (String email : emails) {
                querySB.append("email").append(EQUALS).append(APOSTROPHE).append(email).append(APOSTROPHE);

                if (--counter != 0) {
                    querySB.append(OR);
                }
            }

            return !statement.executeQuery(querySB.toString()).next();
        } catch (SQLException e) {
            throw new DaoException("Error checking emails for availability.", e);
        }
    }

    @Override
    public List<Reader> loadReaders() {
        List<Reader> readers = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_READERS);

            while (rs.next()) {
                Reader reader = createReaderFromRS(rs);
                readers.add(reader);
            }

        } catch (SQLException e) {
            throw new DaoException("Error loading readers.", e);
        }

        return readers;
    }

    private Reader createReaderFromRS(ResultSet rs) throws SQLException {
        return new Reader(
                rs.getString(readerEmailCol),
                rs.getString(readerNameCol));
    }
}
