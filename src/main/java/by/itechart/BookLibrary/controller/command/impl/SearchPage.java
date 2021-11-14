package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.PagePath;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchPage implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp) {
        return new CommandResult(PagePath.SEARCH_PAGE, CommandResult.Type.FORWARD);
    }
}
