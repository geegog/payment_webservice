package com.luminor.webservice.payment.application.service;

import com.luminor.webservice.common.application.dto.MoneyDTO;
import com.luminor.webservice.payment.application.dto.PaymentDTO;
import com.luminor.webservice.payment.domain.model.Payment;
import com.luminor.webservice.payment.rest.PaymentRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PaymentAssembler extends RepresentationModelAssemblerSupport<Payment, PaymentDTO> {

    public PaymentAssembler() {
        super(PaymentService.class, PaymentDTO.class);
    }

    @Override
    public PaymentDTO toModel(Payment payment) {
        PaymentDTO dto = instantiateModel(payment);
        MoneyDTO moneyDTO = MoneyDTO.of(payment.getMoney().getAmount(), payment.getMoney().getCurrency());
        dto.setId(payment.getId());
        dto.setCreated(payment.getCreated());
        dto.setUpdated(payment.getUpdated());
        dto.setBICCode(payment.getBICCode());
        dto.setCancellationFee(payment.getCancellationFee());
        dto.setCreditorIBAN(payment.getCreditorIBAN());
        dto.setDebtorIBAN(payment.getDebtorIBAN());
        dto.setDetails(payment.getDetails());
        dto.setType(payment.getType().name());
        dto.setStatus(payment.getStatus().name());
        dto.setMoney(moneyDTO);

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .create(dto, dto.getType())).withRel("create").withType(HttpMethod.POST.toString()));

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .cancelPayment(dto.getId().toString())).withRel("cancel").withType(HttpMethod.PATCH.toString()));

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .getPayment(dto.getId().toString())).withSelfRel().withType(HttpMethod.GET.toString()));

        dto.add(linkTo(methodOn(PaymentRestController.class)
                .getPayments(dto.getMoney().getAmount())).withRel("all").withType(HttpMethod.GET.toString()));


        return dto;
    }
}
