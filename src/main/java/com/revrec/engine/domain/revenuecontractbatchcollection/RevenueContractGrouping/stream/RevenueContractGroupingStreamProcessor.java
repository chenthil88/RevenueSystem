package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.stream;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.config.BatchProcessingConfig;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.GroupingProcessingResult;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.RevenueContractReferenceService;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule.DynamicGroupingRule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public GroupingProcessingResult processGroupingForBatch(
            String tenantId, Long batchId, String groupingRuleExpr, DynamicGroupingRule groupingRule) {

        log.info("Starting grouping processing for tenant={}, batchId={}", tenantId, batchId);
        GroupingProcessingResult result = new GroupingProcessingResult();
        result.setStartTime(System.currentTimeMillis());

        try {
            long totalRecords = streamReader.countUnprocessedRecords(tenantId);
            result.setTotalRecords((int) totalRecords);
            if (totalRecords == 0) {
                log.info("No unprocessed records found for tenant={}", tenantId);
                result.setEndTime(System.currentTimeMillis());
                return result;
            }

            List<RevRecStageGroupingRecord> batch = new ArrayList<>();
            List<Long> processedIds = new ArrayList<>();
            List<Long> failedIds = new ArrayList<>();

            streamReader.streamUnprocessedRecords(tenantId).forEach(record -> {
                try {
                    enrichRecordWithGrouping(record, batchId, groupingRule);
                    batch.add(record);
                    if (batch.size() >= config.getBatchSize()) {
                        referenceService.resolveRevenueContractIdsForBatch(batch);
                        int updated = batchUpdater.bulkUpdateGroupingData(batch);
                        processedIds.addAll(
                                batch.stream().map(RevRecStageGroupingRecord::getId).collect(Collectors.toList()));
                        result.incrementUpdatedRecords(updated);
                        batch.clear();
                    }
                } catch (Exception e) {
                    log.error("Error processing record id={}", record.getId(), e);
                    failedIds.add(record.getId());
                    result.incrementFailedRecords();
                }
            });

            if (!batch.isEmpty()) {
                referenceService.resolveRevenueContractIdsForBatch(batch);
                int updated = batchUpdater.bulkUpdateGroupingData(batch);
                processedIds.addAll(
                        batch.stream().map(RevRecStageGroupingRecord::getId).collect(Collectors.toList()));
                result.incrementUpdatedRecords(updated);
            }

            if (!processedIds.isEmpty()) {
                batchUpdater.markRecordsAsProcessed(
                        processedIds, RevenueContractGroupingConstants.PROCESSED_FLAG_YES);
            }
            if (!failedIds.isEmpty()) {
                batchUpdater.markRecordsWithError(failedIds, "Processing failed - see logs");
            }

            result.setSuccess(true);
            log.info(
                    "Grouping processing completed: totalRecords={}, updated={}, failed={}, duration={}ms",
                    totalRecords,
                    result.getUpdatedRecords(),
                    result.getFailedRecords(),
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

    public GroupingProcessingResult processGroupingParallel(
            String tenantId, Long batchId, DynamicGroupingRule groupingRule) {

        log.info("Starting parallel grouping processing for tenant={}, batchId={}", tenantId, batchId);
        GroupingProcessingResult result = new GroupingProcessingResult();
        result.setStartTime(System.currentTimeMillis());

        try {
            List<RevRecStageGroupingRecord> allBatches = new ArrayList<>();
            int batchSize = config.getBatchSize();

            streamReader.streamUnprocessedRecords(tenantId).forEach(record -> {
                try {
                    enrichRecordWithGrouping(record, batchId, groupingRule);
                    allBatches.add(record);
                    if (allBatches.size() >= batchSize) {
                        referenceService.resolveRevenueContractIdsForBatch(allBatches);
                        batchUpdater.bulkUpdateGroupingData(allBatches);
                        result.incrementUpdatedRecords(allBatches.size());
                        allBatches.clear();
                    }
                } catch (Exception e) {
                    log.error("Error enriching record id={}", record.getId(), e);
                    result.incrementFailedRecords();
                }
            });

            if (!allBatches.isEmpty()) {
                referenceService.resolveRevenueContractIdsForBatch(allBatches);
                batchUpdater.bulkUpdateGroupingData(allBatches);
                result.incrementUpdatedRecords(allBatches.size());
            }

            result.setSuccess(true);
            log.info(
                    "Parallel grouping processing completed: total={}, updated={}, failed={}",
                    result.getTotalRecords(),
                    result.getUpdatedRecords(),
                    result.getFailedRecords());
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
            RevRecStageGroupingRecord record, Long batchId, DynamicGroupingRule groupingRule) {
        record.setBatchId(batchId);
        if (record.getRevenueContractGroupValue() == null) {
            String groupingValue = groupingRule.evaluateGrouping(record);
            record.setRevenueContractGroupValue(
                    groupingValue != null
                            ? groupingValue
                            : RevenueContractGroupingConstants.DEFAULT_GROUPING_VALUE);
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(
                    RevenueContractGroupingConstants.EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
