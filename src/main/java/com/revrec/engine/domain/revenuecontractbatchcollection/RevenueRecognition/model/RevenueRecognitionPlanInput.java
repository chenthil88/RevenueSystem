package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Normalized plan input DTO — schema-independent contract recognition inputs.
 */
public class RevenueRecognitionPlanInput {

    private String recognitionRuleId;
    private String planType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private boolean endDateInclusive;
    private boolean exactStartDays;
    private BigDecimal term;
    private String recognizedOn;
    private CalendarConfig calendarConfig;
    private LocalDate arrangementEffectiveDate;
    private int maxContractYears = 20;
    private boolean startDateInclusive = true;
    private boolean endDateAdjustedForTerm;
    private boolean true30StartDateInclusive;
    private boolean dereferenceRuleHasPriority;

    public String getRecognitionRuleId() {
        return recognitionRuleId;
    }

    public void setRecognitionRuleId(String recognitionRuleId) {
        this.recognitionRuleId = recognitionRuleId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isEndDateInclusive() {
        return endDateInclusive;
    }

    public void setEndDateInclusive(boolean endDateInclusive) {
        this.endDateInclusive = endDateInclusive;
    }

    public boolean isExactStartDays() {
        return exactStartDays;
    }

    public void setExactStartDays(boolean exactStartDays) {
        this.exactStartDays = exactStartDays;
    }

    public BigDecimal getTerm() {
        return term;
    }

    public void setTerm(BigDecimal term) {
        this.term = term;
    }

    public String getRecognizedOn() {
        return recognizedOn;
    }

    public void setRecognizedOn(String recognizedOn) {
        this.recognizedOn = recognizedOn;
    }

    public CalendarConfig getCalendarConfig() {
        return calendarConfig;
    }

    public void setCalendarConfig(CalendarConfig calendarConfig) {
        this.calendarConfig = calendarConfig;
    }

    public LocalDate getArrangementEffectiveDate() {
        return arrangementEffectiveDate;
    }

    public void setArrangementEffectiveDate(LocalDate arrangementEffectiveDate) {
        this.arrangementEffectiveDate = arrangementEffectiveDate;
    }

    public int getMaxContractYears() {
        return maxContractYears;
    }

    public void setMaxContractYears(int maxContractYears) {
        this.maxContractYears = maxContractYears;
    }

    public boolean isStartDateInclusive() {
        return startDateInclusive;
    }

    public void setStartDateInclusive(boolean startDateInclusive) {
        this.startDateInclusive = startDateInclusive;
    }

    public boolean isEndDateAdjustedForTerm() {
        return endDateAdjustedForTerm;
    }

    public void setEndDateAdjustedForTerm(boolean endDateAdjustedForTerm) {
        this.endDateAdjustedForTerm = endDateAdjustedForTerm;
    }

    public boolean isTrue30StartDateInclusive() {
        return true30StartDateInclusive;
    }

    public void setTrue30StartDateInclusive(boolean true30StartDateInclusive) {
        this.true30StartDateInclusive = true30StartDateInclusive;
    }

    public boolean isDereferenceRuleHasPriority() {
        return dereferenceRuleHasPriority;
    }

    public void setDereferenceRuleHasPriority(boolean dereferenceRuleHasPriority) {
        this.dereferenceRuleHasPriority = dereferenceRuleHasPriority;
    }
}
