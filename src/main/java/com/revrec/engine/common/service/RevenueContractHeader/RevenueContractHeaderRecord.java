package com.revrec.engine.domain.service.RevenueContractHeader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from Aurora PostgreSQL table `revenueContractHeader`.
 */
public class RevenueContractHeaderRecord implements RevenueContractHeader, Serializable {

    public static final String TABLE_NAME = "revenueContractHeader";

    private Long revenueContractId;
    private Long version;
    private BigDecimal totalSellPrice;
    private BigDecimal totalListPrice;
    private BigDecimal totalCarveAmount;
    private Long createdPeriodId;
    private LocalDate initialContractModificationDate;
    private LocalDate contractModificationDate;
    private Boolean isRevenueContractPosted;
    private String allocationTreatment;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private Boolean isAllocationInitialEntryCreated;
    private Boolean isActive;

    // non-DB flags
    private Boolean isEligibleForRelease;
    private Boolean isEligibleForRecalculation;

    public RevenueContractHeaderRecord() {}

    public Long getRevenueContractId() { return revenueContractId; }
    public void setRevenueContractId(Long revenueContractId) { this.revenueContractId = revenueContractId; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public BigDecimal getTotalSellPrice() { return totalSellPrice; }
    public void setTotalSellPrice(BigDecimal totalSellPrice) { this.totalSellPrice = totalSellPrice; }
    public BigDecimal getTotalListPrice() { return totalListPrice; }
    public void setTotalListPrice(BigDecimal totalListPrice) { this.totalListPrice = totalListPrice; }
    public BigDecimal getTotalCarveAmount() { return totalCarveAmount; }
    public void setTotalCarveAmount(BigDecimal totalCarveAmount) { this.totalCarveAmount = totalCarveAmount; }
    public Long getCreatedPeriodId() { return createdPeriodId; }
    public void setCreatedPeriodId(Long createdPeriodId) { this.createdPeriodId = createdPeriodId; }
    public LocalDate getInitialContractModificationDate() { return initialContractModificationDate; }
    public void setInitialContractModificationDate(LocalDate initialContractModificationDate) { this.initialContractModificationDate = initialContractModificationDate; }
    public LocalDate getContractModificationDate() { return contractModificationDate; }
    public void setContractModificationDate(LocalDate contractModificationDate) { this.contractModificationDate = contractModificationDate; }
    public Boolean getIsRevenueContractPosted() { return isRevenueContractPosted; }
    public void setIsRevenueContractPosted(Boolean revenueContractPosted) { isRevenueContractPosted = revenueContractPosted; }
    public String getAllocationTreatment() { return allocationTreatment; }
    public void setAllocationTreatment(String allocationTreatment) { this.allocationTreatment = allocationTreatment; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsAllocationInitialEntryCreated() { return isAllocationInitialEntryCreated; }
    public void setIsAllocationInitialEntryCreated(Boolean isAllocationInitialEntryCreated) {
        this.isAllocationInitialEntryCreated = Boolean.TRUE.equals(isAllocationInitialEntryCreated);
    }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    public Boolean getIsEligibleForRelease() { return isEligibleForRelease; }
    public void setIsEligibleForRelease(Boolean eligibleForRelease) {
        isEligibleForRelease = Boolean.TRUE.equals(eligibleForRelease);
    }
    public Boolean getIsEligibleForRecalculation() { return isEligibleForRecalculation; }
    public void setIsEligibleForRecalculation(Boolean eligibleForRecalculation) {
        isEligibleForRecalculation = Boolean.TRUE.equals(eligibleForRecalculation);
    }

    // interface methods
    @Override public Long revenueContractId() { return revenueContractId; }
    @Override public Long version() { return version; }
    @Override public BigDecimal totalSellPrice() { return totalSellPrice; }
    @Override public BigDecimal totalListPrice() { return totalListPrice; }
    @Override public BigDecimal totalCarveAmount() { return totalCarveAmount; }
    @Override public Long createdPeriodId() { return createdPeriodId; }
    @Override public LocalDate initialContractModificationDate() { return initialContractModificationDate; }
    @Override public LocalDate contractModificationDate() { return contractModificationDate; }
    @Override public Boolean isRevenueContractPosted() { return isRevenueContractPosted; }
    @Override public String allocationTreatment() { return allocationTreatment; }
    @Override public String createdBy() { return createdBy; }
    @Override public LocalDateTime createdAt() { return createdAt; }
    @Override public String updatedBy() { return updatedBy; }
    @Override public LocalDateTime updatedAt() { return updatedAt; }
    @Override public Boolean isAllocationInitialEntryCreated() { return Boolean.TRUE.equals(isAllocationInitialEntryCreated); }
    @Override public Boolean isActive() { return isActive; }
    @Override public Boolean isEligibleForRelease() { return Boolean.TRUE.equals(isEligibleForRelease); }
    @Override public Boolean isEligibleForRecalculation() { return Boolean.TRUE.equals(isEligibleForRecalculation); }
}
