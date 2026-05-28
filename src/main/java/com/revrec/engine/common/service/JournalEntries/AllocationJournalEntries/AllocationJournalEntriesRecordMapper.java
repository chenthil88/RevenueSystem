package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.persistence.PersistenceFlags;
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
        var record = new AllocationJournalEntriesRecord();
        record.setId(rs.getObject("id", Long.class));
        record.setRevenueContractId(rs.getObject("revenueContractId", Long.class));
        record.setRevenueContractLineId(rs.getObject("revenueContractLineId", Long.class));
        record.setRevenueContractVersion(rs.getObject("revenueContractVersion", Long.class));
        record.setAccountPeriodId(rs.getObject("accountPeriodId", Long.class));
        record.setJournalAccountPeriodId(rs.getObject("JournalAccountPeriodId", Long.class));
        record.setDebitAccountName(rs.getString("DebitAccountName"));
        record.setCreditAccountName(rs.getString("CreditAccountName"));
        record.setAmount(rs.getBigDecimal("Amount"));
        record.setCurrency(rs.getString("Currency"));
        record.setFunctionalCurrency(rs.getString("functionalCurrency"));
        record.setExchangeRate(rs.getBigDecimal("exchangeRate"));
        record.setGlobalexchangeRate(rs.getBigDecimal("globalexchangeRate"));
        record.setExchangeRateDate(rs.getObject("exchangeRateDate", java.time.LocalDate.class));
        record.setDebitAccount1(rs.getString("debitAccount1"));
        record.setDebitAccount2(rs.getString("debitAccount2"));
        record.setDebitAccount3(rs.getString("debitAccount3"));
        record.setDebitAccount4(rs.getString("debitAccount4"));
        record.setDebitAccount5(rs.getString("debitAccount5"));
        record.setDebitAccount6(rs.getString("debitAccount6"));
        record.setDebitAccount7(rs.getString("debitAccount7"));
        record.setDebitAccount8(rs.getString("debitAccount8"));
        record.setDebitAccount9(rs.getString("debitAccount9"));
        record.setDebitAccount10(rs.getString("debitAccount10"));
        record.setCreditAccount1(rs.getString("creditAccount1"));
        record.setCreditAccount2(rs.getString("creditAccount2"));
        record.setCreditAccount3(rs.getString("creditAccount3"));
        record.setCreditAccount4(rs.getString("creditAccount4"));
        record.setCreditAccount5(rs.getString("creditAccount5"));
        record.setCreditAccount6(rs.getString("creditAccount6"));
        record.setCreditAccount7(rs.getString("creditAccount7"));
        record.setCreditAccount8(rs.getString("creditAccount8"));
        record.setCreditAccount9(rs.getString("creditAccount9"));
        record.setCreditAccount10(rs.getString("creditAccount10"));
        record.setCreatedAt(rs.getObject("CreatedAt", java.time.LocalDateTime.class));
        record.setUpdatedAt(rs.getObject("UpdatedAt", java.time.LocalDateTime.class));
        record.setCreatedBy(rs.getString("CreatedBy"));
        record.setUpdatedBy(rs.getString("UpdatedBy"));
        record.setCreatedPeriodId(rs.getObject("createdPeriodId", Long.class));
        record.setUpdatedPeriodId(rs.getObject("updatedPeriodId", Long.class));
        record.setPosted(rs.getObject("IsPosted", Boolean.class));
        record.setUnbilledAccount(rs.getObject("IsUnbilledAccount", Boolean.class));
        record.setInitialEntry(rs.getObject("IsInitialEntry", Boolean.class));
        record.setUnbilledReversal(rs.getObject("isUnbilledReversal", Boolean.class));
        record.setReversalFlag(rs.getString("reversalFlag"));
        record.setCustomField1(rs.getString("customField1"));
        record.setCustomField2(rs.getString("customField2"));
        record.setCustomField3(rs.getString("customField3"));
        record.setCustomField4(rs.getString("customField4"));
        record.setCustomField5(rs.getString("customField5"));
        record.setCustomField6(rs.getString("customField6"));
        record.setCustomField7(rs.getString("customField7"));
        record.setCustomField8(rs.getString("customField8"));
        record.setCustomField9(rs.getString("customField9"));
        record.setCustomField10(rs.getString("customField10"));
        record.setIsUpdate(PersistenceFlags.notUpdate());
        record.setIsInsert(PersistenceFlags.notInsert());
        return record;
    }
}
