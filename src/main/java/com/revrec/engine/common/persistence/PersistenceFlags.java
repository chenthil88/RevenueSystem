package com.revrec.engine.common.persistence;

/**
 * In-memory persistence intent for Aurora PostgreSQL row types ({@code *Record}). Not stored in the database.
 *
 * <p>Add {@code Boolean isUpdate} and {@code Boolean isInsert} as the last components on any record
 * that participates in batch insert/update, implement this interface on the row interface, and
 * normalize flags in the record compact constructor:
 *
 * <pre>{@code
 * public record ExampleRecord(..., Boolean isUpdate, Boolean isInsert) implements Example, PersistenceFlags {
 *     public ExampleRecord {
 *         isUpdate = PersistenceFlags.normalizeUpdate(isUpdate);
 *         isInsert = PersistenceFlags.normalizeInsert(isInsert);
 *     }
 * }
 * }</pre>
 *
 * <p>JDBC mappers should pass {@code false, false} for rows loaded from the database.
 */
public interface PersistenceFlags {

    /** When {@code true}, persist by updating an existing row. */
    Boolean isUpdate();

    /** When {@code true}, persist by inserting a new row. */
    Boolean isInsert();

    default boolean shouldUpdate() {
        return normalizeUpdate(isUpdate());
    }

    default boolean shouldInsert() {
        return normalizeInsert(isInsert());
    }

    /** Values to pass from mappers for database-loaded rows. */
    static boolean notUpdate() {
        return false;
    }

    /** Values to pass from mappers for database-loaded rows. */
    static boolean notInsert() {
        return false;
    }

    static boolean normalizeUpdate(Boolean isUpdate) {
        return Boolean.TRUE.equals(isUpdate);
    }

    static boolean normalizeInsert(Boolean isInsert) {
        return Boolean.TRUE.equals(isInsert);
    }
}
