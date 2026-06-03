package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Rich accounting period (ActgPeriod) used by plan algorithms.
 */
public class AccountingPeriod {

    private String period;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStrMonth() {
        if (period == null || period.length() < 2) {
            return startDate != null ? String.format("%02d", startDate.getMonthValue()) : null;
        }
        return period.substring(period.length() - 2);
    }

    public int numberOfDays() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AccountingPeriod accountingPeriod)) {
            return false;
        }
        return period != null && period.equals(accountingPeriod.period);
    }

    @Override
    public int hashCode() {
        return period != null ? period.hashCode() : 0;
    }
}
