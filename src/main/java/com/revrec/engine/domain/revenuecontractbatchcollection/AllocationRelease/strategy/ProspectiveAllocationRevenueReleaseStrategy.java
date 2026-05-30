package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.AllocationRevenueReleaseUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProspectiveAllocationRevenueReleaseStrategy implements AllocationRevenueReleaseStrategy {

    private final RevenueJournalEntriesService revenueJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;

    public ProspectiveAllocationRevenueReleaseStrategy(
            RevenueJournalEntriesService revenueJournalEntriesService,
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService) {
        this.revenueJournalEntriesService = revenueJournalEntriesService;
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

        for (Map.Entry<Long, List<RevenueJournalEntriesPerPeriod>> lineEntry :
                prospectiveRevenueJournalEntriesByLine.entrySet()) {
            Long revenueContractLineId = lineEntry.getKey();
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod = lineEntry.getValue();

            var allocationRevenueReleaseLineContext =
                    allocationRevenueReleaseUtilityService.buildAllocationRevenueReleaseLineContext(
                            revenueContractLineId,
                            revenueContractVersion,
                            revenueContractOrderRecords,
                            revenueJournalEntriesPerPeriod);
            if (allocationRevenueReleaseLineContext.isEmpty()) {
                continue;
            }

            AllocationRevenueReleaseLineContext lineContext = allocationRevenueReleaseLineContext.get();

            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords =
                    revenueJournalEntriesService.findByRevenueContractLineIdAndVersion(
                            revenueContractLineId, revenueContractId, revenueContractVersion);

            for (RevenueJournalEntriesRecord revenueJournalEntryRecord : revenueJournalEntryRecords) {
                BigDecimal perPeriodReleaseAmount =
                        allocationRevenueReleaseUtilityService.calculatePerPeriodReleaseAmount(
                                lineContext, revenueJournalEntryRecord, revenueJournalEntriesPerPeriod);
                // TODO: persist prospective allocation revenue release journal using lineContext,
                // revenueJournalEntryRecord, perPeriodReleaseAmount
            }
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
