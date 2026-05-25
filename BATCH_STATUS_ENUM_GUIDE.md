# BatchStatus Enum - Migration Guide

## Overview
Created type-safe `BatchStatus` enum to replace string-based status in `RevenueContractBatchHeader`.

## Files Created
1. `BatchStatus.java` - Enum with 5 status values
2. `BatchStatusConverter.java` - JPA converter for persistence

## Status Values

| Enum | Code | Description |
|------|------|-------------|
| NEW | N | New batch, not started |
| IN_PROGRESS | IP | Batch processing in progress |
| FAILED | F | Batch processing failed |
| WARNING | W | Batch completed with warnings |
| CANCELED | X | Batch processing canceled |
| COLLECTED | C | Batch processing collected |

## Migration Path

### Step 1: Update Interface (Optional - for type safety)
```java
// Before
public interface RevenueContractBatchHeader {
    String status();
}

// After (if changing contract)
public interface RevenueContractBatchHeader {
    BatchStatus status();
}
```

### Step 2: Update Record
```java
// Before
public record RevenueContractBatchHeaderRecord(
    // ...
    String status,
    // ...
) implements RevenueContractBatchHeader, Serializable {
}

// After
public record RevenueContractBatchHeaderRecord(
    // ...
    @Convert(converter = BatchStatusConverter.class)
    BatchStatus status,
    // ...
) implements RevenueContractBatchHeader, Serializable {
}
```

### Step 3: Update Usage
```java
// Before
if ("F".equals(batch.status())) {
    // handle failed
}

// After
if (batch.status() == BatchStatus.FAILED) {
    // handle failed
}

// Or use helper methods
if (batch.status().hasIssues()) {
    // handle failed or warning
}
```

## Helper Methods

```java
// Check if terminal state
if (batch.status().isTerminal()) {
    // FAILED or COLLECTED
}

// Check if processing
if (batch.status().isProcessing()) {
    // IN_PROGRESS
}

// Check if has issues
if (batch.status().hasIssues()) {
    // FAILED or WARNING
}

// Convert code to enum
BatchStatus status = BatchStatus.fromCode("F");  // FAILED
```

## Backward Compatible Usage

If you can't change the record immediately, use this approach:

```java
// Convert String to Enum for use
BatchStatus status = BatchStatus.fromCode(batch.status());

if (status == BatchStatus.FAILED) {
    // handle
}
```

## Database Mapping

The `BatchStatusConverter` automatically handles conversion:
- Database stores: "N", "IP", "F", "W", "C"
- Java uses: `BatchStatus.NEW`, `.IN_PROGRESS`, etc.
- Automatic conversion in both directions

No SQL changes needed.

## Benefits

✅ Type-safe status values (no invalid strings)
✅ IDE autocomplete for status checks
✅ Refactoring-safe (rename enum safely)
✅ Helper methods (isTerminal, hasIssues, etc.)
✅ Default to NEW for null values
✅ Clear business logic in code

## Next Steps

1. Add `BatchStatus.java` and `BatchStatusConverter.java` to project
2. Update `RevenueContractBatchHeaderRecord` to use `BatchStatus`
3. Update interface if desired (breaking change)
4. Update service methods that work with status
5. Update SQL queries/criteria (if using JPA Criteria API)
