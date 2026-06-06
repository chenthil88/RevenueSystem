package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetupService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationReleasePeriodLoop;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderLineRecords;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Shared helpers for allocation revenue release (line ids, line context, period loop prep, strategy selection).
 */
@Service
public class AllocationRevenueReleaseUtilityService {

    private final JournalAccountsSetupService journalAccountsSetupService;

    public AllocationRevenueReleaseUtilityService(JournalAccountsSetupService journalAccountsSetupService) {
        this.journalAccountsSetupService = journalAccountsSetupService;
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

    public BigDecimal calculateTotalUnreleasedCarveAmount(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        BigDecimal unreleasedCarveAmount = nullToZero(revenueContractAllocationDetailsRecord.getUnreleasedCarveAmount());
        BigDecimal carveAmount = nullToZero(revenueContractAllocationDetailsRecord.getCarveAmount());
        return unreleasedCarveAmount.add(carveAmount);
    }

    public DerivedJournalAccountValue deriveDebitAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                AllocationRevenueReleaseConstants.ALLOCATION_DEBIT_ACCOUNT_NAME,
                revenueContractOrderAccountDetailsRecord);
    }

    public DerivedJournalAccountValue deriveCreditAccountSegments(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        return deriveAccountSegments(
                AllocationRevenueReleaseConstants.ALLOCATION_CREDIT_ACCOUNT_NAME,
                revenueContractOrderAccountDetailsRecord);
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
        allocationRevenueReleaseLineContext.setRevenueContractAllocationDetailsRecord(
                revenueContractAllocationDetailsRecord);
        allocationRevenueReleaseLineContext.setTotalUnreleasedCarveAmount(
                calculateTotalUnreleasedCarveAmount(revenueContractAllocationDetailsRecord));
        allocationRevenueReleaseLineContext.setDebitAccountSegments(
                deriveDebitAccountSegments(revenueContractOrderAccountDetailsRecord));
        allocationRevenueReleaseLineContext.setCreditAccountSegments(
                deriveCreditAccountSegments(revenueContractOrderAccountDetailsRecord));
        allocationRevenueReleaseLineContext.setRevenueJournalEntriesPerPeriod(revenueJournalEntriesPerPeriod);
        return allocationRevenueReleaseLineContext;
    }

    public Optional<AllocationReleasePeriodLoop> prepareAllocationReleasePeriodLoop(
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords) {
        return AllocationReleasePeriodLoop.from(revenueJournalEntryRecords);
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

    public BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
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
}
