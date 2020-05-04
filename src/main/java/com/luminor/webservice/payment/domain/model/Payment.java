package com.luminor.webservice.payment.domain.model;

import com.luminor.webservice.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Payment extends BaseEntity {

    @Embedded
    private Money money;

    @NotNull
    private String debtorIBAN;

    @NotNull
    private String creditorIBAN;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @Embedded
    private CancellationMoney cancellationFee;

    private String details;

    @Column(name = "BIC_code")
    private String BICCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

}
