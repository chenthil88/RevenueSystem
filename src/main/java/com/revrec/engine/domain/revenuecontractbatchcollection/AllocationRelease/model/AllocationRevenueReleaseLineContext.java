package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model;

import com.revrec.engine.common.metadataservice.JournalAccountsSetup.DerivedJournalAccountValue;
import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesPerPeriod;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import java.math.BigDecimal;
import java.util.List;

/**
 * Per-line context for allocation revenue release (retrospective and prospective).
 */
public class AllocationRevenueReleaseLineContext {

    private Long revenueContractLineId;
    private Long revenueContractVersion;
    private BigDecimal totalUnreleasedCarveAmount;
    private RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord;
    private DerivedJournalAccountValue debitAccountSegments;
    private DerivedJournalAccountValue creditAccountSegments;
    private List<RevenueJournalEntriesPerPeriod> revenueJournalEntriesPerPeriod;

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

    public BigDecimal getTotalUnreleasedCarveAmount() {
        return totalUnreleasedCarveAmount;
    }

    public void setTotalUnreleasedCarveAmount(BigDecimal totalUnreleasedCarveAmount) {
        this.totalUnreleasedCarveAmount = totalUnreleasedCarveAmount;
    }

    public RevenueContractAllocationDetailsRecord getRevenueContractAllocationDetailsRecord() {
        return revenueContractAllocationDetailsRecord;
    }

    public void setRevenueContractAllocationDetailsRecord(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        this.revenueContractAllocationDetailsRecord = revenueContractAllocationDetailsRecord;
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
        this.revenueJournalEntriesPerPeriod =
                revenueJournalEntriesPerPeriod == null ? List.of() : List.copyOf(revenueJournalEntriesPerPeriod);
    }
}
