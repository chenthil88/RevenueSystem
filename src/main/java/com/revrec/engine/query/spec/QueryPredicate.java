package com.revrec.engine.query.spec;

/**
 * WHERE clause fragment with a bound parameter.
 */
public sealed interface QueryPredicate permits QueryPredicate.Eq {

    String column();

    String paramName();

    Object value();

    record Eq(String column, String paramName, Object value) implements QueryPredicate {

        public Eq(String column, Object value) {
            this(column, "w_" + column, value);
        }
    }
}
