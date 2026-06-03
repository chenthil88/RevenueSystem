package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.strategy;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.RevenueRecognitionPlanConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.RevenueRecognitionPlanUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.adapter.AccountingPeriodDateAdapter;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.adapter.AccountingPeriodDateAdapter.DateComponents;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.AccountingPeriod;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.CalendarConfig;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanInput;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanRow;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class True30_360RevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public True30_360RevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return !revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput)
                && revenueRecognitionPlanUtilityService.isPlanType(
                        revenueRecognitionPlanInput, RevenueRecognitionPlanConstants.PLAN_TYPE_TRUE_30_360);
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        revenueRecognitionPlanUtilityService.validateEndDateNotBeforeStartDate(
                revenueRecognitionPlanInput, "True30_360Plan");

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        BigDecimal originalAmount =
                revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());
        boolean startDateInclusive = revenueRecognitionPlanInput.isStartDateInclusive();
        boolean endDateAdjustedForTerm = revenueRecognitionPlanInput.isEndDateAdjustedForTerm();

        DateComponents startComponents = accountingPeriodDateAdapter.toComponents(startDate);
        DateComponents endComponents = accountingPeriodDateAdapter.toComponents(endDate);
        boolean movedEndDay = false;

        int startDayMax30 = Math.min(30, startComponents.getDay());
        int endDayMax30 = Math.min(30, endComponents.getDay() + (startDateInclusive ? 1 : 0));

        if (startDate.equals(endDate) && startDateInclusive) {
            // keep dates as-is
        } else if (startComponents.getDay() == endComponents.getDay()
                && !accountingPeriodDateAdapter.isEndOfMonth(startDate)
                && !startDateInclusive) {
            startDate = accountingPeriodDateAdapter.addDays(startDate, 1);
            startComponents = accountingPeriodDateAdapter.toComponents(startDate);
            startDayMax30 = Math.min(30, startComponents.getDay());
        } else if (startComponents.getDay() == endComponents.getDay()
                && startDateInclusive
                && !endDateAdjustedForTerm) {
            endDate = accountingPeriodDateAdapter.addDays(endDate, -1);
            endComponents = accountingPeriodDateAdapter.toComponents(endDate);
            endDayMax30 = Math.min(30, endComponents.getDay() + 1);
            movedEndDay = true;
        }

        int numYears = endComponents.getYear() - startComponents.getYear();
        int numMonths = endComponents.getMonth() - startComponents.getMonth();
        int adjustedDays = endDayMax30 - startDayMax30;

        adjustedDays = applySpecialDayAdjustments(
                startDate,
                endDate,
                startComponents,
                endComponents,
                startDateInclusive,
                movedEndDay,
                adjustedDays);

        adjustedDays = Math.min(30, adjustedDays);
        long revenueDays = 360L * numYears + 30L * numMonths + adjustedDays;

        if (endDateAdjustedForTerm) {
            long rounded = Math.round(revenueDays / 30.0) * 30L;
            if (rounded > 0) {
                revenueDays = rounded;
            }
        }

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);
        List<AccountingPeriod> periods = accountingPeriodDateAdapter.periodsBetween(
                startPeriod, endPeriod, true, true, calendarConfig);

        long daysBeforeNextPeriod = calculateDaysBeforeNextPeriod(
                startDate, endDate, startComponents, endComponents, startDayMax30, movedEndDay);

        long realPeriodCount = Math.max(1, Math.round(revenueDays / 30.0));
        BigDecimal amountPerDay = revenueRecognitionPlanUtilityService.divide(
                originalAmount, BigDecimal.valueOf(revenueDays));
        BigDecimal runningAmount = BigDecimal.ZERO;

        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();
        for (int index = 0; index < periods.size(); index++) {
            AccountingPeriod period = periods.get(index);
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;
            BigDecimal periodAmount;

            if (isFirst) {
                periodAmount = amountPerDay.multiply(BigDecimal.valueOf(daysBeforeNextPeriod));
                if (realPeriodCount == 1
                        && startComponents.getMonth() == endComponents.getMonth()
                        && startComponents.getYear() == endComponents.getYear()) {
                    periodAmount = originalAmount;
                }
            } else if (isLast) {
                periodAmount = originalAmount.subtract(runningAmount);
            } else {
                periodAmount = amountPerDay.multiply(BigDecimal.valueOf(30));
            }

            periodAmount = revenueRecognitionPlanUtilityService.scaleAmount(periodAmount);
            runningAmount = runningAmount.add(periodAmount);
            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(period, periodAmount));
        }

        return planRows;
    }

    private int applySpecialDayAdjustments(
            LocalDate startDate,
            LocalDate endDate,
            DateComponents startComponents,
            DateComponents endComponents,
            boolean startDateInclusive,
            boolean movedEndDay,
            int adjustedDays) {
        int startDay = startComponents.getDay();
        int endDay = endComponents.getDay();

        if (endComponents.getMonth() == 2 && endDay == 27 && startDay > 27) {
            return 0;
        }
        if (startComponents.getMonth() == 2 && startDay == 28 && endDay >= 28) {
            return 0;
        }
        if (endComponents.getMonth() == 2
                && endDay == 28
                && accountingPeriodDateAdapter.isLeapYear(endComponents.getYear())) {
            return 0;
        }
        if (startComponents.getMonth() == 2
                && startDay == 29
                && accountingPeriodDateAdapter.isLeapYear(startComponents.getYear())
                && endDay >= 30) {
            return 0;
        }
        if (startComponents.getMonth() == 2
                && endComponents.getMonth() == 2
                && endDay == accountingPeriodDateAdapter.getDaysInMonth(2, endComponents.getYear())) {
            return 30;
        }
        if (startDay == 1
                && endComponents.getMonth() == 2
                && endDay >= 28) {
            return 30;
        }
        if (!startDateInclusive && startDay == endDay + 1) {
            return 0;
        }
        if (startComponents.getYear() == endComponents.getYear()
                && startComponents.getMonth() != endComponents.getMonth()
                && endDay == accountingPeriodDateAdapter.getDaysInMonth(
                        endComponents.getMonth(), endComponents.getYear())) {
            return adjustedDays + 1;
        }
        return adjustedDays;
    }

    private long calculateDaysBeforeNextPeriod(
            LocalDate startDate,
            LocalDate endDate,
            DateComponents startComponents,
            DateComponents endComponents,
            int startDayMax30,
            boolean movedEndDay) {
        if (startDayMax30 >= 30 || isLastDayOfFebruary(startDate)) {
            return 1;
        }

        long daysBeforeNextPeriod = 30L - startDayMax30 + 1;

        if (startComponents.getMonth() == 2
                && endComponents.getDay() <= 28
                && isLastDayOfFebruary(startDate)) {
            if (startComponents.getDay() == endComponents.getDay() + 1 && movedEndDay) {
                return 2;
            }
            return Math.min(
                    30L - startComponents.getDay() + 1,
                    30L - endComponents.getDay());
        }

        return daysBeforeNextPeriod;
    }

    private boolean isLastDayOfFebruary(LocalDate date) {
        return date.getMonthValue() == 2 && date.equals(accountingPeriodDateAdapter.toEndOfMonth(date));
    }
}
