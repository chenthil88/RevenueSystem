package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.RevenueRecognitionPlanConstants;

/**
 * Fiscal or retail calendar configuration for accounting period conversion.
 */
public class CalendarConfig {

    private String calendarType;
    private String retailWeekGrouping;
    private Integer lastMonthOfYear;

    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public String getRetailWeekGrouping() {
        return retailWeekGrouping;
    }

    public void setRetailWeekGrouping(String retailWeekGrouping) {
        this.retailWeekGrouping = retailWeekGrouping;
    }

    public Integer getLastMonthOfYear() {
        return lastMonthOfYear;
    }

    public void setLastMonthOfYear(Integer lastMonthOfYear) {
        this.lastMonthOfYear = lastMonthOfYear;
    }

    public boolean isRetailCalendar() {
        return RevenueRecognitionPlanConstants.CALENDAR_TYPE_RETAIL.equalsIgnoreCase(calendarType);
    }
}
