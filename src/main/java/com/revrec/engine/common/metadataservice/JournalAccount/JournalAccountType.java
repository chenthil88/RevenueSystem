package com.revrec.engine.common.metadataservice.JournalAccount;

/**
 * Known journal account setup names in {@code JournalAccountsSetup.name}.
 */
public enum JournalAccountType {

    ALLOCATION_LIABILITY("Allocation Liability"),
    ALLOCATION_REVENUE("Allocation Revenue"),
    CONTRACT_REVENUE("Contract Revenue"),
    CONTRACT_LIABILITY("Contract Liability"),
    UNBILLED_REVENUE("Unbilled Revenue");

    private final String accountName;

    JournalAccountType(String accountName) {
        this.accountName = accountName;
    }

    /** Value stored in {@link JournalAccount#name()} / {@code JournalAccountsSetup.name}. */
    public String accountName() {
        return accountName;
    }
}
