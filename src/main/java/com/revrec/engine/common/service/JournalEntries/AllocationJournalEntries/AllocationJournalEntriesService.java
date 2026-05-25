package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.common.persistence.PersistenceFlags;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import java.util.List;
import java.util.Map;
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

    private static final String NEXT_ID = "SELECT NEXTVAL(allocation_journal_entries_id_seq)";

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

    public long nextId() {
        Long id = jdbc.queryForObject(NEXT_ID, Map.of(), Long.class);
        if (id == null) {
            throw new IllegalStateException("NEXTVAL(allocation_journal_entries_id_seq) returned null");
        }
        return id;
    }

    public void insertAll(List<AllocationJournalEntriesRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        SqlParameterSource[] batch = records.stream()
                .filter(PersistenceFlags::shouldInsert)
                .map(record -> {
                    if (record.getId() == null) {
                        record.setId(nextId());
                    }
                    return toInsertParameters(record);
                })
                .toArray(SqlParameterSource[]::new);
        if (batch.length == 0) {
            return;
        }
        jdbc.batchUpdate(INSERT, batch);
    }

    /**
     * Prepares an {@link AllocationJournalEntriesRecord} from a revenue contract allocation record.
     *
     * <p>Fields that cannot be derived from the allocation record are left as {@code null} and must
     * be populated by upstream services (e.g. account segments, period ids, posting flags).
     */
    public AllocationJournalEntriesRecord prepareAllocationJournalEntry(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord,
            Long openAccountPeriodId,
            Long revenueContractVersion) {
        if (revenueContractAllocationDetailsRecord == null) {
            throw new IllegalArgumentException("revenueContractAllocationDetailsRecord must not be null");
        }

        AllocationJournalEntriesRecord allocationJournalEntryRecord = new AllocationJournalEntriesRecord();
        allocationJournalEntryRecord.setRevenueContractId(
                revenueContractAllocationDetailsRecord.getRevenueContractId());
        allocationJournalEntryRecord.setRevenueContractLineId(revenueContractAllocationDetailsRecord.getId());
        allocationJournalEntryRecord.setRevenueContractVersion(revenueContractVersion);
        allocationJournalEntryRecord.setAmount(revenueContractAllocationDetailsRecord.getCarveAmount());
        allocationJournalEntryRecord.setCurrency(revenueContractAllocationDetailsRecord.getAllocationCurrency());
        allocationJournalEntryRecord.setExchangeRate(revenueContractAllocationDetailsRecord.getExchangeRate());
        allocationJournalEntryRecord.setGlobalexchangeRate(
                revenueContractAllocationDetailsRecord.getGlobalexchangeRate());
        allocationJournalEntryRecord.setExchangeRateDate(
                revenueContractAllocationDetailsRecord.getExchangeRateDate());
        allocationJournalEntryRecord.setInitialEntry(Boolean.TRUE);
        allocationJournalEntryRecord.setCreatedPeriodId(openAccountPeriodId);
        allocationJournalEntryRecord.setCreatedBy(revenueContractAllocationDetailsRecord.getCreatedBy());
        allocationJournalEntryRecord.setCreatedAt(revenueContractAllocationDetailsRecord.getCreatedAt());
        allocationJournalEntryRecord.setUpdatedBy(revenueContractAllocationDetailsRecord.getUpdatedBy());
        allocationJournalEntryRecord.setUpdatedAt(revenueContractAllocationDetailsRecord.getUpdatedAt());
        allocationJournalEntryRecord.setIsUpdate(PersistenceFlags.notUpdate());
        allocationJournalEntryRecord.setIsInsert(PersistenceFlags.insert());
        return allocationJournalEntryRecord;
    }

    /**
     * Prepares a release (non-initial) {@link AllocationJournalEntriesRecord} for a single accounting period.
     */
    public AllocationJournalEntriesRecord prepareAllocationReleaseJournalEntry(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord,
            BigDecimal allocationPerPeriodRevenue,
            Long openAccountPeriodId) {
        RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord =
                allocationRevenueReleaseLineContext.getRevenueContractAllocationDetailsRecord();

        AllocationJournalEntriesRecord allocationJournalEntryRecord = new AllocationJournalEntriesRecord();
        allocationJournalEntryRecord.setRevenueContractId(revenueContractAllocationDetailsRecord.getRevenueContractId());
        allocationJournalEntryRecord.setRevenueContractLineId(revenueContractAllocationDetailsRecord.getId());
        allocationJournalEntryRecord.setRevenueContractVersion(
                allocationRevenueReleaseLineContext.getRevenueContractVersion());
        allocationJournalEntryRecord.setAccountPeriodId(revenueJournalEntryRecord.getAccountPeriodId());
        allocationJournalEntryRecord.setJournalAccountPeriodId(revenueJournalEntryRecord.getJournalAccountPeriodId());
        allocationJournalEntryRecord.setAmount(allocationPerPeriodRevenue);
        allocationJournalEntryRecord.setCurrency(revenueContractAllocationDetailsRecord.getAllocationCurrency());
        allocationJournalEntryRecord.setExchangeRate(revenueContractAllocationDetailsRecord.getExchangeRate());
        allocationJournalEntryRecord.setGlobalexchangeRate(
                revenueContractAllocationDetailsRecord.getGlobalexchangeRate());
        allocationJournalEntryRecord.setExchangeRateDate(
                revenueContractAllocationDetailsRecord.getExchangeRateDate());
        allocationJournalEntryRecord.setInitialEntry(Boolean.FALSE);
        allocationJournalEntryRecord.setCreatedPeriodId(openAccountPeriodId);
        allocationJournalEntryRecord.setCreatedBy(revenueContractAllocationDetailsRecord.getCreatedBy());
        allocationJournalEntryRecord.setCreatedAt(revenueContractAllocationDetailsRecord.getCreatedAt());
        allocationJournalEntryRecord.setUpdatedBy(revenueContractAllocationDetailsRecord.getUpdatedBy());
        allocationJournalEntryRecord.setUpdatedAt(revenueContractAllocationDetailsRecord.getUpdatedAt());
        applyDerivedJournalAccountValue(
                allocationJournalEntryRecord, allocationRevenueReleaseLineContext.getDebitAccountSegments(), true);
        applyDerivedJournalAccountValue(
                allocationJournalEntryRecord, allocationRevenueReleaseLineContext.getCreditAccountSegments(), false);
        allocationJournalEntryRecord.setIsUpdate(PersistenceFlags.notUpdate());
        allocationJournalEntryRecord.setIsInsert(PersistenceFlags.insert());
        return allocationJournalEntryRecord;
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
                .addValue("amount", record.getAmount())
                .addValue("currency", record.getCurrency())
                .addValue("functionalCurrency", record.getFunctionalCurrency())
                .addValue("exchangeRate", record.getExchangeRate())
                .addValue("globalexchangeRate", record.getGlobalexchangeRate())
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
