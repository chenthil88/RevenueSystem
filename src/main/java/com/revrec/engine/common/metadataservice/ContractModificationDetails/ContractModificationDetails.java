package com.revrec.engine.domain.metadataservice.ContractModificationDetails;

import java.time.LocalDateTime;

/**
 * Row shape for {@link ContractModificationDetailsRecord}.
 */
public interface ContractModificationDetails {

    Integer id();

    String tenantId();

    String ruleName();

    String ruleTreatmentForDistinctPob();

    String ruleTreatmentForNonDistinctPob();

    String ruleCategory();

    Boolean isActive();

    Integer createdPeriodId();

    Integer updatedPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();
}
