package by.itechart.BookLibrary.controller;

import by.itechart.BookLibrary.controller.attribute.JspAttribute;
import by.itechart.BookLibrary.controller.attribute.ServletAttribute;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandProvider;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.BorrowRecord;
import by.itechart.BookLibrary.model.service.MailService;
import by.itechart.BookLibrary.model.service.impl.MailServiceImpl;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import by.itechart.BookLibrary.util.mail.MailSender;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@WebServlet(urlPatterns = ServletAttribute.SERVLET_PATTERN, name = ServletAttribute.SERVLET_NAME)
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        try{
            List<BorrowRecord> borrowRecords = ServiceFactory.getInstance().getBorrowRecordService().loadDataForNotifications();

            if(borrowRecords.size() > 0){
                new MailServiceImpl().sendNotificationsToReaders(borrowRecords);
            }
        } catch (ServiceException | IOException | TemplateException e){
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Command> command = CommandProvider.defineCommand(req);
        try {
            CommandResult result = command.isPresent() ? command.get().execute(req, resp)
                    : new CommandResult(CommandResult.DEFAULT_PATH);
            result.redirect(req, resp);
        } catch (CommandException e) {
            LOGGER.log(Level.ERROR, "Couldn't process req: " + e);
            req.setAttribute(JspAttribute.ERROR_MSG, e.getMessage());
            throw new ServletException(e);
        }
    }
}
