package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for streaming {@code RevRecStage} records during grouping batch processing. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevRecStageGroupingRecord {
    private Long id;
    private String tenantId;
    private String transactionType;
    private String processsedFlag;
    private String errorMessage;
    private Long revenueContractId;
    private String revenueContractGroupValue;
    private Long batchId;
    private String salesOrderId;
    private String invoiceId;
    private String originalInvoiceId;
    private String originalSalesOrderId;
    private String customerNumber;
    private String customerName;
    private LocalDateTime createdAt;
}
