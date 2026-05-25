package com.revrec.engine.domain.metadataservice.RevenueContractGroupingTemplate;

import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractGroupingTemplateRecord}.
 */
public interface RevenueContractGroupingTemplate {

    Long id();

    String tenantId();

    String name();

    String description();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
