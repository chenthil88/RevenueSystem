package com.revrec.engine.domain.service.RevenueContractOrder;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAttributes.RevenueContractOrderAttributesRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecord;
import java.io.Serializable;

/**
 * Per-line aggregate of {@link RevenueContractOrderRecords} rows keyed by revenue contract line id.
 */
public class RevenueContractOrderLineRecords implements Serializable {

    private Long revenueContractLineId;
    private RevenueContractOrderDetailsRecord revenueContractOrderDetailsRecord;
    private RevenueContractOrderAttributesRecord revenueContractOrderAttributesRecord;
    private RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord;
    private RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord;

    public Long getRevenueContractLineId() {
        return revenueContractLineId;
    }

    public void setRevenueContractLineId(Long revenueContractLineId) {
        this.revenueContractLineId = revenueContractLineId;
    }

    public RevenueContractOrderDetailsRecord getRevenueContractOrderDetailsRecord() {
        return revenueContractOrderDetailsRecord;
    }

    public void setRevenueContractOrderDetailsRecord(
            RevenueContractOrderDetailsRecord revenueContractOrderDetailsRecord) {
        this.revenueContractOrderDetailsRecord = revenueContractOrderDetailsRecord;
    }

    public RevenueContractOrderAttributesRecord getRevenueContractOrderAttributesRecord() {
        return revenueContractOrderAttributesRecord;
    }

    public void setRevenueContractOrderAttributesRecord(
            RevenueContractOrderAttributesRecord revenueContractOrderAttributesRecord) {
        this.revenueContractOrderAttributesRecord = revenueContractOrderAttributesRecord;
    }

    public RevenueContractOrderAccountDetailsRecord getRevenueContractOrderAccountDetailsRecord() {
        return revenueContractOrderAccountDetailsRecord;
    }

    public void setRevenueContractOrderAccountDetailsRecord(
            RevenueContractOrderAccountDetailsRecord revenueContractOrderAccountDetailsRecord) {
        this.revenueContractOrderAccountDetailsRecord = revenueContractOrderAccountDetailsRecord;
    }

    public RevenueContractAllocationDetailsRecord getRevenueContractAllocationDetailsRecord() {
        return revenueContractAllocationDetailsRecord;
    }

    public void setRevenueContractAllocationDetailsRecord(
            RevenueContractAllocationDetailsRecord revenueContractAllocationDetailsRecord) {
        this.revenueContractAllocationDetailsRecord = revenueContractAllocationDetailsRecord;
    }
}
