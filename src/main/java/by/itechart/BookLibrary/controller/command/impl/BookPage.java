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
import java.time.LocalDate;
import java.util.Optional;

public class BookPage implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp){
        String bookId = req.getParameter(RequestParameter.BOOK_ID);

        CommandResult result = new CommandResult(PagePath.BOOK_PAGE, CommandResult.Type.FORWARD);
        req.setAttribute(RequestParameter.CURRENT_DATE, LocalDate.now());

        if(bookId != null){
            try {
                BookService service = ServiceFactory.getInstance().getBookService();
                Optional<Book> bookOptional = service.findById(Short.parseShort(bookId));

                if (bookOptional.isPresent()) {
                    Book book = bookOptional.get();
                    req.setAttribute(RequestParameter.BOOK, book);
                    result = new CommandResult(PagePath.BOOK_PAGE, CommandResult.Type.FORWARD);
                }
            } catch (ServiceException | NumberFormatException e) {
                throw new CommandException(e);
            }
        }
        return result;
    }
}
