package com.revrec.engine.domain.metadataservice.StandaloneSellPriceBatchHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link StandaloneSellPriceBatchHeaderRecord}.
 */
public interface StandaloneSellPriceBatchHeader {

    Long id();

    String tenantId();

    Long standaloneSellPriceTemplateId();

    String name();

    String description();

    String sspType();

    LocalDate effectiveFromDate();

    LocalDate effectiveToDate();

    String status();

    Long createdPeriodId();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();

    Boolean isActive();
}
