package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import org.springframework.stereotype.Service;

/**
 * Batch collection orchestration for allocation release steps.
 *
 * <p>Input: {@link RevenueContractOrderRecords} for one revenue contract and a {@link RevenueContractHeaderRecord}.
 */
@Service
public class AllocationReleaseService {

    private final AllocationInitialEntryService allocationInitialEntryService;

    public AllocationReleaseService(AllocationInitialEntryService allocationInitialEntryService) {
        this.allocationInitialEntryService = allocationInitialEntryService;
    }

    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        if (revenueContractHeaderRecord.shouldCreateAllocationInitialEntry()) {
            allocationInitialEntryService.createInitialEntry(revenueContractOrderRecords, revenueContractHeaderRecord);
        }
        
    }
}

