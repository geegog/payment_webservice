package com.luminor.webservice.common.application.exception;

import com.luminor.webservice.payment.application.dto.PaymentDTO;

public class PaymentNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PaymentNotFoundException() {
        super();
    }

    public PaymentNotFoundException(PaymentDTO paymentDTO, Throwable cause) {
        super(String.format("Payment not found! (Payment id: %s)", paymentDTO.getId()), cause);
    }

    public PaymentNotFoundException(String id) {
        super(String.format("Payment not found! (Record id: %s)", id));
    }
}

