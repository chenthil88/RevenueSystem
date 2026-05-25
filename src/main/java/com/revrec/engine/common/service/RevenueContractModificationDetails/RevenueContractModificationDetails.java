package com.revrec.engine.domain.service.RevenueContractModificationDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractModificationDetailsRecord}.
 */
public interface RevenueContractModificationDetails {

    Long id();

    String tenantId();

    Long revenueContractId();

    /** Maps SQL column {@code revenueContractLineId}. */
    Long revenueContractLineId();

    String contractModificationRuleName();

    LocalDate contractModificationDate();

    String contractModificationRuleTreatment();

    Long createdPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();
}
