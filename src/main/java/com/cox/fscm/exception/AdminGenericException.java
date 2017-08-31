package com.cox.fscm.exception;

import java.io.Serializable;

public class AdminGenericException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;

    public AdminGenericException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
