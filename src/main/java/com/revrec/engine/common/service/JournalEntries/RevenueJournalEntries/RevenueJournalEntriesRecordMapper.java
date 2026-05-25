package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Maps JDBC columns to {@link RevenueJournalEntriesRecord}.
 */
@Component
public final class RevenueJournalEntriesRecordMapper implements RowMapper<RevenueJournalEntriesRecord> {

    @Override
    public @NonNull RevenueJournalEntriesRecord mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        int c = 1;
        var id = rs.getObject(c++, Long.class);
        var revenueContractId = rs.getObject(c++, Long.class);
        var accountPeriodId = rs.getObject(c++, Long.class);
        var journalAccountPeriodId = rs.getObject(c++, Long.class);
        var debitAccountName = rs.getString(c++);
        var creditAccountName = rs.getString(c++);
        var amount = rs.getBigDecimal(c++);
        var currency = rs.getString(c++);
        var functionalCurrency = rs.getString(c++);
        var exchangeRate = rs.getBigDecimal(c++);
        var globalexchangeRate = rs.getBigDecimal(c++);
        var exchangeRateDate = rs.getObject(c++, java.time.LocalDate.class);
        var debitAccount1 = rs.getString(c++);
        var debitAccount2 = rs.getString(c++);
        var debitAccount3 = rs.getString(c++);
        var debitAccount4 = rs.getString(c++);
        var debitAccount5 = rs.getString(c++);
        var debitAccount6 = rs.getString(c++);
        var debitAccount7 = rs.getString(c++);
        var debitAccount8 = rs.getString(c++);
        var debitAccount9 = rs.getString(c++);
        var debitAccount10 = rs.getString(c++);
        var creditAccount1 = rs.getString(c++);
        var creditAccount2 = rs.getString(c++);
        var creditAccount3 = rs.getString(c++);
        var creditAccount4 = rs.getString(c++);
        var creditAccount5 = rs.getString(c++);
        var creditAccount6 = rs.getString(c++);
        var creditAccount7 = rs.getString(c++);
        var creditAccount8 = rs.getString(c++);
        var creditAccount9 = rs.getString(c++);
        var creditAccount10 = rs.getString(c++);
        var createdAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var updatedAt = rs.getObject(c++, java.time.LocalDateTime.class);
        var createdBy = rs.getString(c++);
        var updatedBy = rs.getString(c++);
        var createdPeriodId = rs.getObject(c++, Long.class);
        var updatedPeriodId = rs.getObject(c++, Long.class);
        var isPosted = rs.getObject(c++, Boolean.class);
        var isUnbilledAccount = rs.getObject(c++, Boolean.class);
        var isInitialEntry = rs.getObject(c++, Boolean.class);
        var isUnbilledReversal = rs.getObject(c++, Boolean.class);
        var reversalFlag = rs.getString(c++);
        var customField1 = rs.getString(c++);
        var customField2 = rs.getString(c++);
        var customField3 = rs.getString(c++);
        var customField4 = rs.getString(c++);
        var customField5 = rs.getString(c++);
        var customField6 = rs.getString(c++);
        var customField7 = rs.getString(c++);
        var customField8 = rs.getString(c++);
        var customField9 = rs.getString(c++);
        var customField10 = rs.getString(c++);
        return new RevenueJournalEntriesRecord(
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
                customField10
        );
    }
}
