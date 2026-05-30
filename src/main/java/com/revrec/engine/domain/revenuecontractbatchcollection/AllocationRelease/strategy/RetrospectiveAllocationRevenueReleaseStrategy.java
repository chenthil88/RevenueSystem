package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.AllocationRevenueReleaseUtilityService;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RetrospectiveAllocationRevenueReleaseStrategy implements AllocationRevenueReleaseStrategy {

    private final RevenueJournalEntriesService revenueJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;

    public RetrospectiveAllocationRevenueReleaseStrategy(
            RevenueJournalEntriesService revenueJournalEntriesService,
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService) {
        this.revenueJournalEntriesService = revenueJournalEntriesService;
        this.allocationRevenueReleaseUtilityService = allocationRevenueReleaseUtilityService;
    }

    @Override
    public boolean supports(RevenueContractHeaderRecord revenueContractHeaderRecord) {
        return revenueContractHeaderRecord.isRetrospective();
    }

    @Override
    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Map<Long, List<RevenueJournalEntriesPerPeriod>> retrospectiveRevenueJournalEntriesByLine =
                loadRetrospectiveJournalEntriesByLine(revenueContractOrderRecords, revenueContractHeaderRecord);
        // TODO: apply retrospective revenue release using retrospectiveRevenueJournalEntriesByLine
    }

    private Map<Long, List<RevenueJournalEntriesPerPeriod>> loadRetrospectiveJournalEntriesByLine(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        List<Long> revenueContractLineIds =
                allocationRevenueReleaseUtilityService.getRevenueContractLineIds(revenueContractOrderRecords);
        Long openAccountPeriodId =
                allocationRevenueReleaseUtilityService.getOpenAccountPeriodId(revenueContractHeaderRecord);
        return revenueJournalEntriesService.getRetrospectiveJournalEntries(
                revenueContractLineIds, openAccountPeriodId);
    }
}
