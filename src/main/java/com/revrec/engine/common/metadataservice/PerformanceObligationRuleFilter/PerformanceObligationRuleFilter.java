package com.revrec.engine.domain.metadataservice.PerformanceObligationRuleFilter;

import com.revrec.engine.common.filter.ActiveFieldFilter;
import java.time.LocalDateTime;

/**
 * Row shape for {@link PerformanceObligationRuleFilterRecord}.
 */
public interface PerformanceObligationRuleFilter extends ActiveFieldFilter {

    Long id();

    String tenantId();

    Long performanceObligationRuleId();

    String filterFieldName();

    String filterOperator();

    String filterValue();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();
}
