package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
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

    public AllocationInitialEntryService(AllocationJournalEntriesService allocationJournalEntriesService) {
        this.allocationJournalEntriesService = allocationJournalEntriesService;
    }

    public void createInitialEntry(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        // TODO: Get openAccountPeriodId from batch context
        Long openAccountPeriodId = revenueContractHeaderRecord.getCreatedPeriodId();
        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();

        List<AllocationJournalEntriesRecord> allocationJournalEntriesToInsert = new ArrayList<>();
        for (RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord :
                revenueContractOrderRecords.getRevenueContractAllocationDetailsRecord().values()) {
            if (hasNonZeroCarveAmount(revenueContractAllocationDetailsRecord)) {
                allocationJournalEntriesToInsert.add(prepareAllocationJournalEntry(
                        revenueContractAllocationDetailsRecord, openAccountPeriodId, revenueContractVersion));
            }
        }

        if (!allocationJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationJournalEntriesToInsert);
        }
        revenueContractHeaderRecord.setIsAllocationInitialEntryCreated(true);
    }

    private AllocationJournalEntriesRecord prepareAllocationJournalEntry(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord,
            Long openAccountPeriodId,
            Long revenueContractVersion) {
        AllocationJournalEntriesRecord allocationJournalEntryRecord =
                allocationJournalEntriesService.prepareAllocationJournalEntry(
                        revenueContractAllocationDetailsRecord, openAccountPeriodId);
        allocationJournalEntryRecord.setRevenueContractVersion(revenueContractVersion);
        return allocationJournalEntryRecord;
    }

    private boolean hasNonZeroCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        BigDecimal carveAmount = revenueContractAllocationDetailsRecord.carveAmount();
        return carveAmount != null && carveAmount.compareTo(BigDecimal.ZERO) != 0;
    }
}
