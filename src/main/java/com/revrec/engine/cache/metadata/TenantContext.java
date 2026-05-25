package com.revrec.engine.cache.metadata;

/**
 * Thread-local holder for current tenant context.
 * Set by filters/interceptors before processing requests.
 */
public class TenantContext {

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();

    public static void setTenantId(String id) {
        tenantId.set(id);
    }

    public static String getTenantId() {
        String id = tenantId.get();
        if (id == null) {
            throw new IllegalStateException("TenantId not set in TenantContext");
        }
        return id;
    }

    public static void clear() {
        tenantId.remove();
    }
}
