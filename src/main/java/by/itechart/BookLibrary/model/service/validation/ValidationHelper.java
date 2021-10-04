package by.itechart.BookLibrary.model.service.validation;

import java.util.regex.Pattern;

public class ValidationHelper {
    public static boolean isFieldValid(String field, Pattern pattern){
        return field != null && pattern.matcher(field).matches();
    }

    private ValidationHelper(){}
}
