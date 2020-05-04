package com.luminor.webservice.payment.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class Money {

    @Column(precision = 8, scale = 2)
    @NotNull
    BigDecimal amount;

    @NotNull
    Currency currency;
}
