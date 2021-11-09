package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.CommandUrl;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EditBook implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp){
        String bookId = req.getParameter(RequestParameter.BOOK_ID);

//        System.out.println(req.getParameter("status"));
//        System.out.println(req.getParameter("input"));
        System.out.println(req.getParameter("remainingAmount"));
        System.out.println(req.getParameter("newBookStatus"));

        System.out.println(req.getParameterMap().entrySet());

        BookService service = ServiceFactory.getInstance().getBookService();
        CommandResult result = new CommandResult(CommandUrl.BOOK_PAGE + bookId, CommandResult.Type.REDIRECT);
        try {
            if (!service.update(Short.parseShort(bookId), receiveBookFields(req))) {
                result = new CommandResult(CommandUrl.BOOK_PAGE + bookId, CommandResult.Type.FORWARD);
            }
        } catch (ServiceException | NumberFormatException e) {
            throw new CommandException(e);
        }
        return result;
    }

    static Map<String, String> receiveBookFields(HttpServletRequest req) {
        String newTitle = req.getParameter(RequestParameter.BOOK_TITLE);
        String newAuthors = req.getParameter(RequestParameter.BOOK_AUTHORS);
        String newPublisher = req.getParameter(RequestParameter.BOOK_PUBLISHER);
        String newPublishDate = req.getParameter(RequestParameter.BOOK_PUBLISH_DATE);
        String newGenres = req.getParameter(RequestParameter.BOOK_GENRES);
        String newPageCount = req.getParameter(RequestParameter.BOOK_PAGE_COUNT);
        String newIsbn = req.getParameter(RequestParameter.BOOK_ISBN);
        String newTotalAmount = req.getParameter(RequestParameter.BOOK_TOTAL_AMOUNT);
        String newDescription = req.getParameter(RequestParameter.BOOK_DESCRIPTION);

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put(RequestParameter.BOOK_TITLE, newTitle);
        fields.put(RequestParameter.BOOK_AUTHORS, newAuthors);
        fields.put(RequestParameter.BOOK_PUBLISHER, newPublisher);
        fields.put(RequestParameter.BOOK_PUBLISH_DATE, newPublishDate);
        fields.put(RequestParameter.BOOK_GENRES, newGenres);
        fields.put(RequestParameter.BOOK_PAGE_COUNT, newPageCount);
        fields.put(RequestParameter.BOOK_ISBN, newIsbn);
        fields.put(RequestParameter.BOOK_TOTAL_AMOUNT, newTotalAmount);
        fields.put(RequestParameter.BOOK_DESCRIPTION, newDescription);

        return fields;
    }
}
