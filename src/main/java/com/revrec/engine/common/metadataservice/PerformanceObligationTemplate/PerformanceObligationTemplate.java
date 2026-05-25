package com.revrec.engine.domain.metadataservice.PerformanceObligationTemplate;

import java.time.LocalDateTime;

/**
 * Row shape for {@link PerformanceObligationTemplateRecord}.
 */
public interface PerformanceObligationTemplate {

    Long id();

    String tenantId();

    String name();

    String description();

    String revenueReleaseMethod();

    String revenueReleaseTiming();

    String revenueCalculationMethod();

    Boolean isDistinctPob();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
