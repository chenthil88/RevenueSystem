package com.revrec.engine.domain.revenuecontractbatchcollection.context;

import com.revrec.engine.common.metadataservice.CurrentOpenPeriod.CurrentOpenPeriodService;
import com.revrec.engine.domain.metadataservice.CurrentOpenPeriod.CurrentOpenPeriodRecord;
import com.revrec.engine.integration.nosql.NoSqlRecordServer;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * Creates, publishes, and attaches tenant/workflow batch context for use across services and nodes.
 */
@Service
public class RevenueContractBatchContextService {

    private final NoSqlRecordServer noSqlRecordServer;
    private final CurrentOpenPeriodService currentOpenPeriodService;

    public RevenueContractBatchContextService(
            NoSqlRecordServer noSqlRecordServer, CurrentOpenPeriodService currentOpenPeriodService) {
        this.noSqlRecordServer = noSqlRecordServer;
        this.currentOpenPeriodService = currentOpenPeriodService;
    }

    /**
     * Storage key: {@code tenantId:workflowId} when workflow id is present, otherwise {@code tenantId:batch:batchId}.
     */
    public static String storageKey(String tenantId, String workflowId, Long batchId) {
        Objects.requireNonNull(tenantId, "tenantId");
        if (workflowId != null && !workflowId.isBlank()) {
            return tenantId + ":" + workflowId;
        }
        Objects.requireNonNull(batchId, "batchId");
        return tenantId + ":batch:" + batchId;
    }

    /**
     * Builds context, resolves open period when not supplied, publishes to Redis, and binds to this node.
     */
    public RevenueContractBatchContext initializeAndPublish(
            String tenantId,
            String workflowId,
            Long batchId,
            Long openAccountPeriodId,
            Long organizationId,
            Long bookId) {
        RevenueContractBatchContext revenueContractBatchContext = new RevenueContractBatchContext();
        revenueContractBatchContext.setTenantId(tenantId);
        revenueContractBatchContext.setWorkflowId(workflowId);
        revenueContractBatchContext.setBatchId(batchId);
        revenueContractBatchContext.setOrganizationId(organizationId);
        revenueContractBatchContext.setBookId(bookId);
        revenueContractBatchContext.setOpenAccountPeriodId(
                resolveOpenAccountPeriodId(openAccountPeriodId, organizationId, bookId));

        publish(revenueContractBatchContext);
        RevenueContractBatchContextHolder.set(revenueContractBatchContext);
        return revenueContractBatchContext;
    }

    /**
     * Loads context from Redis onto this node (call at the start of remote workflow steps).
     */
    public RevenueContractBatchContext attach(String tenantId, String workflowId, Long batchId) {
        String storageKey = storageKey(tenantId, workflowId, batchId);
        RevenueContractBatchContext revenueContractBatchContext = noSqlRecordServer
                .get(
                        RevenueContractBatchContext.TABLE_NAME,
                        storageKey,
                        RevenueContractBatchContext.class)
                .orElseThrow(() -> new IllegalStateException(
                        "RevenueContractBatchContext not found for key=" + storageKey));
        RevenueContractBatchContextHolder.set(revenueContractBatchContext);
        return revenueContractBatchContext;
    }

    public void publish(RevenueContractBatchContext revenueContractBatchContext) {
        Objects.requireNonNull(revenueContractBatchContext.getTenantId(), "tenantId");
        String storageKey = storageKey(
                revenueContractBatchContext.getTenantId(),
                revenueContractBatchContext.getWorkflowId(),
                revenueContractBatchContext.getBatchId());
        noSqlRecordServer.put(RevenueContractBatchContext.TABLE_NAME, storageKey, revenueContractBatchContext);
    }

    public RevenueContractBatchContext getRequired() {
        return RevenueContractBatchContextHolder.getRequired();
    }

    public Long getOpenAccountPeriodId() {
        Long openAccountPeriodId = getRequired().getOpenAccountPeriodId();
        if (openAccountPeriodId == null) {
            throw new IllegalStateException("openAccountPeriodId is not set in RevenueContractBatchContext");
        }
        return openAccountPeriodId;
    }

    public void clearLocal() {
        RevenueContractBatchContextHolder.clear();
    }

    public void clearAndEvict(String tenantId, String workflowId, Long batchId) {
        clearLocal();
        noSqlRecordServer.evict(RevenueContractBatchContext.TABLE_NAME, storageKey(tenantId, workflowId, batchId));
    }

    public void runWithContext(RevenueContractBatchContext revenueContractBatchContext, Runnable action) {
        runWithContext(revenueContractBatchContext, () -> {
            action.run();
            return null;
        });
    }

    public <T> T runWithContext(RevenueContractBatchContext revenueContractBatchContext, Supplier<T> action) {
        RevenueContractBatchContext previousContext = RevenueContractBatchContextHolder.get().orElse(null);
        try {
            RevenueContractBatchContextHolder.set(revenueContractBatchContext);
            return action.get();
        } finally {
            if (previousContext != null) {
                RevenueContractBatchContextHolder.set(previousContext);
            } else {
                RevenueContractBatchContextHolder.clear();
            }
        }
    }

    private Long resolveOpenAccountPeriodId(
            Long openAccountPeriodId, Long organizationId, Long bookId) {
        if (openAccountPeriodId != null) {
            return openAccountPeriodId;
        }
        return currentOpenPeriodService
                .findOpenPeriodByOrganizationAndBook(organizationId, bookId)
                .map(CurrentOpenPeriodRecord::openPeriodId)
                .orElseThrow(() -> new IllegalStateException(
                        "openAccountPeriodId is required when no CurrentOpenPeriod exists for organizationId="
                                + organizationId
                                + ", bookId="
                                + bookId));
    }
}
