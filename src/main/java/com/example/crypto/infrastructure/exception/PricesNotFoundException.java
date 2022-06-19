package com.example.crypto.infrastructure.exception;

public class PricesNotFoundException extends NotFoundException {

    public PricesNotFoundException() {
        super("Prices not found for given criteria");
    }
}
