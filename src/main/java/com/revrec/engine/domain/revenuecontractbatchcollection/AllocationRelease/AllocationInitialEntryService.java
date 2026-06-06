package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.context.RevenueContractBatchContextService;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AllocationInitialEntryService {

    private final AllocationJournalEntriesService allocationJournalEntriesService;
    private final RevenueContractBatchContextService revenueContractBatchContextService;

    public AllocationInitialEntryService(
            AllocationJournalEntriesService allocationJournalEntriesService,
            RevenueContractBatchContextService revenueContractBatchContextService) {
        this.allocationJournalEntriesService = allocationJournalEntriesService;
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
            if (hasNonZeroCarveAmount(revenueContractAllocationDetailsRecord)) {
                allocationJournalEntriesToInsert.add(
                        allocationJournalEntriesService.prepareAllocationJournalEntry(
                                revenueContractAllocationDetailsRecord,
                                openAccountPeriodId,
                                revenueContractVersion));
            }
        }

        if (!allocationJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationJournalEntriesToInsert);
        }
        revenueContractHeaderRecord.setIsAllocationInitialEntryCreated(true);
    }

    private boolean hasNonZeroCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        BigDecimal carveAmount = revenueContractAllocationDetailsRecord.getCarveAmount();
        return carveAmount != null && carveAmount.compareTo(BigDecimal.ZERO) != 0;
    }
}
