package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.adapter;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.AccountingPeriod;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.CalendarConfig;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Date and accounting-period adapter for revenue recognition plan algorithms.
 *
 * <p>Default calendar uses Gregorian months (YYYYMM). Retail/shifted fiscal calendars can extend
 * {@link #toActgPeriod(LocalDate, CalendarConfig)} later.
 */
@Component
public class AccountingPeriodDateAdapter {

    public boolean isBefore(LocalDate dateA, LocalDate dateB) {
        return dateA.isBefore(dateB);
    }

    public long daysBetween(LocalDate startDate, LocalDate endDate, boolean includeStart, boolean includeEnd) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        if (endDate.isBefore(startDate)) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (includeStart) {
            days += 1;
        }
        if (!includeEnd && days > 0) {
            days -= 1;
        }
        return Math.max(0, days);
    }

    public AccountingPeriod toActgPeriod(LocalDate date, CalendarConfig calendarConfig) {
        YearMonth yearMonth = YearMonth.from(date);
        AccountingPeriod accountingPeriod = new AccountingPeriod();
        accountingPeriod.setPeriod(formatPeriodLabel(yearMonth));
        accountingPeriod.setStartDate(yearMonth.atDay(1));
        accountingPeriod.setEndDate(yearMonth.atEndOfMonth());
        return accountingPeriod;
    }

    public List<AccountingPeriod> periodsBetween(
            AccountingPeriod startPeriod,
            AccountingPeriod endPeriod,
            boolean includeStart,
            boolean includeEnd,
            CalendarConfig calendarConfig) {
        List<AccountingPeriod> periods = new ArrayList<>();
        YearMonth current = YearMonth.from(startPeriod.getStartDate());
        YearMonth end = YearMonth.from(endPeriod.getStartDate());

        if (!includeStart) {
            current = current.plusMonths(1);
        }

        while (!current.isAfter(end)) {
            if (current.equals(end) && !includeEnd) {
                break;
            }
            periods.add(toActgPeriod(current.atDay(1), calendarConfig));
            if (current.equals(end)) {
                break;
            }
            current = current.plusMonths(1);
        }

        return periods;
    }

    public AccountingPeriod priorPeriod(AccountingPeriod period, CalendarConfig calendarConfig) {
        YearMonth prior = YearMonth.from(period.getStartDate()).minusMonths(1);
        return toActgPeriod(prior.atDay(1), calendarConfig);
    }

    public LocalDate toStartOfMonth(LocalDate date) {
        return YearMonth.from(date).atDay(1);
    }

    public LocalDate toEndOfMonth(LocalDate date) {
        return YearMonth.from(date).atEndOfMonth();
    }

    public int getDayOfMonth(LocalDate date) {
        return date.getDayOfMonth();
    }

    public int getDaysInMonth(int month, int year) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public LocalDate addDays(LocalDate date, long count) {
        return date.plusDays(count);
    }

    public LocalDate addMonths(LocalDate date, int count) {
        return date.plusMonths(count);
    }

    public DateComponents toComponents(LocalDate date) {
        DateComponents dateComponents = new DateComponents();
        dateComponents.setYear(date.getYear());
        dateComponents.setMonth(date.getMonthValue());
        dateComponents.setDay(date.getDayOfMonth());
        return dateComponents;
    }

    /**
     * End date from start date and term using true 30-day months (360 convention).
     */
    public LocalDate calculateEndDateBasedOnTrue30DayTerm(LocalDate startDate, BigDecimal termMonths) {
        if (startDate == null || termMonths == null) {
            return startDate;
        }
        int wholeMonths = termMonths.intValue();
        int startDay = Math.min(30, startDate.getDayOfMonth());
        LocalDate adjustedStart = startDate.withDayOfMonth(Math.min(startDate.getDayOfMonth(), startDate.lengthOfMonth()));
        LocalDate endAnchor = addMonths(adjustedStart, wholeMonths);
        int endDay = Math.min(30, startDay);
        int maxDayInEndMonth = endAnchor.lengthOfMonth();
        return endAnchor.withDayOfMonth(Math.min(endDay, maxDayInEndMonth));
    }

    public boolean isEndOfMonth(LocalDate date) {
        return date.equals(toEndOfMonth(date));
    }

    public boolean isFebruary(LocalDate date) {
        return date.getMonthValue() == 2;
    }

    public boolean isLeapYear(int year) {
        return YearMonth.of(year, 1).lengthOfYear() == 366;
    }

    private String formatPeriodLabel(YearMonth yearMonth) {
        return String.format("%04d%02d", yearMonth.getYear(), yearMonth.getMonthValue());
    }

    public static class DateComponents {
        private int year;
        private int month;
        private int day;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }
}
