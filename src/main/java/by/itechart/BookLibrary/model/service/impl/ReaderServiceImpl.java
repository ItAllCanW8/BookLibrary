package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.dao.ReaderDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Reader;
import by.itechart.BookLibrary.model.service.ReaderService;

import java.util.List;

public class ReaderServiceImpl implements ReaderService {
    private static final ReaderDao readerDao = DaoFactory.getInstance().getReaderDao();

    @Override
    public List<Reader> loadReaders() {
        try{
            return readerDao.loadReaders();
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
