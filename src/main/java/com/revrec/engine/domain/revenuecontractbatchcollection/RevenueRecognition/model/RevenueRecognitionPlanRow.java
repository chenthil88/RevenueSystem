package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Normalized plan output row — amount recognized per accounting period.
 */
public class RevenueRecognitionPlanRow {

    private String period;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private BigDecimal amount;
    private BigDecimal percentRecognized;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(LocalDate periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(LocalDate periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentRecognized() {
        return percentRecognized;
    }

    public void setPercentRecognized(BigDecimal percentRecognized) {
        this.percentRecognized = percentRecognized;
    }
}
