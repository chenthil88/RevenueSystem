package com.revrec.engine.common.filter;

/**
 * Filter criterion with an {@code IsActive} flag from metadata tables.
 */
public interface ActiveFieldFilter extends FieldFilter {

    Boolean isActive();

    @Override
    default boolean isFilterActive() {
        return Boolean.TRUE.equals(isActive());
    }
}
