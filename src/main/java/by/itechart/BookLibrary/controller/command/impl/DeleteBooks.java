package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.PagePath;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeleteBooks implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        String bookIdsStr = req.getParameter(RequestParameter.BOOK_IDS);
        CommandResult result = new CommandResult(CommandResult.DEFAULT_PATH, CommandResult.Type.FORWARD);

        if(!bookIdsStr.isEmpty()){
            Set<Short> bookIds = new HashSet<>();
            String[] bookIdsSplitted = bookIdsStr.split(",");

            try{
                for (String id : bookIdsSplitted){
                    bookIds.add(Short.parseShort(id));
                }

                BookService service = ServiceFactory.getInstance().getBookService();

                if(service.delete(bookIds)){
                    result = new CommandResult(CommandResult.DEFAULT_PATH, CommandResult.Type.REDIRECT);
                }
            } catch (NumberFormatException | ServiceException e){
                throw new CommandException(e);
            }
        }

        return result;
    }
}
