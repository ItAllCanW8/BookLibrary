package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Search implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        BookService service = ServiceFactory.getInstance().getBookService();

        try {
            Map<String, String> searchFields = new HashMap<>();
            String title = req.getParameter(RequestParameter.BOOK_TITLE);
            String description = req.getParameter(RequestParameter.BOOK_DESCRIPTION);
            String authors = req.getParameter(RequestParameter.BOOK_AUTHORS);
            String genres = req.getParameter(RequestParameter.BOOK_GENRES);

            searchFields.put(RequestParameter.BOOK_TITLE, title);
            searchFields.put(RequestParameter.BOOK_AUTHORS, authors);
            searchFields.put(RequestParameter.BOOK_GENRES, genres);
            searchFields.put(RequestParameter.BOOK_DESCRIPTION, description);

            Set<Book> books = service.searchBooks(searchFields);

            if (books.size() > 0) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                PrintWriter writer = resp.getWriter();
                writer.write(new Gson().toJson(books));
                writer.close();
            } else {
                resp.setStatus(204);
            }
        } catch (
                ServiceException | IOException e) {
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
