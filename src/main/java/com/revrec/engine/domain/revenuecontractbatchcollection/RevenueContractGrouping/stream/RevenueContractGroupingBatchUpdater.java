package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.stream;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.config.BatchProcessingConfig;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.RevenueContractReferenceSqlBuilder;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RevenueContractGroupingBatchUpdater {

    private final NamedParameterJdbcTemplate jdbc;
    private final BatchProcessingConfig config;
    private final RevenueContractReferenceSqlBuilder sqlBuilder;

    public RevenueContractGroupingBatchUpdater(
            NamedParameterJdbcTemplate jdbc,
            BatchProcessingConfig config,
            RevenueContractReferenceSqlBuilder sqlBuilder) {
        this.jdbc = jdbc;
        this.config = config;
        this.sqlBuilder = sqlBuilder;
    }

    public int bulkUpdateGroupingData(List<RevRecStageGroupingRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }

        String table = RevenueContractGroupingConstants.REV_REC_STAGE_TABLE;
        String sql = "UPDATE \"" + table + "\" SET "
                + "\"RevenueContractGroupValue\" = :revenueContractGroupValue, "
                + "\"BatchId\" = :batchId, "
                + "\"revenueContractId\" = :revenueContractId "
                + "WHERE \"id\" = :id AND \"tenantId\" = :tenantId";

        int totalUpdated = 0;
        int batchSize = config.getBatchSize();
        for (int i = 0; i < records.size(); i += batchSize) {
            int end = Math.min(i + batchSize, records.size());
            List<RevRecStageGroupingRecord> batch = records.subList(i, end);
            int[] updateCounts = jdbc.batchUpdate(
                    sql, batch.stream().map(this::buildParameterMap).toArray(Map[]::new));
            for (int count : updateCounts) {
                totalUpdated += count;
            }
        }
        return totalUpdated;
    }

    public boolean updateGroupingRecord(RevRecStageGroupingRecord record, int maxRetries) {
        String table = RevenueContractGroupingConstants.REV_REC_STAGE_TABLE;
        String sql = "UPDATE \"" + table + "\" SET "
                + "\"RevenueContractGroupValue\" = :revenueContractGroupValue, "
                + "\"BatchId\" = :batchId, "
                + "\"revenueContractId\" = :revenueContractId "
                + "WHERE \"id\" = :id AND \"tenantId\" = :tenantId";

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                int updated = jdbc.update(sql, buildParameterMap(record));
                return updated > 0;
            } catch (Exception e) {
                if (attempt == maxRetries - 1) {
                    throw new RuntimeException(
                            "Failed to update record after " + maxRetries + " attempts: " + record.getId(), e);
                }
                try {
                    Thread.sleep(RevenueContractGroupingConstants.RETRY_WAIT_MS * (attempt + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return false;
    }

    public int markRecordsAsProcessed(List<Long> recordIds, String flag) {
        if (recordIds == null || recordIds.isEmpty()) {
            return 0;
        }
        return jdbc.update(sqlBuilder.buildMarkProcessedQuery(), Map.of("flag", flag, "ids", recordIds));
    }

    public int markRecordsWithError(List<Long> recordIds, String errorMessage) {
        if (recordIds == null || recordIds.isEmpty()) {
            return 0;
        }
        return jdbc.update(sqlBuilder.buildMarkErrorQuery(), Map.of("errorMessage", errorMessage, "ids", recordIds));
    }

    private Map<String, Object> buildParameterMap(RevRecStageGroupingRecord record) {
        return Map.of(
                "id", record.getId(),
                "tenantId", record.getTenantId(),
                "revenueContractGroupValue", record.getRevenueContractGroupValue(),
                "batchId", record.getBatchId(),
                "revenueContractId", record.getRevenueContractId());
    }
}
