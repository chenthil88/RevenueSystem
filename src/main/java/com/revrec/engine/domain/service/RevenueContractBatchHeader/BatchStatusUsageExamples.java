package com.revrec.engine.domain.service.RevenueContractBatchHeader;

/**
 * Example usage patterns for BatchStatus enum
 * Shows common operations in batch processing
 */
public class BatchStatusUsageExamples {

    // Example 1: Update batch status
    public void updateBatchStatus(RevenueContractBatchHeaderRecord batch, BatchStatus newStatus) {
        // In a service:
        // batchHeaderService.updateStatus(batch.batchId(), newStatus);
    }

    // Example 2: Check batch state
    public void processBasedOnStatus(RevenueContractBatchHeader batch) {
        BatchStatus status = BatchStatus.fromCode(batch.status());

        if (status == BatchStatus.NEW) {
            startProcessing();
        } else if (status == BatchStatus.IN_PROGRESS) {
            resumeProcessing();
        } else if (status == BatchStatus.FAILED) {
            handleFailure();
        } else if (status == BatchStatus.WARNING) {
            handleWarnings();
        } else if (status == BatchStatus.CANCELED) {
            handleCancellation();
        } else if (status == BatchStatus.COLLECTED) {
            finalize();
        }
    }

    // Example 3: Use helper methods
    public void handleCompletion(RevenueContractBatchHeader batch) {
        BatchStatus status = BatchStatus.fromCode(batch.status());

        if (status.isTerminal()) {
            // Batch is done (FAILED or COLLECTED)
            cleanup();
        } else if (status.isProcessing()) {
            // Still processing
            wait();
        }
    }

    // Example 4: Check for errors
    public void validateBatchHealth(RevenueContractBatchHeader batch) {
        BatchStatus status = BatchStatus.fromCode(batch.status());

        if (status.hasIssues()) {
            // Failed or warning
            logAlert();
            notifyAdmins();
        }
    }

    // Example 5: In service layer
    public void transitionBatchStatus(Long batchId, BatchStatus from, BatchStatus to) {
        // Validate transition
        validateTransition(from, to);

        // Update in database
        // UPDATE revenueContractBatchHeader SET status = ? WHERE batchId = ? AND status = ?
        // updateBatchStatus(batchId, to.getCode(), from.getCode());
    }

    // Example 6: JPA Repository query
    // Before
    // List<RevenueContractBatchHeader> findByStatus(String status);

    // After (with enum)
    // List<RevenueContractBatchHeader> findByStatus(BatchStatus status);

    // Usage:
    // List<RevenueContractBatchHeader> failedBatches =
    //     repo.findByStatus(BatchStatus.FAILED);

    // Example 7: Database query
    // Before:
    // SELECT * FROM revenueContractBatchHeader WHERE status = 'F'

    // After (same SQL, typed Java):
    // @Query("SELECT b FROM RevenueContractBatchHeader b WHERE b.status = ?1")
    // List<RevenueContractBatchHeader> findFailed(BatchStatus status);
    //
    // Calling:
    // List<RevenueContractBatchHeader> failed = findFailed(BatchStatus.FAILED);

    // Example 8: Stream processing
    public void processBatches(Iterable<RevenueContractBatchHeader> batches) {
        batches.forEach(batch -> {
            BatchStatus status = BatchStatus.fromCode(batch.status());

            switch (status) {
                case NEW -> initializeBatch(batch);
                case IN_PROGRESS -> continueBatch(batch);
                case FAILED -> retryBatch(batch);
                case WARNING -> reviewBatch(batch);
                case CANCELED -> handleCancellation(batch);
                case COLLECTED -> archiveBatch(batch);
            }
        });
    }

    // Example 9: Status transition validation
    private void validateTransition(BatchStatus from, BatchStatus to) {
        // Define valid transitions
        boolean validTransition = switch (from) {
            case NEW -> to == BatchStatus.IN_PROGRESS || to == BatchStatus.CANCELED;
            case IN_PROGRESS -> to == BatchStatus.FAILED || to == BatchStatus.WARNING || to == BatchStatus.CANCELED || to == BatchStatus.COLLECTED;
            case FAILED -> to == BatchStatus.IN_PROGRESS || to == BatchStatus.CANCELED;  // retry or cancel
            case WARNING -> to == BatchStatus.COLLECTED || to == BatchStatus.FAILED || to == BatchStatus.CANCELED;
            case CANCELED -> false;  // terminal state
            case COLLECTED -> false;  // terminal state
        };

        if (!validTransition) {
            throw new IllegalStateException(
                    "Cannot transition from " + from + " to " + to);
        }
    }

    // Example 10: Batch status metrics
    public void collectMetrics(Iterable<RevenueContractBatchHeader> batches) {
        long newCount = 0;
        long inProgressCount = 0;
        long failedCount = 0;
        long warningCount = 0;
        long canceledCount = 0;
        long collectedCount = 0;

        for (RevenueContractBatchHeader batch : batches) {
            BatchStatus status = BatchStatus.fromCode(batch.status());

            switch (status) {
                case NEW -> newCount++;
                case IN_PROGRESS -> inProgressCount++;
                case FAILED -> failedCount++;
                case WARNING -> warningCount++;
                case CANCELED -> canceledCount++;
                case COLLECTED -> collectedCount++;
            }
        }

        // Report metrics
        reportMetrics(newCount, inProgressCount, failedCount, warningCount, canceledCount, collectedCount);
    }

    private void startProcessing() {}
    private void resumeProcessing() {}
    private void handleFailure() {}
    private void handleWarnings() {}
    private void handleCancellation() {}
    private void handleCancellation(RevenueContractBatchHeader batch) {}
    private void finalize() {}
    private void cleanup() {}
    private void wait() {}
    private void logAlert() {}
    private void notifyAdmins() {}
    private void initializeBatch(RevenueContractBatchHeader batch) {}
    private void continueBatch(RevenueContractBatchHeader batch) {}
    private void retryBatch(RevenueContractBatchHeader batch) {}
    private void reviewBatch(RevenueContractBatchHeader batch) {}
    private void archiveBatch(RevenueContractBatchHeader batch) {}
    private void reportMetrics(long n, long ip, long f, long w, long c, long collected) {}
}
