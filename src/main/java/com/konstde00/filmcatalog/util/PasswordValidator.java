package com.konstde00.filmcatalog.util;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PasswordValidator {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    //Must be at least 8 characters long
    //Must contain at least one letter
    //Must contain at least one digit
    public static boolean isValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
