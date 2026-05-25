package com.revrec.engine.common.filter;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads a named field from Java records or JavaBeans (case-insensitive).
 */
final class RecordFieldAccessor {

    private static final Map<String, FieldReader> READERS = new ConcurrentHashMap<>();

    private RecordFieldAccessor() {}

    static Object readField(Object record, String fieldName) {
        if (record == null) {
            throw new IllegalArgumentException("record must not be null");
        }
        if (fieldName == null || fieldName.isBlank()) {
            throw new IllegalArgumentException("filterFieldName must not be blank");
        }
        String cacheKey = record.getClass().getName() + "#" + normalize(fieldName);
        FieldReader reader = READERS.computeIfAbsent(cacheKey, ignored -> resolveReader(record.getClass(), fieldName));
        try {
            return reader.read(record);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    "Unable to read field '" + fieldName + "' from " + record.getClass().getSimpleName(), e);
        }
    }

    private static FieldReader resolveReader(Class<?> type, String fieldName) {
        String normalized = normalize(fieldName);
        if (type.isRecord()) {
            for (RecordComponent component : type.getRecordComponents()) {
                if (normalize(component.getName()).equals(normalized)) {
                    Method accessor = component.getAccessor();
                    accessor.setAccessible(true);
                    return record -> accessor.invoke(record);
                }
            }
        }
        for (Method method : type.getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            String methodName = method.getName();
            if (normalize(methodName).equals(normalized)
                    || normalize(methodName).equals("get" + normalized)
                    || normalize(methodName).equals("is" + normalized)) {
                method.setAccessible(true);
                return method::invoke;
            }
        }
        throw new IllegalArgumentException("Unknown field '" + fieldName + "' on " + type.getSimpleName());
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    private interface FieldReader {
        Object read(Object record) throws ReflectiveOperationException;
    }
}
