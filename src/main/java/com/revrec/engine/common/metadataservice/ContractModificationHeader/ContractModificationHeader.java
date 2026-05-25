package com.revrec.engine.domain.metadataservice.ContractModificationHeader;

import java.time.LocalDateTime;

/**
 * Row shape for {@link ContractModificationHeaderRecord}.
 */
public interface ContractModificationHeader {

    Integer id();

    String tenantId();

    String initialContractModificationDuration();

    String revisionContractModificationDuration();

    String initialContractModificationSspDateMethod();

    String revisionContractModificationSspDateMethod();

    Integer createdPeriodId();

    Integer updatedPeriodId();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();
}
