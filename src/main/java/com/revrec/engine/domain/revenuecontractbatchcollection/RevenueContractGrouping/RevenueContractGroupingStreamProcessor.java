package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Main service for streaming, processing, and bulk updating revenue contract grouping
 * Handles large data volumes efficiently with parallel processing
 */
@Slf4j
@Service
public class RevenueContractGroupingStreamProcessor {

    private final RevenueContractGroupingStreamReader streamReader;
    private final RevenueContractGroupingBatchUpdater batchUpdater;
    private final RevenueContractReferenceService referenceService;
    private final BatchProcessingConfig config;
    private final ExecutorService executor;

    public RevenueContractGroupingStreamProcessor(
            RevenueContractGroupingStreamReader streamReader,
            RevenueContractGroupingBatchUpdater batchUpdater,
            RevenueContractReferenceService referenceService,
            BatchProcessingConfig config) {
        this.streamReader = streamReader;
        this.batchUpdater = batchUpdater;
        this.referenceService = referenceService;
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.getThreadPoolSize());
    }

    /**
     * Stream and process revenue contract grouping for a batch
     * Main entry point for the grouping processing workflow
     */
    public GroupingProcessingResult processGroupingForBatch(
            String tenantId,
            Long batchId,
            String groupingRuleExpr,
            DynamicGroupingRule groupingRule) {

        log.info("Starting grouping processing for tenant={}, batchId={}", tenantId, batchId);

        GroupingProcessingResult result = new GroupingProcessingResult();
        result.setStartTime(System.currentTimeMillis());

        try {
            // Count total records
            long totalRecords = streamReader.countUnprocessedRecords(tenantId);
            result.setTotalRecords((int) totalRecords);

            if (totalRecords == 0) {
                log.info("No unprocessed records found for tenant={}", tenantId);
                result.setEndTime(System.currentTimeMillis());
                return result;
            }

            // Stream records and process in batches
            List<RevRecStageGroupingRecord> batch = new ArrayList<>();
            List<Long> processedIds = new ArrayList<>();
            List<Long> failedIds = new ArrayList<>();

            streamReader.streamUnprocessedRecords(tenantId)
                    .forEach(record -> {
                        try {
                            // Enrich record with grouping data
                            enrichRecordWithGrouping(record, batchId, groupingRule);
                            batch.add(record);

                            // Process batch when size reached
                            if (batch.size() >= config.getBatchSize()) {
                                int updated = batchUpdater.bulkUpdateGroupingData(batch);
                                processedIds.addAll(batch.stream()
                                        .map(RevRecStageGroupingRecord::getId)
                                        .collect(Collectors.toList()));
                                result.incrementUpdatedRecords(updated);
                                batch.clear();
                            }
                        } catch (Exception e) {
                            log.error("Error processing record id={}", record.getId(), e);
                            failedIds.add(record.getId());
                            result.incrementFailedRecords();
                        }
                    });

            // Process remaining records
            if (!batch.isEmpty()) {
                int updated = batchUpdater.bulkUpdateGroupingData(batch);
                processedIds.addAll(batch.stream()
                        .map(RevRecStageGroupingRecord::getId)
                        .collect(Collectors.toList()));
                result.incrementUpdatedRecords(updated);
            }

            // Mark processed records
            if (!processedIds.isEmpty()) {
                batchUpdater.markRecordsAsProcessed(processedIds, "Y");
            }

            // Mark failed records
            if (!failedIds.isEmpty()) {
                batchUpdater.markRecordsWithError(failedIds, "Processing failed - see logs");
            }

            result.setSuccess(true);
            log.info("Grouping processing completed: totalRecords={}, updated={}, failed={}, duration={}ms",
                    totalRecords, result.getUpdatedRecords(), result.getFailedRecords(),
                    result.getDuration());

        } catch (Exception e) {
            log.error("Grouping processing failed", e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } finally {
            result.setEndTime(System.currentTimeMillis());
        }

        return result;
    }

    /**
     * Process records with parallel streams for better performance
     */
    public GroupingProcessingResult processGroupingParallel(
            String tenantId,
            Long batchId,
            DynamicGroupingRule groupingRule) {

        log.info("Starting parallel grouping processing for tenant={}, batchId={}", tenantId, batchId);

        GroupingProcessingResult result = new GroupingProcessingResult();
        result.setStartTime(System.currentTimeMillis());

        try {
            List<RevRecStageGroupingRecord> records = streamReader.streamUnprocessedRecords(tenantId)
                    .collect(Collectors.toList());

            result.setTotalRecords(records.size());

            // Process in parallel
            records.parallelStream()
                    .forEach(record -> {
                        try {
                            enrichRecordWithGrouping(record, batchId, groupingRule);
                        } catch (Exception e) {
                            log.error("Error enriching record id={}", record.getId(), e);
                            result.incrementFailedRecords();
                        }
                    });

            // Batch update enriched records
            int batchSize = config.getBatchSize();
            for (int i = 0; i < records.size(); i += batchSize) {
                int end = Math.min(i + batchSize, records.size());
                List<RevRecStageGroupingRecord> batch = records.subList(i, end);
                int updated = batchUpdater.bulkUpdateGroupingData(batch);
                result.incrementUpdatedRecords(updated);
            }

            result.setSuccess(true);
            log.info("Parallel grouping processing completed: total={}, updated={}, failed={}",
                    records.size(), result.getUpdatedRecords(), result.getFailedRecords());

        } catch (Exception e) {
            log.error("Parallel grouping processing failed", e);
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
        } finally {
            result.setEndTime(System.currentTimeMillis());
        }

        return result;
    }

    private void enrichRecordWithGrouping(
            RevRecStageGroupingRecord record,
            Long batchId,
            DynamicGroupingRule groupingRule) {

        // Set batch ID
        record.setBatchId(batchId);

        // Resolve revenue contract ID
        if (record.getRevenueContractId() == null) {
            Long resolvedId = referenceService.resolveRevenueContractId(
                    record.getSalesOrderId(),
                    record.getInvoiceId(),
                    record.getOriginalInvoiceId(),
                    record.getOriginalSalesOrderId());
            if (resolvedId != null) {
                record.setRevenueContractId(resolvedId);
            }
        }

        // Apply grouping rule
        if (record.getRevenueContractGroupValue() == null) {
            String groupingValue = groupingRule.evaluateGrouping(record);
            record.setRevenueContractGroupValue(
                    groupingValue != null ? groupingValue : "GROUPING_DEFAULT_VALUE");
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
