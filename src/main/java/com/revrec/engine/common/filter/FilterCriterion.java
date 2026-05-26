package com.revrec.engine.common.filter;

/**
 * Simple immutable {@link FieldFilter} for ad-hoc filtering or tests.
 */
public record FilterCriterion(String filterFieldName, String filterOperator, String filterValue)
        implements FieldFilter {

    public FilterCriterion {
        if (filterFieldName == null || filterFieldName.isBlank()) {
            throw new IllegalArgumentException("filterFieldName must not be blank");
        }
        if (filterOperator == null || filterOperator.isBlank()) {
            throw new IllegalArgumentException("filterOperator must not be blank");
        }
    }
}
