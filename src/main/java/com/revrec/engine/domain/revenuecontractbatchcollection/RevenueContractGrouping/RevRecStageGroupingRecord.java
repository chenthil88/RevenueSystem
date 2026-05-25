package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for streaming RevRecStage records for grouping processing
 */
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
