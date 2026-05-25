# RevenueContractGrouping

## API

`RevenueContractGroupingService.applyGrouping(batchId, templateName)` → `RevenueContractGroupingResult`.

## Flow

1. Resolve `templateName` → `RevenueContractGroupingTemplate` (`Name` = :templateName, active).
2. Load lowest-`sequence` active `RevenueContractGroupingHierarchy` row for that template id.
3. Build grouping-rule SQL from `GroupingFields` (colon-separated **RevRecStage column names**, e.g. `customerName:salesOrderId` or `CustomerName:SalesOrderId`) via `GroupingRuleSqlBuilder`. Tokens are validated against `metadata/rev-rec-stage-columns.txt` (synced from `db_script.sql`).
4. `RevRecStageService.applyContractGrouping` — stamp `RevenueContractGroupValue`, `BatchId`, and resolve `revenueContractId` from existing `revenueContractReferenceDetails` where possible.
5. Stream unassigned `RevRecStage` lines (`revenueContractId` still null) ordered by `RevenueContractGroupValue`.
6. For each distinct `RevenueContractGroupValue`, allocate one `revenueContractHeaderIdSeq` id; insert `revenueContractGroupDetails`; insert deduplicated `revenueContractReferenceDetails` (sales order / invoice); bulk-update stage `revenueContractId`.

## Reference UPDATE (implemented in `RevRecStageService`)

```sql
UPDATE `RevRecStage` stg
SET `RevenueContractGroupValue` = CASE
        WHEN `RevenueContractGroupValue` IS NULL THEN IFNULL(<groupingRuleExpr>, 'GROUPING_DEFAULT_VALUE')
        ELSE `RevenueContractGroupValue` END,
    `BatchId` = :batchId,
    `revenueContractId` = CASE
        WHEN `revenueContractId` IS NULL THEN (
            SELECT rcf.`RevenueContractId` FROM `revenueContractReferenceDetails` rcf
            WHERE (rcf.`salesOrderId` = stg.`salesOrderId`)
               OR (rcf.`invoiceId` = stg.`InvoiceId`)
               OR (rcf.`invoiceId` = stg.`OriginalInvoiceId`)
               OR (rcf.`salesOrderId` = stg.`OriginalSalesOrderId`)
            LIMIT 1
        )
        ELSE `revenueContractId` END
WHERE IFNULL(stg.`processsedFlag`, 'N') IN ('N', 'E');
```
