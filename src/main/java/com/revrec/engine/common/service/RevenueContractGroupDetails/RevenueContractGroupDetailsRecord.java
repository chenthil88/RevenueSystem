package com.revrec.engine.domain.service.RevenueContractGroupDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractGroupDetails`.
 */
public record RevenueContractGroupDetailsRecord(
        Long id,
        Long revenueContractId,
        String groupingValue,
        String closedFlag,
        Long createdPeriodId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements RevenueContractGroupDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractGroupDetails";
}
