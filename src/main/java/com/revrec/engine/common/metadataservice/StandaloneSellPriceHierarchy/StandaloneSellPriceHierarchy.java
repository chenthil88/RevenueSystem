package com.revrec.engine.domain.metadataservice.StandaloneSellPriceHierarchy;

import java.time.LocalDateTime;

/**
 * Row shape for {@link StandaloneSellPriceHierarchyRecord}.
 */
public interface StandaloneSellPriceHierarchy {

    Long seq();

    String tenantId();

    Long standaloneSellPriceTemplateId();

    String standaloneSellPriceTemplateName();

    Long revenueContractGroupingTemplateId();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
