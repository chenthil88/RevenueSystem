package com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries;

import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecordMapper;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Aurora PostgreSQL-backed persistence with optional Redis materialization.
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
            "SELECT `id`, `revenueContractId`, `revenueContractLineId`, `revenueContractVersion`, `accountPeriodId`, `JournalAccountPeriodId`, `DebitAccountName`, `CreditAccountName`, `Amount`, `Currency`, `functionalCurrency`, `exchangeRate`, `globalexchangeRate`, `exchangeRateDate`, `debitAccount1`, `debitAccount2`, `debitAccount3`, `debitAccount4`, `debitAccount5`, `debitAccount6`, `debitAccount7`, `debitAccount8`, `debitAccount9`, `debitAccount10`, `creditAccount1`, `creditAccount2`, `creditAccount3`, `creditAccount4`, `creditAccount5`, `creditAccount6`, `creditAccount7`, `creditAccount8`, `creditAccount9`, `creditAccount10`, `CreatedAt`, `UpdatedAt`, `CreatedBy`, `UpdatedBy`, `createdPeriodId`, `updatedPeriodId`, `IsPosted`, `IsUnbilledAccount`, `IsInitialEntry`, `isUnbilledReversal`, `reversalFlag`, `customField1`, `customField2`, `customField3`, `customField4`, `customField5`, `customField6`, `customField7`, `customField8`, `customField9`, `customField10` FROM `RevenueJournalEntries`";
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

    private static final String RETROSPECTIVE_BY_LINE_AND_PERIOD =
            """
            SELECT
                `revenueContractLineId`,
                SUM(`Amount`) AS amount,
                CASE
                    WHEN `accountPeriodId` <= :openPeriodId THEN :openPeriodId
                    ELSE `accountPeriodId`
                END AS periodId
            FROM `RevenueJournalEntries`
            WHERE `revenueContractLineId` IN (:revenueContractLineIds)
            GROUP BY `revenueContractLineId`,
                CASE
                    WHEN `accountPeriodId` <= :openPeriodId THEN :openPeriodId
                    ELSE `accountPeriodId`
                END
            ORDER BY `revenueContractLineId` ASC, periodId ASC
            """;

    /**
     * Aggregates journal amounts per revenue contract line and effective period.
     *
     * <p>Period id is {@code openPeriodId} when {@code accountPeriodId <= openPeriodId}, otherwise
     * {@code accountPeriodId}. Returns one {@link RevenueJournalEntriesPerPeriod} per period per line,
     * ordered by period id ascending.
     */
    public Map<Long, List<RevenueJournalEntriesPerPeriod>> getRetrospectiveJournalEntries(
            List<Long> revenueContractLineIds, Long openPeriodId) {
        Objects.requireNonNull(openPeriodId, "openPeriodId");
        if (revenueContractLineIds == null || revenueContractLineIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, List<RevenueJournalEntriesPerPeriod>> byLineId = new LinkedHashMap<>();
        revenueContractLineIds.forEach(lineId -> byLineId.put(lineId, new ArrayList<>()));

        var params = new MapSqlParameterSource()
                .addValue("openPeriodId", openPeriodId)
                .addValue("revenueContractLineIds", revenueContractLineIds);

        aggregateJournalEntriesByLine(RETROSPECTIVE_BY_LINE_AND_PERIOD, params, byLineId);
        return immutableByLineId(byLineId);
    }

    private static final String PROSPECTIVE_BY_CONTRACT_AND_PERIOD =
            """
            SELECT
                `revenueContractLineId`,
                SUM(`Amount`) AS amount,
                CASE
                    WHEN `accountPeriodId` <= :accountPeriodId THEN :accountPeriodId
                    ELSE `accountPeriodId`
                END AS periodId
            FROM `RevenueJournalEntries`
            WHERE `revenueContractId` = :revenueContractId
              AND `revenueContractVersion` = :revenueContractVersion
            GROUP BY `revenueContractLineId`,
                CASE
                    WHEN `accountPeriodId` <= :accountPeriodId THEN :accountPeriodId
                    ELSE `accountPeriodId`
                END
            ORDER BY `revenueContractLineId` ASC, periodId ASC
            """;

    /**
     * Aggregates prospective journal amounts per line for a revenue contract version.
     *
     * <p>Filters by {@code revenueContractId} and {@code revenueContractVersion}. Period id uses the
     * same rule as {@link #getRetrospectiveJournalEntries}: the given {@code accountPeriodId} when
     * {@code row.accountPeriodId <= accountPeriodId}, otherwise the row's {@code accountPeriodId}.
     */
    public Map<Long, List<RevenueJournalEntriesPerPeriod>> getProspectiveJournalEntries(
            Long revenueContractId, Long accountPeriodId, Long revenueContractVersion) {
        Objects.requireNonNull(revenueContractId, "revenueContractId");
        Objects.requireNonNull(accountPeriodId, "accountPeriodId");
        Objects.requireNonNull(revenueContractVersion, "revenueContractVersion");

        Map<Long, List<RevenueJournalEntriesPerPeriod>> byLineId = new LinkedHashMap<>();
        var params = new MapSqlParameterSource()
                .addValue("revenueContractId", revenueContractId)
                .addValue("accountPeriodId", accountPeriodId)
                .addValue("revenueContractVersion", revenueContractVersion);

        aggregateJournalEntriesByLine(PROSPECTIVE_BY_CONTRACT_AND_PERIOD, params, byLineId);
        return immutableByLineId(byLineId);
    }

    private void aggregateJournalEntriesByLine(
            String sql,
            MapSqlParameterSource params,
            Map<Long, List<RevenueJournalEntriesPerPeriod>> byLineId) {
        jdbc.query(sql, params, (rs, rowNum) -> {
            Long lineId = rs.getObject("revenueContractLineId", Long.class);
            Long periodId = rs.getObject("periodId", Long.class);
            BigDecimal amount = rs.getBigDecimal("amount");
            byLineId.computeIfAbsent(lineId, ignored -> new ArrayList<>())
                    .add(new RevenueJournalEntriesPerPeriod(periodId, amount));
            return null;
        });
    }

    private static Map<Long, List<RevenueJournalEntriesPerPeriod>> immutableByLineId(
            Map<Long, List<RevenueJournalEntriesPerPeriod>> byLineId) {
        byLineId.replaceAll((lineId, periods) -> List.copyOf(periods));
        return Map.copyOf(byLineId);
    }
}
