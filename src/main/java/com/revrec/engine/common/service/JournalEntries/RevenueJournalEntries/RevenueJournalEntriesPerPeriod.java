package com.revrec.engine.domain.service.JournalEntries.RevenueJournalEntries;

import com.revrec.engine.common.math.ChargebeeDecimal;
import java.io.Serializable;

/** Summed journal amounts for a revenue contract line in a single accounting period. */
public record RevenueJournalEntriesPerPeriod(Long periodId, ChargebeeDecimal amount) implements Serializable {}
