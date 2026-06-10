package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import com.revrec.engine.common.math.ChargebeeDecimal;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from Aurora PostgreSQL table `revenueContractAllocationDetails`.
 */
public class RevenueContractAllocationDetailsRecord implements RevenueContractAllocationDetails, Serializable {

    public static final String TABLE_NAME = "revenueContractAllocationDetails";

    private Long id;
    private Long revenueContractId;
    private ChargebeeDecimal extendedSspPrice;
    private String allocationCurrency;
    private ChargebeeDecimal exchangeRate;
    private ChargebeeDecimal globalexchangeRate;
    private LocalDate exchangeRateDate;
    private ChargebeeDecimal carveAmount;
    private ChargebeeDecimal unreleasedCarveAmount;
    private ChargebeeDecimal cumulativeReleasedAmount;
    private ChargebeeDecimal cumulativeUnReleasedAmount;
    private ChargebeeDecimal transactionPrice;
    private ChargebeeDecimal postedPercentage;
    private ChargebeeDecimal allocatedPrice;
    private ChargebeeDecimal netQuantity;
    private ChargebeeDecimal term;
    private Long bookId;
    private Long organizationId;
    private ChargebeeDecimal transactionFunctionalPrice;
    private Long sspTemplateId;
    private Long sspId;
    private String sspType;
    private ChargebeeDecimal sspPrice;
    private ChargebeeDecimal sspPercentage;
    private ChargebeeDecimal aboveSspPrice;
    private ChargebeeDecimal belowSspPrice;
    private ChargebeeDecimal belowMidPercentage;
    private ChargebeeDecimal aboveMidPercentage;
    private Boolean isCancelOrder;
    private Boolean isReturnOrder;
    private Long createdPeriodId;
    private ChargebeeDecimal cumulativeCarveAmount;
    private ChargebeeDecimal cumulativeAllocatedPrice;
    private String comments;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public RevenueContractAllocationDetailsRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRevenueContractId() { return revenueContractId; }
    public void setRevenueContractId(Long revenueContractId) { this.revenueContractId = revenueContractId; }
    public ChargebeeDecimal getExtendedSspPrice() { return extendedSspPrice; }
    public void setExtendedSspPrice(ChargebeeDecimal extendedSspPrice) { this.extendedSspPrice = extendedSspPrice; }
    public String getAllocationCurrency() { return allocationCurrency; }
    public void setAllocationCurrency(String allocationCurrency) { this.allocationCurrency = allocationCurrency; }
    public ChargebeeDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(ChargebeeDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public ChargebeeDecimal getGlobalexchangeRate() { return globalexchangeRate; }
    public void setGlobalexchangeRate(ChargebeeDecimal globalexchangeRate) { this.globalexchangeRate = globalexchangeRate; }
    public LocalDate getExchangeRateDate() { return exchangeRateDate; }
    public void setExchangeRateDate(LocalDate exchangeRateDate) { this.exchangeRateDate = exchangeRateDate; }
    public ChargebeeDecimal getCarveAmount() { return carveAmount; }
    public void setCarveAmount(ChargebeeDecimal carveAmount) { this.carveAmount = carveAmount; }
    public ChargebeeDecimal getUnreleasedCarveAmount() { return unreleasedCarveAmount; }
    public void setUnreleasedCarveAmount(ChargebeeDecimal unreleasedCarveAmount) {
        this.unreleasedCarveAmount = unreleasedCarveAmount;
    }
    public ChargebeeDecimal getCumulativeReleasedAmount() { return cumulativeReleasedAmount; }
    public void setCumulativeReleasedAmount(ChargebeeDecimal cumulativeReleasedAmount) { this.cumulativeReleasedAmount = cumulativeReleasedAmount; }
    public ChargebeeDecimal getCumulativeUnReleasedAmount() { return cumulativeUnReleasedAmount; }
    public void setCumulativeUnReleasedAmount(ChargebeeDecimal cumulativeUnReleasedAmount) { this.cumulativeUnReleasedAmount = cumulativeUnReleasedAmount; }
    public ChargebeeDecimal getTransactionPrice() { return transactionPrice; }
    public void setTransactionPrice(ChargebeeDecimal transactionPrice) { this.transactionPrice = transactionPrice; }
    public ChargebeeDecimal getPostedPercentage() { return postedPercentage; }
    public void setPostedPercentage(ChargebeeDecimal postedPercentage) { this.postedPercentage = postedPercentage; }
    public ChargebeeDecimal getAllocatedPrice() { return allocatedPrice; }
    public void setAllocatedPrice(ChargebeeDecimal allocatedPrice) { this.allocatedPrice = allocatedPrice; }
    public ChargebeeDecimal getNetQuantity() { return netQuantity; }
    public void setNetQuantity(ChargebeeDecimal netQuantity) { this.netQuantity = netQuantity; }
    public ChargebeeDecimal getTerm() { return term; }
    public void setTerm(ChargebeeDecimal term) { this.term = term; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public ChargebeeDecimal getTransactionFunctionalPrice() { return transactionFunctionalPrice; }
    public void setTransactionFunctionalPrice(ChargebeeDecimal transactionFunctionalPrice) { this.transactionFunctionalPrice = transactionFunctionalPrice; }
    public Long getSspTemplateId() { return sspTemplateId; }
    public void setSspTemplateId(Long sspTemplateId) { this.sspTemplateId = sspTemplateId; }
    public Long getSspId() { return sspId; }
    public void setSspId(Long sspId) { this.sspId = sspId; }
    public String getSspType() { return sspType; }
    public void setSspType(String sspType) { this.sspType = sspType; }
    public ChargebeeDecimal getSspPrice() { return sspPrice; }
    public void setSspPrice(ChargebeeDecimal sspPrice) { this.sspPrice = sspPrice; }
    public ChargebeeDecimal getSspPercentage() { return sspPercentage; }
    public void setSspPercentage(ChargebeeDecimal sspPercentage) { this.sspPercentage = sspPercentage; }
    public ChargebeeDecimal getAboveSspPrice() { return aboveSspPrice; }
    public void setAboveSspPrice(ChargebeeDecimal aboveSspPrice) { this.aboveSspPrice = aboveSspPrice; }
    public ChargebeeDecimal getBelowSspPrice() { return belowSspPrice; }
    public void setBelowSspPrice(ChargebeeDecimal belowSspPrice) { this.belowSspPrice = belowSspPrice; }
    public ChargebeeDecimal getBelowMidPercentage() { return belowMidPercentage; }
    public void setBelowMidPercentage(ChargebeeDecimal belowMidPercentage) { this.belowMidPercentage = belowMidPercentage; }
    public ChargebeeDecimal getAboveMidPercentage() { return aboveMidPercentage; }
    public void setAboveMidPercentage(ChargebeeDecimal aboveMidPercentage) { this.aboveMidPercentage = aboveMidPercentage; }
    public Boolean getIsCancelOrder() { return isCancelOrder; }
    public void setIsCancelOrder(Boolean cancelOrder) { isCancelOrder = cancelOrder; }
    public Boolean getIsReturnOrder() { return isReturnOrder; }
    public void setIsReturnOrder(Boolean returnOrder) { isReturnOrder = returnOrder; }
    public Long getCreatedPeriodId() { return createdPeriodId; }
    public void setCreatedPeriodId(Long createdPeriodId) { this.createdPeriodId = createdPeriodId; }
    public ChargebeeDecimal getCumulativeCarveAmount() { return cumulativeCarveAmount; }
    public void setCumulativeCarveAmount(ChargebeeDecimal cumulativeCarveAmount) { this.cumulativeCarveAmount = cumulativeCarveAmount; }
    public ChargebeeDecimal getCumulativeAllocatedPrice() { return cumulativeAllocatedPrice; }
    public void setCumulativeAllocatedPrice(ChargebeeDecimal cumulativeAllocatedPrice) { this.cumulativeAllocatedPrice = cumulativeAllocatedPrice; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // interface methods
    @Override public Long id() { return id; }
    @Override public Long revenueContractId() { return revenueContractId; }
    @Override public ChargebeeDecimal extendedSspPrice() { return extendedSspPrice; }
    @Override public String allocationCurrency() { return allocationCurrency; }
    @Override public ChargebeeDecimal exchangeRate() { return exchangeRate; }
    @Override public ChargebeeDecimal globalexchangeRate() { return globalexchangeRate; }
    @Override public LocalDate exchangeRateDate() { return exchangeRateDate; }
    @Override public ChargebeeDecimal carveAmount() { return carveAmount; }
    @Override public ChargebeeDecimal unreleasedCarveAmount() { return unreleasedCarveAmount; }
    @Override public ChargebeeDecimal cumulativeReleasedAmount() { return cumulativeReleasedAmount; }
    @Override public ChargebeeDecimal cumulativeUnReleasedAmount() { return cumulativeUnReleasedAmount; }
    @Override public ChargebeeDecimal transactionPrice() { return transactionPrice; }
    @Override public ChargebeeDecimal postedPercentage() { return postedPercentage; }
    @Override public ChargebeeDecimal allocatedPrice() { return allocatedPrice; }
    @Override public ChargebeeDecimal netQuantity() { return netQuantity; }
    @Override public ChargebeeDecimal term() { return term; }
    @Override public Long bookId() { return bookId; }
    @Override public Long organizationId() { return organizationId; }
    @Override public ChargebeeDecimal transactionFunctionalPrice() { return transactionFunctionalPrice; }
    @Override public Long sspTemplateId() { return sspTemplateId; }
    @Override public Long sspId() { return sspId; }
    @Override public String sspType() { return sspType; }
    @Override public ChargebeeDecimal sspPrice() { return sspPrice; }
    @Override public ChargebeeDecimal sspPercentage() { return sspPercentage; }
    @Override public ChargebeeDecimal aboveSspPrice() { return aboveSspPrice; }
    @Override public ChargebeeDecimal belowSspPrice() { return belowSspPrice; }
    @Override public ChargebeeDecimal belowMidPercentage() { return belowMidPercentage; }
    @Override public ChargebeeDecimal aboveMidPercentage() { return aboveMidPercentage; }
    @Override public Boolean isCancelOrder() { return isCancelOrder; }
    @Override public Boolean isReturnOrder() { return isReturnOrder; }
    @Override public Long createdPeriodId() { return createdPeriodId; }
    @Override public ChargebeeDecimal cumulativeCarveAmount() { return cumulativeCarveAmount; }
    @Override public ChargebeeDecimal cumulativeAllocatedPrice() { return cumulativeAllocatedPrice; }
    @Override public String comments() { return comments; }
    @Override public String createdBy() { return createdBy; }
    @Override public LocalDateTime createdAt() { return createdAt; }
    @Override public String updatedBy() { return updatedBy; }
    @Override public LocalDateTime updatedAt() { return updatedAt; }
}
