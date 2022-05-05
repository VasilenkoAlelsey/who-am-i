package com.eleks.academy.whoami.exceptions;

public class FailToObtainException extends RuntimeException {
    public FailToObtainException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
