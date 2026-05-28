package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RevenueContractReferenceService {

    private final RevenueContractReferenceLookupStrategy lookupStrategy;

    public RevenueContractReferenceService(RevenueContractReferenceLookupStrategy lookupStrategy) {
        this.lookupStrategy = lookupStrategy;
    }

    public void resolveRevenueContractIdsForBatch(List<RevRecStageGroupingRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        try {
            lookupStrategy.resolveBatch(records);
            log.debug(
                    "Batch resolution completed for {} records using strategy: {}",
                    records.size(),
                    lookupStrategy.getStrategyName());
        } catch (ReferenceResolutionException e) {
            log.error("Batch resolution failed: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during batch resolution", e);
            throw new ReferenceResolutionException("Batch resolution failed unexpectedly", e);
        }
    }

    public Optional<Long> resolveRevenueContractId(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId) {
        if (!lookupStrategy.canHandle(salesOrderId, invoiceId, originalInvoiceId, originalSalesOrderId)) {
            log.warn(
                    "Strategy cannot handle resolution request for IDs: so={}, inv={}, origInv={}, origSo={}",
                    salesOrderId,
                    invoiceId,
                    originalInvoiceId,
                    originalSalesOrderId);
            return Optional.empty();
        }
        try {
            return lookupStrategy.resolve(salesOrderId, invoiceId, originalInvoiceId, originalSalesOrderId);
        } catch (Exception e) {
            log.warn("Single record resolution failed for salesOrderId={}, invoiceId={}", salesOrderId, invoiceId, e);
            return Optional.empty();
        }
    }
}
