package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;
import java.util.List;
import java.util.Optional;

public interface RevenueContractReferenceLookupStrategy {

    Optional<Long> resolve(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId);

    void resolveBatch(List<RevRecStageGroupingRecord> records);

    String getStrategyName();

    boolean canHandle(
            String salesOrderId,
            String invoiceId,
            String originalInvoiceId,
            String originalSalesOrderId);
}
