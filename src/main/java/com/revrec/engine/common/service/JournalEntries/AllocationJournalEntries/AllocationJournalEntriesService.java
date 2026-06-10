package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.math.ChargebeeDecimal;
import com.revrec.engine.common.metadataservice.JournalAccount.DerivedJournalAccountValue;
import com.revrec.engine.common.persistence.PersistenceFlags;
import com.revrec.engine.common.persistence.SequenceIdGenerator;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

/**
 * Aurora PostgreSQL-backed access to {@code AllocationJournalEntries} with optional Redis materialization.
 */
@Service
public class AllocationJournalEntriesService {

    private final NamedParameterJdbcTemplate jdbc;
    private final AllocationJournalEntriesRecordMapper rowMapper;
    private final NoSqlRecordServer noSqlRecordServer;

    public AllocationJournalEntriesService(
            NamedParameterJdbcTemplate jdbc,
            AllocationJournalEntriesRecordMapper rowMapper,
            NoSqlRecordServer noSqlRecordServer) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.noSqlRecordServer = noSqlRecordServer;
    }

    private static final String INSERT =
            """
            INSERT INTO `AllocationJournalEntries`
                (`id`, `revenueContractId`, `revenueContractLineId`, `revenueContractVersion`, `accountPeriodId`,
                 `JournalAccountPeriodId`, `DebitAccountName`, `CreditAccountName`, `Amount`, `Currency`,
                 `functionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`,
                 `debitAccount1`, `debitAccount2`, `debitAccount3`, `debitAccount4`, `debitAccount5`,
                 `debitAccount6`, `debitAccount7`, `debitAccount8`, `debitAccount9`, `debitAccount10`,
                 `creditAccount1`, `creditAccount2`, `creditAccount3`, `creditAccount4`, `creditAccount5`,
                 `creditAccount6`, `creditAccount7`, `creditAccount8`, `creditAccount9`, `creditAccount10`,
                 `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy`, `createdPeriodId`, `updatedPeriodId`,
                 `IsPosted`, `IsUnbilledAccount`, `IsInitialEntry`, `isUnbilledReversal`, `reversalFlag`,
                 `customField1`, `customField2`, `customField3`, `customField4`, `customField5`,
                 `customField6`, `customField7`, `customField8`, `customField9`, `customField10`)
            VALUES
                (:id, :revenueContractId, :revenueContractLineId, :revenueContractVersion, :accountPeriodId,
                 :journalAccountPeriodId, :debitAccountName, :creditAccountName, :amount, :currency,
                 :functionalCurrency, :exchangeRate, :globalexchangeRate, :exchangeRateDate,
                 :debitAccount1, :debitAccount2, :debitAccount3, :debitAccount4, :debitAccount5,
                 :debitAccount6, :debitAccount7, :debitAccount8, :debitAccount9, :debitAccount10,
                 :creditAccount1, :creditAccount2, :creditAccount3, :creditAccount4, :creditAccount5,
                 :creditAccount6, :creditAccount7, :creditAccount8, :creditAccount9, :creditAccount10,
                 :createdAt, :updatedAt, :createdBy, :updatedBy, :createdPeriodId, :updatedPeriodId,
                 :isPosted, :isUnbilledAccount, :isInitialEntry, :isUnbilledReversal, :reversalFlag,
                 :customField1, :customField2, :customField3, :customField4, :customField5,
                 :customField6, :customField7, :customField8, :customField9, :customField10)
            """;

    private static final String SELECT =
            "SELECT `id`, `revenueContractId`, `revenueContractLineId`, `revenueContractVersion`, `accountPeriodId`, `JournalAccountPeriodId`, `DebitAccountName`, `CreditAccountName`, `Amount`, `Currency`, `functionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `debitAccount1`, `debitAccount2`, `debitAccount3`, `debitAccount4`, `debitAccount5`, `debitAccount6`, `debitAccount7`, `debitAccount8`, `debitAccount9`, `debitAccount10`, `creditAccount1`, `creditAccount2`, `creditAccount3`, `creditAccount4`, `creditAccount5`, `creditAccount6`, `creditAccount7`, `creditAccount8`, `creditAccount9`, `creditAccount10`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy`, `createdPeriodId`, `updatedPeriodId`, `IsPosted`, `IsUnbilledAccount`, `IsInitialEntry`, `isUnbilledReversal`, `reversalFlag`, `customField1`, `customField2`, `customField3`, `customField4`, `customField5`, `customField6`, `customField7`, `customField8`, `customField9`, `customField10` FROM `AllocationJournalEntries`";

    public Optional<AllocationJournalEntriesRecord> findById(Long id) {
        var list = jdbc.query(SELECT + " WHERE `id` = :id", Map.of("id", id), rowMapper);
        return list.stream().findFirst();
    }

    public Optional<AllocationJournalEntriesRecord> findByIdCached(Long id) {
        return noSqlRecordServer
                .get(AllocationJournalEntriesRecord.TABLE_NAME, String.valueOf(id), AllocationJournalEntriesRecord.class)
                .or(() -> findById(id).map(row -> {
                    noSqlRecordServer.put(
                            AllocationJournalEntriesRecord.TABLE_NAME, String.valueOf(row.getId()), row);
                    return row;
                }));
    }

    public List<AllocationJournalEntriesRecord> findAll(int limit, int offset) {
        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",
                Map.of("limit", limit, "offset", offset), rowMapper);
    }

    /**
     * All allocation journal rows for a contract version.
     */
    public List<AllocationJournalEntriesRecord> findByRevenueContractIdAndVersion(
            Long revenueContractId, Long revenueContractVersion) {
        if (revenueContractId == null || revenueContractVersion == null) {
            return List.of();
        }
        return jdbc.query(
                SELECT
                        + " WHERE `revenueContractId` = :revenueContractId"
                        + " AND `revenueContractVersion` = :revenueContractVersion"
                        + " ORDER BY `revenueContractLineId` ASC, `accountPeriodId` ASC, `id` ASC",
                Map.of(
                        "revenueContractId", revenueContractId,
                        "revenueContractVersion", revenueContractVersion),
                rowMapper);
    }

    private static final String ALLOCATION_SCHEDULE_BY_RC_ID =
            """
            SELECT
                `revenueContractLineId`,
                `IsInitialEntry`,
                SUM(`Amount`) AS amount
            FROM `AllocationJournalEntries`
            WHERE `revenueContractId` = :revenueContractId
            GROUP BY `revenueContractLineId`, `IsInitialEntry`
            ORDER BY `revenueContractLineId` ASC
            """;

    /**
     * Aggregated allocation journal amounts per line, split by initial vs release entry.
     */
    public Map<Long, List<AllocationScheduleByRcId>> getAllocationScheduleByRcId(
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Objects.requireNonNull(revenueContractHeaderRecord, "revenueContractHeaderRecord");
        Long revenueContractId = revenueContractHeaderRecord.getRevenueContractId();
        Objects.requireNonNull(revenueContractId, "revenueContractId");

        Map<Long, List<AllocationScheduleByRcId>> byLineId = new LinkedHashMap<>();
        var params = new MapSqlParameterSource().addValue("revenueContractId", revenueContractId);

        aggregateAllocationScheduleByRcId(ALLOCATION_SCHEDULE_BY_RC_ID, params, byLineId);
        return immutableScheduleByLineId(byLineId);
    }

    public long nextId() {
        return SequenceIdGenerator.nextId();
    }

    public void insertAll(List<AllocationJournalEntriesRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = records.stream()
                .filter(PersistenceFlags::shouldInsert)
                .map(AllocationJournalEntriesService::toInsertParameters)
                .toArray(SqlParameterSource[]::new);
        if (batch.length == 0) {
            return;
        }
        jdbc.batchUpdate(INSERT, batch);
    }

    /**
     * Prepares an {@link AllocationJournalEntriesRecord} for initial carve or per-period release.
     *
     * <p>Initial entry: set {@code isInitialEntry} to {@code true} on the line context.
     * Release entry: pass {@code accountPeriodId}, {@code amount}, and line context with {@code isInitialEntry} false.
     */
    public AllocationJournalEntriesRecord prepareAllocationJournalEntry(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord,
            Long revenueContractVersion,
            Long openAccountPeriodId,
            ChargebeeDecimal amount,
            Long accountPeriodId,
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext) {
        if (revenueContractAllocationDetailsRecord == null) {
            throw new IllegalArgumentException("revenueContractAllocationDetailsRecord must not be null");
        }
        if (allocationRevenueReleaseLineContext == null) {
            throw new IllegalArgumentException("allocationRevenueReleaseLineContext must not be null");
        }

        boolean initialEntry = allocationRevenueReleaseLineContext.isInitialEntry();
        if (!initialEntry && accountPeriodId == null) {
            throw new IllegalArgumentException("accountPeriodId required for release journal entries");
        }

        AllocationJournalEntriesRecord allocationJournalEntryRecord = new AllocationJournalEntriesRecord();
        allocationJournalEntryRecord.setId(nextId());
        allocationJournalEntryRecord.setRevenueContractId(
                revenueContractAllocationDetailsRecord.getRevenueContractId());
        allocationJournalEntryRecord.setRevenueContractLineId(revenueContractAllocationDetailsRecord.getId());
        allocationJournalEntryRecord.setRevenueContractVersion(revenueContractVersion);
        allocationJournalEntryRecord.setAmount(
                amount != null ? amount : revenueContractAllocationDetailsRecord.getCarveAmount());
        allocationJournalEntryRecord.setCurrency(revenueContractAllocationDetailsRecord.getAllocationCurrency());
        allocationJournalEntryRecord.setExchangeRate(revenueContractAllocationDetailsRecord.getExchangeRate());
        allocationJournalEntryRecord.setGlobalexchangeRate(
                revenueContractAllocationDetailsRecord.getGlobalexchangeRate());
        allocationJournalEntryRecord.setExchangeRateDate(
                revenueContractAllocationDetailsRecord.getExchangeRateDate());
        allocationJournalEntryRecord.setInitialEntry(initialEntry);
        allocationJournalEntryRecord.setCreatedPeriodId(openAccountPeriodId);
        allocationJournalEntryRecord.setCreatedBy(revenueContractAllocationDetailsRecord.getCreatedBy());
        allocationJournalEntryRecord.setCreatedAt(revenueContractAllocationDetailsRecord.getCreatedAt());
        allocationJournalEntryRecord.setUpdatedBy(revenueContractAllocationDetailsRecord.getUpdatedBy());
        allocationJournalEntryRecord.setUpdatedAt(revenueContractAllocationDetailsRecord.getUpdatedAt());
        allocationJournalEntryRecord.setAccountPeriodId(accountPeriodId);
        allocationJournalEntryRecord.setJournalAccountPeriodId(accountPeriodId);
        applyDerivedJournalAccountValue(
                    allocationJournalEntryRecord,
                    allocationRevenueReleaseLineContext.getCreditAccountSegments(),
                    false);

        if (!initialEntry) { 
            applyDerivedJournalAccountValue(
                    allocationJournalEntryRecord,
                    allocationRevenueReleaseLineContext.getDebitAccountSegments(),
                    true);
          
        }

        allocationJournalEntryRecord.setIsUpdate(PersistenceFlags.notUpdate());
        allocationJournalEntryRecord.setIsInsert(PersistenceFlags.insert());
        return allocationJournalEntryRecord;
    }

    private void aggregateAllocationScheduleByRcId(
            String sql,
            MapSqlParameterSource params,
            Map<Long, List<AllocationScheduleByRcId>> byLineId) {
        jdbc.query(sql, params, (rs, rowNum) -> {
            Long lineId = rs.getObject("revenueContractLineId", Long.class);
            Boolean isInitialEntry = rs.getObject("IsInitialEntry", Boolean.class);
            ChargebeeDecimal amount = ChargebeeDecimal.of(rs.getBigDecimal("amount"));
            byLineId.computeIfAbsent(lineId, ignored -> new ArrayList<>())
                    .add(new AllocationScheduleByRcId(lineId, isInitialEntry, amount));
            return null;
        });
    }

    private static Map<Long, List<AllocationScheduleByRcId>> immutableScheduleByLineId(
            Map<Long, List<AllocationScheduleByRcId>> byLineId) {
        byLineId.replaceAll((lineId, periods) -> List.copyOf(periods));
        return Map.copyOf(byLineId);
    }

    private static void applyDerivedJournalAccountValue(
            AllocationJournalEntriesRecord entry,
            DerivedJournalAccountValue derivedJournalAccountValue,
            boolean debit) {
        if (derivedJournalAccountValue == null) {
            return;
        }
        if (debit) {
            entry.setDebitAccountName(derivedJournalAccountValue.journalAccountName());
            entry.setDebitAccount1(derivedJournalAccountValue.segment(1));
            entry.setDebitAccount2(derivedJournalAccountValue.segment(2));
            entry.setDebitAccount3(derivedJournalAccountValue.segment(3));
            entry.setDebitAccount4(derivedJournalAccountValue.segment(4));
            entry.setDebitAccount5(derivedJournalAccountValue.segment(5));
            entry.setDebitAccount6(derivedJournalAccountValue.segment(6));
            entry.setDebitAccount7(derivedJournalAccountValue.segment(7));
            entry.setDebitAccount8(derivedJournalAccountValue.segment(8));
            entry.setDebitAccount9(derivedJournalAccountValue.segment(9));
            entry.setDebitAccount10(derivedJournalAccountValue.segment(10));
        } else {
            entry.setCreditAccountName(derivedJournalAccountValue.journalAccountName());
            entry.setCreditAccount1(derivedJournalAccountValue.segment(1));
            entry.setCreditAccount2(derivedJournalAccountValue.segment(2));
            entry.setCreditAccount3(derivedJournalAccountValue.segment(3));
            entry.setCreditAccount4(derivedJournalAccountValue.segment(4));
            entry.setCreditAccount5(derivedJournalAccountValue.segment(5));
            entry.setCreditAccount6(derivedJournalAccountValue.segment(6));
            entry.setCreditAccount7(derivedJournalAccountValue.segment(7));
            entry.setCreditAccount8(derivedJournalAccountValue.segment(8));
            entry.setCreditAccount9(derivedJournalAccountValue.segment(9));
            entry.setCreditAccount10(derivedJournalAccountValue.segment(10));
        }
    }

    private static SqlParameterSource toInsertParameters(AllocationJournalEntriesRecord record) {
        return new MapSqlParameterSource()
                .addValue("id", record.getId())
                .addValue("revenueContractId", record.getRevenueContractId())
                .addValue("revenueContractLineId", record.getRevenueContractLineId())
                .addValue("revenueContractVersion", record.getRevenueContractVersion())
                .addValue("accountPeriodId", record.getAccountPeriodId())
                .addValue("journalAccountPeriodId", record.getJournalAccountPeriodId())
                .addValue("debitAccountName", record.getDebitAccountName())
                .addValue("creditAccountName", record.getCreditAccountName())
                .addValue("amount", record.getAmount() == null ? null : record.getAmount().toBigDecimal())
                .addValue("currency", record.getCurrency())
                .addValue("functionalCurrency", record.getFunctionalCurrency())
                .addValue("exchangeRate", record.getExchangeRate() == null ? null : record.getExchangeRate().toBigDecimal())
                .addValue("globalexchangeRate", record.getGlobalexchangeRate() == null ? null : record.getGlobalexchangeRate().toBigDecimal())
                .addValue("exchangeRateDate", record.getExchangeRateDate())
                .addValue("debitAccount1", record.getDebitAccount1())
                .addValue("debitAccount2", record.getDebitAccount2())
                .addValue("debitAccount3", record.getDebitAccount3())
                .addValue("debitAccount4", record.getDebitAccount4())
                .addValue("debitAccount5", record.getDebitAccount5())
                .addValue("debitAccount6", record.getDebitAccount6())
                .addValue("debitAccount7", record.getDebitAccount7())
                .addValue("debitAccount8", record.getDebitAccount8())
                .addValue("debitAccount9", record.getDebitAccount9())
                .addValue("debitAccount10", record.getDebitAccount10())
                .addValue("creditAccount1", record.getCreditAccount1())
                .addValue("creditAccount2", record.getCreditAccount2())
                .addValue("creditAccount3", record.getCreditAccount3())
                .addValue("creditAccount4", record.getCreditAccount4())
                .addValue("creditAccount5", record.getCreditAccount5())
                .addValue("creditAccount6", record.getCreditAccount6())
                .addValue("creditAccount7", record.getCreditAccount7())
                .addValue("creditAccount8", record.getCreditAccount8())
                .addValue("creditAccount9", record.getCreditAccount9())
                .addValue("creditAccount10", record.getCreditAccount10())
                .addValue("createdAt", record.getCreatedAt())
                .addValue("updatedAt", record.getUpdatedAt())
                .addValue("createdBy", record.getCreatedBy())
                .addValue("updatedBy", record.getUpdatedBy())
                .addValue("createdPeriodId", record.getCreatedPeriodId())
                .addValue("updatedPeriodId", record.getUpdatedPeriodId())
                .addValue("isPosted", record.isPosted())
                .addValue("isUnbilledAccount", record.isUnbilledAccount())
                .addValue("isInitialEntry", record.isInitialEntry())
                .addValue("isUnbilledReversal", record.isUnbilledReversal())
                .addValue("reversalFlag", record.getReversalFlag())
                .addValue("customField1", record.getCustomField1())
                .addValue("customField2", record.getCustomField2())
                .addValue("customField3", record.getCustomField3())
                .addValue("customField4", record.getCustomField4())
                .addValue("customField5", record.getCustomField5())
                .addValue("customField6", record.getCustomField6())
                .addValue("customField7", record.getCustomField7())
                .addValue("customField8", record.getCustomField8())
                .addValue("customField9", record.getCustomField9())
                .addValue("customField10", record.getCustomField10());
    }
}
