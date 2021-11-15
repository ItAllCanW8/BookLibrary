package by.itechart.BookLibrary.model.service.validation;

import by.itechart.BookLibrary.controller.attribute.JspAttribute;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.regex.Pattern;

public class BookValidator {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern TITLE_PATTERN = Pattern.compile("[\\w\\s,']{1,255}");
    private static final Pattern AUTHORS_PATTERN = Pattern.compile("([a-zA-Z'\\s]{2,255}[,\\s}]?)+");
    private static final Pattern PUBLISHER_PATTERN = Pattern.compile("[А-Яа-я\\w\\s,]{2,45}");
    private static final Pattern GENRES_PATTERN = Pattern.compile("([a-zA-Z-'\\s]{2,255}[,\\s}]?)+");
    static final Pattern PAGE_COUNT_PATTERN = Pattern.compile("[\\d]{1,5}");
    private static final Pattern ISBN_PATTERN = Pattern.compile("(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$" +
            "|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("[\\w\\s,!@#$%^&*()-.]{2,1000}");
    static final Pattern TOTAL_AMOUNT_PATTERN = Pattern.compile("[\\d]{1,5}");

    private BookValidator(){}

    public static boolean isBookFormValid(Map<String, String> fields) {
        boolean result = true;

        String title = fields.get(RequestParameter.BOOK_TITLE);
        if (!isTitleValid(title)) {
            fields.put(RequestParameter.BOOK_TITLE, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String authors = fields.get(RequestParameter.BOOK_AUTHORS);
        if (!isAuthorsFieldValid(authors)) {
            fields.put(RequestParameter.BOOK_AUTHORS, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String publisher = fields.get(RequestParameter.BOOK_PUBLISHER);
        if (!isPublisherValid(publisher)) {
            fields.put(RequestParameter.BOOK_PUBLISHER, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String genres = fields.get(RequestParameter.BOOK_GENRES);
        if (!isGenresFieldValid(genres)) {
            fields.put(RequestParameter.BOOK_GENRES, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String pageCount = fields.get(RequestParameter.BOOK_PAGE_COUNT);
        if (!isPageCountValid(pageCount)) {
            fields.put(RequestParameter.BOOK_PAGE_COUNT, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String isbn = fields.get(RequestParameter.BOOK_ISBN);
        if (!isISBNValid(isbn)) {
            fields.put(RequestParameter.BOOK_ISBN, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String description = fields.get(RequestParameter.BOOK_DESCRIPTION);
        if (!isDescriptionValid(description)) {
            fields.put(RequestParameter.BOOK_DESCRIPTION, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        String totalAmount = fields.get(RequestParameter.BOOK_TOTAL_AMOUNT);
        if (!isTotalAmountValid(totalAmount)) {
            fields.put(RequestParameter.BOOK_TOTAL_AMOUNT, JspAttribute.INVALID_INPUT_DATA_MSG);
            result = false;
        }

        return result;
    }

    private static boolean isTitleValid(String title) {
        boolean result = ValidationHelper.isFieldValid(title, TITLE_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Book title isn't valid: " + title);
        }
        return result;
    }

    private static boolean isAuthorsFieldValid(String authors) {
        boolean result = ValidationHelper.isFieldValid(authors, AUTHORS_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Author str isn't valid: " + authors);
        }
        return result;
    }

    private static boolean isPublisherValid(String publisher) {
        boolean result = ValidationHelper.isFieldValid(publisher, PUBLISHER_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Publisher isn't valid: " + publisher);
        }
        return result;
    }

    private static boolean isISBNValid(String isbn) {
        boolean result = ValidationHelper.isFieldValid(isbn, ISBN_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "ISBN isn't valid: " + isbn);
        }
        return result;
    }

    private static boolean isGenresFieldValid(String genre) {
        boolean result = ValidationHelper.isFieldValid(genre, GENRES_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Genre str isn't valid: " + genre);
        }
        return result;
    }

    private static boolean isPageCountValid(String pageCount) {
        boolean result = ValidationHelper.isFieldValid(pageCount, PAGE_COUNT_PATTERN)
                && Integer.parseInt(pageCount) <= Short.MAX_VALUE;

        if (!result) {
            LOGGER.log(Level.DEBUG, "Page count isn't valid: " + pageCount);
        }
        return result;
    }

    private static boolean isTotalAmountValid(String totalAmount) {
        boolean result = ValidationHelper.isFieldValid(totalAmount, TOTAL_AMOUNT_PATTERN)
                && Integer.parseInt(totalAmount) <= Short.MAX_VALUE;

        if (!result) {
            LOGGER.log(Level.DEBUG, "Total amount isn't valid: " + totalAmount);
        }
        return result;
    }

    private static boolean isDescriptionValid(String description) {
        boolean result = ValidationHelper.isFieldValid(description, DESCRIPTION_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Description isn't valid: " + description);
        }
        return result;
    }
}
