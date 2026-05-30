package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model;

import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import java.math.BigDecimal;
import java.util.List;

/**
 * Per-line context for allocation revenue release (retrospective and prospective).
 */
public final class AllocationRevenueReleaseLineContext {

    private final Long revenueContractLineId;
    private final Long revenueContractVersion;
    private final BigDecimal totalUnreleasedCarveAmount;
    private final DerivedJournalAccountValue debitAccountSegments;
    private final DerivedJournalAccountValue creditAccountSegments;
    private final List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod;

    public AllocationRevenueReleaseLineContext(
            Long revenueContractLineId,
            Long revenueContractVersion,
            BigDecimal totalUnreleasedCarveAmount,
            DerivedJournalAccountValue debitAccountSegments,
            DerivedJournalAccountValue creditAccountSegments,
            List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod) {
        this.revenueContractLineId = revenueContractLineId;
        this.revenueContractVersion = revenueContractVersion;
        this.totalUnreleasedCarveAmount = totalUnreleasedCarveAmount;
        this.debitAccountSegments = debitAccountSegments;
        this.creditAccountSegments = creditAccountSegments;
        this.revenueJournalEntriesPerPeriod = List.copyOf(revenueJournalEntriesPerPeriod);
    }

    public Long getRevenueContractLineId() {
        return revenueContractLineId;
    }

    public Long getRevenueContractVersion() {
        return revenueContractVersion;
    }

    public BigDecimal getTotalUnreleasedCarveAmount() {
        return totalUnreleasedCarveAmount;
    }

    public DerivedJournalAccountValue getDebitAccountSegments() {
        return debitAccountSegments;
    }

    public DerivedJournalAccountValue getCreditAccountSegments() {
        return creditAccountSegments;
    }

    public List<RevenueJournalEntriesPerPeriod> getRevenueJournalEntriesPerPeriod() {
        return revenueJournalEntriesPerPeriod;
    }
}
