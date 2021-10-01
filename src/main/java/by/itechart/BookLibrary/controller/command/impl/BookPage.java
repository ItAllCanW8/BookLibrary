package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.CommandUrl;
import by.itechart.BookLibrary.controller.attribute.JspAttribute;
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
import java.util.Optional;

public class BookPage implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        String bookId = req.getParameter(RequestParameter.BOOK_ID);
        BookService service = ServiceFactory.getInstance().getBookService();

//        CommandResult result = new CommandResult(CommandUrl.HOME, CommandResult.Type.FORWARD);
        CommandResult result = new CommandResult(PagePath.BOOK_PAGE, CommandResult.Type.FORWARD);
        try {
            Optional<Book> bookOptional = service.findById(Short.parseShort(bookId));
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                req.setAttribute(RequestParameter.BOOK, book);
                result = new CommandResult(PagePath.BOOK_PAGE, CommandResult.Type.FORWARD);
            }
        } catch (NumberFormatException e) {
//            req.setAttribute(JspAttribute.ERROR_INPUT_DATA, JspAttribute.ERROR_INPUT_DATA_MSG);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return result;
    }
}
