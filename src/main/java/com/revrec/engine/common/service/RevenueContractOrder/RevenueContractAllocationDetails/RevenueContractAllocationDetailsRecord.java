package com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from Aurora PostgreSQL table `revenueContractAllocationDetails`.
 */
public class RevenueContractAllocationDetailsRecord implements RevenueContractAllocationDetails, Serializable {

    public static final String TABLE_NAME = "revenueContractAllocationDetails";

    private Long id;
    private Long revenueContractId;
    private BigDecimal extendedSspPrice;
    private String allocationCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal globalexchangeRate;
    private LocalDate exchangeRateDate;
    private BigDecimal carveAmount;
    private BigDecimal cumulativeReleasedAmount;
    private BigDecimal cumulativeUnReleasedAmount;
    private BigDecimal transactionPrice;
    private BigDecimal allocatedPrice;
    private BigDecimal netQuantity;
    private BigDecimal term;
    private Long bookId;
    private Long organizationId;
    private BigDecimal transactionFunctionalPrice;
    private Long sspTemplateId;
    private Long sspId;
    private String sspType;
    private BigDecimal sspPrice;
    private BigDecimal sspPercentage;
    private BigDecimal aboveSspPrice;
    private BigDecimal belowSspPrice;
    private BigDecimal belowMidPercentage;
    private BigDecimal aboveMidPercentage;
    private Boolean isCancelOrder;
    private Boolean isReturnOrder;
    private Long createdPeriodId;
    private BigDecimal cumulativeCarveAmount;
    private BigDecimal cumulativeAllocatedPrice;
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
    public BigDecimal getExtendedSspPrice() { return extendedSspPrice; }
    public void setExtendedSspPrice(BigDecimal extendedSspPrice) { this.extendedSspPrice = extendedSspPrice; }
    public String getAllocationCurrency() { return allocationCurrency; }
    public void setAllocationCurrency(String allocationCurrency) { this.allocationCurrency = allocationCurrency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public BigDecimal getGlobalexchangeRate() { return globalexchangeRate; }
    public void setGlobalexchangeRate(BigDecimal globalexchangeRate) { this.globalexchangeRate = globalexchangeRate; }
    public LocalDate getExchangeRateDate() { return exchangeRateDate; }
    public void setExchangeRateDate(LocalDate exchangeRateDate) { this.exchangeRateDate = exchangeRateDate; }
    public BigDecimal getCarveAmount() { return carveAmount; }
    public void setCarveAmount(BigDecimal carveAmount) { this.carveAmount = carveAmount; }
    public BigDecimal getCumulativeReleasedAmount() { return cumulativeReleasedAmount; }
    public void setCumulativeReleasedAmount(BigDecimal cumulativeReleasedAmount) { this.cumulativeReleasedAmount = cumulativeReleasedAmount; }
    public BigDecimal getCumulativeUnReleasedAmount() { return cumulativeUnReleasedAmount; }
    public void setCumulativeUnReleasedAmount(BigDecimal cumulativeUnReleasedAmount) { this.cumulativeUnReleasedAmount = cumulativeUnReleasedAmount; }
    public BigDecimal getTransactionPrice() { return transactionPrice; }
    public void setTransactionPrice(BigDecimal transactionPrice) { this.transactionPrice = transactionPrice; }
    public BigDecimal getAllocatedPrice() { return allocatedPrice; }
    public void setAllocatedPrice(BigDecimal allocatedPrice) { this.allocatedPrice = allocatedPrice; }
    public BigDecimal getNetQuantity() { return netQuantity; }
    public void setNetQuantity(BigDecimal netQuantity) { this.netQuantity = netQuantity; }
    public BigDecimal getTerm() { return term; }
    public void setTerm(BigDecimal term) { this.term = term; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public BigDecimal getTransactionFunctionalPrice() { return transactionFunctionalPrice; }
    public void setTransactionFunctionalPrice(BigDecimal transactionFunctionalPrice) { this.transactionFunctionalPrice = transactionFunctionalPrice; }
    public Long getSspTemplateId() { return sspTemplateId; }
    public void setSspTemplateId(Long sspTemplateId) { this.sspTemplateId = sspTemplateId; }
    public Long getSspId() { return sspId; }
    public void setSspId(Long sspId) { this.sspId = sspId; }
    public String getSspType() { return sspType; }
    public void setSspType(String sspType) { this.sspType = sspType; }
    public BigDecimal getSspPrice() { return sspPrice; }
    public void setSspPrice(BigDecimal sspPrice) { this.sspPrice = sspPrice; }
    public BigDecimal getSspPercentage() { return sspPercentage; }
    public void setSspPercentage(BigDecimal sspPercentage) { this.sspPercentage = sspPercentage; }
    public BigDecimal getAboveSspPrice() { return aboveSspPrice; }
    public void setAboveSspPrice(BigDecimal aboveSspPrice) { this.aboveSspPrice = aboveSspPrice; }
    public BigDecimal getBelowSspPrice() { return belowSspPrice; }
    public void setBelowSspPrice(BigDecimal belowSspPrice) { this.belowSspPrice = belowSspPrice; }
    public BigDecimal getBelowMidPercentage() { return belowMidPercentage; }
    public void setBelowMidPercentage(BigDecimal belowMidPercentage) { this.belowMidPercentage = belowMidPercentage; }
    public BigDecimal getAboveMidPercentage() { return aboveMidPercentage; }
    public void setAboveMidPercentage(BigDecimal aboveMidPercentage) { this.aboveMidPercentage = aboveMidPercentage; }
    public Boolean getIsCancelOrder() { return isCancelOrder; }
    public void setIsCancelOrder(Boolean cancelOrder) { isCancelOrder = cancelOrder; }
    public Boolean getIsReturnOrder() { return isReturnOrder; }
    public void setIsReturnOrder(Boolean returnOrder) { isReturnOrder = returnOrder; }
    public Long getCreatedPeriodId() { return createdPeriodId; }
    public void setCreatedPeriodId(Long createdPeriodId) { this.createdPeriodId = createdPeriodId; }
    public BigDecimal getCumulativeCarveAmount() { return cumulativeCarveAmount; }
    public void setCumulativeCarveAmount(BigDecimal cumulativeCarveAmount) { this.cumulativeCarveAmount = cumulativeCarveAmount; }
    public BigDecimal getCumulativeAllocatedPrice() { return cumulativeAllocatedPrice; }
    public void setCumulativeAllocatedPrice(BigDecimal cumulativeAllocatedPrice) { this.cumulativeAllocatedPrice = cumulativeAllocatedPrice; }
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
    @Override public BigDecimal extendedSspPrice() { return extendedSspPrice; }
    @Override public String allocationCurrency() { return allocationCurrency; }
    @Override public BigDecimal exchangeRate() { return exchangeRate; }
    @Override public BigDecimal globalexchangeRate() { return globalexchangeRate; }
    @Override public LocalDate exchangeRateDate() { return exchangeRateDate; }
    @Override public BigDecimal carveAmount() { return carveAmount; }
    @Override public BigDecimal cumulativeReleasedAmount() { return cumulativeReleasedAmount; }
    @Override public BigDecimal cumulativeUnReleasedAmount() { return cumulativeUnReleasedAmount; }
    @Override public BigDecimal transactionPrice() { return transactionPrice; }
    @Override public BigDecimal allocatedPrice() { return allocatedPrice; }
    @Override public BigDecimal netQuantity() { return netQuantity; }
    @Override public BigDecimal term() { return term; }
    @Override public Long bookId() { return bookId; }
    @Override public Long organizationId() { return organizationId; }
    @Override public BigDecimal transactionFunctionalPrice() { return transactionFunctionalPrice; }
    @Override public Long sspTemplateId() { return sspTemplateId; }
    @Override public Long sspId() { return sspId; }
    @Override public String sspType() { return sspType; }
    @Override public BigDecimal sspPrice() { return sspPrice; }
    @Override public BigDecimal sspPercentage() { return sspPercentage; }
    @Override public BigDecimal aboveSspPrice() { return aboveSspPrice; }
    @Override public BigDecimal belowSspPrice() { return belowSspPrice; }
    @Override public BigDecimal belowMidPercentage() { return belowMidPercentage; }
    @Override public BigDecimal aboveMidPercentage() { return aboveMidPercentage; }
    @Override public Boolean isCancelOrder() { return isCancelOrder; }
    @Override public Boolean isReturnOrder() { return isReturnOrder; }
    @Override public Long createdPeriodId() { return createdPeriodId; }
    @Override public BigDecimal cumulativeCarveAmount() { return cumulativeCarveAmount; }
    @Override public BigDecimal cumulativeAllocatedPrice() { return cumulativeAllocatedPrice; }
    @Override public String comments() { return comments; }
    @Override public String createdBy() { return createdBy; }
    @Override public LocalDateTime createdAt() { return createdAt; }
    @Override public String updatedBy() { return updatedBy; }
    @Override public LocalDateTime updatedAt() { return updatedAt; }
}
