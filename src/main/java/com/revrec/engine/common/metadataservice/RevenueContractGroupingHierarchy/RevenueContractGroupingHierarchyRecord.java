package com.revrec.engine.domain.metadataservice.RevenueContractGroupingHierarchy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `RevenueContractGroupingHierarchy`.
 */
public record RevenueContractGroupingHierarchyRecord(
        Long id,
        Long sequence,
        Long revenueContractGroupingTemplateId,
        String groupingFields,
        Long createdPeriodId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements RevenueContractGroupingHierarchy, Serializable {
    public static final String TABLE_NAME = "RevenueContractGroupingHierarchy";
}
