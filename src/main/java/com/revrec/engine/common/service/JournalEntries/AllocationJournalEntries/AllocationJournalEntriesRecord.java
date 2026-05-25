package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.persistence.PersistenceFlags;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from Aurora PostgreSQL table `AllocationJournalEntries`.
 *
 * <p>{@code isUpdate} and {@code isInsert} are not database columns; use them in memory to decide
 * update vs insert during batch processing.
 */
public class AllocationJournalEntriesRecord implements AllocationJournalEntries, Serializable {

    public static final String TABLE_NAME = "AllocationJournalEntries";

    private Long id;
    private Long revenueContractId;
    private Long revenueContractLineId;
    private Long revenueContractVersion;
    private Long accountPeriodId;
    private Long journalAccountPeriodId;
    private String debitAccountName;
    private String creditAccountName;
    private BigDecimal amount;
    private String currency;
    private String functionalCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal globalexchangeRate;
    private LocalDate exchangeRateDate;
    private String debitAccount1;
    private String debitAccount2;
    private String debitAccount3;
    private String debitAccount4;
    private String debitAccount5;
    private String debitAccount6;
    private String debitAccount7;
    private String debitAccount8;
    private String debitAccount9;
    private String debitAccount10;
    private String creditAccount1;
    private String creditAccount2;
    private String creditAccount3;
    private String creditAccount4;
    private String creditAccount5;
    private String creditAccount6;
    private String creditAccount7;
    private String creditAccount8;
    private String creditAccount9;
    private String creditAccount10;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Long createdPeriodId;
    private Long updatedPeriodId;
    private Boolean isPosted;
    private Boolean isUnbilledAccount;
    private Boolean isInitialEntry;
    private Boolean isUnbilledReversal;
    private String reversalFlag;
    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private String customField5;
    private String customField6;
    private String customField7;
    private String customField8;
    private String customField9;
    private String customField10;
    private Boolean isUpdate;
    private Boolean isInsert;

    public AllocationJournalEntriesRecord() {}

