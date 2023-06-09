package com.konstde00.filmcatalog.model.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class CacheException extends RuntimeException{
    public CacheException(String message) {
        super(message);
    }
}
