package com.konstde00.filmcatalog.model.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class UserNotInGroupException extends RuntimeException {

    public UserNotInGroupException(String message) {
        super(message);
    }
}
