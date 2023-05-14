package com.konstde00.filmcatalog;

import com.konstde00.filmcatalog.model.exception.NotValidException;
import com.konstde00.filmcatalog.util.PasswordValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    @Test
    public void testValidPassword() {
        String password = "myPassword123";
        assertDoesNotThrow(() -> PasswordValidator.validatePassword(password));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        String password = "short1";

        Exception exception = assertThrows(NotValidException.class, () -> {
            PasswordValidator.validatePassword(password);
        });

        String expectedMessage = "Password is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testInvalidPasswordNoLetter() {
        String password = "123456789";
        Exception exception = assertThrows(NotValidException.class, () -> {
            PasswordValidator.validatePassword(password);
        });

        String expectedMessage = "Password is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testInvalidPasswordNoDigit() {
        String password = "abcdefghi";
        Exception exception = assertThrows(NotValidException.class, () -> {
            PasswordValidator.validatePassword(password);
        });

        String expectedMessage = "Password is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testInvalidPasswordNoLetterOrDigit() {
        String password = "!@#$%^&*";
        Exception exception = assertThrows(NotValidException.class, () -> {
            PasswordValidator.validatePassword(password);
        });

        String expectedMessage = "Password is not valid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

