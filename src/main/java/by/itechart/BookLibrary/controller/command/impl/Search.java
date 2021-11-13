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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        BookService service = ServiceFactory.getInstance().getBookService();

        System.out.println("SEARCH");

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

            System.out.println(searchFields);

            List<Book> books = service.searchBooks(searchFields);

            if (books.size() > 0) {
                req.setAttribute(RequestParameter.BOOKS, books);
                System.out.println("EEE");
                System.out.println(books);
//                req.setAttribute(RequestParameter.NUMBER_OF_PAGES, numberOfPages);
//                req.setAttribute(RequestParameter.CURRENT_PAGE, page);
//                req.setAttribute(RequestParameter.RECORDS_PER_PAGE, recordsPerPage);

//                filterModeOptional.ifPresent(filterMode -> req.setAttribute(RequestParameter.FILTER_MODE, filterMode));
            }
        } catch (
                ServiceException e) {
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
