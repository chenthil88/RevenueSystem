package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

/**
 * Interface for dynamic grouping rule evaluation
 */
public interface DynamicGroupingRule {

    /**
     * Evaluate grouping value for a stage record
     */
    String evaluateGrouping(RevRecStageGroupingRecord record);

    /**
     * Get rule name
     */
    String getRuleName();
}
