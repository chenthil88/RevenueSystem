package com.revrec.engine.domain.service.RevenueContractHeader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractHeaderRecord}.
 */
public interface RevenueContractHeader {

    Long revenueContractId();

    String tenantId();

    Long version();

    BigDecimal totalSellPrice();

    BigDecimal totalListPrice();

    BigDecimal totalCarveAmount();

    Long createdPeriodId();

    LocalDate initialContractModificationDate();

    LocalDate contractModificationDate();

    Boolean isRevenueContractPosted();

    String allocationTreatment();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();

    Boolean isActive();
}
