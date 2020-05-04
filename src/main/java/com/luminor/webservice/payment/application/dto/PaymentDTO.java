package com.luminor.webservice.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luminor.webservice.common.application.dto.MoneyDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO extends RepresentationModel<PaymentDTO> {

    private UUID id;

    private LocalDateTime created;

    private LocalDateTime updated;

    private MoneyDTO money;

    private String debtorIBAN;

    private String creditorIBAN;

    private String type;

    private String status;

    private BigDecimal cancellationFee;

    private String details;

    @JsonProperty("BICCode")
    private String BICCode;
}
