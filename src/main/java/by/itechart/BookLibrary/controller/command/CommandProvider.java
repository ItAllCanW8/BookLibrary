package by.itechart.BookLibrary.controller.command;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CommandProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DO_SUBSTRING = ".do";
    private static final String SLASH = "/";

    private CommandProvider() {
    }

    public static Optional<Command> defineCommand(HttpServletRequest request) {
        Optional<String> optionalCommand = parseCommandName(request.getRequestURI());
        if (optionalCommand.isPresent()) {
            String stringCommand = optionalCommand.get();
            try {
                CommandType commandType = CommandType.valueOf(stringCommand.toUpperCase());
                return Optional.of(commandType.getCurrentCommand());
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.ERROR, "Command " + stringCommand + " isn't correct: " + e);
            }
        }
        return Optional.empty();
    }

    public static Optional<String> parseCommandName(String url) {
        int doPosition = url.indexOf(DO_SUBSTRING);
        if (doPosition == -1) {
            return Optional.empty();
        }
        int lastSlashPosition = url.lastIndexOf(SLASH);
        return Optional.of(url.substring(lastSlashPosition + 1, doPosition));
    }
}
