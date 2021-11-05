package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import com.sun.xml.internal.bind.v2.TODO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.*;

public class BorrowRecordDaoImpl implements BorrowRecordDao {
    private static final String SELECT_LIST = "SELECT borrow_record_id, email, name, borrow_date, due_date, return_date, status, comment" +
            " FROM borrow_records JOIN readers ON reader_email_fk = email WHERE book_id_fk = ?;";

    public static final String INSERT = "INSERT INTO borrow_records(borrow_date, due_date, book_id_fk, reader_email_fk)" +
            " VALUES ";

    @Override
    public boolean add(Connection connection, List<BorrowRecord> borrowRecords) {
        try(Statement statement = connection.createStatement()){
            StringBuilder insertQuerySB = new StringBuilder(INSERT);

            byte counter = (byte) borrowRecords.size();
            for (BorrowRecord borrowRecord : borrowRecords){
                insertQuerySB.append(LEFT_PARENTHESIS).append(APOSTROPHE).append(borrowRecord.getBorrowDate()).append(APOSTROPHE)
                        .append(COMMA).append(APOSTROPHE).append(borrowRecord.getDueDate()).append(APOSTROPHE).append(COMMA)
                        .append(borrowRecord.getBookIdFk()).append(COMMA).append(APOSTROPHE)
                        .append(borrowRecord.getReaderEmail()).append(APOSTROPHE).append(RIGHT_PARENTHESIS);
//                Incorrect datetime value: '05.11.2021 13:43:56' for column 'borrow_date' at row 1
                //TODO fix datetime
                if(--counter != 0){
                    insertQuerySB.append(COMMA);
                }
            }

            return statement.executeUpdate(insertQuerySB.toString()) > 0;
        } catch (SQLException e) {
            throw new DaoException("Error adding borrow records.", e);
        }
    }

    @Override
    public List<BorrowRecord> loadBorrowRecords(short bookId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LIST)) {
            statement.setShort(1, bookId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                borrowRecords.add(createBorrowRecFromRS(rs));
            }
        } catch (SQLException | NumberFormatException e) {
            throw new DaoException("Error loading borrow records.", e);
        }

        return borrowRecords;
    }

    private BorrowRecord createBorrowRecFromRS(ResultSet rs) throws SQLException {
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

        return new BorrowRecord(borrowRecId, borrowDate, dueDate, returnDate, status, comment, readerEmail, readerName);
    }
}
