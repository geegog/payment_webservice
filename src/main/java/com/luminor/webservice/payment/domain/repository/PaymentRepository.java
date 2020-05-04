package com.luminor.webservice.payment.domain.repository;

import com.luminor.webservice.payment.domain.model.Payment;
import com.luminor.webservice.payment.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT p.id from Payment p " +
            "where p.status <> :status " +
            "and p.money.amount >= :amount order by p.money.amount desc ")
    List<UUID> getPaymentsNotCancelled(@Param("status") Status status, @Param("amount") BigDecimal amount);

}
