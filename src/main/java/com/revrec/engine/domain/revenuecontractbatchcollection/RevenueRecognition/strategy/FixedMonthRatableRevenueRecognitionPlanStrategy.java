package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.strategy;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.RevenueRecognitionPlanConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.RevenueRecognitionPlanUtilityService;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.adapter.AccountingPeriodDateAdapter;
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
public class FixedMonthRatableRevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public FixedMonthRatableRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return !revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput)
                && revenueRecognitionPlanUtilityService.isPlanType(
                        revenueRecognitionPlanInput, RevenueRecognitionPlanConstants.PLAN_TYPE_FIXED)
                && !revenueRecognitionPlanInput.isExactStartDays();
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        if (calendarConfig.isRetailCalendar()) {
            throw new IllegalArgumentException("FixedMonthRatablePlan: retail calendar is not supported");
        }

        revenueRecognitionPlanUtilityService.validateEndDateNotBeforeStartDate(
                revenueRecognitionPlanInput, "FixedMonthRatablePlan");

        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        boolean endDateInclusive = revenueRecognitionPlanInput.isEndDateInclusive();
        boolean exactDays = revenueRecognitionPlanInput.isExactStartDays();
        BigDecimal amount = revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);

        long firstDays = startDate.equals(endDate)
                ? 30
                : (exactDays ? startPeriod.numberOfDays() : 30)
                        - accountingPeriodDateAdapter.getDayOfMonth(startDate)
                        + 1;

        long lastDays = startDate.equals(endDate)
                ? 0
                : accountingPeriodDateAdapter.getDayOfMonth(endDate) - (endDateInclusive ? 0 : 1);

        if (endDateInclusive
                && lastDays != 0
                && ((endDate.getMonthValue() != 2 && lastDays > 30)
                        || (endDate.getMonthValue() == 2 && endDate.equals(endPeriod.getEndDate())))) {
            lastDays = 30;
        }

        List<AccountingPeriod> periods = accountingPeriodDateAdapter.periodsBetween(
                startPeriod, endPeriod, true, true, calendarConfig);

        if (!startDate.equals(endDate) && startPeriod.equals(endPeriod)) {
            firstDays = lastDays;
            lastDays = 0;
        }

        long middlePeriodCount = Math.max(0, periods.size() - 2);
        long totalDays = firstDays + lastDays + 30 * middlePeriodCount;
        if (totalDays == 0) {
            totalDays = 1;
            firstDays = 1;
        }

        BigDecimal rate = revenueRecognitionPlanUtilityService.divide(amount, BigDecimal.valueOf(totalDays));
        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();

        for (int index = 0; index < periods.size(); index++) {
            AccountingPeriod period = periods.get(index);
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;
            BigDecimal periodAmount;

            if (isFirst) {
                periodAmount = rate.multiply(BigDecimal.valueOf(firstDays));
            } else if (isLast) {
                periodAmount = rate.multiply(BigDecimal.valueOf(lastDays));
            } else {
                periodAmount = rate.multiply(BigDecimal.valueOf(30));
            }

            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(period, periodAmount));
        }

        return planRows;
    }
}
