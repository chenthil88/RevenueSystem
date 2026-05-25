package com.revrec.engine.domain.metadataservice.StandaloneSellPriceHierarchy;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `StandaloneSellPriceHierarchy`.
 */
public record StandaloneSellPriceHierarchyRecord(
        Long seq,
        Long standaloneSellPriceTemplateId,
        String standaloneSellPriceTemplateName,
        Long revenueContractGroupingTemplateId,
        Boolean isActive,
        Long createdPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements StandaloneSellPriceHierarchy, Serializable {
    public static final String TABLE_NAME = "StandaloneSellPriceHierarchy";
}
