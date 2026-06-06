package com.revrec.engine.domain.revenuecontractbatchcollection.context;

import java.util.Optional;

/**
 * Thread-local holder for the active {@link RevenueContractBatchContext} on the current processing node.
 */
public final class RevenueContractBatchContextHolder {

    private static final ThreadLocal<RevenueContractBatchContext> context = new ThreadLocal<>();

    private RevenueContractBatchContextHolder() {}

    public static void set(RevenueContractBatchContext revenueContractBatchContext) {
        context.set(revenueContractBatchContext);
    }

    public static Optional<RevenueContractBatchContext> get() {
        return Optional.ofNullable(context.get());
    }

    public static RevenueContractBatchContext getRequired() {
        RevenueContractBatchContext revenueContractBatchContext = context.get();
        if (revenueContractBatchContext == null) {
            throw new IllegalStateException("RevenueContractBatchContext is not set on this node");
        }
        return revenueContractBatchContext;
    }

    public static void clear() {
        context.remove();
    }
}
