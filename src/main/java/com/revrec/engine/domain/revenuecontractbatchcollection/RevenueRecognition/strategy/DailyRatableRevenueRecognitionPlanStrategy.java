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
public class DailyRatableRevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public DailyRatableRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return !revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput)
                && revenueRecognitionPlanUtilityService.isPlanType(
                        revenueRecognitionPlanInput, RevenueRecognitionPlanConstants.PLAN_TYPE_DAILY);
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        revenueRecognitionPlanUtilityService.validateEndDateNotBeforeStartDate(
                revenueRecognitionPlanInput, "DailyRatablePlan");

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        BigDecimal amount = revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);

        RevenueRecognitionPlanUtilityService.DailyStyleDays dailyStyleDays =
                revenueRecognitionPlanUtilityService.calculateDailyStyleFirstAndLastDays(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        List<AccountingPeriod> periods =
                revenueRecognitionPlanUtilityService.resolveRatablePeriods(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        BigDecimal dailyRate = revenueRecognitionPlanUtilityService.divide(
                amount, BigDecimal.valueOf(dailyStyleDays.getTotalDays()));

        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();
        for (int index = 0; index < periods.size(); index++) {
            AccountingPeriod period = periods.get(index);
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;
            BigDecimal periodAmount;

            if (isFirst) {
                periodAmount = dailyRate.multiply(BigDecimal.valueOf(dailyStyleDays.getFirstDays()));
            } else if (isLast) {
                periodAmount = dailyRate.multiply(BigDecimal.valueOf(dailyStyleDays.getLastDays()));
            } else {
                periodAmount = dailyRate.multiply(BigDecimal.valueOf(period.numberOfDays()));
            }

            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(period, periodAmount));
        }

        return planRows;
    }
}
