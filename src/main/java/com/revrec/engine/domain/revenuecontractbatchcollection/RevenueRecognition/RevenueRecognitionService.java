package com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition;

import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanInput;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.model.RevenueRecognitionPlanRow;
import com.revrec.engine.domain.revenuecontractbatchcollection.RevenueRecognition.strategy.RevenueRecognitionPlanStrategy;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Batch collection orchestration for revenue recognition plan generation.
 *
 * <p>Loads contract data into {@link RevenueRecognitionPlanInput}, delegates to the plan dispatcher
 * in {@link RevenueRecognitionPlanUtilityService}, and maps {@link RevenueRecognitionPlanRow} results
 * to persistence (TODO).
 */
@Service
public class RevenueRecognitionService {

    private final RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService;
    private final List<RevenueRecognitionPlanStrategy> revenueRecognitionPlanStrategies;

    public RevenueRecognitionService(
            RevenueRecognitionPlanUtilityService revenueRecognitionPlanUtilityService,
            List<RevenueRecognitionPlanStrategy> revenueRecognitionPlanStrategies) {
        this.revenueRecognitionPlanUtilityService = revenueRecognitionPlanUtilityService;
        this.revenueRecognitionPlanStrategies = revenueRecognitionPlanStrategies;
    }

    /**
     * Runs the selected plan algorithm for the given normalized input.
     */
    public List<RevenueRecognitionPlanRow> buildRecognitionPlan(
            RevenueRecognitionPlanInput revenueRecognitionPlanInput) {
        return revenueRecognitionPlanUtilityService.buildPlan(
                revenueRecognitionPlanInput, revenueRecognitionPlanStrategies);
    }
}
