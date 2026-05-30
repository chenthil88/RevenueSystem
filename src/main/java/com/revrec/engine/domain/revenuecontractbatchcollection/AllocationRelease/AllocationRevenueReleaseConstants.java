package com.revrec.engine.domain.revenuecontractbatchcollection.AllocationRelease;

/**
 * Journal account setup names used when deriving debit/credit segments for allocation revenue release.
 */
public final class AllocationRevenueReleaseConstants {

    /** {@link com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetup#name()} for debit side. */
    public static final String ALLOCATION_RELEASE_DEBIT_JOURNAL_ACCOUNT_NAME = "AllocationReleaseDebit";

    /** {@link com.revrec.engine.common.metadataservice.JournalAccountsSetup.JournalAccountsSetup#name()} for credit side. */
    public static final String ALLOCATION_RELEASE_CREDIT_JOURNAL_ACCOUNT_NAME = "AllocationReleaseCredit";

    private AllocationRevenueReleaseConstants() {}
}
