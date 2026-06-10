package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy;

import com.revrec.engine.common.math.ChargebeeDecimal;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.common.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.AllocationRevenueReleaseUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.context.RevenueContractBatchContextService;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderLineRecords;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.RoundingMode;
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

        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntriesToInsert = new ArrayList<>();

        for (Map.Entry<Long, List<RevenueJournalEntriesPerPeriod>> lineEntry :
                prospectiveRevenueJournalEntriesByLine.entrySet()) {
            Long revenueContractLineId = lineEntry.getKey();
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod = lineEntry.getValue();

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
                    revenueContractOrderLineRecords.getRevenueContractAllocationDetailsRecord(),
                    openAccountPeriodId));
        }

        if (!allocationReleaseJournalEntriesToInsert.isEmpty()) {
            allocationJournalEntriesService.insertAll(allocationReleaseJournalEntriesToInsert);
        }
    }

    /**
     * Prospective: {@code normalizedPct = perPeriodRevenueReleasedPercentage / (1 - postedPercentage / 100)},
     * capped at {@code 1}, then {@code allocationPerPeriodRevenue = normalizedPct * totalUnreleasedCarveAmount}.
     * Last period receives the remainder.
     */
    private List<AllocationJournalEntriesRecord> buildAllocationReleaseJournalEntries(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord,
            Long openAccountPeriodId) {
        List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod =
                allocationRevenueReleaseLineContext.getRevenueJournalEntriesPerPeriod();
        if (revenueJournalEntriesPerPeriod.isEmpty()) {
            return List.of();
        }

        Long lastAccountPeriodId = revenueJournalEntriesPerPeriod
                .get(revenueJournalEntriesPerPeriod.size() - 1)
                .periodId();

        ChargebeeDecimal sumAllocationPerPeriodRevenue = ChargebeeDecimal.ZERO;
        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntries = new ArrayList<>();

        for (RevenueJournalEntriesPerPeriod revenueJournalEntryPerPeriod : revenueJournalEntriesPerPeriod) {
            Long accountPeriodId = revenueJournalEntryPerPeriod.periodId();
            ChargebeeDecimal perPeriodReleaseAmount = ChargebeeDecimal.nullToZero(
                    revenueJournalEntryPerPeriod.amount()).abs();

            ChargebeeDecimal allocationPerPeriodRevenue;
            if (accountPeriodId.equals(lastAccountPeriodId)) {
                allocationPerPeriodRevenue = allocationRevenueReleaseLineContext
                        .getTotalUnreleasedCarveAmount()
                        .subtract(sumAllocationPerPeriodRevenue);
            } else {
                allocationPerPeriodRevenue = calculateAllocationPerPeriodRevenue(
                        allocationRevenueReleaseLineContext, perPeriodReleaseAmount);
                sumAllocationPerPeriodRevenue =
                        sumAllocationPerPeriodRevenue.add(allocationPerPeriodRevenue);
            }

            allocationReleaseJournalEntries.add(allocationJournalEntriesService.prepareAllocationJournalEntry(
                    revenueContractAllocationDetailsRecord,
                    allocationRevenueReleaseLineContext.getRevenueContractVersion(),
                    openAccountPeriodId,
                    allocationPerPeriodRevenue,
                    accountPeriodId,
                    allocationRevenueReleaseLineContext));
        }

        return allocationReleaseJournalEntries;
    }

    private ChargebeeDecimal calculateAllocationPerPeriodRevenue(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            ChargebeeDecimal perPeriodReleaseAmount) {
        ChargebeeDecimal transactionPrice = ChargebeeDecimal.nullToZero(
                allocationRevenueReleaseLineContext.getTransactionPrice().abs());
        if (transactionPrice.isEqual(ChargebeeDecimal.ZERO)) {
            return perPeriodReleaseAmount.isGreaterThan(ChargebeeDecimal.ONE) ? ChargebeeDecimal.ZERO : perPeriodReleaseAmount;
        }
        int roundingPrecision = allocationRevenueReleaseLineContext.getRoundingPrecision();
        ChargebeeDecimal perPeriodRevenueReleasedPercentage = perPeriodReleaseAmount.divide(
                transactionPrice, roundingPrecision, RoundingMode.HALF_UP);

        ChargebeeDecimal postedPercentageDecimal = ChargebeeDecimal.nullToZero(
                allocationRevenueReleaseLineContext.getPostedPercentage())
                .divide(ChargebeeDecimal.HUNDRED, roundingPrecision, RoundingMode.HALF_UP);

        ChargebeeDecimal postedPercentageDivisor = ChargebeeDecimal.ONE.subtract(postedPercentageDecimal);

        ChargebeeDecimal normalizedPerPeriodRevenueReleasedPercentage;
        if (postedPercentageDivisor.isEqual(ChargebeeDecimal.ZERO)) {
            normalizedPerPeriodRevenueReleasedPercentage = ChargebeeDecimal.ZERO;
        } else {
            normalizedPerPeriodRevenueReleasedPercentage = perPeriodRevenueReleasedPercentage.divide(
                    postedPercentageDivisor, roundingPrecision, RoundingMode.HALF_UP);
            if (normalizedPerPeriodRevenueReleasedPercentage.isGreaterThan(ChargebeeDecimal.ONE)) {
                normalizedPerPeriodRevenueReleasedPercentage = ChargebeeDecimal.ONE;
            }
        }

        return normalizedPerPeriodRevenueReleasedPercentage.multiply(
                allocationRevenueReleaseLineContext.getTotalUnreleasedCarveAmount());
    }
}
