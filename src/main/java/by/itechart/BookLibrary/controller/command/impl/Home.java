package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.PagePath;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Home implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        BookService service = ServiceFactory.getInstance().getBookService();

        try {
            int page = req.getParameter(RequestParameter.PAGE) != null ?
                    Integer.parseInt(req.getParameter(RequestParameter.PAGE)) : 1;

            int recordsPerPage = req.getParameter(RequestParameter.RECORDS_PER_PAGE) != null ?
                    Integer.parseInt(req.getParameter(RequestParameter.RECORDS_PER_PAGE)) : 10;

            int bookCount = service.getBookCount();
            int numberOfPages = (int) Math.ceil(bookCount * 1.0 / recordsPerPage);

            List<Book> books = service.loadBookList((page - 1) * recordsPerPage, recordsPerPage);
            if (books.size() > 0) {
                req.setAttribute(RequestParameter.BOOKS, books);
                req.setAttribute(RequestParameter.NUMBER_OF_PAGES, numberOfPages);
                req.setAttribute(RequestParameter.CURRENT_PAGE, page);
                req.setAttribute(RequestParameter.RECORDS_PER_PAGE, recordsPerPage);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        return new CommandResult(PagePath.HOME, CommandResult.Type.FORWARD);
    }
}
