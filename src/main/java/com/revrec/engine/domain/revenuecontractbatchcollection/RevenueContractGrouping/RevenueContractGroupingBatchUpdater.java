package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * Handles bulk update operations to Aurora PostgreSQL for revenue contract grouping
 */
@Component
public class RevenueContractGroupingBatchUpdater {

    private final NamedParameterJdbcTemplate jdbc;
    private final BatchProcessingConfig config;

    public RevenueContractGroupingBatchUpdater(
            NamedParameterJdbcTemplate jdbc,
            BatchProcessingConfig config) {
        this.jdbc = jdbc;
        this.config = config;
    }

    /**
     * Bulk update revenue contract grouping records
     * Uses batch update for optimal performance with large datasets
     */
    public int bulkUpdateGroupingData(List<RevRecStageGroupingRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }

        String sql = "UPDATE \"RevRecStage\" SET " +
                "\"RevenueContractGroupValue\" = :revenueContractGroupValue, " +
                "\"BatchId\" = :batchId, " +
                "\"revenueContractId\" = :revenueContractId " +
                "WHERE \"id\" = :id AND \"tenantId\" = :tenantId";

        int totalUpdated = 0;
        int batchSize = config.getBatchSize();

        // Process in batches
        for (int i = 0; i < records.size(); i += batchSize) {
            int end = Math.min(i + batchSize, records.size());
            List<RevRecStageGroupingRecord> batch = records.subList(i, end);

            int[] updateCounts = jdbc.batchUpdate(sql,
                    batch.stream()
                            .map(this::buildParameterMap)
                            .toArray(Map[]::new));

            for (int count : updateCounts) {
                totalUpdated += count;
            }
        }

        return totalUpdated;
    }

    /**
     * Update single record with retry logic
     */
    public boolean updateGroupingRecord(RevRecStageGroupingRecord record, int maxRetries) {
        String sql = "UPDATE \"RevRecStage\" SET " +
                "\"RevenueContractGroupValue\" = :revenueContractGroupValue, " +
                "\"BatchId\" = :batchId, " +
                "\"revenueContractId\" = :revenueContractId " +
                "WHERE \"id\" = :id AND \"tenantId\" = :tenantId";

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
                    Thread.sleep(100 * (attempt + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Mark records as processed/failed
     */
    public int markRecordsAsProcessed(List<Long> recordIds, String flag) {
        if (recordIds == null || recordIds.isEmpty()) {
            return 0;
        }

        String sql = "UPDATE \"RevRecStage\" SET \"processsedFlag\" = :flag WHERE \"id\" IN (:ids)";
        return jdbc.update(sql, Map.of("flag", flag, "ids", recordIds));
    }

    /**
     * Mark records with error message
     */
    public int markRecordsWithError(List<Long> recordIds, String errorMessage) {
        if (recordIds == null || recordIds.isEmpty()) {
            return 0;
        }

        String sql = "UPDATE \"RevRecStage\" SET \"processsedFlag\" = 'E', \"errorMessage\" = :errorMessage WHERE \"id\" IN (:ids)";
        return jdbc.update(sql, Map.of("errorMessage", errorMessage, "ids", recordIds));
    }

    private Map<String, Object> buildParameterMap(RevRecStageGroupingRecord record) {
        return Map.of(
                "id", record.getId(),
                "tenantId", record.getTenantId(),
                "revenueContractGroupValue", record.getRevenueContractGroupValue(),
                "batchId", record.getBatchId(),
                "revenueContractId", record.getRevenueContractId()
        );
    }
}
