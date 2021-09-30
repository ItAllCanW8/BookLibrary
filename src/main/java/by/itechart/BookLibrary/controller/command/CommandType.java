package by.itechart.BookLibrary.controller.command;

import by.itechart.BookLibrary.controller.command.impl.Home;

public enum CommandType {
    HOME {{
        this.command = new Home();
    }};

    Command command;

    public Command getCurrentCommand() {
        return command;
    }
}
