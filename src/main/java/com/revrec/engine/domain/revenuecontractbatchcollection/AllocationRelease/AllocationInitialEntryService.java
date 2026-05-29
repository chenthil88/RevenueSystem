package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class AllocationInitialEntryService {

    private final AllocationJournalEntriesService allocationJournalEntriesService;

    public AllocationInitialEntryService(AllocationJournalEntriesService allocationJournalEntriesService) {
        this.allocationJournalEntriesService = allocationJournalEntriesService;
    }

    public void createInitialEntry(
            List<RevenueContractOrderRecords> revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Objects.requireNonNull(revenueContractOrderRecords, "revenueContractOrderRecords");
        Objects.requireNonNull(revenueContractHeaderRecord, "revenueContractHeaderRecord");

        Long openPeriodId = revenueContractHeaderRecord.getCreatedPeriodId();
        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();

        List<AllocationJournalEntriesRecord> entriesToInsert = new ArrayList<>();
        for (RevenueContractOrderRecords orderRecords : revenueContractOrderRecords) {
            for (RevenueContractOrderDetailsRecord orderDetail : orderRecords.orderDetails()) {
                orderRecords.findAllocationDetailsFor(orderDetail)
                        .filter(AllocationInitialEntryService::hasNonZeroCarveAmount)
                        .map(allocation -> prepareEntry(allocation, openPeriodId, revenueContractVersion))
                        .ifPresent(entriesToInsert::add);
            }
        }

        if (!entriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(entriesToInsert);
        }
        revenueContractHeaderRecord.setIsAllocationInitialEntryCreated(true);
    }

    private AllocationJournalEntriesRecord prepareEntry(
            RevenueContractAllocationDetailsRecord allocation,
            Long openPeriodId,
            Long revenueContractVersion) {
        AllocationJournalEntriesRecord entry =
                allocationJournalEntriesService.prepareAllocationJournalEntry(allocation, openPeriodId);
        entry.setRevenueContractVersion(revenueContractVersion);
        return entry;
    }

    private boolean hasNonZeroCarveAmount(RevenueContractAllocationDetailsRecord allocation) {
        BigDecimal carveAmount = allocation.carveAmount();
        return carveAmount != null && carveAmount.compareTo(BigDecimal.ZERO) != 0;
    }
}
