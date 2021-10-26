package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.CommandUrl;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AddBook implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> fields = EditBook.receiveBookFields(req);

        BookService service = ServiceFactory.getInstance().getBookService();
        CommandResult result = new CommandResult(CommandUrl.HOME, CommandResult.Type.REDIRECT);
        try {
            if (!service.add(fields)) {
                req.setAttribute(RequestParameter.BOOK_TITLE, fields.get(RequestParameter.BOOK_TITLE));
                req.setAttribute(RequestParameter.BOOK_AUTHORS, fields.get(RequestParameter.BOOK_AUTHORS));
                req.setAttribute(RequestParameter.BOOK_PUBLISHER, fields.get(RequestParameter.BOOK_PUBLISHER));
                req.setAttribute(RequestParameter.BOOK_PUBLISH_DATE, fields.get(RequestParameter.BOOK_PUBLISH_DATE));
                req.setAttribute(RequestParameter.BOOK_GENRES, fields.get(RequestParameter.BOOK_GENRES));
                req.setAttribute(RequestParameter.BOOK_PAGE_COUNT, fields.get(RequestParameter.BOOK_PAGE_COUNT));
                req.setAttribute(RequestParameter.BOOK_ISBN, fields.get(RequestParameter.BOOK_ISBN));
                req.setAttribute(RequestParameter.BOOK_TOTAL_AMOUNT, fields.get(RequestParameter.BOOK_TOTAL_AMOUNT));
                req.setAttribute(RequestParameter.BOOK_DESCRIPTION, fields.get(RequestParameter.BOOK_DESCRIPTION));

                result = new CommandResult(CommandUrl.HOME, CommandResult.Type.FORWARD);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return result;
    }
}
