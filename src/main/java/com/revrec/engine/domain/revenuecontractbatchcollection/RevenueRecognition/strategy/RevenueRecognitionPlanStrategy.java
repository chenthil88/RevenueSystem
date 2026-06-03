package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.strategy;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanInput;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanRow;
import java.util.List;

/**
 * Revenue recognition plan algorithm selected by recognition rule and plan type.
 */
public interface RevenueRecognitionPlanStrategy {

    boolean supports(RevenueRecognitionPlanInput revenueRecognitionPlanInput);

    List<RevenueRecognitionPlanRow> buildPlan(RevenueRecognitionPlanInput revenueRecognitionPlanInput);
}
