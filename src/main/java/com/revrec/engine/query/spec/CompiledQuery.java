package com.revrec.engine.query.spec;

import java.util.Map;

/**
 * Dialect-specific SQL ready for {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate}.
 */
public record CompiledQuery(String sql, Map<String, Object> parameters) {}
