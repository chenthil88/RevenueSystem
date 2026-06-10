package com.revrec.engine.common.service.JournalEntries.AllocationJournalEntries;

import com.revrec.engine.common.math.ChargebeeDecimal;
import java.io.Serializable;

/** Summed allocation journal amounts per revenue contract line and initial-entry flag. */
public record AllocationScheduleByRcId(
        Long revenueContractLineId,
        Boolean isInitialEntry,
        ChargebeeDecimal amount) implements Serializable {}
