package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.common.math.ChargebeeDecimal;
import com.revrec.engine.common.metadataservice.JournalAccount.DerivedJournalAccountValue;
import com.revrec.engine.common.metadataservice.JournalAccount.JournalAccountService;
import com.revrec.engine.common.metadataservice.JournalAccount.JournalAccountType;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationScheduleByRcId;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderLineRecords;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Shared helpers for allocation revenue release (line ids, line context, period loop prep, strategy selection).
 */
@Service
public class AllocationRevenueReleaseUtilityService {

    private final JournalAccountService journalAccountService;
    private final AllocationJournalEntriesService allocationJournalEntriesService;

    public AllocationRevenueReleaseUtilityService(
            JournalAccountService journalAccountService,
            AllocationJournalEntriesService allocationJournalEntriesService) {
        this.journalAccountService = journalAccountService;
        this.allocationJournalEntriesService = allocationJournalEntriesService;
    }

    public List<Long> getRevenueContractLineIds(RevenueContractOrderRecords revenueContractOrderRecords) {
        return new ArrayList<>(revenueContractOrderRecords.getRevenueContractOrderDetailsRecord().keySet());
    }

    public AllocationRevenueReleaseStrategy resolveAllocationRevenueReleaseStrategy(
            RevenueContractHeaderRecord revenueContractHeaderRecord,
            List<AllocationRevenueReleaseStrategy> allocationRevenueReleaseStrategies) {
        return allocationRevenueReleaseStrategies.stream()
                .filter(allocationRevenueReleaseStrategy ->
                        allocationRevenueReleaseStrategy.supports(revenueContractHeaderRecord))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No allocation revenue release strategy for allocationTreatment="
                                + revenueContractHeaderRecord.getAllocationTreatment()));
    }

    /**
     * Derives cumulative allocation totals from persisted allocation journal entries and updates
     * each {@link RevenueContractAllocationDetailsRecord} in memory.
     */
    public void updateCumulativeAllocationFields(
            RevenueContractHeaderRecord revenueContractHeaderRecord,
            Collection<RevenueContractAllocationDetailsRecord> revenueContractAllocationDetailsRecords) {
        Objects.requireNonNull(revenueContractHeaderRecord, "revenueContractHeaderRecord");
        if (revenueContractAllocationDetailsRecords == null || revenueContractAllocationDetailsRecords.isEmpty()) {
            return;
        }

        Map<Long, List<AllocationScheduleByRcId>> allocationScheduleByLine =
                allocationJournalEntriesService.getAllocationScheduleByRcId(revenueContractHeaderRecord);

        for (RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord :
                revenueContractAllocationDetailsRecords) {
            List<AllocationScheduleByRcId> allocationScheduleByRcId =
                    allocationScheduleByLine.getOrDefault(
                            revenueContractAllocationDetailsRecord.getId(), List.of());
            applyCumulativeAllocationFields(revenueContractAllocationDetailsRecord, allocationScheduleByRcId);
        }
    }

    public ChargebeeDecimal calculateTotalUnreleasedCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        ChargebeeDecimal unreleasedCarveAmount = ChargebeeDecimal.nullToZero(
                revenueContractAllocationDetailsRecord.getUnreleasedCarveAmount());
        ChargebeeDecimal carveAmount = ChargebeeDecimal.nullToZero(
                revenueContractAllocationDetailsRecord.getCarveAmount());
        return unreleasedCarveAmount.add(carveAmount);
    }

    public DerivedJournalAccountValue deriveDebitAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                JournalAccountType.ALLOCATION_LIABILITY, revenueContractOrderAccountDetailsRecord);
    }

    public DerivedJournalAccountValue deriveCreditAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                JournalAccountType.ALLOCATION_REVENUE, revenueContractOrderAccountDetailsRecord);
    }

    public AllocationRevenueReleaseLineContext buildAllocationRevenueReleaseLineContext(
            Long revenueContractVersion,
            RevenueContractOrderLineRecords revenueContractOrderLineRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord =
                revenueContractOrderLineRecords.getRevenueContractAllocationDetailsRecord();
        RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord =
                revenueContractOrderLineRecords.getRevenueContractOrderAccountDetailsRecord();

        AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext =
                new AllocationRevenueReleaseLineContext();
        allocationRevenueReleaseLineContext.setRevenueContractLineId(revenueContractAllocationDetailsRecord.getId());
        allocationRevenueReleaseLineContext.setRevenueContractVersion(revenueContractVersion);
        allocationRevenueReleaseLineContext.setTransactionPrice(
                ChargebeeDecimal.nullToZero(revenueContractAllocationDetailsRecord.getTransactionPrice()));
        allocationRevenueReleaseLineContext.setTotalUnreleasedCarveAmount(
                calculateTotalUnreleasedCarveAmount(revenueContractAllocationDetailsRecord));
        allocationRevenueReleaseLineContext.setDebitAccountSegments(
                deriveDebitAccountSegments(revenueContractOrderAccountDetailsRecord));
        allocationRevenueReleaseLineContext.setCreditAccountSegments(
                deriveCreditAccountSegments(revenueContractOrderAccountDetailsRecord));
        allocationRevenueReleaseLineContext.setRevenueJournalEntriesPerPeriod(revenueJournalEntriesPerPeriod);
        allocationRevenueReleaseLineContext.setAllocationCurrency(
                revenueContractAllocationDetailsRecord.getAllocationCurrency());
        allocationRevenueReleaseLineContext.setRoundingPrecision(
                resolveRoundingPrecision(revenueContractAllocationDetailsRecord.getAllocationCurrency()));
        allocationRevenueReleaseLineContext.setPostedPercentage(
                ChargebeeDecimal.nullToZero(revenueContractAllocationDetailsRecord.getPostedPercentage()));
        return allocationRevenueReleaseLineContext;
    }

     
     //TODO: Resolve decimal precision from org currency setup using 
    private int resolveRoundingPrecision(String allocationCurrency) {
        return 2;
    }

    public Map<Long, RevenueJournalEntriesRecord> indexRevenueJournalEntriesByAccountPeriodId(
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords) {
        Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId = new LinkedHashMap<>();
        if (revenueJournalEntryRecords == null) {
            return revenueJournalEntryByPeriodId;
        }
        for (RevenueJournalEntriesRecord revenueJournalEntryRecord : revenueJournalEntryRecords) {
            Long accountPeriodId = revenueJournalEntryRecord.getAccountPeriodId();
            if (accountPeriodId != null) {
                revenueJournalEntryByPeriodId.put(accountPeriodId, revenueJournalEntryRecord);
            }
        }
        return revenueJournalEntryByPeriodId;
    }

    public List<Long> sortedAccountPeriodIds(
            Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId) {
        return revenueJournalEntryByPeriodId.keySet().stream().sorted().toList();
    }

    /**
     * Per-period release amount from aggregated journal data and the revenue journal entry row.
     */
    public ChargebeeDecimal calculatePerPeriodReleaseAmount(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        Long effectivePeriodId = resolveEffectivePeriodId(
                revenueJournalEntryRecord.getAccountPeriodId(),
                allocationRevenueReleaseLineContext.getRevenueJournalEntriesPerPeriod());

        return findAggregatedAmountForPeriod(revenueJournalEntriesPerPeriod, effectivePeriodId)
                .orElse(ChargebeeDecimal.nullToZero(revenueJournalEntryRecord.getAmount()));
    }

    private DerivedJournalAccountValue deriveAccountSegments(
            JournalAccountType journalAccountType, AccountDetailsRecord accountDetailsRecord) {
        if (accountDetailsRecord == null) {
            return new DerivedJournalAccountValue(
                    journalAccountType.accountName(), null, null, null, null, null, null, null, null, null, null);
        }
        return journalAccountService.deriveJournalAccountValue(journalAccountType, accountDetailsRecord);
    }

    private static Long resolveEffectivePeriodId(
            Long accountPeriodId, List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        if (accountPeriodId == null) {
            return null;
        }
        return revenueJournalEntriesPerPeriod.stream()
                .map(RevenueJournalEntriesPerPeriod::periodId)
                .filter(periodId -> periodId != null && periodId.equals(accountPeriodId))
                .findFirst()
                .orElse(accountPeriodId);
    }

    private static Optional<ChargebeeDecimal> findAggregatedAmountForPeriod(
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod, Long periodId) {
        if (periodId == null) {
            return Optional.empty();
        }
        return revenueJournalEntriesPerPeriod.stream()
                .filter(entry -> periodId.equals(entry.periodId()))
                .map(RevenueJournalEntriesPerPeriod::amount)
                .findFirst();
    }

    private static void applyCumulativeAllocationFields(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord,
            List<AllocationScheduleByRcId> allocationScheduleByRcId) {
        ChargebeeDecimal cumulativeCarveAmount = ChargebeeDecimal.ZERO;
        ChargebeeDecimal cumulativeReleasedAmount = ChargebeeDecimal.ZERO;

        for (AllocationScheduleByRcId schedule : allocationScheduleByRcId) {
            ChargebeeDecimal amount = ChargebeeDecimal.nullToZero(schedule.amount()).abs();
            if (Boolean.TRUE.equals(schedule.isInitialEntry())) {
                cumulativeCarveAmount = cumulativeCarveAmount.add(amount);
            } else {
                cumulativeReleasedAmount = cumulativeReleasedAmount.add(amount);
            }
        }

        ChargebeeDecimal cumulativeUnReleasedAmount = cumulativeCarveAmount.subtract(cumulativeReleasedAmount);
        ChargebeeDecimal cumulativeAllocatedPrice = ChargebeeDecimal.nullToZero(
                        revenueContractAllocationDetailsRecord.getTransactionPrice())
                .add(cumulativeCarveAmount);

        revenueContractAllocationDetailsRecord.setCumulativeCarveAmount(cumulativeCarveAmount);
        revenueContractAllocationDetailsRecord.setCumulativeReleasedAmount(cumulativeReleasedAmount);
        revenueContractAllocationDetailsRecord.setCumulativeUnReleasedAmount(cumulativeUnReleasedAmount);
        revenueContractAllocationDetailsRecord.setCumulativeAllocatedPrice(cumulativeAllocatedPrice);
        revenueContractAllocationDetailsRecord.setUnreleasedCarveAmount(cumulativeUnReleasedAmount);
    }
}
