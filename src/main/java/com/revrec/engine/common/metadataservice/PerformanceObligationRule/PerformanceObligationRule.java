package com.revrec.engine.domain.metadataservice.PerformanceObligationRule;

import java.time.LocalDateTime;

/**
 * Row shape for {@link PerformanceObligationRuleRecord}.
 */
public interface PerformanceObligationRule {

    Long id();

    String tenantId();

    String name();

    String description();

    Long performanceObligationTemplateId();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
