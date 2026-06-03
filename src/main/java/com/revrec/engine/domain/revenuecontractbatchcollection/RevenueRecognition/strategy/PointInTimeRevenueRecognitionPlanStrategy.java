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
public class PointInTimeRevenueRecognitionPlanStrategy implements RevenueRecognitionPlanStrategy {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final AccountingPeriodDateAdapter accountingPeriodDateAdapter;

    public PointInTimeRevenueRecognitionPlanStrategy(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            AccountingPeriodDateAdapter accountingPeriodDateAdapter) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.accountingPeriodDateAdapter = accountingPeriodDateAdapter;
    }

    @Override
    public boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return revenueRecognitionPlanUtilityService.isPointInTime(revenueRecognitionPlanInput);
    }

    @Override
    public List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        LocalDate startDate = revenueRecognitionPlanInput.getStartDate();
        if (startDate == null) {
            throw new IllegalArgumentException("PointInTimePlan: startDate is required");
        }

        CalendarConfig calendarConfig =
                revenueRecognitionPlanUtilityService.calendarConfigOrDefault(revenueRecognitionPlanInput);
        BigDecimal amount = revenueRecognitionPlanUtilityService.nullToZero(revenueRecognitionPlanInput.getAmount());

        AccountingPeriod startPeriod = accountingPeriodDateAdapter.toActgPeriod(startDate, calendarConfig);
        List<RevenueRecognitionPlanRow> planRows = new ArrayList<>();
        planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(startPeriod, amount));

        LocalDate endDate = revenueRecognitionPlanInput.getEndDate();
        if (endDate == null) {
            return planRows;
        }

        AccountingPeriod endPeriod = accountingPeriodDateAdapter.toActgPeriod(endDate, calendarConfig);
        List<AccountingPeriod> trailingPeriods = accountingPeriodDateAdapter.periodsBetween(
                startPeriod, endPeriod, false, true, calendarConfig);
        for (AccountingPeriod trailingPeriod : trailingPeriods) {
            planRows.add(revenueRecognitionPlanUtilityService.createPlanRow(trailingPeriod, BigDecimal.ZERO));
        }

        if (RevenueRecognitionPlanConstants.RECOGNIZED_ON_END_DATE.equalsIgnoreCase(
                revenueRecognitionPlanInput.getRecognizedOn())) {
            planRows.get(0).setAmount(BigDecimal.ZERO);
            planRows.get(planRows.size() - 1).setAmount(amount);
        }

        return planRows;
    }
}
