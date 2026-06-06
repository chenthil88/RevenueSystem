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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RetrospectiveAllocationRevenueReleaseStrategy implements AllocationRevenueReleaseStrategy {

    private final RevenueJournalEntriesService revenueJournalEntriesService;
    private final AllocationJournalEntriesService allocationJournalEntriesService;
    private final AllocationRevenueReleaseUtilityService allocationRevenueReleaseUtilityService;
    private final RevenueContractBatchContextService revenueContractBatchContextService;

    public RetrospectiveAllocationRevenueReleaseStrategy(
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
        return revenueContractHeaderRecord.isRetrospective();
    }

    @Override
    public void processRelease(
            RevenueContractOrderRecords revenueContractOrderRecords,
            RevenueContractHeaderRecord revenueContractHeaderRecord) {
        Long revenueContractVersion = revenueContractHeaderRecord.getVersion();
        Long openAccountPeriodId = revenueContractBatchContextService.getOpenAccountPeriodId();
        List<Long> revenueContractLineIds =
                allocationRevenueReleaseUtilityService.getRevenueContractLineIds(revenueContractOrderRecords);

        Map<Long, List<RevenueJournalEntriesPerPeriod>> retrospectiveRevenueJournalEntriesByLine =
                revenueJournalEntriesService.getRetrospectiveJournalEntries(
                        revenueContractLineIds, openAccountPeriodId);
        Map<Long, List<RevenueJournalEntriesRecord>> revenueJournalEntryRecordsByLine =
                revenueJournalEntriesService.groupByRevenueContractLineId(
                        revenueJournalEntriesService.findByRevenueContractLineIds(revenueContractLineIds));

        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntriesToInsert = new ArrayList<>();

        for (Map.Entry<Long, List<RevenueJournalEntriesPerPeriod>> lineEntry :
                retrospectiveRevenueJournalEntriesByLine.entrySet()) {
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
     * Retrospective: {@code allocationPerPeriodRevenue = (perPeriodReleaseAmount / transactionPrice) * totalUnreleasedCarveAmount}.
     * Last period receives the remainder.
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
        BigDecimal transactionPrice = allocationRevenueReleaseUtilityService.nullToZero(
                allocationRevenueReleaseLineContext
                        .getRevenueContractAllocationDetailsRecord()
                        .getTransactionPrice());

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
                allocationPerPeriodRevenue = totalUnreleasedCarveAmount.subtract(sumAllocationPerPeriodRevenue);
            } else {
                allocationPerPeriodRevenue = calculateAllocationPerPeriodRevenue(
                        perPeriodReleaseAmount, transactionPrice, totalUnreleasedCarveAmount);
                sumAllocationPerPeriodRevenue =
                        sumAllocationPerPeriodRevenue.add(allocationPerPeriodRevenue);
            }

            allocationReleaseJournalEntries.add(allocationJournalEntriesService.prepareAllocationReleaseJournalEntry(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecord,
                    allocationPerPeriodRevenue,
                    openAccountPeriodId));
        }

        return allocationReleaseJournalEntries;
    }

    private static BigDecimal calculateAllocationPerPeriodRevenue(
            BigDecimal perPeriodReleaseAmount,
            BigDecimal transactionPrice,
            BigDecimal totalUnreleasedCarveAmount) {
        if (transactionPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal perPeriodRevenueReleasedPercentage = perPeriodReleaseAmount.divide(
                transactionPrice, 10, RoundingMode.HALF_UP);
        return perPeriodRevenueReleasedPercentage.multiply(totalUnreleasedCarveAmount);
    }
}
