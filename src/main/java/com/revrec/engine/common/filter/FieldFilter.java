package com.revrec.engine.common.filter;

/**
 * Common filter criterion shape shared by metadata filter tables such as
 * {@code PerformanceObligationRuleFilter} and {@code RevenueContractGroupingFilter}.
 */
public interface FieldFilter {

    String filterFieldName();

    String filterOperator();

    String filterValue();

    default boolean isFilterActive() {
        return true;
    }
}
