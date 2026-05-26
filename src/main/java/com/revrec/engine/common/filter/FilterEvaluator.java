package com.revrec.engine.common.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

final class FilterEvaluator {

    private FilterEvaluator() {}

    static boolean matches(Object fieldValue, FieldFilter filter) {
        FilterOperator operator = FilterOperator.from(filter.filterOperator());
        return switch (operator) {
            case IS_NULL -> fieldValue == null;
            case IS_NOT_NULL -> fieldValue != null;
            case EQ -> compare(fieldValue, filter.filterValue()) == 0;
            case NE -> compare(fieldValue, filter.filterValue()) != 0;
            case GT -> compare(fieldValue, filter.filterValue()) > 0;
            case LT -> compare(fieldValue, filter.filterValue()) < 0;
            case GTE -> compare(fieldValue, filter.filterValue()) >= 0;
            case LTE -> compare(fieldValue, filter.filterValue()) <= 0;
            case LIKE -> like(fieldValue, filter.filterValue());
            case NOT_LIKE -> !like(fieldValue, filter.filterValue());
            case IN -> in(fieldValue, filter.filterValue());
            case NOT_IN -> !in(fieldValue, filter.filterValue());
            case BETWEEN -> between(fieldValue, filter.filterValue());
            case NOT_BETWEEN -> !between(fieldValue, filter.filterValue());
        };
    }

    private static int compare(Object fieldValue, String filterValue) {
        if (fieldValue == null) {
            return filterValue == null ? 0 : -1;
        }
        if (filterValue == null) {
            return 1;
        }
        Comparable<Object> left = toComparable(fieldValue);
        Comparable<Object> right = toComparable(coerce(filterValue, fieldValue.getClass()));
        return Comparator.<Comparable<Object>>nullsLast(Comparator.naturalOrder()).compare(left, right);
    }

    private static boolean like(Object fieldValue, String pattern) {
        if (fieldValue == null || pattern == null) {
            return false;
        }
        String regex = Pattern.quote(pattern)
                .replace("%", "\\E.*\\Q")
                .replace("_", "\\E.\\Q");
        regex = "^\\Q" + regex + "\\E$";
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(String.valueOf(fieldValue)).matches();
    }

    private static boolean in(Object fieldValue, String filterValue) {
        List<String> values = splitValues(filterValue);
        if (fieldValue == null) {
            return values.stream().anyMatch("null"::equalsIgnoreCase);
        }
        return values.stream().anyMatch(value -> compare(fieldValue, value) == 0);
    }

    private static boolean between(Object fieldValue, String filterValue) {
        List<String> bounds = splitValues(filterValue);
        if (bounds.size() != 2) {
            throw new IllegalArgumentException("BETWEEN filterValue must contain exactly two values: " + filterValue);
        }
        return compare(fieldValue, bounds.get(0)) >= 0 && compare(fieldValue, bounds.get(1)) <= 0;
    }

    private static List<String> splitValues(String filterValue) {
        if (filterValue == null || filterValue.isBlank()) {
            return List.of();
        }
        return Arrays.stream(filterValue.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private static Comparable<Object> toComparable(Object value) {
        if (value instanceof Comparable<?> comparable) {
            return (Comparable<Object>) comparable;
        }
        return String.valueOf(value);
    }

    private static Object coerce(String rawValue, Class<?> targetType) {
        if (rawValue == null) {
            return null;
        }
        String trimmed = rawValue.trim();
        if (targetType == String.class) {
            return trimmed;
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(trimmed);
        }
        if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(trimmed);
        }
        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(trimmed);
        }
        if (targetType == BigDecimal.class) {
            return new BigDecimal(trimmed);
        }
        if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(trimmed);
        }
        if (targetType == Float.class || targetType == float.class) {
            return Float.parseFloat(trimmed);
        }
        if (targetType == LocalDate.class) {
            return parseDate(trimmed);
        }
        if (targetType == LocalDateTime.class) {
            return parseDateTime(trimmed);
        }
        if (Number.class.isAssignableFrom(targetType)) {
            return new BigDecimal(trimmed);
        }
        if (Enum.class.isAssignableFrom(targetType)) {
            Object[] constants = targetType.getEnumConstants();
            for (Object constant : constants) {
                if (Objects.equals(constant.toString(), trimmed)
                        || ((Enum<?>) constant).name().equalsIgnoreCase(trimmed)) {
                    return constant;
                }
            }
            throw new IllegalArgumentException("Unknown enum value '" + trimmed + "' for " + targetType.getSimpleName());
        }
        return trimmed;
    }

    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date filterValue: " + value, ex);
        }
    }

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date-time filterValue: " + value, ex);
        }
    }
}
