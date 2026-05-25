package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueJournalEntriesRecord}.
 */
public interface RevenueJournalEntries {

    Long id();

    String tenantId();

    Long revenueContractId();

    Long accountPeriodId();

    Long journalAccountPeriodId();

    String debitAccountName();

    String creditAccountName();

    BigDecimal amount();

    String currency();

    String functionalCurrency();

    BigDecimal exchangeRate();

    BigDecimal globalexchangeRate();

    LocalDate exchangeRateDate();

    String debitAccount1();

    String debitAccount2();

    String debitAccount3();

    String debitAccount4();

    String debitAccount5();

    String debitAccount6();

    String debitAccount7();

    String debitAccount8();

    String debitAccount9();

    String debitAccount10();

    String creditAccount1();

    String creditAccount2();

    String creditAccount3();

    String creditAccount4();

    String creditAccount5();

    String creditAccount6();

    String creditAccount7();

    String creditAccount8();

    String creditAccount9();

    String creditAccount10();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();

    String createdBy();

    String updatedBy();

    Long createdPeriodId();

    Long updatedPeriodId();

    Boolean isPosted();

    Boolean isUnbilledAccount();

    Boolean isInitialEntry();

    Boolean isUnbilledReversal();

    String reversalFlag();

    String customField1();

    String customField2();

    String customField3();

    String customField4();

    String customField5();

    String customField6();

    String customField7();

    String customField8();

    String customField9();

    String customField10();
}
