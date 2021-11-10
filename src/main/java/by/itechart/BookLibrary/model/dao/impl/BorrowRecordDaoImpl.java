package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.*;

public class BorrowRecordDaoImpl implements BorrowRecordDao {
    private static final String SELECT_LIST = "SELECT borrow_record_id, email, name, borrow_date, due_date, return_date, status, comment" +
            " FROM borrow_records JOIN readers ON reader_email_fk = email WHERE book_id_fk = ?;";

    private static final String INSERT = "INSERT INTO borrow_records(borrow_date, due_date, book_id_fk, reader_email_fk)" +
            " VALUES ";

    private static final String UPDATE = "UPDATE borrow_records SET status = (";

    private static final String WHEN_READER_EMAIL = " WHEN reader_email_fk = ";

    private static final String FIND_AVAILABILITY_DATE = "SELECT MIN(due_date) FROM borrow_records WHERE book_id_fk = ?" +
            " AND return_date IS NULL;";

    public static final String CASE = " CASE ";
    public static final String THEN = " THEN ";
    public static final String END = " END ";
    public static final String AND = " AND ";

    @Override
    public boolean add(Connection connection, List<BorrowRecord> borrowRecords) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuerySB = new StringBuilder(INSERT);

            byte counter = (byte) borrowRecords.size();
            for (BorrowRecord borrowRecord : borrowRecords) {
                insertQuerySB.append(LEFT_PARENTHESIS).append(APOSTROPHE).append(borrowRecord.getBorrowDate()).append(APOSTROPHE)
                        .append(COMMA).append(APOSTROPHE).append(borrowRecord.getDueDate()).append(APOSTROPHE).append(COMMA)
                        .append(borrowRecord.getBookIdFk()).append(COMMA).append(APOSTROPHE)
                        .append(borrowRecord.getReaderEmail()).append(APOSTROPHE).append(RIGHT_PARENTHESIS);

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
    public boolean update(Connection connection, List<BorrowRecord> borrowRecords) {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(prepareUpdQuery(borrowRecords)) > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private String prepareUpdQuery(List<BorrowRecord> borrowRecords){
        StringBuilder updateQuerySB = new StringBuilder(UPDATE);
        boolean isCommentPresented = false;
        List<String> borrowRecEmails = new ArrayList<>();

        updateQuerySB.append(CASE);

        byte counter = (byte) borrowRecords.size();
        for (BorrowRecord borrowRecord : borrowRecords) {
            if (!isCommentPresented && borrowRecord.getComment() != null) {
                isCommentPresented = true;
            }

            updateQuerySB.append(WHEN_READER_EMAIL).append(APOSTROPHE).append(borrowRecord.getReaderEmail())
                    .append(APOSTROPHE).append(THEN).append(APOSTROPHE).append(borrowRecord.getStatus())
                    .append(APOSTROPHE);

            borrowRecEmails.add(borrowRecord.getReaderEmail());

            if (--counter == 0) {
                updateQuerySB.append(END).append(RIGHT_PARENTHESIS).append(COMMA);
            }
        }

        updateQuerySB.append("return_date = (").append(CASE);

        counter = (byte) borrowRecords.size();
        for (BorrowRecord borrowRecord : borrowRecords) {
            updateQuerySB.append(WHEN_READER_EMAIL).append(APOSTROPHE).append(borrowRecord.getReaderEmail())
                    .append(APOSTROPHE).append(THEN).append(APOSTROPHE).append(borrowRecord.getReturnDate())
                    .append(APOSTROPHE);

            if (--counter == 0) {
                updateQuerySB.append(END).append(RIGHT_PARENTHESIS);
            }
        }

        if (isCommentPresented) {
            updateQuerySB.append(COMMA).append("comment = (").append(CASE);

            counter = (byte) borrowRecords.size();
            for (BorrowRecord borrowRecord : borrowRecords) {
                updateQuerySB.append(WHEN_READER_EMAIL).append(APOSTROPHE).append(borrowRecord.getReaderEmail())
                        .append(APOSTROPHE).append(THEN).append(APOSTROPHE).append(borrowRecord.getComment())
                        .append(APOSTROPHE);

                if (--counter == 0) {
                    updateQuerySB.append(END).append(RIGHT_PARENTHESIS);
                }
            }
        }

        updateQuerySB.append("WHERE reader_email_fk IN").append(WHITESPACE).append(LEFT_PARENTHESIS);

        counter = (byte) borrowRecEmails.size();
        for (String email : borrowRecEmails) {
            updateQuerySB.append(APOSTROPHE).append(email).append(APOSTROPHE);

            if (--counter != 0) {
                updateQuerySB.append(COMMA);
            } else {
                updateQuerySB.append(RIGHT_PARENTHESIS);
            }
        }

        updateQuerySB.append(AND).append("book_id_fk = ").append(borrowRecords.get(0).getBookIdFk());

        return updateQuerySB.toString();
    }

    @Override
    public List<BorrowRecord> loadBorrowRecords(short bookId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LIST)) {
            statement.setShort(1, bookId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                borrowRecords.add(createBorrowRecFromRS(rs, bookId));
            }
        } catch (SQLException | NumberFormatException e) {
            throw new DaoException("Error loading borrow records.", e);
        }

        return borrowRecords;
    }

    @Override
    public Optional<String> findAvailabilityDate(short bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_AVAILABILITY_DATE)) {
            statement.setShort(1, bookId);
            ResultSet rs = statement.executeQuery();

            return rs.next() ? Optional.of(rs.getString("min(due_date)")) : Optional.empty();
        } catch (SQLException | NumberFormatException e) {
            throw new DaoException("Error loading borrow records.", e);
        }
    }

    private BorrowRecord createBorrowRecFromRS(ResultSet rs, short bookId) throws SQLException {
        short borrowRecId = rs.getShort("borrow_record_id");
//        LocalDateTime borrowDate = LocalDateTime.parse(rs.getString("borrow_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        LocalDateTime dueDate = LocalDateTime.parse(rs.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String borrowDate = rs.getString("borrow_date");
        String dueDate = rs.getString("due_date");
        String returnDate = rs.getString("return_date");

//        if(rs.getString("return_date") != null){
//            returnDate = rs.getString("return_date");
//        }

        String status = rs.getString("status");
        String comment = rs.getString("comment");
        String readerEmail = rs.getString("email");
        String readerName = rs.getString("name");


        return new BorrowRecord(borrowRecId, borrowDate, dueDate, returnDate, status, comment, readerEmail, readerName,
                bookId);
    }
}
