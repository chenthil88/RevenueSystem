package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease.model;

import com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries.RevenueJournalEntriesRecord;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Indexed revenue journal rows by account period for allocation release period iteration.
 */
public class AllocationReleasePeriodLoop {

    private final Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId;
    private final List<Long> sortedAccountPeriodIds;
    private final Long lastAccountPeriodId;

    public AllocationReleasePeriodLoop(
            Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId,
            List<Long> sortedAccountPeriodIds,
            Long lastAccountPeriodId) {
        this.revenueJournalEntryByPeriodId = revenueJournalEntryByPeriodId;
        this.sortedAccountPeriodIds = sortedAccountPeriodIds;
        this.lastAccountPeriodId = lastAccountPeriodId;
    }

    public static Optional<AllocationReleasePeriodLoop> from(
            List<RevenueJournalEntriesRecord> revenueJournalEntryRecords) {
        if (revenueJournalEntryRecords == null || revenueJournalEntryRecords.isEmpty()) {
            return Optional.empty();
        }

        Map<Long, RevenueJournalEntriesRecord> revenueJournalEntryByPeriodId = new LinkedHashMap<>();
        for (RevenueJournalEntriesRecord revenueJournalEntryRecord : revenueJournalEntryRecords) {
            Long accountPeriodId = revenueJournalEntryRecord.getAccountPeriodId();
            if (accountPeriodId != null) {
                revenueJournalEntryByPeriodId.put(accountPeriodId, revenueJournalEntryRecord);
            }
        }

        List<Long> sortedAccountPeriodIds =
                revenueJournalEntryByPeriodId.keySet().stream().sorted().toList();
        if (sortedAccountPeriodIds.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new AllocationReleasePeriodLoop(
                revenueJournalEntryByPeriodId,
                sortedAccountPeriodIds,
                sortedAccountPeriodIds.get(sortedAccountPeriodIds.size() - 1)));
    }

    public Map<Long, RevenueJournalEntriesRecord> getRevenueJournalEntryByPeriodId() {
        return revenueJournalEntryByPeriodId;
    }

    public List<Long> getSortedAccountPeriodIds() {
        return sortedAccountPeriodIds;
    }

    public Long getLastAccountPeriodId() {
        return lastAccountPeriodId;
    }

    public RevenueJournalEntriesRecord getRevenueJournalEntryForPeriod(Long accountPeriodId) {
        return revenueJournalEntryByPeriodId.get(accountPeriodId);
    }
}
