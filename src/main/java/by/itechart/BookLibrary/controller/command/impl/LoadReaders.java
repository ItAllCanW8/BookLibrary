package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Reader;
import by.itechart.BookLibrary.model.service.ReaderService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LoadReaders implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        ReaderService readerService = ServiceFactory.getInstance().getReaderService();

        try{
            List<Reader> readers = readerService.loadReaders();

            if(readers.size() > 0){
                Gson g = new Gson();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                PrintWriter writer = resp.getWriter();
                writer.write(g.toJson(readers));
//                writer.write(readers.toString());
                writer.close();

//                req.setAttribute(RequestParameter.READERS, g.toJson(readers));
            } else {
                resp.setStatus(204);
            }
        } catch (ServiceException | IOException e){
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
