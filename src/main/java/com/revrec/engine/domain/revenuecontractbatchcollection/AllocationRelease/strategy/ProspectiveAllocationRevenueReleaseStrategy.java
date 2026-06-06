package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.AllocationRevenueReleaseUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationReleasePeriodLoop;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.context.RevenueContractBatchContextService;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderLineRecords;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProspectiveAllocationRevenueReleaseStrategy implements AllocationRevenueReleaseStrategy {

    private final RevenueJournalEntriesService revenueJournalEntriesService;
    private final AllocationJournalEntriesService allocationJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;
    private final RevenueContractBatchContextService revenueContractBatchContextService;

    public ProspectiveAllocationRevenueReleaseStrategy(
            RevenueJournalEntriesService revenueJournalEntriesService,
            AllocationJournalEntriesService allocationJournalEntriesService,
            AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService,
            RevenueContractBatchContextService revenueContractBatchContextService) {
        this.revenueJournalEntriesService = revenueJournalEntriesService;
        this.allocationJournalEntriesService = allocationJournalEntriesService;
        this.allocationRevenueReleaseUtilityService = allocationRevenueReleaseUtilityService;
        this.revenueContractBatchContextService = revenueContractBatchContextService;
    }

    @Override
    public boolean supports(RevenueContractHeaderRecord revenueContractHeaderRecord) {
        return !revenueContractHeaderRecord.isRetrospective();
    }

    @Override
    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();
        Long revenueContractId = revenueContractHeaderRecord.getRevenueContractId();
        Long openAccountPeriodId = revenueContractBatchContextService.getOpenAccountPeriodId();

        Map<Long, List<RevenueJournalEntriesPerPeriod>> prospectiveRevenueJournalEntriesByLine =
                revenueJournalEntriesService.getProspectiveJournalEntries(
                        revenueContractId, openAccountPeriodId, revenueContractVersion);
        Map<Long, List<RevenueJournalEntriesRecord>> revenueJournalEntryRecordsByLine =
                revenueJournalEntriesService.groupByRevenueContractLineId(
                        revenueJournalEntriesService.findByRevenueContractIdAndVersion(
                                revenueContractId, revenueContractVersion));

        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntriesToInsert = new ArrayList<>();

        for (Map.Entry<Long, List<RevenueJournalEntriesPerPeriod>> lineEntry :
                prospectiveRevenueJournalEntriesByLine.entrySet()) {
            Long revenueContractLineId = lineEntry.getKey();
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod = lineEntry.getValue();
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords =
                    revenueJournalEntryRecordsByLine.getOrDefault(revenueContractLineId, List.of());

            RevenueContractOrderLineRecords revenueContractOrderLineRecords =
                    revenueContractOrderRecords.getLineRecords(revenueContractLineId).orElse(null);
            if (revenueContractOrderLineRecords == null) {
                continue;
            }

            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext =
                    allocationRevenueReleaseUtilityService.buildAllocationRevenueReleaseLineContext(
                            revenueContractVersion,
                            revenueContractOrderLineRecords,
                            revenueJournalEntriesPerPeriod);

            allocationReleaseJournalEntriesToInsert.addAll(buildAllocationReleaseJournalEntries(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecords,
                    revenueJournalEntriesPerPeriod,
                    openAccountPeriodId));
        }

        if (!allocationReleaseJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationReleaseJournalEntriesToInsert);
        }
    }

    /**
     * Prospective release uses a different percentage formula than retrospective.
     * Last period receives the remainder only when non-last periods produced amounts.
     */
    private List<AllocationJournalEntriesRecord> buildAllocationReleaseJournalEntries(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod,
            Long openAccountPeriodId) {
        AllocationReleasePeriodLoop allocationReleasePeriodLoop = allocationRevenueReleaseUtilityService
                .prepareAllocationReleasePeriodLoop(revenueJournalEntryRecords)
                .orElse(null);
        if (allocationReleasePeriodLoop == null) {
            return List.of();
        }

        BigDecimal totalUnreleasedCarveAmount = allocationRevenueReleaseLineContext.getTotalUnreleasedCarveAmount();
        BigDecimal sumAllocationPerPeriodRevenue = BigDecimal.ZERO;
        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntries = new ArrayList<>();

        for (Long accountPeriodId : allocationReleasePeriodLoop.getSortedAccountPeriodIds()) {
            RevenueJournalEntriesRecord revenueJournalEntryRecord =
                    allocationReleasePeriodLoop.getRevenueJournalEntryForPeriod(accountPeriodId);
            BigDecimal perPeriodReleaseAmount = allocationRevenueReleaseUtilityService.calculatePerPeriodReleaseAmount(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecord,
                    revenueJournalEntriesPerPeriod);

            BigDecimal allocationPerPeriodRevenue;
            if (accountPeriodId.equals(allocationReleasePeriodLoop.getLastAccountPeriodId())) {
                if (sumAllocationPerPeriodRevenue.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                allocationPerPeriodRevenue = totalUnreleasedCarveAmount.subtract(sumAllocationPerPeriodRevenue);
            } else {
                BigDecimal perPeriodRevenueReleasedPercentage = calculatePerPeriodRevenueReleasedPercentage(
                        perPeriodReleaseAmount,
                        allocationRevenueReleaseLineContext,
                        revenueJournalEntryRecord,
                        revenueJournalEntriesPerPeriod);
                allocationPerPeriodRevenue = calculateAllocationPerPeriodRevenue(
                        perPeriodRevenueReleasedPercentage,
                        totalUnreleasedCarveAmount,
                        allocationRevenueReleaseLineContext,
                        revenueJournalEntryRecord);
                sumAllocationPerPeriodRevenue =
                        sumAllocationPerPeriodRevenue.add(allocationPerPeriodRevenue);
            }

            if (allocationPerPeriodRevenue.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            allocationReleaseJournalEntries.add(allocationJournalEntriesService.prepareAllocationReleaseJournalEntry(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecord,
                    allocationPerPeriodRevenue,
                    openAccountPeriodId));
        }

        return allocationReleaseJournalEntries;
    }

    /**
     * TODO: Prospective {@code perPeriodRevenueReleasedPercentage} (not {@code perPeriodReleaseAmount / transactionPrice}).
     */
    private BigDecimal calculatePerPeriodRevenueReleasedPercentage(
            BigDecimal perPeriodReleaseAmount,
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        return BigDecimal.ZERO;
    }

    /**
     * TODO: Prospective {@code allocationPerPeriodRevenue} from {@code perPeriodRevenueReleasedPercentage}.
     */
    private BigDecimal calculateAllocationPerPeriodRevenue(
            BigDecimal perPeriodRevenueReleasedPercentage,
            BigDecimal totalUnreleasedCarveAmount,
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord) {
        return BigDecimal.ZERO;
    }
}
