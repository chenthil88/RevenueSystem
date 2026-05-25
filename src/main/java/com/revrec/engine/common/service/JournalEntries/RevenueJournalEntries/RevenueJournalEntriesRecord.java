package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import com.revrec.engine.common.persistence.PersistenceFlags;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from Aurora PostgreSQL table `RevenueJournalEntries`.
 *
 * <p>{@code isUpdate} and {@code isInsert} are not database columns; use them in memory to decide
 * update vs insert during batch processing.
 */
public class RevenueJournalEntriesRecord implements RevenueJournalEntries, Serializable {

    public static final String TABLE_NAME = "RevenueJournalEntries";

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

    public RevenueJournalEntriesRecord() {}

    public RevenueJournalEntriesRecord(
            Long id,
            Long revenueContractId,
            Long revenueContractLineId,
            Long revenueContractVersion,
            Long accountPeriodId,
            Long journalAccountPeriodId,
            String debitAccountName,
            String creditAccountName,
            BigDecimal amount,
            String currency,
            String functionalCurrency,
            BigDecimal exchangeRate,
            BigDecimal globalexchangeRate,
            LocalDate exchangeRateDate,
            String debitAccount1,
            String debitAccount2,
            String debitAccount3,
            String debitAccount4,
            String debitAccount5,
            String debitAccount6,
            String debitAccount7,
            String debitAccount8,
            String debitAccount9,
            String debitAccount10,
            String creditAccount1,
            String creditAccount2,
            String creditAccount3,
            String creditAccount4,
            String creditAccount5,
            String creditAccount6,
            String creditAccount7,
            String creditAccount8,
            String creditAccount9,
            String creditAccount10,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String createdBy,
            String updatedBy,
            Long createdPeriodId,
            Long updatedPeriodId,
            Boolean isPosted,
            Boolean isUnbilledAccount,
            Boolean isInitialEntry,
            Boolean isUnbilledReversal,
            String reversalFlag,
            String customField1,
            String customField2,
            String customField3,
            String customField4,
            String customField5,
            String customField6,
            String customField7,
            String customField8,
            String customField9,
            String customField10,
            Boolean isUpdate,
            Boolean isInsert) {
        this.id = id;
        this.revenueContractId = revenueContractId;
        this.revenueContractLineId = revenueContractLineId;
        this.revenueContractVersion = revenueContractVersion;
        this.accountPeriodId = accountPeriodId;
        this.journalAccountPeriodId = journalAccountPeriodId;
        this.debitAccountName = debitAccountName;
        this.creditAccountName = creditAccountName;
        this.amount = amount;
        this.currency = currency;
        this.functionalCurrency = functionalCurrency;
        this.exchangeRate = exchangeRate;
        this.globalexchangeRate = globalexchangeRate;
        this.exchangeRateDate = exchangeRateDate;
        this.debitAccount1 = debitAccount1;
        this.debitAccount2 = debitAccount2;
        this.debitAccount3 = debitAccount3;
        this.debitAccount4 = debitAccount4;
        this.debitAccount5 = debitAccount5;
        this.debitAccount6 = debitAccount6;
        this.debitAccount7 = debitAccount7;
        this.debitAccount8 = debitAccount8;
        this.debitAccount9 = debitAccount9;
        this.debitAccount10 = debitAccount10;
        this.creditAccount1 = creditAccount1;
        this.creditAccount2 = creditAccount2;
        this.creditAccount3 = creditAccount3;
        this.creditAccount4 = creditAccount4;
        this.creditAccount5 = creditAccount5;
        this.creditAccount6 = creditAccount6;
        this.creditAccount7 = creditAccount7;
        this.creditAccount8 = creditAccount8;
        this.creditAccount9 = creditAccount9;
        this.creditAccount10 = creditAccount10;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdPeriodId = createdPeriodId;
        this.updatedPeriodId = updatedPeriodId;
        this.isPosted = isPosted;
        this.isUnbilledAccount = isUnbilledAccount;
        this.isInitialEntry = isInitialEntry;
        this.isUnbilledReversal = isUnbilledReversal;
        this.reversalFlag = reversalFlag;
        this.customField1 = customField1;
        this.customField2 = customField2;
        this.customField3 = customField3;
        this.customField4 = customField4;
        this.customField5 = customField5;
        this.customField6 = customField6;
        this.customField7 = customField7;
        this.customField8 = customField8;
        this.customField9 = customField9;
        this.customField10 = customField10;
        this.isUpdate = PersistenceFlags.normalizeUpdate(isUpdate);
        this.isInsert = PersistenceFlags.normalizeInsert(isInsert);
    }

