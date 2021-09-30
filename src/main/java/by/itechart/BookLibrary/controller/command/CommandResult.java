package by.itechart.BookLibrary.controller.command;

import by.itechart.BookLibrary.controller.attribute.CommandUrl;
import by.itechart.BookLibrary.controller.attribute.PagePath;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommandResult {
    public static final String DEFAULT_PATH = CommandUrl.HOME;

    private String path;
    private Type type;

    public enum Type {
        FORWARD,
        REDIRECT
    }
    public CommandResult(String path, Type type) {
        this.path = path;
        this.type = type;
    }
    public CommandResult(String path) {
        this.path = path;
        this.type = Type.FORWARD;
    }
    public CommandResult(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String providePath() {
        return (path == null || path.isEmpty() ? DEFAULT_PATH : path);
    }

    public void redirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (this.getType()) {
            case FORWARD: {
                request.getRequestDispatcher(this.providePath()).forward(request, response);
                break;
            }
            case REDIRECT: {
                response.sendRedirect(request.getContextPath() + this.providePath());
                break;
            }
            default: {
                request.getRequestDispatcher(PagePath.ERROR_404).forward(request, response);
            }
        }
    }
}
