package com.revrec.engine.domain.service.RevenueContractModificationDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table {@code RevenueContractModificationDetails}.
 */
public record RevenueContractModificationDetailsRecord(
        Long id,
        Long revenueContractId,
        Long revenueContractLineId,
        String contractModificationRuleName,
        LocalDate contractModificationDate,
        String contractModificationRuleTreatment,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) implements RevenueContractModificationDetails, Serializable {
    public static final String TABLE_NAME = "RevenueContractModificationDetails";
}
