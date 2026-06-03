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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApportionedRatableRevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private static final Logger log = LoggerFactory.getLogger(ApportionedRatableRevenueRecognitionPlanStrategy.class);

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public ApportionedRatableRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return !revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput)
                && revenueRecognitionPlanUtilityService.isDefaultPlanType(revenueRecognitionPlanInput);
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        if (startDate != null
                && endDate != null
                && accountingPeriodDateAdapter.isBefore(endDate, startDate)) {
            log.warn("ApportionedRatablePlan: endDate {} is before startDate {}", endDate, startDate);
            return List.of();
        }

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        BigDecimal originalAmount =
                revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);

        RevenueRecognitionPlanUtilityService.DailyStyleDays dailyStyleDays =
                revenueRecognitionPlanUtilityService.calculateDailyStyleFirstAndLastDays(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        List<AccountingPeriod> periods =
                revenueRecognitionPlanUtilityService.resolveRatablePeriods(
                        revenueRecognitionPlanInput, startPeriod, endPeriod);

        long totalPeriods = startDate.equals(endDate) ? 1 : periods.size();

        BigDecimal firstPercent = dailyStyleDays.getFirstDays() < 28
                ? revenueRecognitionPlanUtilityService.divide(
                        BigDecimal.valueOf(dailyStyleDays.getFirstDays()),
                        BigDecimal.valueOf(dailyStyleDays.getTotalDays()))
                : null;
        BigDecimal lastPercent = dailyStyleDays.getLastDays() < 28
                ? revenueRecognitionPlanUtilityService.divide(
                        BigDecimal.valueOf(dailyStyleDays.getLastDays()),
                        BigDecimal.valueOf(dailyStyleDays.getTotalDays()))
                : null;

        BigDecimal remainingPercent = BigDecimal.ONE
                .subtract(revenueRecognitionPlanUtilityService.valueOrZero(firstPercent))
                .subtract(revenueRecognitionPlanUtilityService.valueOrZero(lastPercent));

        long remainingDuration = totalPeriods;
        if (lastPercent != null && totalPeriods > 1) {
            remainingDuration -= 1;
        }
        if (firstPercent != null) {
            remainingDuration -= 1;
        }

        BigDecimal monthlyPercent = remainingDuration > 0
                ? revenueRecognitionPlanUtilityService.divide(
                        remainingPercent, BigDecimal.valueOf(remainingDuration))
                : BigDecimal.ZERO;

        if (dailyStyleDays.getFirstDays() >= 28) {
            firstPercent = monthlyPercent;
        }
        if (dailyStyleDays.getLastDays() >= 28) {
            lastPercent = monthlyPercent;
        }

        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();
        for (int index = 0; index < periods.size(); index++) {
            AccountingPeriod period = periods.get(index);
            boolean isFirst = index == 0;
            boolean isLast = index == periods.size() - 1;
            BigDecimal percent;

            if (isFirst) {
                percent = firstPercent != null ? firstPercent : monthlyPercent;
            } else if (isLast) {
                percent = lastPercent != null ? lastPercent : monthlyPercent;
            } else {
                percent = monthlyPercent;
            }

            BigDecimal periodAmount = originalAmount.multiply(percent);
            RevenueRecognitionPlanRow revenueRecognitionPlanRow =
                    revenueRecognitionPlanUtilityService.createPlanRow(period, periodAmount);
            revenueRecognitionPlanRow.setPercentRecognized(percent);
            planRows.add(revenueRecognitionPlanRow);
        }

        return planRows;
    }
}
