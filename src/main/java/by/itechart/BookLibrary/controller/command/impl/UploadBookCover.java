package by.itechart.BookLibrary.controller.command.impl;

import by.itechart.BookLibrary.controller.attribute.CommandUrl;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.controller.command.Command;
import by.itechart.BookLibrary.controller.command.CommandResult;
import by.itechart.BookLibrary.exception.CommandException;
import by.itechart.BookLibrary.exception.ServiceException;
import by.itechart.BookLibrary.model.entity.Book;
import by.itechart.BookLibrary.model.service.BookService;
import by.itechart.BookLibrary.model.service.impl.ServiceFactory;
import by.itechart.BookLibrary.util.FileHandler;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
public class UploadBookCover implements Command {
    @Override
    public CommandResult execute(HttpServletRequest req, HttpServletResponse resp){
        String bookId = req.getParameter(RequestParameter.BOOK_ID);
        BookService service = ServiceFactory.getInstance().getBookService();

        try {
            Optional<Book> bookOptional = service.findById(Short.parseShort(bookId));
            if (ServletFileUpload.isMultipartContent(req) && bookOptional.isPresent()) {
                Book book = bookOptional.get();
                Part part;

                try {
                    part = req.getPart(RequestParameter.BOOK_COVER);
                } catch (IOException | ServletException e) {
                    throw new CommandException(e);
                }

                String path = part.getSubmittedFileName();
                if (path != null && !path.isEmpty()) {
                    String randomFilename = UUID.randomUUID() + path.substring(path.lastIndexOf(FileHandler.DOT_SYMBOL));

                    try (InputStream inputStream = part.getInputStream()) {
                        if (FileHandler.uploadFile(inputStream, FileHandler.WEBAPP_FOLDER_PATH
                                + FileHandler.BOOK_COVERS_SUBFOLDER + randomFilename) &&
                                service.changeBookCover(book.getId(), randomFilename)) {
                            book.setCover(randomFilename);
                            req.setAttribute(RequestParameter.BOOK, book);
                        }
                    }
                }
            }
        } catch (IOException | ServletException | ServiceException e) {
            throw new CommandException(e);
        }
        return new CommandResult(CommandUrl.BOOK_PAGE + bookId, CommandResult.Type.REDIRECT);
    }
}
