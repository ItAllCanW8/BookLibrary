package by.itechart.BookLibrary.model.service.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.exception.DaoException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.connection.DataSource;
import by.itechart.BookLibrary.model.dao.ReaderDao;
import by.itechart.BookLibrary.model.dao.impl.DaoFactory;
import by.itechart.BookLibrary.model.entity.Reader;
import by.itechart.BookLibrary.model.service.ReaderService;
import by.itechart.BookLibrary.model.service.validation.ReaderValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderServiceImpl implements ReaderService {
    private static final ReaderDao readerDao = DaoFactory.getInstance().getReaderDao();

    @Override
    public boolean add(List<Reader> readers) {
        boolean result = false;

        if (ReaderValidator.areReadersValid(readers)) {
            List<String> emails = new ArrayList<>();
            for (Reader reader: readers) {
                emails.add(reader.getEmail());
            }

            if (readerDao.areEmailsAvailable(emails)) {
                try (Connection connection = DataSource.getConnection()) {
                    result = readerDao.add(connection, readers);
                } catch (DaoException | SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
        return result;
    }

    @Override
    public List<Reader> loadReaders() {
        try{
            return readerDao.loadReaders();
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
