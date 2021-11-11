package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.entity.Reader;
import by.itechart.BookLibrary.model.service.BorrowRecordService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddReaders implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            List<Reader> readers = Arrays.asList(new Gson().fromJson(json, Reader[].class));

            if(ServiceFactory.getInstance().getReaderService().add(readers)){
                resp.setStatus(200);
            }
        } catch (ServiceException | IOException e) {
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
