package com.revrec.engine.domain.metadataservice.PerformanceObligationRuleFilter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `PerformanceObligationRuleFilter`.
 */
public record PerformanceObligationRuleFilterRecord(
        Long id,
        Long performanceObligationRuleId,
        String filterFieldName,
        String filterOperator,
        String filterValue,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) implements PerformanceObligationRuleFilter, Serializable {
    public static final String TABLE_NAME = "PerformanceObligationRuleFilter";
}
