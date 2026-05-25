package com.revrec.engine.domain.service.RevenueContractReferenceDetails;

import java.time.LocalDateTime;

public interface RevenueContractReferenceDetails {

    Long id();

    Long revenueContractId();

    String salesOrderId();

    String invoiceId();

    Long createdPeriodId();

    Boolean isActive();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
