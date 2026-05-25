package com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries;

import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecordMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence with optional Redis materialization.
 */
@Service
public class RevenueJournalEntriesService {

    private final NamedParameterJdbcTemplate jdbc;
    private final RevenueJournalEntriesRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public RevenueJournalEntriesService(
            NamedParameterJdbcTemplate jdbc,
            RevenueJournalEntriesRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `accountPeriodId`, `JournalAccountPeriodId`, `DebitAccountName`, `CreditAccountName`, `Amount`, `Currency`, `functionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `debitAccount1`, `debitAccount2`, `debitAccount3`, `debitAccount4`, `debitAccount5`, `debitAccount6`, `debitAccount7`, `debitAccount8`, `debitAccount9`, `debitAccount10`, `creditAccount1`, `creditAccount2`, `creditAccount3`, `creditAccount4`, `creditAccount5`, `creditAccount6`, `creditAccount7`, `creditAccount8`, `creditAccount9`, `creditAccount10`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy`, `createdPeriodId`, `updatedPeriodId`, `IsPosted`, `IsUnbilledAccount`, `IsInitialEntry`, `isUnbilledReversal`, `reversalFlag`, `customField1`, `customField2`, `customField3`, `customField4`, `customField5`, `customField6`, `customField7`, `customField8`, `customField9`, `customField10` FROM `RevenueJournalEntries`";
    public Optional<RevenueJournalEntriesRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }
    public Optional<RevenueJournalEntriesRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(RevenueJournalEntriesRecord.TABLE_NAME, String.valueOf(id), RevenueJournalEntriesRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            RevenueJournalEntriesRecord.TABLE_NAME, String.valueOf(row.id()), row);
                    return row;
                }));
    }
    public List<RevenueJournalEntriesRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }
}
