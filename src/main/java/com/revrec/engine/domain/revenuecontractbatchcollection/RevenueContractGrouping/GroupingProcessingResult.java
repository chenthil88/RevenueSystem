package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import lombok.Data;

/**
 * Result object for grouping processing operations
 */
@Data
public class GroupingProcessingResult {
    private boolean success;
    private int totalRecords;
    private int updatedRecords;
    private int failedRecords;
    private long startTime;
    private long endTime;
    private String errorMessage;

    public void incrementUpdatedRecords(int count) {
        this.updatedRecords += count;
    }

    public void incrementFailedRecords() {
        this.failedRecords++;
    }

    public long getDuration() {
        return endTime - startTime;
    }

    public double getSuccessRate() {
        if (totalRecords == 0) return 0.0;
        return ((double) updatedRecords / totalRecords) * 100;
    }
}