    // --- Bean getters/setters (for easier population) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRevenueContractId() { return revenueContractId; }
    public void setRevenueContractId(Long revenueContractId) { this.revenueContractId = revenueContractId; }
    public Long getRevenueContractLineId() { return revenueContractLineId; }
    public void setRevenueContractLineId(Long revenueContractLineId) { this.revenueContractLineId = revenueContractLineId; }
    public Long getRevenueContractVersion() { return revenueContractVersion; }
    public void setRevenueContractVersion(Long revenueContractVersion) { this.revenueContractVersion = revenueContractVersion; }
    public Long getAccountPeriodId() { return accountPeriodId; }
    public void setAccountPeriodId(Long accountPeriodId) { this.accountPeriodId = accountPeriodId; }
    public Long getJournalAccountPeriodId() { return journalAccountPeriodId; }
    public void setJournalAccountPeriodId(Long journalAccountPeriodId) { this.journalAccountPeriodId = journalAccountPeriodId; }
    public String getDebitAccountName() { return debitAccountName; }
    public void setDebitAccountName(String debitAccountName) { this.debitAccountName = debitAccountName; }
    public String getCreditAccountName() { return creditAccountName; }
    public void setCreditAccountName(String creditAccountName) { this.creditAccountName = creditAccountName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getFunctionalCurrency() { return functionalCurrency; }
    public void setFunctionalCurrency(String functionalCurrency) { this.functionalCurrency = functionalCurrency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public BigDecimal getGlobalexchangeRate() { return globalexchangeRate; }
    public void setGlobalexchangeRate(BigDecimal globalexchangeRate) { this.globalexchangeRate = globalexchangeRate; }
    public LocalDate getExchangeRateDate() { return exchangeRateDate; }
    public void setExchangeRateDate(LocalDate exchangeRateDate) { this.exchangeRateDate = exchangeRateDate; }
    public String getDebitAccount1() { return debitAccount1; }
    public void setDebitAccount1(String debitAccount1) { this.debitAccount1 = debitAccount1; }
    public String getDebitAccount2() { return debitAccount2; }
    public void setDebitAccount2(String debitAccount2) { this.debitAccount2 = debitAccount2; }
    public String getDebitAccount3() { return debitAccount3; }
    public void setDebitAccount3(String debitAccount3) { this.debitAccount3 = debitAccount3; }
    public String getDebitAccount4() { return debitAccount4; }
    public void setDebitAccount4(String debitAccount4) { this.debitAccount4 = debitAccount4; }
    public String getDebitAccount5() { return debitAccount5; }
    public void setDebitAccount5(String debitAccount5) { this.debitAccount5 = debitAccount5; }
    public String getDebitAccount6() { return debitAccount6; }
    public void setDebitAccount6(String debitAccount6) { this.debitAccount6 = debitAccount6; }
    public String getDebitAccount7() { return debitAccount7; }
    public void setDebitAccount7(String debitAccount7) { this.debitAccount7 = debitAccount7; }
    public String getDebitAccount8() { return debitAccount8; }
    public void setDebitAccount8(String debitAccount8) { this.debitAccount8 = debitAccount8; }
    public String getDebitAccount9() { return debitAccount9; }
    public void setDebitAccount9(String debitAccount9) { this.debitAccount9 = debitAccount9; }
    public String getDebitAccount10() { return debitAccount10; }
    public void setDebitAccount10(String debitAccount10) { this.debitAccount10 = debitAccount10; }
    public String getCreditAccount1() { return creditAccount1; }
    public void setCreditAccount1(String creditAccount1) { this.creditAccount1 = creditAccount1; }
    public String getCreditAccount2() { return creditAccount2; }
    public void setCreditAccount2(String creditAccount2) { this.creditAccount2 = creditAccount2; }
    public String getCreditAccount3() { return creditAccount3; }
    public void setCreditAccount3(String creditAccount3) { this.creditAccount3 = creditAccount3; }
    public String getCreditAccount4() { return creditAccount4; }
    public void setCreditAccount4(String creditAccount4) { this.creditAccount4 = creditAccount4; }
    public String getCreditAccount5() { return creditAccount5; }
    public void setCreditAccount5(String creditAccount5) { this.creditAccount5 = creditAccount5; }
    public String getCreditAccount6() { return creditAccount6; }
    public void setCreditAccount6(String creditAccount6) { this.creditAccount6 = creditAccount6; }
    public String getCreditAccount7() { return creditAccount7; }
    public void setCreditAccount7(String creditAccount7) { this.creditAccount7 = creditAccount7; }
    public String getCreditAccount8() { return creditAccount8; }
    public void setCreditAccount8(String creditAccount8) { this.creditAccount8 = creditAccount8; }
    public String getCreditAccount9() { return creditAccount9; }
    public void setCreditAccount9(String creditAccount9) { this.creditAccount9 = creditAccount9; }
    public String getCreditAccount10() { return creditAccount10; }
    public void setCreditAccount10(String creditAccount10) { this.creditAccount10 = creditAccount10; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Long getCreatedPeriodId() { return createdPeriodId; }
    public void setCreatedPeriodId(Long createdPeriodId) { this.createdPeriodId = createdPeriodId; }
    public Long getUpdatedPeriodId() { return updatedPeriodId; }
    public void setUpdatedPeriodId(Long updatedPeriodId) { this.updatedPeriodId = updatedPeriodId; }
    public Boolean getIsPosted() { return isPosted; }
    public void setIsPosted(Boolean posted) { isPosted = posted; }
    public Boolean getIsUnbilledAccount() { return isUnbilledAccount; }
    public void setIsUnbilledAccount(Boolean unbilledAccount) { isUnbilledAccount = unbilledAccount; }
    public Boolean getIsInitialEntry() { return isInitialEntry; }
    public void setIsInitialEntry(Boolean initialEntry) { isInitialEntry = initialEntry; }
    public Boolean getIsUnbilledReversal() { return isUnbilledReversal; }
    public void setIsUnbilledReversal(Boolean unbilledReversal) { isUnbilledReversal = unbilledReversal; }
    public String getReversalFlag() { return reversalFlag; }
    public void setReversalFlag(String reversalFlag) { this.reversalFlag = reversalFlag; }
    public String getCustomField1() { return customField1; }
    public void setCustomField1(String customField1) { this.customField1 = customField1; }
    public String getCustomField2() { return customField2; }
    public void setCustomField2(String customField2) { this.customField2 = customField2; }
    public String getCustomField3() { return customField3; }
    public void setCustomField3(String customField3) { this.customField3 = customField3; }
    public String getCustomField4() { return customField4; }
    public void setCustomField4(String customField4) { this.customField4 = customField4; }
    public String getCustomField5() { return customField5; }
    public void setCustomField5(String customField5) { this.customField5 = customField5; }
    public String getCustomField6() { return customField6; }
    public void setCustomField6(String customField6) { this.customField6 = customField6; }
    public String getCustomField7() { return customField7; }
    public void setCustomField7(String customField7) { this.customField7 = customField7; }
    public String getCustomField8() { return customField8; }
    public void setCustomField8(String customField8) { this.customField8 = customField8; }
    public String getCustomField9() { return customField9; }
    public void setCustomField9(String customField9) { this.customField9 = customField9; }
    public String getCustomField10() { return customField10; }
    public void setCustomField10(String customField10) { this.customField10 = customField10; }
    public void setIsUpdate(Boolean isUpdate) { this.isUpdate = PersistenceFlags.normalizeUpdate(isUpdate); }
    public void setIsInsert(Boolean isInsert) { this.isInsert = PersistenceFlags.normalizeInsert(isInsert); }

    // --- Interface methods (keep existing call sites working) ---
    @Override public Long id() { return id; }
    @Override public Long revenueContractId() { return revenueContractId; }
    @Override public Long revenueContractLineId() { return revenueContractLineId; }
    @Override public Long revenueContractVersion() { return revenueContractVersion; }
    @Override public Long accountPeriodId() { return accountPeriodId; }
    @Override public Long journalAccountPeriodId() { return journalAccountPeriodId; }
    @Override public String debitAccountName() { return debitAccountName; }
    @Override public String creditAccountName() { return creditAccountName; }
    @Override public BigDecimal amount() { return amount; }
    @Override public String currency() { return currency; }
    @Override public String functionalCurrency() { return functionalCurrency; }
    @Override public BigDecimal exchangeRate() { return exchangeRate; }
    @Override public BigDecimal globalexchangeRate() { return globalexchangeRate; }
    @Override public LocalDate exchangeRateDate() { return exchangeRateDate; }
    @Override public String debitAccount1() { return debitAccount1; }
    @Override public String debitAccount2() { return debitAccount2; }
    @Override public String debitAccount3() { return debitAccount3; }
    @Override public String debitAccount4() { return debitAccount4; }
    @Override public String debitAccount5() { return debitAccount5; }
    @Override public String debitAccount6() { return debitAccount6; }
    @Override public String debitAccount7() { return debitAccount7; }
    @Override public String debitAccount8() { return debitAccount8; }
    @Override public String debitAccount9() { return debitAccount9; }
    @Override public String debitAccount10() { return debitAccount10; }
    @Override public String creditAccount1() { return creditAccount1; }
    @Override public String creditAccount2() { return creditAccount2; }
    @Override public String creditAccount3() { return creditAccount3; }
    @Override public String creditAccount4() { return creditAccount4; }
    @Override public String creditAccount5() { return creditAccount5; }
    @Override public String creditAccount6() { return creditAccount6; }
    @Override public String creditAccount7() { return creditAccount7; }
    @Override public String creditAccount8() { return creditAccount8; }
    @Override public String creditAccount9() { return creditAccount9; }
    @Override public String creditAccount10() { return creditAccount10; }
    @Override public LocalDateTime createdAt() { return createdAt; }
    @Override public LocalDateTime updatedAt() { return updatedAt; }
    @Override public String createdBy() { return createdBy; }
    @Override public String updatedBy() { return updatedBy; }
    @Override public Long createdPeriodId() { return createdPeriodId; }
    @Override public Long updatedPeriodId() { return updatedPeriodId; }
    @Override public Boolean isPosted() { return isPosted; }
    @Override public Boolean isUnbilledAccount() { return isUnbilledAccount; }
    @Override public Boolean isInitialEntry() { return isInitialEntry; }
    @Override public Boolean isUnbilledReversal() { return isUnbilledReversal; }
    @Override public String reversalFlag() { return reversalFlag; }
    @Override public String customField1() { return customField1; }
    @Override public String customField2() { return customField2; }
    @Override public String customField3() { return customField3; }
    @Override public String customField4() { return customField4; }
    @Override public String customField5() { return customField5; }
    @Override public String customField6() { return customField6; }
    @Override public String customField7() { return customField7; }
    @Override public String customField8() { return customField8; }
    @Override public String customField9() { return customField9; }
    @Override public String customField10() { return customField10; }
    @Override public Boolean isUpdate() { return isUpdate; }
    @Override public Boolean isInsert() { return isInsert; }
}
