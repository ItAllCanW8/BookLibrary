package by.itechart.BookLibrary.controller.command;

import by.itechart.BookLibrary.controller.command.impl.*;

public enum CommandType {
    HOME {{
        this.command = new Home();
    }},
    BOOK_PAGE {{
        this.command = new BookPage();
    }},
    ADD_BOOK{{
        this.command = new AddBook();
    }},
    EDIT_BOOK{{
        this.command = new EditBook();
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
