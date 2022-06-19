package com.example.crypto.infrastructure.exception;

public class FileNotFoundException extends NotFoundException {

    public FileNotFoundException() {
        super("File not found");
    }
}
