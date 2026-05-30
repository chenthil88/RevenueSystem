package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Shared helpers for allocation revenue release (line ids, period resolution, strategy selection).
 */
@Service
public class AllocationRevenueReleaseUtilityService {

    /**
     * Revenue contract line ids from indexed order details ({@code id} key).
     */
    public List<Long> getRevenueContractLineIds(RevenueContractOrderRecords revenueContractOrderRecords) {
        return new ArrayList<>(revenueContractOrderRecords.getRevenueContractOrderDetailsRecord().keySet());
    }

    /**
     * Open / account period used for journal aggregation.
     */
    public Long getOpenAccountPeriodId(RevenueContractHeaderRecord revenueContractHeaderRecord) {
        // TODO: resolve from batch context (CurrentOpenPeriod)
        return revenueContractHeaderRecord.getCreatedPeriodId();
    }

    public AllocationRevenueReleaseStrategy resolveAllocationRevenueReleaseStrategy(
            RevenueContractHeaderRecord revenueContractHeaderRecord,
            List<AllocationRevenueReleaseStrategy> allocationRevenueReleaseStrategies) {
        return allocationRevenueReleaseStrategies.stream()
                .filter(allocationRevenueReleaseStrategy ->
                        allocationRevenueReleaseStrategy.supports(revenueContractHeaderRecord))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No allocation revenue release strategy for allocationTreatment="
                                + revenueContractHeaderRecord.getAllocationTreatment()));
    }
}
