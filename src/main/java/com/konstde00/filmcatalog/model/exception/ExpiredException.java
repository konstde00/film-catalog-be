package com.konstde00.filmcatalog.model.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.GONE;

@ResponseStatus(value = GONE)
public class ExpiredException extends RuntimeException {

    public ExpiredException(String message) {
        super(message);
    }
}
