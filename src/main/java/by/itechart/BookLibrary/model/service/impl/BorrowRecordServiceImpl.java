package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.service.BorrowRecordService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BorrowRecordServiceImpl implements BorrowRecordService {
    private static final BorrowRecordDao borrowRecordDao = DaoFactory.getInstance().getBorrowRecordDao();

    @Override
    public boolean add(List<BorrowRecord> borrowRecords) {
        try(Connection connection = DataSource.getConnection()){
            return borrowRecordDao.add(connection, borrowRecords);
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(List<BorrowRecord> borrowRecords) {
        try(Connection connection = DataSource.getConnection()){
            return borrowRecordDao.update(connection, borrowRecords);
        } catch (DaoException | SQLException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BorrowRecord> loadBorrowRecords(short bookId) {
        try{
            return borrowRecordDao.loadBorrowRecords(bookId);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<String> findAvailabilityDate(short bookId) {
        try{
            return borrowRecordDao.findAvailabilityDate(bookId);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
