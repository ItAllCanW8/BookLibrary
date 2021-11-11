package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;

public class LoadAvailabilityDate implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            short bookId = Short.parseShort(req.getParameter(RequestParameter.BOOK_ID));

            Optional<String> availabilityDateOpt = ServiceFactory.getInstance().getBorrowRecordService()
                    .findAvailabilityDate(bookId);

            if(availabilityDateOpt.isPresent()){
                String availabilityDate = LocalDate.parse(availabilityDateOpt.get().split(" ")[0]).toString();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                PrintWriter writer = resp.getWriter();
                writer.write(new Gson().toJson(availabilityDate));
                writer.close();
            } else {
                resp.setStatus(204);
            }
        } catch (ServiceException | NumberFormatException | IOException e){
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
