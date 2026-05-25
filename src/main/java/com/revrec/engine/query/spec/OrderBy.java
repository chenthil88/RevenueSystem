package com.revrec.engine.query.spec;

public record OrderBy(String column, boolean ascending) {

    public static OrderBy asc(String column) {
        return new OrderBy(column, true);
    }

    public static OrderBy desc(String column) {
        return new OrderBy(column, false);
    }
}
