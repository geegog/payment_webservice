package com.luminor.webservice.common.application.exception;

public class CannotCancelException extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotCancelException() {
        super(String.format("Cannot cancel payment!"));
    }
}
