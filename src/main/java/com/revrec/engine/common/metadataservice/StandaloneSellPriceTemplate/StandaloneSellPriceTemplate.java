package com.revrec.engine.domain.metadataservice.StandaloneSellPriceTemplate;

import java.time.LocalDateTime;

/**
 * Row shape for {@link StandaloneSellPriceTemplateRecord}.
 */
public interface StandaloneSellPriceTemplate {

    Long id();

    String tenantId();

    String name();

    String description();

    String stratificationFields();

    String applyFieldName();

    Long createdPeriodId();

    Boolean isActive();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
