package com.luminor.webservice.common.application.exception;

public class BadRequestException extends Exception {
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(String.format("Invalid request! (%s)", message));
    }
}
