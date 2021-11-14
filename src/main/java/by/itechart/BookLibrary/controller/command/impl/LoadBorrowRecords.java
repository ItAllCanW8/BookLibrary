package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.service.BorrowRecordService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LoadBorrowRecords implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        try {
            short bookId = Short.parseShort(req.getParameter(RequestParameter.BOOK_ID));
            BorrowRecordService service = ServiceFactory.getInstance().getBorrowRecordService();

            List<BorrowRecord> borrowRecords = service.loadBorrowRecords(bookId);

            if(borrowRecords.size() > 0){
                Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                PrintWriter writer = resp.getWriter();

                writer.write(g.toJson(borrowRecords));
                writer.close();
            } else {
                resp.setStatus(204);
            }
        } catch (NumberFormatException | IOException e){
            throw new CommandException(e);
        }

        return new CommandResult(CommandResult.Type.ASYNC);
    }
}
