package com.luminor.webservice.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentIdsDTO extends RepresentationModel<PaymentIdsDTO> {

    private List<UUID> ids;

}
