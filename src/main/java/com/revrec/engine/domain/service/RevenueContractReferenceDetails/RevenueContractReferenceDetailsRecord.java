package com.revrec.engine.domain.service.RevenueContractReferenceDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

public record RevenueContractReferenceDetailsRecord(
        Long id,
        Long revenueContractId,
        String salesOrderId,
        String invoiceId,
        Long createdPeriodId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
        implements RevenueContractReferenceDetails, Serializable {

    public static final String TABLE_NAME = "revenueContractReferenceDetails";
}
