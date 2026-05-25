package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `AllocationJournalEntries`.
 */
public record AllocationJournalEntriesRecord(
        Long id,
        Long revenueContractId,
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
        String customField10
) implements AllocationJournalEntries, Serializable {

    public static final String TABLE_NAME = "AllocationJournalEntries";

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getRevenueContractId() {
        return revenueContractId;
    }

    @Override
    public Long getAccountPeriodId() {
        return accountPeriodId;
    }

    @Override
    public Long getJournalAccountPeriodId() {
        return journalAccountPeriodId;
    }

    @Override
    public String getDebitAccountName() {
        return debitAccountName;
    }

    @Override
    public String getCreditAccountName() {
        return creditAccountName;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public String getFunctionalCurrency() {
        return functionalCurrency;
    }

    @Override
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public BigDecimal getGlobalexchangeRate() {
        return globalexchangeRate;
    }

    @Override
    public LocalDate getExchangeRateDate() {
        return exchangeRateDate;
    }

    @Override
    public String getDebitAccount1() {
        return debitAccount1;
    }

    @Override
    public String getDebitAccount2() {
        return debitAccount2;
    }

    @Override
    public String getDebitAccount3() {
        return debitAccount3;
    }

    @Override
    public String getDebitAccount4() {
        return debitAccount4;
    }

    @Override
    public String getDebitAccount5() {
        return debitAccount5;
    }

    @Override
    public String getDebitAccount6() {
        return debitAccount6;
    }

    @Override
    public String getDebitAccount7() {
        return debitAccount7;
    }

    @Override
    public String getDebitAccount8() {
        return debitAccount8;
    }

    @Override
    public String getDebitAccount9() {
        return debitAccount9;
    }

    @Override
    public String getDebitAccount10() {
        return debitAccount10;
    }

    @Override
    public String getCreditAccount1() {
        return creditAccount1;
    }

    @Override
    public String getCreditAccount2() {
        return creditAccount2;
    }

    @Override
    public String getCreditAccount3() {
        return creditAccount3;
    }

    @Override
    public String getCreditAccount4() {
        return creditAccount4;
    }

    @Override
    public String getCreditAccount5() {
        return creditAccount5;
    }

    @Override
    public String getCreditAccount6() {
        return creditAccount6;
    }

    @Override
    public String getCreditAccount7() {
        return creditAccount7;
    }

    @Override
    public String getCreditAccount8() {
        return creditAccount8;
    }

    @Override
    public String getCreditAccount9() {
        return creditAccount9;
    }

    @Override
    public String getCreditAccount10() {
        return creditAccount10;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public Long getCreatedPeriodId() {
        return createdPeriodId;
    }

    @Override
    public Long getUpdatedPeriodId() {
        return updatedPeriodId;
    }

    @Override
    public String getReversalFlag() {
        return reversalFlag;
    }

    @Override
    public String getCustomField1() {
        return customField1;
    }

    @Override
    public String getCustomField2() {
        return customField2;
    }

    @Override
    public String getCustomField3() {
        return customField3;
    }

    @Override
    public String getCustomField4() {
        return customField4;
    }

    @Override
    public String getCustomField5() {
        return customField5;
    }

    @Override
    public String getCustomField6() {
        return customField6;
    }

    @Override
    public String getCustomField7() {
        return customField7;
    }

    @Override
    public String getCustomField8() {
        return customField8;
    }

    @Override
    public String getCustomField9() {
        return customField9;
    }

    @Override
    public String getCustomField10() {
        return customField10;
    }
}
