package by.itechart.BookLibrary.model.service.validation;

import by.itechart.BookLibrary.controller.attribute.JspAttribute;
import by.itechart.BookLibrary.controller.attribute.RequestParameter;
import by.itechart.BookLibrary.model.entity.Reader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

public class ReaderValidator {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,255}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("((\\w)|(\\w[.-]\\w))+@((\\w)|(\\w[.-]\\w))+.[a-zA-Zа-яА-Я]{2,4}");

    private ReaderValidator(){}

    public static boolean areReadersValid(List<Reader> readers) {
        boolean result = true;

        for (Reader reader: readers){
            if(!isEmailValid(reader.getEmail()) || !isNameValid(reader.getName())){
                result = false;
            }
        }

        return result;
    }

    private static boolean isEmailValid(String email) {
        boolean result = ValidationHelper.isFieldValid(email, EMAIL_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Reader email isn't valid: " + email);
        }
        return result;
    }

    private static boolean isNameValid(String name) {
        boolean result = ValidationHelper.isFieldValid(name, NAME_PATTERN);
        if (!result) {
            LOGGER.log(Level.DEBUG, "Reader name isn't valid: " + name);
        }
        return result;
    }
}
