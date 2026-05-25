package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

import com.revrec.engine.common.accountdetails.AccountDetailsRecord;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetupService;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesRecord;
import com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries.AllocationJournalEntriesService;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model.AllocationRevenueReleaseLineContext;
import com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.strategy.AllocationRevenueReleaseStrategy;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import com.revrec.engine.domain.service.RevenueContractHeader.RevenueContractHeaderRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderRecords;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
            Long revenueContractLineId,
            Long revenueContractVersion,
            RevenueContractOrderRecords revenueContractOrderRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord =
                revenueContractOrderRecords.getAllocationDetails(revenueContractLineId).orElse(null);
        if (revenueContractAllocationDetailsRecord == null) {
            return null;
        }

        RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord =
                revenueContractOrderRecords.getOrderAccountDetails(revenueContractLineId).orElse(null);

        AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext =
                new AllocationRevenueReleaseLineContext();
        allocationRevenueReleaseLineContext.setRevenueContractLineId(revenueContractLineId);
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

    /**
     * Retrospective release: derives per-period allocation amounts and builds allocation release journal rows.
     *
     * <p>{@code allocationPerPeriodRevenue = (perPeriodReleaseAmount / transactionPrice) * totalUnreleasedCarveAmount}.
     * The last period receives the remainder so period amounts sum to {@code totalUnreleasedCarveAmount}.
     */
    public List<AllocationJournalEntriesRecord> buildRetrospectiveAllocationReleaseJournalEntries(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod,
            Long openAccountPeriodId,
            AllocationJournalEntriesService allocationJournalEntriesService) {
        if (revenueJournalEntryRecords == null || revenueJournalEntryRecords.isEmpty()) {
            return List.of();
        }

        Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId =
                indexRevenueJournalEntriesByAccountPeriodId(revenueJournalEntryRecords);
        List<Long> sortedAccountPeriodIds = sortedAccountPeriodIds(revenueJournalEntryByPeriodId);
        if (sortedAccountPeriodIds.isEmpty()) {
            return List.of();
        }

        Long lastAccountPeriodId = sortedAccountPeriodIds.get(sortedAccountPeriodIds.size() - 1);
        BigDecimal totalUnreleasedCarveAmount = allocationRevenueReleaseLineContext.getTotalUnreleasedCarveAmount();
        BigDecimal transactionPrice = nullToZero(allocationRevenueReleaseLineContext
                .getRevenueContractAllocationDetailsRecord()
                .getTransactionPrice());

        BigDecimal sumAllocationPerPeriodRevenue = BigDecimal.ZERO;
        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntries = new ArrayList<>();

        for (Long accountPeriodId : sortedAccountPeriodIds) {
            RevenueJournalEntriesRecord revenueJournalEntryRecord = revenueJournalEntryByPeriodId.get(accountPeriodId);
            BigDecimal perPeriodReleaseAmount = calculatePerPeriodReleaseAmount(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecord,
                    revenueJournalEntriesPerPeriod);

            BigDecimal allocationPerPeriodRevenue;
            if (accountPeriodId.equals(lastAccountPeriodId)) {
                allocationPerPeriodRevenue = totalUnreleasedCarveAmount.subtract(sumAllocationPerPeriodRevenue);
            } else {
                allocationPerPeriodRevenue = calculateRetrospectiveAllocationPerPeriodRevenue(
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

    /**
     * Prospective release journal rows (same period structure as retrospective).
     *
     * <p>TODO: Implement {@link #calculateProspectivePerPeriodRevenueReleasedPercentage} and
     * {@link #calculateProspectiveAllocationPerPeriodRevenue} (formulas differ from retrospective).
     */
    public List<AllocationJournalEntriesRecord> buildProspectiveAllocationReleaseJournalEntries(
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod,
            Long openAccountPeriodId,
            AllocationJournalEntriesService allocationJournalEntriesService) {
        if (revenueJournalEntryRecords == null || revenueJournalEntryRecords.isEmpty()) {
            return List.of();
        }

        Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId =
                indexRevenueJournalEntriesByAccountPeriodId(revenueJournalEntryRecords);
        List<Long> sortedAccountPeriodIds = sortedAccountPeriodIds(revenueJournalEntryByPeriodId);
        if (sortedAccountPeriodIds.isEmpty()) {
            return List.of();
        }

        Long lastAccountPeriodId = sortedAccountPeriodIds.get(sortedAccountPeriodIds.size() - 1);
        BigDecimal totalUnreleasedCarveAmount = allocationRevenueReleaseLineContext.getTotalUnreleasedCarveAmount();

        BigDecimal sumAllocationPerPeriodRevenue = BigDecimal.ZERO;
        List<AllocationJournalEntriesRecord> allocationReleaseJournalEntries = new ArrayList<>();

        for (Long accountPeriodId : sortedAccountPeriodIds) {
            RevenueJournalEntriesRecord revenueJournalEntryRecord = revenueJournalEntryByPeriodId.get(accountPeriodId);
            BigDecimal perPeriodReleaseAmount = calculatePerPeriodReleaseAmount(
                    allocationRevenueReleaseLineContext,
                    revenueJournalEntryRecord,
                    revenueJournalEntriesPerPeriod);

            BigDecimal allocationPerPeriodRevenue;
            if (accountPeriodId.equals(lastAccountPeriodId)) {
                // Remainder only applies once non-last periods have been calculated.
                if (sumAllocationPerPeriodRevenue.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                allocationPerPeriodRevenue = totalUnreleasedCarveAmount.subtract(sumAllocationPerPeriodRevenue);
            } else {
                BigDecimal perPeriodRevenueReleasedPercentage = calculateProspectivePerPeriodRevenueReleasedPercentage(
                        perPeriodReleaseAmount,
                        allocationRevenueReleaseLineContext,
                        revenueJournalEntryRecord,
                        revenueJournalEntriesPerPeriod);
                allocationPerPeriodRevenue = calculateProspectiveAllocationPerPeriodRevenue(
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
    private BigDecimal calculateProspectivePerPeriodRevenueReleasedPercentage(
            BigDecimal perPeriodReleaseAmount,
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        return BigDecimal.ZERO;
    }

    /**
     * TODO: Prospective {@code allocationPerPeriodRevenue} from {@code perPeriodRevenueReleasedPercentage}.
     */
    private BigDecimal calculateProspectiveAllocationPerPeriodRevenue(
            BigDecimal perPeriodRevenueReleasedPercentage,
            BigDecimal totalUnreleasedCarveAmount,
            AllocationRevenueReleaseLineContext allocationRevenueReleaseLineContext,
            RevenueJournalEntriesRecord revenueJournalEntryRecord) {
        return BigDecimal.ZERO;
    }

    private static BigDecimal calculateRetrospectiveAllocationPerPeriodRevenue(
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

    private static Map<Long, RevenueJournalEntriesRecord> indexRevenueJournalEntriesByAccountPeriodId(
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords) {
        Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId = new LinkedHashMap<>();
        for (RevenueJournalEntriesRecord revenueJournalEntryRecord : revenueJournalEntryRecords) {
            Long accountPeriodId = revenueJournalEntryRecord.getAccountPeriodId();
            if (accountPeriodId != null) {
                revenueJournalEntryByPeriodId.put(accountPeriodId, revenueJournalEntryRecord);
            }
        }
        return revenueJournalEntryByPeriodId;
    }

    private static List<Long> sortedAccountPeriodIds(Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId) {
        return revenueJournalEntryByPeriodId.keySet().stream().sorted().toList();
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
