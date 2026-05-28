package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import java.util.List;
import java.util.Optional;

/**
 * Strategy interface for different reference resolution approaches
 * Allows adding new lookup strategies without modifying existing code (Open/Closed Principle)
 */
public interface RevenueContractReferenceLookupStrategy {

    /**
     * Resolve revenue contract ID using this strategy
     */
    Optional<Long> resolve(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId);

    /**
     * Batch resolve for multiple records at once
     */
    void resolveBatch(List<RevRecStageGroupingRecord> records);

    /**
     * Get strategy name for logging/monitoring
     */
    String getStrategyName();

    /**
     * Check if this strategy can handle the given inputs
     */
    boolean canHandle(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId);
}
