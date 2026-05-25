package com.revrec.engine.domain.service.RevenueContractBatchHeader;

/**
 * Status enum for revenue contract batch processing lifecycle
 * Represents stages of batch processing from creation to collection
 */
public enum BatchStatus {
    NEW("N", "New batch, not started"),
    IN_PROGRESS("IP", "Batch processing in progress"),
    FAILED("F", "Batch processing failed"),
    WARNING("W", "Batch completed with warnings"),
    CANCELED("X", "Batch processing canceled"),
    COLLECTED("C", "Batch processing collected and completed");

    private final String code;
    private final String description;

    BatchStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Convert status code to enum
     */
    public static BatchStatus fromCode(String code) {
        if (code == null || code.isBlank()) {
            return NEW;
        }
        for (BatchStatus status : BatchStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown batch status code: " + code);
    }

    /**
     * Check if batch is in terminal state
     */
    public boolean isTerminal() {
        return this == FAILED || this == CANCELED || this == COLLECTED;
    }

    /**
     * Check if batch is processing
     */
    public boolean isProcessing() {
        return this == IN_PROGRESS;
    }

    /**
     * Check if batch has errors/warnings
     */
    public boolean hasIssues() {
        return this == FAILED || this == WARNING;
    }
}
