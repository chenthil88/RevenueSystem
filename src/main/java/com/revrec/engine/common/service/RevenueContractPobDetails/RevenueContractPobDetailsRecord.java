package com.revrec.engine.domain.service.RevenueContractPobDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractPobDetails`.
 */
public record RevenueContractPobDetailsRecord(
        Long id,
        Long revenueContractId,
        Long pobTemplateId,
        Long pobRuleId,
        String name,
        String description,
        LocalDate pobExpiryDate,
        String processsedFlag,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) implements RevenueContractPobDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractPobDetails";
}
