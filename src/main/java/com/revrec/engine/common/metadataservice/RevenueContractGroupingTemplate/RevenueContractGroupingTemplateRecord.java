package com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `RevenueContractGroupingTemplate`.
 */
public record RevenueContractGroupingTemplateRecord(
        Long id,
        String name,
        String description,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements RevenueContractGroupingTemplate, Serializable {
    public static final String TABLE_NAME = "RevenueContractGroupingTemplate";
}
