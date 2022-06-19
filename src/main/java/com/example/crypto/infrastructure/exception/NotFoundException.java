package com.example.crypto.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception which should be thrown/emitted in cases where the error is occurred because of the client has requested
 * to view a resource that doesn't exist.
 *
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }


    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
