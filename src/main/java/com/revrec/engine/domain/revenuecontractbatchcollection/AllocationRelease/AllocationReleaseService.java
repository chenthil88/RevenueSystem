package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 * Batch collection orchestration for allocation release steps.
 *
 * <p>Input: list of {@link RevenueContractOrderRecords} and a {@link RevenueContractHeaderRecord}.
 */
@Service
public class AllocationReleaseService {

    private final AllocationInitialEntryService allocationInitialEntryService;

    public AllocationReleaseService(AllocationInitialEntryService allocationInitialEntryService) {
        this.allocationInitialEntryService = allocationInitialEntryService;
    }

    public void processRelease(
            List<RevenueContractOrderRecords> revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Objects.requireNonNull(revenueContractOrderRecords, "revenueContractOrderRecords");
        Objects.requireNonNull(revenueContractHeaderRecord, "revenueContractHeaderRecord");

        if (revenueContractHeaderRecord.shouldCreateAllocationInitialEntry()) {
            allocationInitialEntryService.createInitialEntry(revenueContractOrderRecords, revenueContractHeaderRecord);
        }
    }
}

