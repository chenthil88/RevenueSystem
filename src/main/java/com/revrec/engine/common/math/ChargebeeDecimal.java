package com.revrec.engine.common.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable wrapper around {@link BigDecimal} with readable comparison helpers.
 * {@link BigDecimal} is final and cannot be extended.
 */
public final class ChargebeeDecimal implements Serializable {

    public static final ChargebeeDecimal ZERO = new ChargebeeDecimal(BigDecimal.ZERO);
    public static final ChargebeeDecimal ONE = new ChargebeeDecimal(BigDecimal.ONE);
    public static final ChargebeeDecimal HUNDRED = new ChargebeeDecimal(new BigDecimal("100"));

    private final BigDecimal value;

    private ChargebeeDecimal(BigDecimal value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    public static ChargebeeDecimal of(BigDecimal value) {
        return value == null ? ZERO : new ChargebeeDecimal(value);
    }

    public static ChargebeeDecimal nullToZero(BigDecimal value) {
        return of(value);
    }

    public static ChargebeeDecimal nullToZero(ChargebeeDecimal value) {
        return value == null ? ZERO : value;
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    public boolean isGreaterThan(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return value.compareTo(other.value) < 0;
    }

    public boolean isEqual(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return value.compareTo(other.value) == 0;
    }

    public boolean isGreaterThanEqualTo(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return value.compareTo(other.value) >= 0;
    }

    public boolean isLessThanEqualTo(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return value.compareTo(other.value) <= 0;
    }

    public ChargebeeDecimal add(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return new ChargebeeDecimal(value.add(other.value));
    }

    public ChargebeeDecimal subtract(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return new ChargebeeDecimal(value.subtract(other.value));
    }

    public ChargebeeDecimal multiply(ChargebeeDecimal other) {
        Objects.requireNonNull(other, "other");
        return new ChargebeeDecimal(value.multiply(other.value));
    }

    public ChargebeeDecimal divide(ChargebeeDecimal other, int scale, RoundingMode roundingMode) {
        Objects.requireNonNull(other, "other");
        Objects.requireNonNull(roundingMode, "roundingMode");
        return new ChargebeeDecimal(value.divide(other.value, scale, roundingMode));
    }

    public ChargebeeDecimal abs() {
        return new ChargebeeDecimal(value.abs());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ChargebeeDecimal chargebeeDecimal)) {
            return false;
        }
        return isEqual(chargebeeDecimal);
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
