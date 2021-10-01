package by.itechart.BookLibrary.controller.command;

import by.itechart.BookLibrary.controller.command.impl.BookPage;
import by.itechart.BookLibrary.controller.command.impl.Home;
import by.itechart.BookLibrary.controller.command.impl.LoadBookCover;
import by.itechart.BookLibrary.controller.command.impl.UploadBookCover;

public enum CommandType {
    HOME {{
        this.command = new Home();
    }},
    BOOK_PAGE {{
        this.command = new BookPage();
    }},
    UPLOAD_BOOK_COVER {{
        this.command = new UploadBookCover();
    }},
    LOAD_BOOK_COVER {{
        this.command = new LoadBookCover();
    }};

    Command command;

    public Command getCurrentCommand() {
        return command;
    }
}
