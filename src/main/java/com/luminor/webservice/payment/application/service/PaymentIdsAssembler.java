package com.luminor.webservice.payment.application.service;

import com.luminor.webservice.payment.application.dto.PaymentIdsDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentIdsAssembler extends RepresentationModelAssemblerSupport<List<UUID>, PaymentIdsDTO> {

    public PaymentIdsAssembler() {
        super(PaymentService.class, PaymentIdsDTO.class);
    }

    @Override
    public PaymentIdsDTO toModel(List<UUID> paymentIds) {
        PaymentIdsDTO dto = instantiateModel(paymentIds);
        dto.setIds(paymentIds);

        return dto;
    }
}
