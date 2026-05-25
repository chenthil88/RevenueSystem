package com.revrec.engine.domain.metadataservice.RevenueContractGroupingFilter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `RevenueContractGroupingFilter`.
 */
public record RevenueContractGroupingFilterRecord(
        Long id,
        Long revenueContractGroupingTemplateId,
        String filterFieldName,
        String filterOperator,
        String filterValue,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) implements RevenueContractGroupingFilter, Serializable {
    public static final String TABLE_NAME = "RevenueContractGroupingFilter";
}
