package com.revrec.engine.domain.metadataservice.PerformanceObligationRule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `PerformanceObligationRule`.
 */
public record PerformanceObligationRuleRecord(
        Long id,
        String name,
        String description,
        Long performanceObligationTemplateId,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements PerformanceObligationRule, Serializable {
    public static final String TABLE_NAME = "PerformanceObligationRule";
}
