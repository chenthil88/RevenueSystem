package com.revrec.engine.common.service.RevRecStage;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.service.RevRecStage.RevRecStageRecord;
import com.revrec.engine.domain.service.RevRecStage.RevRecStageRecordMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * TiDB-backed persistence for {@code RevRecStage}.
 */
@Service
public class RevRecStageService {

    public static final String DEFAULT_GROUPING_VALUE = RevenueContractGroupingConstants.DEFAULT_GROUPING_VALUE;

    /**
     * Column order must match {@link RevRecStageRecordMapper} (same as {@code db_script.sql}).
     */
    private static final String SELECT_ALL =
            "SELECT * FROM `" + RevRecStageRecord.TABLE_NAME + "`";

    private static final String STREAM_UNASSIGNED_BY_BATCH =
            SELECT_ALL
                    + """
                     WHERE `BatchId` = :batchId
                       AND `revenueContractId` IS NULL
                       AND IFNULL(`processsedFlag`, 'N') = 'N'
                       AND `ErrorMessage` IS NULL
                       AND `transactionType` = 'SalesOrder'
                     ORDER BY `RevenueContractGroupValue`, `id`
                    """;

    private final NamedParameterJdbcTemplate jdbc;
    private final RevRecStageRecordMapper rowMapper;

    public RevRecStageService(NamedParameterJdbcTemplate jdbc, RevRecStageRecordMapper rowMapper) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
    }

    /**
     * Stamps {@code RevenueContractGroupValue}, {@code BatchId}, and {@code revenueContractId} on rows
     * that are not yet processed (or are in error).
     *
     * @param groupingRuleSql SQL expression for the group value (built from hierarchy {@code GroupingFields})
     * @return number of rows updated
     */
    public int applyContractGrouping(long batchId, String groupingRuleSql) {
        return jdbc.update(buildApplyContractGroupingSql(groupingRuleSql), Map.of("batchId", batchId));
    }

    /**
     * Streams stage lines for a batch that still need a revenue contract assignment, ordered by
     * {@code RevenueContractGroupValue} so the same group can be processed together.
     *
     * <p>Caller must close the stream, e.g. {@code try (var lines = service.stream...) { ... }}
     */
    public Stream<RevRecStageRecord> streamUnassignedByGroupValue(long batchId) {
        return jdbc.queryForStream(STREAM_UNASSIGNED_BY_BATCH, Map.of("batchId", batchId), rowMapper);
    }

    private static final String BULK_UPDATE_REVENUE_CONTRACT_ID =
            """
            UPDATE `RevRecStage`
            SET `revenueContractId` = :revenueContractId
            WHERE `id` = :stageId
              AND `revenueContractId` IS NULL
            """;

    /**
     * @param assignments stage row id → revenue contract id
     * @return total rows updated
     */
    public int bulkUpdateRevenueContractIds(List<StageRevenueContractAssignment> assignments) {
        if (assignments.isEmpty()) {
            return 0;
        }
        SqlParameterSource[] batch = assignments.stream()
                .map(assignment -> new MapSqlParameterSource()
                        .addValue("revenueContractId", assignment.revenueContractId())
                        .addValue("stageId", assignment.stageId()))
                .toArray(SqlParameterSource[]::new);
        int[] counts = jdbc.batchUpdate(BULK_UPDATE_REVENUE_CONTRACT_ID, batch);
        return Arrays.stream(counts).sum();
    }

    public record StageRevenueContractAssignment(long stageId, long revenueContractId) {}

    private static String buildApplyContractGroupingSql(String groupingRuleSql) {
        return """
                UPDATE `%s` stg
                SET `RevenueContractGroupValue` = CASE
                        WHEN `RevenueContractGroupValue` IS NULL THEN IFNULL(%s, '%s')
                        ELSE `RevenueContractGroupValue`
                    END,
                    `BatchId` = :batchId,
                    `revenueContractId` = CASE
                        WHEN `revenueContractId` IS NULL THEN (
                            SELECT rcf.`RevenueContractId`
                            FROM `revenueContractReferenceDetails` rcf
                            WHERE (rcf.`salesOrderId` = stg.`salesOrderId`)
                               OR (rcf.`invoiceId` = stg.`InvoiceId`)
                               OR (rcf.`invoiceId` = stg.`OriginalInvoiceId`)
                               OR (rcf.`salesOrderId` = stg.`OriginalSalesOrderId`)
                            LIMIT 1
                        )
                        ELSE `revenueContractId`
                    END
                WHERE IFNULL(stg.`processsedFlag`, 'N') IN ('N', 'E')
                """
                .formatted(
                        RevRecStageRecord.TABLE_NAME,
                        groupingRuleSql,
                        DEFAULT_GROUPING_VALUE);
    }
}
