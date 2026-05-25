package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import java.io.Serializable;
import java.math.BigDecimal;

/** Summed journal amounts for a revenue contract line in a single accounting period. */
public record RevenueJournalEntriesPerPeriod(Long periodId, BigDecimal amount) implements Serializable {}
