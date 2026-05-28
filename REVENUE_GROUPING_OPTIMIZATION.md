# Revenue Contract Grouping - Performance Optimization

## Problem
Original SQL approach with subqueries didn't scale for large data volumes:
```sql
UPDATE RevRecStage stg
SET revenueContractId = CASE
WHEN revenueContractId IS NULL THEN (
    SELECT rcf.RevenueContractId FROM revenueContractReferenceDetails rcf
    WHERE (rcf.salesOrderId = stg.salesOrderId)
    OR (rcf.invoiceId = stg.InvoiceId) ...
    LIMIT 1
)
```
This caused N+1 query problem and was inefficient for millions of rows.

## Solution: Streaming + Batch Resolution

### Architecture
1. **Stream from RevRecStage** (cursor-based pagination)
   - Processes in configurable batches (default 1000)
   - Low memory footprint

2. **Enrich in Memory** (per batch)
   - Set `batchId` from input parameter
   - Evaluate `revenueContractGroupValue` using grouping rule

3. **Batch Resolve Revenue Contract IDs** (NEW)
   - Single SQL query with UNION to load all needed references
   - Replaces N per-record lookups with 1 batch query per batch
   - Dramatically reduces DB round trips

4. **Bulk Update to Aurora PostgreSQL**
   - NamedParameterJdbcTemplate.batchUpdate()
   - Configurable batch size for optimal throughput

### Key Changes

**RevenueContractReferenceService**
- Added `resolveRevenueContractIdsForBatch()` - single query for all records in batch
- Uses UNION to combine lookups on 4 different ID fields
- Map-based resolution to avoid duplicate queries

**RevenueContractGroupingStreamProcessor**
- Moved reference resolution from enrichment to batch processing
- Call batch resolver after accumulating batch (not per-record)
- Same pattern for both sequential and parallel methods

### Performance Impact

#### Before (Original SQL Subquery)
- 1M records = ~1M+ database queries
- Lock contention on RevRecStage during full table update
- Subquery executes for every row evaluation
- High memory in single UPDATE statement

#### After (Streaming + Batch Resolution)
- 1M records with batch size 1000:
  - ~1000 reads from RevRecStage (streaming)
  - ~1000 batch resolutions (1 query per 1000 records)
  - ~1000 bulk updates
  - **Total: ~3000 queries vs 1M+ before**
- **Reduction: >99% query reduction**
- Lower lock contention (smaller batches)
- Constant memory usage per batch

## Database Optimization

### Required Indexes
```sql
-- Improve RevRecStage streaming
CREATE INDEX idx_revrecstage_processedFlag_id 
ON "RevRecStage"("processsedFlag", "id")
INCLUDE ("salesOrderId", "invoiceId", "originalInvoiceId", "originalSalesOrderId");

-- Speed up reference lookups
CREATE INDEX idx_refdetails_salesOrderId 
ON "revenueContractReferenceDetails"("salesOrderId")
INCLUDE ("RevenueContractId");

CREATE INDEX idx_refdetails_invoiceId 
ON "revenueContractReferenceDetails"("invoiceId")
INCLUDE ("RevenueContractId");

CREATE INDEX idx_refdetails_originalInvoiceId 
ON "revenueContractReferenceDetails"("originalInvoiceId")
INCLUDE ("RevenueContractId");

CREATE INDEX idx_refdetails_originalSalesOrderId 
ON "revenueContractReferenceDetails"("originalSalesOrderId")
INCLUDE ("RevenueContractId");

-- Batch update optimization
CREATE INDEX idx_revrecstage_id_tenantId 
ON "RevRecStage"("id", "tenantId");
```

### Configuration Tuning

**BatchProcessingConfig defaults (tunable for your volume):**
- `batchSize`: 1000 (balance between memory and DB round trips)
- `fetchSize`: 5000 (stream read chunk size)
- `threadPoolSize`: 4 (if using parallel mode)

For 100M+ records:
```java
new BatchProcessingConfig(
    batchSize = 5000,      // Larger batches, fewer DB calls
    fetchSize = 10000,     // Stream in larger chunks
    threadPoolSize = 8     // Parallel enrichment if needed
)
```

## Usage

```java
// Call with input batchId and grouping rule
GroupingProcessingResult result = processor.processGroupingForBatch(
    tenantId,
    batchId,           // Input: all records get same batchId
    groupingRuleExpr,  // Optional SQL expression
    groupingRule       // DynamicGroupingRule implementation
);

log.info("Processed {} records, updated {}, failed {}, duration {}ms",
    result.getTotalRecords(),
    result.getUpdatedRecords(),
    result.getFailedRecords(),
    result.getDuration());
```

## Monitoring

Track these metrics:
- `totalRecords`: Records with processsedFlag IN ('N', 'E')
- `updatedRecords`: Successfully updated
- `failedRecords`: Errors during processing
- `duration`: Total processing time in ms
- **Throughput = updatedRecords / (duration/1000) records/sec**

Target: 1000-10000 records/sec depending on grouping rule complexity.

## Future Optimizations

1. **Partition by processsedFlag** - Keep hot data separate
2. **Async processing** - Use virtual threads (Java 19+) instead of thread pool
3. **Caching layer** - Redis cache for reference details (if stable)
4. **Native queries** - PL/pgSQL function for grouping evaluation
