package com.konstde00.filmcatalog;

import com.konstde00.filmcatalog.util.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    @Test
    public void testValidPassword() {
        String password = "myPassword123";
        assertTrue(PasswordValidator.isValid(password));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        String password = "short1";
        assertFalse(PasswordValidator.isValid(password));
    }

    @Test
    public void testInvalidPasswordNoLetter() {
        String password = "123456789";
        assertFalse(PasswordValidator.isValid(password));
    }

    @Test
    public void testInvalidPasswordNoDigit() {
        String password = "abcdefghi";
        assertFalse(PasswordValidator.isValid(password));
    }

    @Test
    public void testInvalidPasswordNoLetterOrDigit() {
        String password = "!@#$%^&*";
        assertFalse(PasswordValidator.isValid(password));
    }
}

