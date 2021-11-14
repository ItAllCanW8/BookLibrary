package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.util.FileHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoadBookCover implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp){
        String fileName = req.getParameter(RequestParameter.BOOK_COVER);

        if (fileName != null && !fileName.isEmpty()) {
            try (ServletOutputStream outputStream = resp.getOutputStream()) {
                outputStream.write(FileHandler.readFile(fileName, FileHandler.BOOK_COVERS_SUBFOLDER));
            } catch (IOException | ServiceException e) {
                LOGGER.log(Level.ERROR, e);
            }
        }
        return null;
    }
}
