package com.luminor.webservice.payment.rest;

import com.luminor.webservice.common.application.exception.*;
import com.luminor.webservice.payment.application.dto.PaymentDTO;
import com.luminor.webservice.payment.application.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentRestController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentRestController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/type/{type}/create")
    public ResponseEntity<PaymentDTO> create(@RequestBody PaymentDTO paymentDTO, @PathVariable String type) {
        try {
            return new ResponseEntity<>(paymentService.create(paymentDTO, type), HttpStatus.CREATED);
        } catch (BadRequestException | InvalidTypeException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable String id) {
        try {
            return new ResponseEntity<>(paymentService.cancel(id), HttpStatus.ACCEPTED);
        } catch (CannotCancelException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);
        } catch (AlreadyCancelledException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}/one")
    public ResponseEntity<?> getPayment(@PathVariable String id) {
        try {
            return new ResponseEntity<>(paymentService.findPayment(id), HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/amount/{amount}")
    public ResponseEntity<?> getPayments(@PathVariable BigDecimal amount) {
        return new ResponseEntity<>(paymentService.allPaymentIdsNotCancelled(amount), HttpStatus.OK);
    }

}
