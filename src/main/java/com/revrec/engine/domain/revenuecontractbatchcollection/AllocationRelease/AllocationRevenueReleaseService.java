package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Batch collection orchestration for allocation revenue release steps.
 *
 * <p>Delegates to retrospective or prospective {@link AllocationRevenueReleaseStrategy} implementations.
 */
@Service
public class AllocationRevenueReleaseService {

    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;
    private final List<AllocationRevenueReleaseStrategy> allocationRevenueReleaseStrategies;

    public AllocationRevenueReleaseService(
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService,
            List<AllocationRevenueReleaseStrategy> allocationRevenueReleaseStrategies) {
        this.allocationRevenueReleaseUtilityService = allocationRevenueReleaseUtilityService;
        this.allocationRevenueReleaseStrategies = allocationRevenueReleaseStrategies;
    }

    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        if (!revenueContractHeaderRecord.eligibleForRelease()) {
            return;
        }

        AllocationRevenueReleaseStrategy allocationRevenueReleaseStrategy =
                allocationRevenueReleaseUtilityService.resolveAllocationRevenueReleaseStrategy(
                        revenueContractHeaderRecord, allocationRevenueReleaseStrategies);
        allocationRevenueReleaseStrategy.processRelease(
                revenueContractOrderRecords, revenueContractHeaderRecord);
    }
}
