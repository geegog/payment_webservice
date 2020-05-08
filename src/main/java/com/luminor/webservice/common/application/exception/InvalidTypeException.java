package com.luminor.webservice.common.application.exception;

import com.luminor.webservice.payment.application.dto.PaymentDTO;

public class InvalidTypeException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTypeException() {
        super();
    }


    public InvalidTypeException(String typeInURL, String typeInPayload) {
        super(String.format("Payment Types Error!! (types: %s %s)", typeInURL, typeInPayload));
    }
}

