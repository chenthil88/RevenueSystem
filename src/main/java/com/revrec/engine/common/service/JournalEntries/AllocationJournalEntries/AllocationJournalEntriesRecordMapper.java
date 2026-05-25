package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link AllocationJournalEntriesRecord} using column labels (cf. legacy column-driven mapping).
 */
@Component
public final class AllocationJournalEntriesRecordMapper implements RowMapper<AllocationJournalEntriesRecord> {

    @Override
    public @NonNull AllocationJournalEntriesRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getObject("id", Long.class);
        var revenueContractId = rs.getObject("revenueContractId", Long.class);
        var accountPeriodId = rs.getObject("accountPeriodId", Long.class);
        var journalAccountPeriodId = rs.getObject("JournalAccountPeriodId", Long.class);
        var debitAccountName = rs.getString("DebitAccountName");
        var creditAccountName = rs.getString("CreditAccountName");
        var amount = rs.getBigDecimal("Amount");
        var currency = rs.getString("Currency");
        var functionalCurrency = rs.getString("functionalCurrency");
        var exchangeRate = rs.getBigDecimal("exchangeRate");
        var globalexchangeRate = rs.getBigDecimal("globalexchangeRate");
        var exchangeRateDate = rs.getObject("exchangeRateDate", java.time.LocalDate.class);
        var debitAccount1 = rs.getString("debitAccount1");
        var debitAccount2 = rs.getString("debitAccount2");
        var debitAccount3 = rs.getString("debitAccount3");
        var debitAccount4 = rs.getString("debitAccount4");
        var debitAccount5 = rs.getString("debitAccount5");
        var debitAccount6 = rs.getString("debitAccount6");
        var debitAccount7 = rs.getString("debitAccount7");
        var debitAccount8 = rs.getString("debitAccount8");
        var debitAccount9 = rs.getString("debitAccount9");
        var debitAccount10 = rs.getString("debitAccount10");
        var creditAccount1 = rs.getString("creditAccount1");
        var creditAccount2 = rs.getString("creditAccount2");
        var creditAccount3 = rs.getString("creditAccount3");
        var creditAccount4 = rs.getString("creditAccount4");
        var creditAccount5 = rs.getString("creditAccount5");
        var creditAccount6 = rs.getString("creditAccount6");
        var creditAccount7 = rs.getString("creditAccount7");
        var creditAccount8 = rs.getString("creditAccount8");
        var creditAccount9 = rs.getString("creditAccount9");
        var creditAccount10 = rs.getString("creditAccount10");
        var createdAt = rs.getObject("CreatedAt", java.time.LocalDateTime.class);
        var updatedAt = rs.getObject("UpdatedAt", java.time.LocalDateTime.class);
        var createdBy = rs.getString("CreatedBy");
        var updatedBy = rs.getString("UpdatedBy");
        var createdPeriodId = rs.getObject("createdPeriodId", Long.class);
        var updatedPeriodId = rs.getObject("updatedPeriodId", Long.class);
        var isPosted = rs.getObject("IsPosted", Boolean.class);
        var isUnbilledAccount = rs.getObject("IsUnbilledAccount", Boolean.class);
        var isInitialEntry = rs.getObject("IsInitialEntry", Boolean.class);
        var isUnbilledReversal = rs.getObject("isUnbilledReversal", Boolean.class);
        var reversalFlag = rs.getString("reversalFlag");
        var customField1 = rs.getString("customField1");
        var customField2 = rs.getString("customField2");
        var customField3 = rs.getString("customField3");
        var customField4 = rs.getString("customField4");
        var customField5 = rs.getString("customField5");
        var customField6 = rs.getString("customField6");
        var customField7 = rs.getString("customField7");
        var customField8 = rs.getString("customField8");
        var customField9 = rs.getString("customField9");
        var customField10 = rs.getString("customField10");
        return new AllocationJournalEntriesRecord(
                id,
                revenueContractId,
                accountPeriodId,
                journalAccountPeriodId,
                debitAccountName,
                creditAccountName,
                amount,
                currency,
                functionalCurrency,
                exchangeRate,
                globalexchangeRate,
                exchangeRateDate,
                debitAccount1,
                debitAccount2,
                debitAccount3,
                debitAccount4,
                debitAccount5,
                debitAccount6,
                debitAccount7,
                debitAccount8,
                debitAccount9,
                debitAccount10,
                creditAccount1,
                creditAccount2,
                creditAccount3,
                creditAccount4,
                creditAccount5,
                creditAccount6,
                creditAccount7,
                creditAccount8,
                creditAccount9,
                creditAccount10,
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                createdPeriodId,
                updatedPeriodId,
                isPosted,
                isUnbilledAccount,
                isInitialEntry,
                isUnbilledReversal,
                reversalFlag,
                customField1,
                customField2,
                customField3,
                customField4,
                customField5,
                customField6,
                customField7,
                customField8,
                customField9,
                customField10);
    }
}
