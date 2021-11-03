package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.dao.BorrowRecordDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.service.BorrowRecordService;

import java.util.List;

public class BorrowRecordServiceImpl implements BorrowRecordService {
    private static final BorrowRecordDao borrowRecordDao = DaoFactory.getInstance().getBorrowRecordDao();
    @Override
    public List<BorrowRecord> loadBorrowRecords(short bookId) {
        try{
            return borrowRecordDao.loadBorrowRecords(bookId);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
