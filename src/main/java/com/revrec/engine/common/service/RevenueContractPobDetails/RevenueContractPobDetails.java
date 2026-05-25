package com.revrec.engine.domain.service.RevenueContractPobDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractPobDetailsRecord}.
 */
public interface RevenueContractPobDetails {

    Long id();

    String tenantId();

    Long revenueContractId();

    Long pobTemplateId();

    Long pobRuleId();

    String name();

    String description();

    LocalDate pobExpiryDate();

    String processsedFlag();

    Boolean isActive();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();
}
