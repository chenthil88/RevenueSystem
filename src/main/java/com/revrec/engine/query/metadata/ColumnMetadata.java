package com.revrec.engine.query.metadata;

/**
 * Physical column on a table; order in {@link TableMetadata} defines SELECT column order.
 */
public record ColumnMetadata(String name) {}
