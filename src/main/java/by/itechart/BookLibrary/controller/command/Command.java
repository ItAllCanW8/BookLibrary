package by.itechart.BookLibrary.controller.command;

import by.itechart.BookLibrary.exception.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    CommandResult execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException;
}
