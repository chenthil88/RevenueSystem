package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetupService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Shared helpers for allocation revenue release (line ids, period resolution, strategy selection, calculations).
 */
@Service
public class AllocationRevenueReleaseUtilityService {

    private final JournalAccountsSetupService journalAccountsSetupService;

    public AllocationRevenueReleaseUtilityService(JournalAccountsSetupService journalAccountsSetupService) {
        this.journalAccountsSetupService = journalAccountsSetupService;
    }

    /**
     * Revenue contract line ids from indexed order details ({@code id} key).
     */
    public List<Long> getRevenueContractLineIds(RevenueContractOrderRecords revenueContractOrderRecords) {
        return new ArrayList<>(revenueContractOrderRecords.getRevenueContractOrderDetailsRecord().keySet());
    }

    /**
     * Open / account period used for journal aggregation.
     */
    public Long getOpenAccountPeriodId(RevenueContractHeaderRecord revenueContractHeaderRecord) {
        // TODO: resolve from batch context (CurrentOpenPeriod)
        return revenueContractHeaderRecord.getCreatedPeriodId();
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
     * {@code unreleasedCarveAmount + carveAmount} (null treated as zero).
     */
    public BigDecimal calculateTotalUnreleasedCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        BigDecimal unreleasedCarveAmount = nullToZero(revenueContractAllocationDetailsRecord.getUnreleasedCarveAmount());
        BigDecimal carveAmount = nullToZero(revenueContractAllocationDetailsRecord.getCarveAmount());
        return unreleasedCarveAmount.add(carveAmount);
    }

    public DerivedJournalAccountValue deriveDebitAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                AllocationRevenueReleaseConstants.ALLOCATION_RELEASE_DEBIT_JOURNAL_ACCOUNT_NAME,
                revenueContractOrderAccountDetailsRecord);
    }

    public DerivedJournalAccountValue deriveCreditAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                AllocationRevenueReleaseConstants.ALLOCATION_RELEASE_CREDIT_JOURNAL_ACCOUNT_NAME,
                revenueContractOrderAccountDetailsRecord);
    }

    public Optional<AllocationRevenueReleaseLineContext> buildAllocationRevenueReleaseLineContext(
            Long revenueContractLineId,
            Long revenueContractVersion,
            RevenueContractOrderRecords revenueContractOrderRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        Optional<RevenueContractAllocationDetailsRecord> revenueContractAllocationDetailsRecord =
                revenueContractOrderRecords.getAllocationDetails(revenueContractLineId);
        if (revenueContractAllocationDetailsRecord.isEmpty()) {
            return Optional.empty();
        }

        RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord =
                revenueContractOrderRecords.getOrderAccountDetails(revenueContractLineId).orElse(null);

        BigDecimal totalUnreleasedCarveAmount =
                calculateTotalUnreleasedCarveAmount(revenueContractAllocationDetailsRecord.get());

        DerivedJournalAccountValue debitAccountSegments =
                deriveDebitAccountSegments(revenueContractOrderAccountDetailsRecord);
        DerivedJournalAccountValue creditAccountSegments =
                deriveCreditAccountSegments(revenueContractOrderAccountDetailsRecord);

        return Optional.of(new AllocationRevenueReleaseLineContext(
                revenueContractLineId,
                revenueContractVersion,
                totalUnreleasedCarveAmount,
                debitAccountSegments,
                creditAccountSegments,
                revenueJournalEntriesPerPeriod));
    }

    /**
     * Per-period release amount from aggregated journal data and the revenue journal entry row.
     */
    public BigDecimal calculatePerPeriodReleaseAmount(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        Long effectivePeriodId = resolveEffectivePeriodId(
                revenueJournalEntryRecord.getAccountPeriodId(),
                allocationRevenueReleaseLineContext.getRevenueJournalEntriesPerPeriod());

        return findAggregatedAmountForPeriod(revenueJournalEntriesPerPeriod, effectivePeriodId)
                .orElse(nullToZero(revenueJournalEntryRecord.getAmount()));
    }

    private DerivedJournalAccountValue deriveAccountSegments(
            String journalAccountName, AccountDetailsRecord accountDetailsRecord) {
        if (accountDetailsRecord == null) {
            return new DerivedJournalAccountValue(journalAccountName, null, null, null, null, null, null, null, null, null, null);
        }
        return journalAccountsSetupService.deriveJournalAccountValue(journalAccountName, accountDetailsRecord);
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

    private static Optional<BigDecimal> findAggregatedAmountForPeriod(
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod, Long periodId) {
        if (periodId == null) {
            return Optional.empty();
        }
        return revenueJournalEntriesPerPeriod.stream()
                .filter(entry -> periodId.equals(entry.periodId()))
                .map(RevenueJournalEntriesPerPeriod::amount)
                .findFirst();
    }

    private static BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
