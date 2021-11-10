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
    DELETE_BOOKS{{
        this.command = new DeleteBooks();
    }},
    EDIT_BOOK{{
        this.command = new EditBook();
    }},
    UPLOAD_BOOK_COVER {{
        this.command = new UploadBookCover();
    }},
    LOAD_BOOK_COVER {{
        this.command = new LoadBookCover();
    }},
    LOAD_READERS {{
        this.command = new LoadReaders();
    }},
    SEARCH_PAGE {{
        this.command = new SearchPage();
    }},
    SEARCH{{
        this.command = new Search();
    }},
    LOAD_BORROW_RECORDS{{
        this.command = new LoadBorrowRecords();
    }},
    ADD_BORROW_RECORDS{{
        this.command = new AddBorrowRecords();
    }},
    UPDATE_BORROW_RECORDS{{
        this.command = new UpdateBorrowRecords();
    }},
    LOAD_AVAILABILITY_DATE{{
        this.command = new LoadAvailabilityDate();
    }};

    Command command;

    public Command getCurrentCommand() {
        return command;
    }
}
