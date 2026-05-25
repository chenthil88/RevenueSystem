package com.revrec.engine.domain.metadataservice.PerformanceObligationTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `PerformanceObligationTemplate`.
 */
public record PerformanceObligationTemplateRecord(
        Long id,
        String name,
        String description,
        String revenueReleaseMethod,
        String revenueReleaseTiming,
        String revenueCalculationMethod,
        Boolean isDistinctPob,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements PerformanceObligationTemplate, Serializable {
    public static final String TABLE_NAME = "PerformanceObligationTemplate";
}
