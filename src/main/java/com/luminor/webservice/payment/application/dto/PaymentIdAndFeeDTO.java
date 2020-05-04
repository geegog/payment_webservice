package com.luminor.webservice.payment.application.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentIdAndFeeDTO extends RepresentationModel<PaymentIdAndFeeDTO> {

    private UUID id;

    private BigDecimal cancellationFee;
}
