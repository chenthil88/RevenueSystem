package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.math.ChargebeeDecimal;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.context.RevenueContractBatchContextService;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderLineRecords;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AllocationInitialEntryService {

    private final AllocationJournalEntriesService allocationJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;
    private final RevenueContractBatchContextService revenueContractBatchContextService;

    public AllocationInitialEntryService(
            AllocationJournalEntriesService allocationJournalEntriesService,
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService,
            RevenueContractBatchContextService revenueContractBatchContextService) {
        this.allocationJournalEntriesService = allocationJournalEntriesService;
        this.allocationRevenueReleaseUtilityService = allocationRevenueReleaseUtilityService;
        this.revenueContractBatchContextService = revenueContractBatchContextService;
    }

    public void createInitialEntry(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Long openAccountPeriodId = revenueContractBatchContextService.getOpenAccountPeriodId();
        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();

        List<AllocationJournalEntriesRecord> allocationJournalEntriesToInsert = new ArrayList<>();
        for (RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord :
                revenueContractOrderRecords.getRevenueContractAllocationDetailsRecord().values()) {
            if (!hasNonZeroCarveAmount(revenueContractAllocationDetailsRecord)) {
                continue;
            }

            RevenueContractOrderLineRecords revenueContractOrderLineRecords =
                    revenueContractOrderRecords
                            .getLineRecords(revenueContractAllocationDetailsRecord.getId())
                            .orElse(null);
            if (revenueContractOrderLineRecords == null) {
                continue;
            }

            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext =
                    allocationRevenueReleaseUtilityService.buildAllocationRevenueReleaseLineContext(
                            revenueContractVersion,
                            revenueContractOrderLineRecords,
                            null);
            allocationRevenueReleaseLineContext.setInitialEntry(true);

            allocationJournalEntriesToInsert.add(
                    allocationJournalEntriesService.prepareAllocationJournalEntry(
                            revenueContractAllocationDetailsRecord,
                            revenueContractVersion,
                            openAccountPeriodId,
                            revenueContractAllocationDetailsRecord.getCarveAmount(),
                            null,
                            allocationRevenueReleaseLineContext));
        }

        if (!allocationJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationJournalEntriesToInsert);
        }
        revenueContractHeaderRecord.setIsAllocationInitialEntryCreated(true);
    }

    private boolean hasNonZeroCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        ChargebeeDecimal carveAmount = revenueContractAllocationDetailsRecord.getCarveAmount();
        return carveAmount != null && !carveAmount.isEqual(ChargebeeDecimal.ZERO);
    }
}
