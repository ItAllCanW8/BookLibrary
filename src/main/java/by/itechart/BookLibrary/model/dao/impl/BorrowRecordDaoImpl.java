package by.itechart.BookLibrary.model.dao.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.entity.BorrowRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.itechart.BookLibrary.model.dao.impl.SqlSymbols.*;

public class BorrowRecordDaoImpl implements BorrowRecordDao {
    private static final String SELECT_LIST = "SELECT borrow_record_id, email, name, borrow_date, due_date, return_date, status, comment" +
            " FROM borrow_records JOIN readers ON reader_email_fk = email WHERE book_id_fk = ?;";

    public static final String INSERT = "INSERT INTO borrow_records(borrow_date, due_date, book_id_fk, reader_email_fk)" +
            " VALUES ";

//    UPDATE borrow_records
//    SET status = (case when borrow_record_id = 20 then 'lost'
//    when borrow_record_id = 21 then 'returned and damaged'
//    end),
//    return_date = (case when borrow_record_id = 20 then '2021-11-08T13:13:48.+03:00'
//    when borrow_record_id = 21 then '2021-11-08T13:13:48.+03:00'
//    end),
//    comment = (case when borrow_record_id = 20 then '622057'
//    when borrow_record_id = 21 then '2913659'
//    end)
//    WHERE borrow_record_id in (20, 21);

    public static final String UPDATE = "UPDATE borrow_records SET status = (";
    public static final String WHEN_BR_ID = " WHEN borrow_record_id = ";
    public static final String CASE = " CASE";
    public static final String THEN = " THEN";
    public static final String END = " END";

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
//                {id=6, borrowDate=2021-11-08T13:13:48.+03:00, dueDate=2021-12-08T13:13:48.+03:00,
//                        returnDate=2021-11-08 13:13:56, status='returned', comment='.com', bookIdFk=62,
//                        readerEmail='tcoleman1y@auda.org.au', readerName='Tabbi'}
//                if(borrowRecord.getReturnDate() != null) {
//                    insertQuerySB.append(COMMA).append(APOSTROPHE)
//                }

//                insertQuerySB.append(RIGHT_PARENTHESIS);

                if(--counter != 0){
                    insertQuerySB.append(COMMA);
                }
            }

            return statement.executeUpdate(insertQuerySB.toString()) > 0;
        } catch (SQLException e) {
            throw new DaoException( e.getMessage());
        }
    }

    @Override
    public boolean update(Connection connection, List<BorrowRecord> borrowRecords) {

        //    UPDATE borrow_records
//    SET status = (case when borrow_record_id = 20 then 'lost'
//    when borrow_record_id = 21 then 'returned and damaged'
//    end),
//    return_date = (case when borrow_record_id = 20 then '2021-11-08T13:13:48.+03:00'
//    when borrow_record_id = 21 then '2021-11-08T13:13:48.+03:00'
//    end),
//    comment = (case when borrow_record_id = 20 then '622057'
//    when borrow_record_id = 21 then '2913659'
//    end)
//    WHERE borrow_record_id in (20, 21);
        try(Statement statement = connection.createStatement()){
            StringBuilder updateQuerySB = new StringBuilder(UPDATE);
            boolean isCommentPresented = false;
            List<Short> borrowRecIds = new ArrayList<>();

            updateQuerySB.append(CASE);

            byte counter = (byte) borrowRecords.size();
            for (BorrowRecord borrowRecord : borrowRecords){
                if(!isCommentPresented && borrowRecord.getComment() != null){
                    isCommentPresented = true;
                }

                updateQuerySB.append(WHEN_BR_ID).append(borrowRecord.getId()).append(THEN).append(WHITESPACE)
                        .append(APOSTROPHE).append(borrowRecord.getStatus()).append(APOSTROPHE);

                borrowRecIds.add(borrowRecord.getId());

                if(--counter == 0){
                    updateQuerySB.append(END).append(RIGHT_PARENTHESIS).append(COMMA);
                }
            }

            updateQuerySB.append("return_date = (").append(CASE);

            counter = (byte) borrowRecords.size();
            for (BorrowRecord borrowRecord : borrowRecords){
                updateQuerySB.append(WHEN_BR_ID).append(borrowRecord.getId()).append(THEN).append(WHITESPACE)
                        .append(APOSTROPHE).append(borrowRecord.getReturnDate()).append(APOSTROPHE);

                if(--counter == 0){
                    updateQuerySB.append(END).append(RIGHT_PARENTHESIS);
                }
            }

            if(isCommentPresented){
                updateQuerySB.append(COMMA).append("comment = (").append(CASE);

                counter = (byte) borrowRecords.size();
                for (BorrowRecord borrowRecord : borrowRecords){
                    updateQuerySB.append(WHEN_BR_ID).append(borrowRecord.getId()).append(THEN).append(WHITESPACE)
                            .append(APOSTROPHE).append(borrowRecord.getComment()).append(APOSTROPHE);

                    if(--counter == 0){
                        updateQuerySB.append(END).append(RIGHT_PARENTHESIS);
                    }
                }
            }

            updateQuerySB.append("WHERE borrow_record_id IN").append(WHITESPACE).append(LEFT_PARENTHESIS);

            counter = (byte) borrowRecIds.size();
            for (Short id : borrowRecIds){
                updateQuerySB.append(id);

                if(--counter != 0){
                    updateQuerySB.append(COMMA);
                } else {
                    updateQuerySB.append(RIGHT_PARENTHESIS);
                }
            }

            System.out.println(updateQuerySB);

            return statement.executeUpdate(updateQuerySB.toString()) > 0;
        } catch (SQLException e) {
            throw new DaoException( e.getMessage());
        }
    }

//    UPDATE borrow_records SET status = ( CASE WHEN borrow_record_id = 23 THEN 'returned and damaged'
//    CASE WHEN borrow_record_id = 25 THEN 'lost' CASE WHEN borrow_record_id = 21 THEN 'returned' END ,
//    return_date = ( CASE WHEN borrow_record_id = 23 THEN '2021-11-08 14:32:10'( CASE WHEN borrow_record_id = 25 THEN '2021-11-08 14:32:14'( CASE WHEN borrow_record_id = 21 THEN '2021-11-08 14:32:20' END ,
//    comment = ( CASE WHEN borrow_record_id = 23 THEN 'returned and damaged' CASE WHEN borrow_record_id = 25 THEN 'lost' CASE WHEN borrow_record_id = 21 THEN 'returned' END
//    WHERE borrow_record_id IN (23,25,21)

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
