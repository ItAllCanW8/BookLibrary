package by.itechart.BookLibrary.controller;

import by.itechart.BookLibrary.controller.attribute.JspAttribute;
import by.itechart.BookLibrary.controller.attribute.ServletAttribute;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandProvider;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@WebServlet(urlPatterns = ServletAttribute.SERVLET_PATTERN, name = ServletAttribute.SERVLET_NAME)
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger();

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
