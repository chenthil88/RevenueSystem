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
public class Modified30_360RevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public Modified30_360RevenueRecognitionPlanStrategy(
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
                && revenueRecognitionPlanInput.isExactStartDays();
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        revenueRecognitionPlanUtilityService.validateEndDateNotBeforeStartDate(
                revenueRecognitionPlanInput, "Modified30_360Plan");

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        BigDecimal originalAmount =
                revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);

        RevenueRecognitionPlanUtilityService.DailyStyleDays dailyStyleDays =
                revenueRecognitionPlanUtilityService.calculateDailyStyleFirstAndLastDays(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        long firstDays = dailyStyleDays.getFirstDays();
        long lastDays = dailyStyleDays.getLastDays();
        long totalContractRevenueDays = dailyStyleDays.getTotalDays();
        if (startDate.equals(endDate)) {
            totalContractRevenueDays = 1;
        }

        List<AccountingPeriod> periods =
                revenueRecognitionPlanUtilityService.resolveRatablePeriods(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        BigDecimal totalTerms = resolveTotalTerms(
                revenueRecognitionPlanInput, startDate, endDate, startPeriod, endPeriod, periods, firstDays, lastDays);

        if (periods.size() == 1 && startPeriod.equals(endPeriod)) {
            totalContractRevenueDays = 1;
            firstDays = startPeriod.numberOfDays();
            totalTerms = BigDecimal.ONE;
        }

        if (firstDays == 0 && lastDays == 0 && totalContractRevenueDays == 1) {
            firstDays = 1;
        }

        BigDecimal amountPerTerm = revenueRecognitionPlanUtilityService.divide(originalAmount, totalTerms);
        BigDecimal cumulativeAmount = BigDecimal.ZERO;
        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();

        for (int index = 0; index < periods.size(); index++) {
            AccountingPeriod period = periods.get(index);
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;
            BigDecimal periodAmount;

            if (isFirst) {
                BigDecimal dailyAmountInStartMonth = revenueRecognitionPlanUtilityService.divide(
                        amountPerTerm, BigDecimal.valueOf(startPeriod.numberOfDays()));
                periodAmount = dailyAmountInStartMonth.multiply(BigDecimal.valueOf(firstDays));
            } else if (isLast) {
                periodAmount = originalAmount.subtract(cumulativeAmount);
            } else {
                periodAmount = amountPerTerm;
            }

            periodAmount = revenueRecognitionPlanUtilityService.scaleAmount(periodAmount);
            cumulativeAmount = cumulativeAmount.add(periodAmount);
            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(period, periodAmount));
        }

        return planRows;
    }

    private BigDecimal resolveTotalTerms(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput,
            LocalDate startDate,
            LocalDate endDate,
            AccountingPeriod startPeriod,
            AccountingPeriod endPeriod,
            List<AccountingPeriod> periods,
            long firstDays,
            long lastDays) {
        if (revenueRecognitionPlanInput.getTerm() != null) {
            return revenueRecognitionPlanInput.getTerm();
        }

        long totalDays = 0;
        for (int index = 0; index < periods.size(); index++) {
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;

            if (isFirst) {
                if (firstDays > 30) {
                    totalDays += 30;
                } else {
                    int actualMonthDays = startPeriod.numberOfDays() <= 30
                            ? 30
                            : startPeriod.numberOfDays() - 1;
                    totalDays += actualMonthDays - (accountingPeriodDateAdapter.getDayOfMonth(startDate) - 1);
                }
            } else if (isLast) {
                if (lastDays > 30) {
                    totalDays += 30;
                } else if (accountingPeriodDateAdapter.toStartOfMonth(startDate).equals(startDate)
                        && accountingPeriodDateAdapter.isFebruary(endDate)
                        && endDate.equals(endPeriod.getEndDate())) {
                    totalDays += 30;
                } else {
                    totalDays += lastDays;
                }
            } else {
                totalDays += 30;
            }
        }

        return revenueRecognitionPlanUtilityService.divide(BigDecimal.valueOf(totalDays), BigDecimal.valueOf(30));
    }
}
