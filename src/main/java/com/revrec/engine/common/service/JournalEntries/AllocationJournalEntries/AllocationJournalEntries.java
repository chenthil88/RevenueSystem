package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.persistence.PersistenceFlags;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row contract for {@link AllocationJournalEntriesRecord} (JavaBean-style accessors, cf. legacy model pattern).
 */
public interface AllocationJournalEntries extends PersistenceFlags {

    Long getId();

    String getTenantId();

    Long getRevenueContractId();

    Long getRevenueContractLineId();

    Long getRevenueContractVersion();

    Long getAccountPeriodId();

    Long getJournalAccountPeriodId();

    String getDebitAccountName();

    String getCreditAccountName();

    BigDecimal getAmount();

    String getCurrency();

    String getFunctionalCurrency();

    BigDecimal getExchangeRate();

    BigDecimal getGlobalexchangeRate();

    LocalDate getExchangeRateDate();

    String getDebitAccount1();

    String getDebitAccount2();

    String getDebitAccount3();

    String getDebitAccount4();

    String getDebitAccount5();

    String getDebitAccount6();

    String getDebitAccount7();

    String getDebitAccount8();

    String getDebitAccount9();

    String getDebitAccount10();

    String getCreditAccount1();

    String getCreditAccount2();

    String getCreditAccount3();

    String getCreditAccount4();

    String getCreditAccount5();

    String getCreditAccount6();

    String getCreditAccount7();

    String getCreditAccount8();

    String getCreditAccount9();

    String getCreditAccount10();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    String getCreatedBy();

    String getUpdatedBy();

    Long getCreatedPeriodId();

    Long getUpdatedPeriodId();

    Boolean isPosted();

    Boolean isUnbilledAccount();

    Boolean isInitialEntry();

    Boolean isUnbilledReversal();

    String getReversalFlag();

    String getCustomField1();

    String getCustomField2();

    String getCustomField3();

    String getCustomField4();

    String getCustomField5();

    String getCustomField6();

    String getCustomField7();

    String getCustomField8();

    String getCustomField9();

    String getCustomField10();
}
