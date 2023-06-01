package com.siewe.inventorymanagementsystem.utils;

public class InvalidOrderException extends Exception {
    public InvalidOrderException() {
        super();
    }

    public InvalidOrderException(String message) {
        super(message);
    }

    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}