package com.revrec.engine.domain.service.RevenueContractHeader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractHeader`.
 */
public record RevenueContractHeaderRecord(
        Long revenueContractId,
        Long version,
        BigDecimal totalSellPrice,
        BigDecimal totalListPrice,
        BigDecimal totalCarveAmount,
        Long createdPeriodId,
        LocalDate initialContractModificationDate,
        LocalDate contractModificationDate,
        Boolean isRevenueContractPosted,
        String allocationTreatment,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt,
        Boolean isActive
) implements RevenueContractHeader, Serializable {
    public static final String TABLE_NAME = "revenueContractHeader";
}
