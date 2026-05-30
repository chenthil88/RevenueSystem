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

    /**
     * Non-DB field used during batch processing to decide release eligibility.
     */
    Boolean isEligibleForRelease();

    /**
     * @return {@code true} only when {@link #isEligibleForRelease()} is {@link Boolean#TRUE}; otherwise {@code false}.
     */
    default boolean eligibleForRelease() {
        return Boolean.TRUE.equals(isEligibleForRelease());
    }

    /**
     * Non-DB field used during batch processing to decide recalculation eligibility.
     */
    Boolean isEligibleForRecalculation();

    /**
     * @return {@code true} only when {@link #isEligibleForRecalculation()} is {@link Boolean#TRUE}; otherwise {@code false}.
     */
    default boolean eligibleForRecalculation() {
        return Boolean.TRUE.equals(isEligibleForRecalculation());
    }

    /**
     * Whether allocation initial entry was created.
     */
    Boolean isAllocationInitialEntryCreated();

    /**
     * @return {@code true} only when {@link #isAllocationInitialEntryCreated()} is {@link Boolean#TRUE}; otherwise {@code false}.
     */
    default boolean allocationInitialEntryCreated() {
        return Boolean.TRUE.equals(isAllocationInitialEntryCreated());
    }

    /**
     * Helper to decide whether allocation initial entry creation is required for release processing.
     */
    default boolean shouldCreateAllocationInitialEntry() {
        return eligibleForRelease() && !allocationInitialEntryCreated();
    }

    /**
     * Whether this contract uses retrospective allocation treatment ({@code allocationTreatment}).
     */
    default boolean isRetrospective() {
        return allocationTreatment() != null
                && allocationTreatment().equalsIgnoreCase("RETROSPECTIVE");
    }
}
