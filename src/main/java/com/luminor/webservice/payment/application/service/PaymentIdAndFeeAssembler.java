package com.luminor.webservice.payment.application.service;

import com.luminor.webservice.payment.application.dto.PaymentIdAndFeeDTO;
import com.luminor.webservice.payment.domain.model.Payment;
import com.luminor.webservice.payment.rest.PaymentRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PaymentIdAndFeeAssembler extends RepresentationModelAssemblerSupport<Payment, PaymentIdAndFeeDTO> {

    public PaymentIdAndFeeAssembler() {
        super(PaymentService.class, PaymentIdAndFeeDTO.class);
    }

    @Override
    public PaymentIdAndFeeDTO toModel(Payment payment) {
        PaymentIdAndFeeDTO dto = instantiateModel(payment);
        dto.setId(payment.getId());
        dto.setCancellationFee(payment.getCancellationFee());

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .cancelPayment(dto.getId().toString())).withRel("cancel").withType(HttpMethod.PATCH.toString()));

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .getPayment(dto.getId().toString())).withSelfRel().withType(HttpMethod.GET.toString()));

        return dto;
    }
}
