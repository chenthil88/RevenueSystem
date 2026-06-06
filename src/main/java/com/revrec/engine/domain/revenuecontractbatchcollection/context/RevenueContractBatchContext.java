package com.revrec.engine.domain.revenuecontractbatchcollection.context;

import java.io.Serializable;

/**
 * Tenant- and workflow-scoped batch run context shared across batch collection services and nodes.
 *
 * <p>Published to Redis at workflow start; each processing node attaches before running batch steps.
 */
public class RevenueContractBatchContext implements Serializable {

    public static final String TABLE_NAME = "RevenueContractBatchContext";

    private String tenantId;
    private String workflowId;
    private Long batchId;
    private Long openAccountPeriodId;
    private Long organizationId;
    private Long bookId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getOpenAccountPeriodId() {
        return openAccountPeriodId;
    }

    public void setOpenAccountPeriodId(Long openAccountPeriodId) {
        this.openAccountPeriodId = openAccountPeriodId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
