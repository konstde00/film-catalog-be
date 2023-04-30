package com.konstde00.filmcatalog.model.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED)
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
