package com.revrec.engine.domain.service.RevenueContractGroupDetails;

import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractGroupDetailsRecord}.
 */
public interface RevenueContractGroupDetails {

    Long id();

    String tenantId();

    Long revenueContractId();

    String groupingValue();

    String closedFlag();

    Long createdPeriodId();

    Boolean isActive();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
