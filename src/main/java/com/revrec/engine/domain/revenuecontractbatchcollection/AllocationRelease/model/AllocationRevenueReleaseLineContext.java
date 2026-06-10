package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model;

import com.revrec.engine.common.math.ChargebeeDecimal;
import com.revrec.engine.common.metadataservice.JournalAccount.DerivedJournalAccountValue;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import java.util.Comparator;
import java.util.List;

/**
 * Per-line context for allocation revenue release (retrospective and prospective).
 */
public class AllocationRevenueReleaseLineContext {

    private Long revenueContractLineId;
    private Long revenueContractVersion;
    private ChargebeeDecimal totalUnreleasedCarveAmount;
    private ChargebeeDecimal transactionPrice;
    private ChargebeeDecimal postedPercentage;
    private DerivedJournalAccountValue debitAccountSegments;
    private DerivedJournalAccountValue creditAccountSegments;
    private List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod;
    private String allocationCurrency;
    private int roundingPrecision;
    private boolean isInitialEntry = false;

    public AllocationRevenueReleaseLineContext() {}

    public Long getRevenueContractLineId() {
        return revenueContractLineId;
    }

    public void setRevenueContractLineId(Long revenueContractLineId) {
        this.revenueContractLineId = revenueContractLineId;
    }

    public Long getRevenueContractVersion() {
        return revenueContractVersion;
    }

    public void setRevenueContractVersion(Long revenueContractVersion) {
        this.revenueContractVersion = revenueContractVersion;
    }

    public ChargebeeDecimal getTotalUnreleasedCarveAmount() {
        return totalUnreleasedCarveAmount;
    }

    public void setTotalUnreleasedCarveAmount(ChargebeeDecimal totalUnreleasedCarveAmount) {
        this.totalUnreleasedCarveAmount = totalUnreleasedCarveAmount;
    }

    public ChargebeeDecimal getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(ChargebeeDecimal transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public ChargebeeDecimal getPostedPercentage() {
        return postedPercentage;
    }

    public void setPostedPercentage(ChargebeeDecimal postedPercentage) {
        this.postedPercentage = postedPercentage;
    }

    public DerivedJournalAccountValue getDebitAccountSegments() {
        return debitAccountSegments;
    }

    public void setDebitAccountSegments(DerivedJournalAccountValue debitAccountSegments) {
        this.debitAccountSegments = debitAccountSegments;
    }

    public DerivedJournalAccountValue getCreditAccountSegments() {
        return creditAccountSegments;
    }

    public void setCreditAccountSegments(DerivedJournalAccountValue creditAccountSegments) {
        this.creditAccountSegments = creditAccountSegments;
    }

    public List<RevenueJournalEntriesPerPeriod> getRevenueJournalEntriesPerPeriod() {
        return revenueJournalEntriesPerPeriod;
    }

    public void setRevenueJournalEntriesPerPeriod(List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        if (revenueJournalEntriesPerPeriod == null || revenueJournalEntriesPerPeriod.isEmpty()) {
            this.revenueJournalEntriesPerPeriod = List.of();
            return;
        }
        this.revenueJournalEntriesPerPeriod = revenueJournalEntriesPerPeriod.stream()
                .sorted(Comparator.comparing(RevenueJournalEntriesPerPeriod::periodId))
                .toList();
    }

    public String getAllocationCurrency() {
        return allocationCurrency;
    }

    public void setAllocationCurrency(String allocationCurrency) {
        this.allocationCurrency = allocationCurrency;
    }

    /**
     * Decimal places for amount rounding on this line (derived from {@link #allocationCurrency}).
     */
    public int getRoundingPrecision() {
        return roundingPrecision;
    }

    public void setRoundingPrecision(int roundingPrecision) {
        this.roundingPrecision = roundingPrecision;
    }

    public boolean isInitialEntry() {
        return isInitialEntry;
    }

    public void setInitialEntry(boolean initialEntry) {
        isInitialEntry = initialEntry;
    }
}
