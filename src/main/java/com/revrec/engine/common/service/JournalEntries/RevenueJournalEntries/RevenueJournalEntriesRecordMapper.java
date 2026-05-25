package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import com.revrec.engine.common.persistence.PersistenceFlags;
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
        var record = new RevenueJournalEntriesRecord();
        record.setId(rs.getObject(c++, Long.class));
        record.setRevenueContractId(rs.getObject(c++, Long.class));
        record.setRevenueContractLineId(rs.getObject(c++, Long.class));
        record.setRevenueContractVersion(rs.getObject(c++, Long.class));
        record.setAccountPeriodId(rs.getObject(c++, Long.class));
        record.setJournalAccountPeriodId(rs.getObject(c++, Long.class));
        record.setDebitAccountName(rs.getString(c++));
        record.setCreditAccountName(rs.getString(c++));
        record.setAmount(rs.getBigDecimal(c++));
        record.setCurrency(rs.getString(c++));
        record.setFunctionalCurrency(rs.getString(c++));
        record.setExchangeRate(rs.getBigDecimal(c++));
        record.setGlobalexchangeRate(rs.getBigDecimal(c++));
        record.setExchangeRateDate(rs.getObject(c++, java.time.LocalDate.class));
        record.setDebitAccount1(rs.getString(c++));
        record.setDebitAccount2(rs.getString(c++));
        record.setDebitAccount3(rs.getString(c++));
        record.setDebitAccount4(rs.getString(c++));
        record.setDebitAccount5(rs.getString(c++));
        record.setDebitAccount6(rs.getString(c++));
        record.setDebitAccount7(rs.getString(c++));
        record.setDebitAccount8(rs.getString(c++));
        record.setDebitAccount9(rs.getString(c++));
        record.setDebitAccount10(rs.getString(c++));
        record.setCreditAccount1(rs.getString(c++));
        record.setCreditAccount2(rs.getString(c++));
        record.setCreditAccount3(rs.getString(c++));
        record.setCreditAccount4(rs.getString(c++));
        record.setCreditAccount5(rs.getString(c++));
        record.setCreditAccount6(rs.getString(c++));
        record.setCreditAccount7(rs.getString(c++));
        record.setCreditAccount8(rs.getString(c++));
        record.setCreditAccount9(rs.getString(c++));
        record.setCreditAccount10(rs.getString(c++));
        record.setCreatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setUpdatedAt(rs.getObject(c++, java.time.LocalDateTime.class));
        record.setCreatedBy(rs.getString(c++));
        record.setUpdatedBy(rs.getString(c++));
        record.setCreatedPeriodId(rs.getObject(c++, Long.class));
        record.setUpdatedPeriodId(rs.getObject(c++, Long.class));
        record.setIsPosted(rs.getObject(c++, Boolean.class));
        record.setIsUnbilledAccount(rs.getObject(c++, Boolean.class));
        record.setIsInitialEntry(rs.getObject(c++, Boolean.class));
        record.setIsUnbilledReversal(rs.getObject(c++, Boolean.class));
        record.setReversalFlag(rs.getString(c++));
        record.setCustomField1(rs.getString(c++));
        record.setCustomField2(rs.getString(c++));
        record.setCustomField3(rs.getString(c++));
        record.setCustomField4(rs.getString(c++));
        record.setCustomField5(rs.getString(c++));
        record.setCustomField6(rs.getString(c++));
        record.setCustomField7(rs.getString(c++));
        record.setCustomField8(rs.getString(c++));
        record.setCustomField9(rs.getString(c++));
        record.setCustomField10(rs.getString(c++));
        record.setIsUpdate(PersistenceFlags.notUpdate());
        record.setIsInsert(PersistenceFlags.notInsert());
        return record;
    }
}
