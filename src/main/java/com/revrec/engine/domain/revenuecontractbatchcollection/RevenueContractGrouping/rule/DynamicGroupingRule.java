package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.rule;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model.RevRecStageGroupingRecord;

/** Evaluates a dynamic grouping value for a stage record. */
public interface DynamicGroupingRule {

    String evaluateGrouping(RevRecStageGroupingRecord record);

    String getRuleName();
}