    // --- Bean getters/setters ---
    @Override public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Override public Long getRevenueContractId() { return revenueContractId; }
    public void setRevenueContractId(Long revenueContractId) { this.revenueContractId = revenueContractId; }
    @Override public Long getRevenueContractLineId() { return revenueContractLineId; }
    public void setRevenueContractLineId(Long revenueContractLineId) { this.revenueContractLineId = revenueContractLineId; }
    @Override public Long getRevenueContractVersion() { return revenueContractVersion; }
    public void setRevenueContractVersion(Long revenueContractVersion) { this.revenueContractVersion = revenueContractVersion; }
    @Override public Long getAccountPeriodId() { return accountPeriodId; }
    public void setAccountPeriodId(Long accountPeriodId) { this.accountPeriodId = accountPeriodId; }
    @Override public Long getJournalAccountPeriodId() { return journalAccountPeriodId; }
    public void setJournalAccountPeriodId(Long journalAccountPeriodId) { this.journalAccountPeriodId = journalAccountPeriodId; }
    @Override public String getDebitAccountName() { return debitAccountName; }
    public void setDebitAccountName(String debitAccountName) { this.debitAccountName = debitAccountName; }
    @Override public String getCreditAccountName() { return creditAccountName; }
    public void setCreditAccountName(String creditAccountName) { this.creditAccountName = creditAccountName; }
    @Override public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    @Override public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    @Override public String getFunctionalCurrency() { return functionalCurrency; }
    public void setFunctionalCurrency(String functionalCurrency) { this.functionalCurrency = functionalCurrency; }
    @Override public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    @Override public BigDecimal getGlobalexchangeRate() { return globalexchangeRate; }
    public void setGlobalexchangeRate(BigDecimal globalexchangerate) { this.globalexchangeRate = globalexchangerate; }
    @Override public LocalDate getExchangeRateDate() { return exchangeRateDate; }
    public void setExchangeRateDate(LocalDate exchangeRateDate) { this.exchangeRateDate = exchangeRateDate; }
    @Override public String getDebitAccount1() { return debitAccount1; }
    public void setDebitAccount1(String debitAccount1) { this.debitAccount1 = debitAccount1; }
    @Override public String getDebitAccount2() { return debitAccount2; }
    public void setDebitAccount2(String debitAccount2) { this.debitAccount2 = debitAccount2; }
    @Override public String getDebitAccount3() { return debitAccount3; }
    public void setDebitAccount3(String debitAccount3) { this.debitAccount3 = debitAccount3; }
    @Override public String getDebitAccount4() { return debitAccount4; }
    public void setDebitAccount4(String debitAccount4) { this.debitAccount4 = debitAccount4; }
    @Override public String getDebitAccount5() { return debitAccount5; }
    public void setDebitAccount5(String debitAccount5) { this.debitAccount5 = debitAccount5; }
    @Override public String getDebitAccount6() { return debitAccount6; }
    public void setDebitAccount6(String debitAccount6) { this.debitAccount6 = debitAccount6; }
    @Override public String getDebitAccount7() { return debitAccount7; }
    public void setDebitAccount7(String debitAccount7) { this.debitAccount7 = debitAccount7; }
    @Override public String getDebitAccount8() { return debitAccount8; }
    public void setDebitAccount8(String debitAccount8) { this.debitAccount8 = debitAccount8; }
    @Override public String getDebitAccount9() { return debitAccount9; }
    public void setDebitAccount9(String debitAccount9) { this.debitAccount9 = debitAccount9; }
    @Override public String getDebitAccount10() { return debitAccount10; }
    public void setDebitAccount10(String debitAccount10) { this.debitAccount10 = debitAccount10; }
    @Override public String getCreditAccount1() { return creditAccount1; }
    public void setCreditAccount1(String creditAccount1) { this.creditAccount1 = creditAccount1; }
    @Override public String getCreditAccount2() { return creditAccount2; }
    public void setCreditAccount2(String creditAccount2) { this.creditAccount2 = creditAccount2; }
    @Override public String getCreditAccount3() { return creditAccount3; }
    public void setCreditAccount3(String creditAccount3) { this.creditAccount3 = creditAccount3; }
    @Override public String getCreditAccount4() { return creditAccount4; }
    public void setCreditAccount4(String creditAccount4) { this.creditAccount4 = creditAccount4; }
    @Override public String getCreditAccount5() { return creditAccount5; }
    public void setCreditAccount5(String creditAccount5) { this.creditAccount5 = creditAccount5; }
    @Override public String getCreditAccount6() { return creditAccount6; }
    public void setCreditAccount6(String creditAccount6) { this.creditAccount6 = creditAccount6; }
    @Override public String getCreditAccount7() { return creditAccount7; }
    public void setCreditAccount7(String creditAccount7) { this.creditAccount7 = creditAccount7; }
    @Override public String getCreditAccount8() { return creditAccount8; }
    public void setCreditAccount8(String creditAccount8) { this.creditAccount8 = creditAccount8; }
    @Override public String getCreditAccount9() { return creditAccount9; }
    public void setCreditAccount9(String creditAccount9) { this.creditAccount9 = creditAccount9; }
    @Override public String getCreditAccount10() { return creditAccount10; }
    public void setCreditAccount10(String creditAccount10) { this.creditAccount10 = creditAccount10; }
    @Override public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    @Override public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    @Override public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    @Override public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    @Override public Long getCreatedPeriodId() { return createdPeriodId; }
    public void setCreatedPeriodId(Long createdPeriodId) { this.createdPeriodId = createdPeriodId; }
    @Override public Long getUpdatedPeriodId() { return updatedPeriodId; }
    public void setUpdatedPeriodId(Long updatedPeriodId) { this.updatedPeriodId = updatedPeriodId; }
    @Override public Boolean isPosted() { return isPosted; }
    public void setPosted(Boolean posted) { isPosted = posted; }
    @Override public Boolean isUnbilledAccount() { return isUnbilledAccount; }
    public void setUnbilledAccount(Boolean unbilledAccount) { isUnbilledAccount = unbilledAccount; }
    @Override public Boolean isInitialEntry() { return isInitialEntry; }
    public void setInitialEntry(Boolean initialEntry) { isInitialEntry = initialEntry; }
    @Override public Boolean isUnbilledReversal() { return isUnbilledReversal; }
    public void setUnbilledReversal(Boolean unbilledReversal) { isUnbilledReversal = unbilledReversal; }
    @Override public String getReversalFlag() { return reversalFlag; }
    public void setReversalFlag(String reversalFlag) { this.reversalFlag = reversalFlag; }
    @Override public String getCustomField1() { return customField1; }
    public void setCustomField1(String customField1) { this.customField1 = customField1; }
    @Override public String getCustomField2() { return customField2; }
    public void setCustomField2(String customField2) { this.customField2 = customField2; }
    @Override public String getCustomField3() { return customField3; }
    public void setCustomField3(String customField3) { this.customField3 = customField3; }
    @Override public String getCustomField4() { return customField4; }
    public void setCustomField4(String customField4) { this.customField4 = customField4; }
    @Override public String getCustomField5() { return customField5; }
    public void setCustomField5(String customField5) { this.customField5 = customField5; }
    @Override public String getCustomField6() { return customField6; }
    public void setCustomField6(String customField6) { this.customField6 = customField6; }
    @Override public String getCustomField7() { return customField7; }
    public void setCustomField7(String customField7) { this.customField7 = customField7; }
    @Override public String getCustomField8() { return customField8; }
    public void setCustomField8(String customField8) { this.customField8 = customField8; }
    @Override public String getCustomField9() { return customField9; }
    public void setCustomField9(String customField9) { this.customField9 = customField9; }
    @Override public String getCustomField10() { return customField10; }
    public void setCustomField10(String customField10) { this.customField10 = customField10; }

    @Override public Boolean isUpdate() { return isUpdate; }
    @Override public Boolean isInsert() { return isInsert; }
    public void setIsUpdate(Boolean isUpdate) { this.isUpdate = PersistenceFlags.normalizeUpdate(isUpdate); }
    public void setIsInsert(Boolean isInsert) { this.isInsert = PersistenceFlags.normalizeInsert(isInsert); }
}
