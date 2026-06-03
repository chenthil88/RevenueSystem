package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.adapter.AccountingPeriodDateAdapter;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.AccountingPeriod;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.CalendarConfig;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanInput;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanRow;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.strategy.RevenueRecognitionPlanStrategy;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Plan dispatcher, shared ratable helpers, and post-processing from {@code ArrangementUtils}.
 */
@Service
public class RevenueRecognitionPlanUtilityService {

    private static final MathContext CALCULATION_CONTEXT = MathContext.DECIMAL64;

    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public RevenueRecognitionPlanUtilityService(AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    public RevenueRecognitionPlanStrategy resolveRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput,
            List<RevenueRecognitionPlanStrategy> revenueRecognitionPlanStrategies) {
        return revenueRecognitionPlanStrategies.stream()
                .filter(revenueRecognitionPlanStrategy ->
                        revenueRecognitionPlanStrategy.supports(revenueRecognitionPlanInput))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No revenue recognition plan strategy for recognitionRuleId="
                                + revenueRecognitionPlanInput.getRecognitionRuleId()
                                + ", planType="
                                + revenueRecognitionPlanInput.getPlanType()));
    }

    public List<RevenueRecognitionPlanRow> buildPlan(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput,
            List<RevenueRecognitionPlanStrategy> revenueRecognitionPlanStrategies) {
        validateRetailCalendarPlanType(revenueRecognitionPlanInput);
        RevenueRecognitionPlanInput adjustedPlanInput =
                adjustPlanInputForTrue30Term(revenueRecognitionPlanInput);

        RevenueRecognitionPlanStrategy revenueRecognitionPlanStrategy =
                resolveRevenueRecognitionPlanStrategy(adjustedPlanInput, revenueRecognitionPlanStrategies);
        List<RevenueRecognitionPlanRow> planRows =
                revenueRecognitionPlanStrategy.buildPlan(adjustedPlanInput);

        planRows = truncatePlanRows(planRows, adjustedPlanInput.getMaxContractYears());
        validatePlanRows(planRows);
        return prependZeroRowsBeforeFirstPeriod(planRows, adjustedPlanInput);
    }

    public void validateEndDateNotBeforeStartDate(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput, String planName) {
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        if (startDate == null || endDate == null) {
            return;
        }
        if (accountingPeriodDateAdapter.isBefore(endDate, startDate)) {
            throw new IllegalArgumentException(
                    planName + ": endDate " + endDate + " is before startDate " + startDate);
        }
    }

    public boolean isPointInTime(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return RevenueRecognitionPlanConstants.RECOGNITION_RULE_POINT_IN_TIME.equalsIgnoreCase(
                revenueRecognitionPlanInput.getRecognitionRuleId());
    }

    public boolean isPlanType(RevenueRecognitionPlanInput revenueRecognitionPlanInput, String planType) {
        return planType.equalsIgnoreCase(normalizePlanType(revenueRecognitionPlanInput.getPlanType()));
    }

    public String normalizePlanType(String planType) {
        if (planType == null || planType.isBlank()) {
            return RevenueRecognitionPlanConstants.PLAN_TYPE_DEFAULT;
        }
        return planType.trim();
    }

    public boolean isDefaultPlanType(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        String planType = normalizePlanType(revenueRecognitionPlanInput.getPlanType());
        return RevenueRecognitionPlanConstants.PLAN_TYPE_DEFAULT.equalsIgnoreCase(planType)
                || RevenueRecognitionPlanConstants.PLAN_TYPE_CUSTOM.equalsIgnoreCase(planType);
    }

    public CalendarConfig calendarConfigOrDefault(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        CalendarConfig calendarConfig = revenueRecognitionPlanInput.getCalendarConfig();
        if (calendarConfig == null) {
            calendarConfig = new CalendarConfig();
        }
        return calendarConfig;
    }

    public RevenueRecognitionPlanRow createPlanRow(AccountingPeriod accountingPeriod, BigDecimal amount) {
        RevenueRecognitionPlanRow revenueRecognitionPlanRow = new RevenueRecognitionPlanRow();
        revenueRecognitionPlanRow.setPeriod(accountingPeriod.getPeriod());
        revenueRecognitionPlanRow.setPeriodStartDate(accountingPeriod.getStartDate());
        revenueRecognitionPlanRow.setPeriodEndDate(accountingPeriod.getEndDate());
        revenueRecognitionPlanRow.setAmount(scaleAmount(amount));
        return revenueRecognitionPlanRow;
    }

    public DailyStyleDays calculateDailyStyleFirstAndLastDays(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput,
            AccountingPeriod startPeriod,
            AccountingPeriod endPeriod) {
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        boolean endDateInclusive = revenueRecognitionPlanInput.isEndDateInclusive();

        long firstDays;
        long lastDays;
        if (startPeriod.equals(endPeriod)) {
            firstDays = accountingPeriodDateAdapter.daysBetween(startDate, endDate, true, endDateInclusive);
            lastDays = 0;
        } else {
            firstDays = accountingPeriodDateAdapter.daysBetween(
                    startDate, startPeriod.getEndDate(), true, true);
            lastDays = accountingPeriodDateAdapter.daysBetween(
                    endPeriod.getStartDate(), endDate, true, endDateInclusive);
        }

        long totalDays =
                startDate.equals(endDate)
                        ? 1
                        : accountingPeriodDateAdapter.daysBetween(startDate, endDate, true, endDateInclusive);

        if (firstDays == 0 && lastDays == 0 && totalDays == 1) {
            firstDays = 1;
        }

        DailyStyleDays dailyStyleDays = new DailyStyleDays();
        dailyStyleDays.setFirstDays(firstDays);
        dailyStyleDays.setLastDays(lastDays);
        dailyStyleDays.setTotalDays(totalDays);
        return dailyStyleDays;
    }

    public List<AccountingPeriod> resolveRatablePeriods(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput,
            AccountingPeriod startPeriod,
            AccountingPeriod endPeriod) {
        CalendarConfig calendarConfig = calendarConfigOrDefault(revenueRecognitionPlanInput);
        if (startPeriod.equals(endPeriod)) {
            return List.of(startPeriod);
        }
        return accountingPeriodDateAdapter.periodsBetween(
                startPeriod, endPeriod, true, true, calendarConfig);
    }

    public BigDecimal scaleAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public BigDecimal valueOrZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, CALCULATION_CONTEXT);
    }

    public AccountingPeriodDateAdapter getAccountingPeriodDateAdapter() {
        return accountingPeriodDateAdapter;
    }

    private void validateRetailCalendarPlanType(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        CalendarConfig calendarConfig = calendarConfigOrDefault(revenueRecognitionPlanInput);
        if (!calendarConfig.isRetailCalendar()) {
            return;
        }
        if (RevenueRecognitionPlanConstants.RETAIL_WEEK_GROUPING_GROUP_444.equalsIgnoreCase(
                calendarConfig.getRetailWeekGrouping())) {
            return;
        }
        if (!isPlanType(revenueRecognitionPlanInput, RevenueRecognitionPlanConstants.PLAN_TYPE_DAILY)) {
            throw new IllegalArgumentException(
                    "Retail calendar (non-Group444) only supports daily plan type");
        }
    }

    private RevenueRecognitionPlanInput adjustPlanInputForTrue30Term(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        if (!isPlanType(revenueRecognitionPlanInput, RevenueRecognitionPlanConstants.PLAN_TYPE_TRUE_30_360)) {
            return revenueRecognitionPlanInput;
        }
        if (!revenueRecognitionPlanInput.isTrue30StartDateInclusive()
                || !revenueRecognitionPlanInput.isDereferenceRuleHasPriority()
                || revenueRecognitionPlanInput.getTerm() == null) {
            return revenueRecognitionPlanInput;
        }

        LocalDate adjustedEndDate = accountingPeriodDateAdapter.calculateEndDateBasedOnTrue30DayTerm(
                revenueRecognitionPlanInput.getStartDate(), revenueRecognitionPlanInput.getTerm());

        RevenueRecognitionPlanInput adjustedPlanInput = copyPlanInput(revenueRecognitionPlanInput);
        adjustedPlanInput.setEndDate(adjustedEndDate);
        adjustedPlanInput.setEndDateAdjustedForTerm(true);
        return adjustedPlanInput;
    }

    private List<RevenueRecognitionPlanRow> truncatePlanRows(
            List<RevenueRecognitionPlanRow> planRows, int maxContractYears) {
        int maxRows = maxContractYears * 12;
        if (planRows.size() <= maxRows) {
            return planRows;
        }
        return new ArrayList<>(planRows.subList(0, maxRows));
    }

    private void validatePlanRows(List<RevenueRecognitionPlanRow> planRows) {
        for (RevenueRecognitionPlanRow revenueRecognitionPlanRow : planRows) {
            if (revenueRecognitionPlanRow.getPeriod() == null
                    || revenueRecognitionPlanRow.getPeriod().isBlank()) {
                throw new IllegalStateException("Plan row missing period");
            }
            if (revenueRecognitionPlanRow.getAmount() == null) {
                throw new IllegalStateException(
                        "Plan row missing amount for period " + revenueRecognitionPlanRow.getPeriod());
            }
        }
    }

    private List<RevenueRecognitionPlanRow> prependZeroRowsBeforeFirstPeriod(
            List<RevenueRecognitionPlanRow> planRows, RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        if (planRows.isEmpty()) {
            return planRows;
        }
        LocalDate arrangementEffectiveDate = revenueRecognitionPlanInput.getArrangementEffectiveDate();
        if (arrangementEffectiveDate == null) {
            return planRows;
        }

        CalendarConfig calendarConfig = calendarConfigOrDefault(revenueRecognitionPlanInput);
        AccountingPeriod arrangementPeriod =
                accountingPeriodDateAdapter.toActgPeriod(arrangementEffectiveDate, calendarConfig);
        AccountingPeriod firstPlanPeriod = accountingPeriodDateAdapter.toActgPeriod(
                planRows.get(0).getPeriodStartDate(), calendarConfig);

        if (!accountingPeriodDateAdapter.isBefore(
                arrangementPeriod.getStartDate(), firstPlanPeriod.getStartDate())) {
            return planRows;
        }

        List<AccountingPeriod> leadingPeriods = accountingPeriodDateAdapter.periodsBetween(
                arrangementPeriod, firstPlanPeriod, true, false, calendarConfig);

        List<RevenueRecognitionPlanRow> result = new ArrayList<>();
        for (AccountingPeriod leadingPeriod : leadingPeriods) {
            result.add(createPlanRow(leadingPeriod, BigDecimal.ZERO));
        }
        result.addAll(planRows);
        return result;
    }

    private RevenueRecognitionPlanInput copyPlanInput(RevenueRecognitionPlanInput source) {
        RevenueRecognitionPlanInput copy = new RevenueRecognitionPlanInput();
        copy.setRecognitionRuleId(source.getRecognitionRuleId());
        copy.setPlanType(source.getPlanType());
        copy.setStartDate(source.getStartDate());
        copy.setEndDate(source.getEndDate());
        copy.setAmount(source.getAmount());
        copy.setEndDateInclusive(source.isEndDateInclusive());
        copy.setExactStartDays(source.isExactStartDays());
        copy.setTerm(source.getTerm());
        copy.setRecognizedOn(source.getRecognizedOn());
        copy.setCalendarConfig(source.getCalendarConfig());
        copy.setArrangementEffectiveDate(source.getArrangementEffectiveDate());
        copy.setMaxContractYears(source.getMaxContractYears());
        copy.setStartDateInclusive(source.isStartDateInclusive());
        copy.setEndDateAdjustedForTerm(source.isEndDateAdjustedForTerm());
        copy.setTrue30StartDateInclusive(source.isTrue30StartDateInclusive());
        copy.setDereferenceRuleHasPriority(source.isDereferenceRuleHasPriority());
        return copy;
    }

    public static class DailyStyleDays {
        private long firstDays;
        private long lastDays;
        private long totalDays;

        public long getFirstDays() {
            return firstDays;
        }

        public void setFirstDays(long firstDays) {
            this.firstDays = firstDays;
        }

        public long getLastDays() {
            return lastDays;
        }

        public void setLastDays(long lastDays) {
            this.lastDays = lastDays;
        }

        public long getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(long totalDays) {
            this.totalDays = totalDays;
        }
    }
}
