package com.luminor.webservice.common.application.exception;

public class AlreadyCancelledException extends Exception {
    private static final long serialVersionUID = 1L;

    public AlreadyCancelledException() {
        super(String.format("Payment already cancelled!"));
    }
}
