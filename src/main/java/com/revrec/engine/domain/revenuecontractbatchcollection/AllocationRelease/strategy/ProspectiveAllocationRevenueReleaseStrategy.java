package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.AllocationRevenueReleaseUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProspectiveAllocationRevenueReleaseStrategy implements AllocationRevenueReleaseStrategy {

    private final RevenueJournalEntriesService revenueJournalEntriesService;
    private final AllocationJournalEntriesService allocationJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;

    public ProspectiveAllocationRevenueReleaseStrategy(
            RevenueJournalEntriesService revenueJournalEntriesService,
            AllocationJournalEntriesService allocationJournalEntriesService,
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService) {
        this.revenueJournalEntriesService = revenueJournalEntriesService;
        this.allocationJournalEntriesService = allocationJournalEntriesService;
        this.allocationRevenueReleaseUtilityService = allocationRevenueReleaseUtilityService;
    }

    @Override
    public boolean supports(RevenueContractHeaderRecord revenueContractHeaderRecord) {
        return !revenueContractHeaderRecord.isRetrospective();
    }

    @Override
    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Map<Long, List<RevenueJournalEntriesPerPeriod>> prospectiveRevenueJournalEntriesByLine =
                loadProspectiveJournalEntriesByLine(revenueContractOrderRecords, revenueContractHeaderRecord);

        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();
        Long revenueContractId = revenueContractHeaderRecord.getRevenueContractId();
        Long openAccountPeriodId =
                allocationRevenueReleaseUtilityService.getOpenAccountPeriodId(revenueContractHeaderRecord);

        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntriesToInsert = new ArrayList<>();

        for (Map.Entry<Long, List<RevenueJournalEntriesPerPeriod>> lineEntry :
                prospectiveRevenueJournalEntriesByLine.entrySet()) {
            Long revenueContractLineId = lineEntry.getKey();
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod = lineEntry.getValue();

            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext =
                    allocationRevenueReleaseUtilityService.buildAllocationRevenueReleaseLineContext(
                            revenueContractLineId,
                            revenueContractVersion,
                            revenueContractOrderRecords,
                            revenueJournalEntriesPerPeriod);
            if (allocationRevenueReleaseLineContext == null) {
                continue;
            }

            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords =
                    revenueJournalEntriesService.findByRevenueContractLineIdAndVersion(
                            revenueContractLineId, revenueContractId, revenueContractVersion);

            allocationReleaseJournalEntriesToInsert.addAll(
                    allocationRevenueReleaseUtilityService.buildProspectiveAllocationReleaseJournalEntries(
                            allocationRevenueReleaseLineContext,
                            revenueJournalEntryRecords,
                            revenueJournalEntriesPerPeriod,
                            openAccountPeriodId,
                            allocationJournalEntriesService));
        }

        if (!allocationReleaseJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationReleaseJournalEntriesToInsert);
        }
    }

    private Map<Long, List<RevenueJournalEntriesPerPeriod>> loadProspectiveJournalEntriesByLine(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Long accountPeriodId =
                allocationRevenueReleaseUtilityService.getOpenAccountPeriodId(revenueContractHeaderRecord);
        return revenueJournalEntriesService.getProspectiveJournalEntries(
                revenueContractHeaderRecord.getRevenueContractId(),
                accountPeriodId,
                revenueContractHeaderRecord.getVersion());
    }
}
