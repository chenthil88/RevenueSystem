package com.revrec.engine.domain.metadataservice.RevenueContractGroupingHierarchy;

import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractGroupingHierarchyRecord}.
 */
public interface RevenueContractGroupingHierarchy {

    Long id();

    String tenantId();

    Long sequence();

    Long revenueContractGroupingTemplateId();

    String groupingFields();

    Long createdPeriodId();

    Boolean isActive();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
