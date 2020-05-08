package com.luminor.webservice.payment.application.service;

import com.luminor.webservice.common.application.exception.*;
import com.luminor.webservice.payment.application.dto.PaymentDTO;
import com.luminor.webservice.payment.application.dto.PaymentIdAndFeeDTO;
import com.luminor.webservice.payment.application.dto.PaymentIdsDTO;
import com.luminor.webservice.payment.domain.model.*;
import com.luminor.webservice.payment.domain.repository.PaymentRepository;
import com.luminor.webservice.payment.infrastructure.http.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private final static String EURO_CODE = "EUR";
    private final static String US_CODE = "USD";
    private final static float COEFFICIENT_TYPE1 = 0.05f;
    private final static float COEFFICIENT_TYPE2 = 0.1f;
    private final static float COEFFICIENT_TYPE3 = 0.15f;
    private final static List<String> allowedCurrencies = Arrays.asList(EURO_CODE, US_CODE);
    private final PaymentRepository paymentRepository;
    private final PaymentAssembler paymentAssembler;
    private final PaymentIdAndFeeAssembler paymentIdAndFeeAssembler;
    private final PaymentIdsAssembler paymentIdsAssembler;
    private final IpService ipService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentAssembler paymentAssembler,
                          PaymentIdAndFeeAssembler paymentIdAndFeeAssembler, PaymentIdsAssembler paymentIdsAssembler,
                          IpService ipService) {
        this.paymentRepository = paymentRepository;
        this.paymentAssembler = paymentAssembler;
        this.paymentIdAndFeeAssembler = paymentIdAndFeeAssembler;
        this.paymentIdsAssembler = paymentIdsAssembler;
        this.ipService = ipService;
    }

    private Payment savePayment(String creatorIBAN, String debtorIBAN, String BICCode, String details, Money money, Type type) {
        Payment payment = new Payment();
        payment.setBICCode(BICCode);
        payment.setCreditorIBAN(creatorIBAN);
        payment.setDebtorIBAN(debtorIBAN);
        payment.setDetails(details);
        payment.setMoney(money);
        payment.setType(type);
        payment.setStatus(Status.CREATED);

        return paymentRepository.save(payment);
    }

    @Transactional
    public PaymentDTO create(PaymentDTO paymentDTO, String type) throws BadRequestException, InvalidTypeException {

        if (!paymentDTO.getType().equals(type)) {
            throw new InvalidTypeException(type, paymentDTO.getType());
        }

        if (Type.TYPE1 == Type.valueOf(type)) {

            if (!paymentDTO.getType().equals(Type.TYPE1.name()) || paymentDTO.getCreditorIBAN() == null || paymentDTO.getDebtorIBAN() == null
                    || paymentDTO.getDetails() == null || paymentDTO.getMoney() == null || (paymentDTO.getMoney() != null
                    && paymentDTO.getMoney().getCurrency() != Currency.getInstance(EURO_CODE))) {
                String error = errorString(paymentDTO, Type.TYPE1);
                log.error("Payment error: {} Request Object: {} ip: {}", error, paymentDTO, ipService.ip());
                throw new BadRequestException(error);
            }

            Payment payment = savePayment(paymentDTO.getCreditorIBAN(), paymentDTO.getDebtorIBAN(), null,
                    paymentDTO.getDetails(), Money.of(paymentDTO.getMoney().getAmount(), paymentDTO.getMoney().getCurrency()), Type.TYPE1);

            log.info("New Payment Created: {} ip: {}", payment, ipService.ip());

            return paymentAssembler.toModel(payment);

        } else if (Type.TYPE2 == Type.valueOf(type)) {

            if (!paymentDTO.getType().equals(Type.TYPE2.name()) || paymentDTO.getCreditorIBAN() == null || paymentDTO.getDebtorIBAN() == null
                    || paymentDTO.getMoney() == null || (paymentDTO.getMoney() != null
                    && paymentDTO.getMoney().getCurrency() != Currency.getInstance(US_CODE))) {
                String error = errorString(paymentDTO, Type.TYPE2);
                log.error("Payment error: {} Request Object: {} ip: {}", error, paymentDTO, ipService.ip());
                throw new BadRequestException(error);
            }

            Payment payment = savePayment(paymentDTO.getCreditorIBAN(), paymentDTO.getDebtorIBAN(), null,
                    paymentDTO.getDetails(), Money.of(paymentDTO.getMoney().getAmount(), paymentDTO.getMoney().getCurrency()), Type.TYPE2);

            log.info("New Payment Created: {} ip: {}", payment, ipService.ip());

            return paymentAssembler.toModel(payment);

        } else {

            if (!paymentDTO.getType().equals(Type.TYPE3.name()) || paymentDTO.getCreditorIBAN() == null || paymentDTO.getDebtorIBAN() == null
                    || paymentDTO.getBICCode() == null || paymentDTO.getMoney() == null || (paymentDTO.getMoney() != null
                    && (!allowedCurrencies.contains(paymentDTO.getMoney().getCurrency().getCurrencyCode())))) {
                String error = errorString(paymentDTO, Type.TYPE3);
                log.error("Payment error: {} Request Object: {} ip: {}", error, paymentDTO, ipService.ip());
                throw new BadRequestException(error);
            }

            Payment payment = savePayment(paymentDTO.getCreditorIBAN(), paymentDTO.getDebtorIBAN(), paymentDTO.getBICCode(),
                    paymentDTO.getDetails(), Money.of(paymentDTO.getMoney().getAmount(), paymentDTO.getMoney().getCurrency()), Type.TYPE3);

            log.info("New Payment Created: {} ip: {}", payment, ipService.ip());

            return paymentAssembler.toModel(payment);

        }

    }

    private Payment getPayment(String id) {
        return paymentRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public PaymentIdAndFeeDTO findPayment(String id) throws PaymentNotFoundException {
        Payment payment = getPayment(id);
        if (payment == null) {
            log.error("Payment not found: {} ip: {}", id, ipService.ip());
            throw new PaymentNotFoundException(id);
        }
        return paymentIdAndFeeAssembler.toModel(getPayment(id));
    }

    public PaymentIdsDTO allPaymentIdsNotCancelled(BigDecimal amount) {
        List<UUID> ids = paymentRepository.getPaymentsNotCancelled(Status.CANCELLED, amount);
        return paymentIdsAssembler.toModel(ids);
    }

    public PaymentDTO cancel(String id) throws CannotCancelException, AlreadyCancelledException, PaymentNotFoundException {
        Payment payment = getPayment(id);
        if (payment == null) {
            log.error("Payment not found: {} ip: {}", id, ipService.ip());
            throw new PaymentNotFoundException(id);
        }
        if (payment.getStatus() == Status.CANCELLED) {
            log.info("Payment already cancelled: {} ip: {}", payment, ipService.ip());
            throw new AlreadyCancelledException();
        }
        if (!canCancel(payment.getCreated())) {
            log.error("Payment cannot be cancelled: {} ip: {}", payment, ipService.ip());
            throw new CannotCancelException();
        }

        payment.setCancellationFee(CancellationMoney.of(calculateCancellationFee(payment.getCreated(), payment.getType()), Currency.getInstance(EURO_CODE)));
        payment.setStatus(Status.CANCELLED);
        payment.setUpdated(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("Payment cancelled: {} ip: {}", payment, ipService.ip());

        return paymentAssembler.toModel(payment);
    }

    private boolean canCancel(LocalDateTime created) {
        LocalDate today = created.toLocalDate();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        return created.isBefore(endOfDay);
    }

    private BigDecimal calculateCancellationFee(LocalDateTime created, Type type) {
        if (type == Type.TYPE1) {
            return BigDecimal.valueOf(numberOfFullHours(created) * COEFFICIENT_TYPE1);
        } else if (type == Type.TYPE2) {
            return BigDecimal.valueOf(numberOfFullHours(created) * COEFFICIENT_TYPE2);
        } else {
            return BigDecimal.valueOf(numberOfFullHours(created) * COEFFICIENT_TYPE3);
        }
    }

    private long numberOfFullHours(LocalDateTime created) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(created, now);
        return duration.toHours();
    }

    public boolean isValidType(String type) {
        for (Type value : Type.values()) {
            if (type.equals(value.name())) {
                return true;
            }
        }
        return false;
    }

    private String errorString(PaymentDTO paymentDTO, Type type) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!paymentDTO.getType().equals(type.name())) {
            stringBuilder.append("Invalid type in payload: ").append(paymentDTO.getType()).append("\t");
        }
        if (paymentDTO.getCreditorIBAN() == null) {
            stringBuilder.append("Creditor's IBAN must be supplied!\t");
        }
        if (paymentDTO.getDebtorIBAN() == null) {
            stringBuilder.append("Debtor's IBAN must be supplied!\t");
        }
        if (type == Type.TYPE1 && paymentDTO.getDetails() == null) {
            stringBuilder.append("Please enter payment details!\t");
        }
        if (type == Type.TYPE3 && paymentDTO.getBICCode() == null) {
            stringBuilder.append("BIC Code cannot be empty!\t");
        }
        if (paymentDTO.getMoney() == null) {
            stringBuilder.append("Money cannot be null!\t");
        }
        if (type == Type.TYPE1 && (paymentDTO.getMoney() != null && paymentDTO.getMoney().getCurrency() != Currency.getInstance(EURO_CODE))) {
            stringBuilder.append("Currency must be in Euros!\t");
        }
        if (type == Type.TYPE2 && (paymentDTO.getMoney() != null && paymentDTO.getMoney().getCurrency() != Currency.getInstance(US_CODE))) {
            stringBuilder.append("Currency must be USD!\t");
        }
        if (type == Type.TYPE3 && (paymentDTO.getMoney() != null
                && (!allowedCurrencies.contains(paymentDTO.getMoney().getCurrency().getCurrencyCode())))) {
            stringBuilder.append("Currency must be in Euros or USD!\t");
        }
        return stringBuilder.toString();
    }
}
