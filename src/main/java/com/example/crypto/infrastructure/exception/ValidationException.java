package com.example.crypto.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception which should be thrown/emitted in cases where the error is occurred because of the client has entered
 * invalid request parameters
 *
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }


    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
