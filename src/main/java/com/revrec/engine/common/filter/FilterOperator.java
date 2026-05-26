package com.revrec.engine.common.filter;

import java.util.Locale;

public enum FilterOperator {
    EQ("="),
    NE("<>"),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    IN("IN"),
    NOT_IN("NOT IN"),
    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOT BETWEEN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL");

    private final String symbol;

    FilterOperator(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public static FilterOperator from(String operator) {
        if (operator == null || operator.isBlank()) {
            throw new IllegalArgumentException("filterOperator must not be blank");
        }
        String normalized = operator.trim().toUpperCase(Locale.ROOT);
        for (FilterOperator value : values()) {
            if (value.symbol.equals(normalized)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unsupported filterOperator: " + operator);
    }
}
