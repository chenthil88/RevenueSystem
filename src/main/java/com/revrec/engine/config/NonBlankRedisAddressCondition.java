package com.revrec.engine.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Matches when {@code REDIS_ADDRESS} is set to a non-blank value (cluster bootstrap list).
 */
public final class NonBlankRedisAddressCondition implements Condition {

    public static final String PROPERTY = "REDIS_ADDRESS";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String v = context.getEnvironment().getProperty(PROPERTY);
        return v != null && !v.isBlank();
    }
}
