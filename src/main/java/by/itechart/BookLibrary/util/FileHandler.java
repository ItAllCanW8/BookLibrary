package by.itechart.BookLibrary.util;

import by.itechart.BookLibrary.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    public static final String DOT_SYMBOL = ".";

    private static final String DEFAULT_BOOK_COVER = "default_book_cover.png";

    public static final String WEBAPP_FOLDER_PATH = "F:" + File.separator + "University" + File.separator
            + "ITechArt" + File.separator + "BookLibrary" + File.separator + "src" + File.separator + "main" + File.separator +
            "webapp" + File.separator;

    public static final String BOOK_COVERS_SUBFOLDER = "images" + File.separator + "book-covers" + File.separator;

    public static final String BOOK_PDF_SUBFOLDER = "pdf" + File.separator;

    private FileHandler() {
    }

    public static boolean uploadFile(InputStream inputStream, String path) throws ServletException {
        try {
            byte[] bytes = new byte[inputStream.available()];
            if (inputStream.read(bytes) != -1) {
                FileOutputStream fops = new FileOutputStream(path);
                fops.write(bytes);
                fops.close();
                return true;
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }
        return false;
    }

    public static byte[] readFile(String fileName, String subfolder) throws ServiceException {
        byte[] result;
        String fileUri = FileHandler.WEBAPP_FOLDER_PATH + subfolder + fileName;
        Path filePath = Paths.get(fileUri);

        if (!Files.exists(filePath)) {
            filePath = Paths.get(FileHandler.WEBAPP_FOLDER_PATH + subfolder + DEFAULT_BOOK_COVER);
        }
        try {
            result = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return result;
    }
}
