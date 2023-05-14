package com.konstde00.filmcatalog.util;
import com.konstde00.filmcatalog.model.exception.NotValidException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PasswordValidator {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    //Must be at least 8 characters long
    //Must contain at least one letter
    //Must contain at least one digit
    public static void validatePassword(String password) {

        var isValidPassword = PASSWORD_PATTERN.matcher(password).matches();
        if (!isValidPassword) {
            throw new NotValidException("Password is not valid");
        }
    }
}
