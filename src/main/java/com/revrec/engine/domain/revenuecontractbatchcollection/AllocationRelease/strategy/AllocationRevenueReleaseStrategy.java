package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;

/**
 * Allocation revenue release by treatment type (retrospective vs prospective).
 */
public interface AllocationRevenueReleaseStrategy {

    boolean supports(RevenueContractHeaderRecord revenueContractHeaderRecord);

    void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord);
}
