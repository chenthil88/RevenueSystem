package com.revrec.engine.domain.metadataservice.RevenueContractGroupingFilter;

import com.revrec.engine.common.filter.ActiveFieldFilter;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractGroupingFilterRecord}.
 */
public interface RevenueContractGroupingFilter extends ActiveFieldFilter {

    Long id();

    String tenantId();

    Long revenueContractGroupingTemplateId();

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
