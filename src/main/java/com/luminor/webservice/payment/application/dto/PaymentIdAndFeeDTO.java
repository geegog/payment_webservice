package com.luminor.webservice.payment.application.dto;

import com.luminor.webservice.common.application.dto.CancellationMoneyDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
public class PaymentIdAndFeeDTO extends RepresentationModel<PaymentIdAndFeeDTO> {

    private UUID id;

    private CancellationMoneyDTO cancellationFee;
}
