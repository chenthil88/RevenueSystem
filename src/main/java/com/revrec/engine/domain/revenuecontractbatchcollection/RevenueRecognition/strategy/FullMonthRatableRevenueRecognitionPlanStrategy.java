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
public class FullMonthRatableRevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public FullMonthRatableRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return !revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput)
                && revenueRecognitionPlanUtilityService.isPlanType(
                        revenueRecognitionPlanInput,
                        RevenueRecognitionPlanConstants.PLAN_TYPE_MONTHLY_ENDMONTH_EXCLUSIVE);
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        revenueRecognitionPlanUtilityService.validateEndDateNotBeforeStartDate(
                revenueRecognitionPlanInput, "FullMonthRatablePlan");

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        BigDecimal amount = revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);
        LocalDate endDateEndOfMonth = accountingPeriodDateAdapter.toEndOfMonth(endDate);

        AccountingPeriod finalPeriod;
        if (endPeriod.equals(startPeriod)) {
            finalPeriod = startPeriod;
        } else if (accountingPeriodDateAdapter.isBefore(endDate, endDateEndOfMonth)) {
            finalPeriod = accountingPeriodDateAdapter.priorPeriod(endPeriod, calendarConfig);
        } else {
            finalPeriod = endPeriod;
        }

        List<AccountingPeriod> periods = accountingPeriodDateAdapter.periodsBetween(
                startPeriod, finalPeriod, true, true, calendarConfig);
        int monthCount = Math.max(1, periods.size());
        BigDecimal monthlyAmount = revenueRecognitionPlanUtilityService.divide(
                amount, BigDecimal.valueOf(monthCount));

        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();
        for (AccountingPeriod period : periods) {
            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(period, monthlyAmount));
        }
        return planRows;
    }
}
