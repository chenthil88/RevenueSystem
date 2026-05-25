

update RevRecStage stg
set revenueContractGroupValue = case when revenueContractGroupValue is null then 
                               NVL(groupingRule1, 'GROUPING_DEFAULT_VALUE')
                               else revenueContractGroupValue end,
    batchId = :batchId,
    revenueContractId = case when revenueContractId is null then  (select revenueContractId from revenueContractReferenceDetails rcf 
    where (rcf.salesOrderId = stg.salesOrderId) or (rcf.invoiceId = stg.invoiceId) or (rcf.invoiceId = stg.originalInvoiceId) or (rcf.salesOrderId = stg.originalSalesOrderId)) ,
                               else revenueContractId end,
Where nvl(processsedFlag, 'N') in ('N','E');
