package com.luminor.webservice.common.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public
class CancellationMoneyDTO {

    @Column(precision = 8, scale = 2)
    BigDecimal fee;

    Currency cancellationCurrency;
}
