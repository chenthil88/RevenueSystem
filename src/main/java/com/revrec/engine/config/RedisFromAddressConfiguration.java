package com.revrec.engine.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Lettuce cluster client from comma-separated {@code REDIS_ADDRESS} URLs, following the same conventions as
 * {@code revanu/docs/REDIS_CONFIGURATION.md} (bootstrap list, {@code rediss://} for TLS, ACL gated by
 * {@code ENABLE_REDIS_ACL}).
 *
 * <p>When {@link NonBlankRedisAddressCondition} is false, Spring Boot auto-configures standalone Redis from
 * {@code spring.data.redis.host} / {@code port} instead.
 */
@Configuration
@Conditional(NonBlankRedisAddressCondition.class)
public class RedisFromAddressConfiguration {

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactoryFromAddress(
            Environment env,
            @Value("${ENABLE_REDIS_ACL:false}") boolean enableRedisAcl,
            @Value("${spring.data.redis.username:}") String springRedisUsername,
            @Value("${spring.data.redis.password:}") String springRedisPassword) {
        String redisAddress = env.getRequiredProperty(NonBlankRedisAddressCondition.PROPERTY);
        List<RedisNode> nodes = parseBootstrapNodes(redisAddress);
        if (nodes.isEmpty()) {
            throw new IllegalStateException(NonBlankRedisAddressCondition.PROPERTY + " produced no host:port nodes");
        }
        boolean useTls = anySchemeUsesTls(redisAddress);

        RedisClusterConfiguration cluster = new RedisClusterConfiguration(nodes);
        if (enableRedisAcl) {
            if (springRedisUsername != null && !springRedisUsername.isBlank()) {
                cluster.setUsername(springRedisUsername.trim());
            }
            if (springRedisPassword != null && !springRedisPassword.isBlank()) {
                cluster.setPassword(RedisPassword.of(springRedisPassword));
            }
        }

        LettuceClientConfiguration.LettuceClientConfigurationBuilder client = LettuceClientConfiguration.builder();
        if (useTls) {
            // RevRec default matches revanu dev tradeoff; use a proper trust store for production ElastiCache.
            client.useSsl().disablePeerVerification();
        }
        client.commandTimeout(java.time.Duration.ofSeconds(2));

        return new LettuceConnectionFactory(cluster, client.build());
    }

    private static boolean anySchemeUsesTls(String redisAddress) {
        for (String part : redisAddress.split(",")) {
            String p = part.trim();
            if (p.regionMatches(true, 0, "rediss:", 0, 7)) {
                return true;
            }
        }
        return false;
    }

    private static List<RedisNode> parseBootstrapNodes(String redisAddress) {
        List<RedisNode> out = new ArrayList<>();
        for (String part : redisAddress.split(",")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String hostPort = hostPortFromRedisUrl(trimmed);
            int colon = hostPort.lastIndexOf(':');
            if (colon <= 0 || colon == hostPort.length() - 1) {
                throw new IllegalStateException("Bad Redis bootstrap entry (expected host:port): " + trimmed);
            }
            String host = hostPort.substring(0, colon);
            int port = Integer.parseInt(hostPort.substring(colon + 1));
            out.add(new RedisNode(host, port));
        }
        return out;
    }

    /**
     * Accepts {@code redis://host:port} / {@code rediss://host:port}; also tolerates bare {@code host:port}.
     */
    private static String hostPortFromRedisUrl(String address) {
        String a = address.trim();
        if (a.contains("://")) {
            URI uri = URI.create(a);
            if (uri.getHost() == null) {
                throw new IllegalStateException("Could not parse host from Redis URL: " + address);
            }
            int port = uri.getPort() > 0 ? uri.getPort() : 6379;
            return uri.getHost() + ":" + port;
        }
        return a;
    }
}
