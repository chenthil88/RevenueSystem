package com.revrec.engine.domain.service.RevenueContractOrder;

import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractAllocationDetails.RevenueContractAllocationDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAccountDetails.RevenueContractOrderAccountDetailsRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderAttributes.RevenueContractOrderAttributesRecord;
import com.revrec.engine.domain.service.RevenueContractOrder.RevenueContractOrderDetails.RevenueContractOrderDetailsRecord;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Aggregate of all {@code RevenueContractOrder} table rows, indexed by {@code id} for O(1) lookup.
 *
 * <p>Example:
 *
 * <pre>{@code
 * for (RevenueContractOrderDetailsRecord detail : records.orderDetails()) {
 *     RevenueContractAllocationDetailsRecord allocation =
 *             records.getAllocationDetails(detail.id()).orElse(null);
 *     // or when table ids differ:
 *     allocation = records.findAllocationDetailsFor(detail).orElse(null);
 * }
 * }</pre>
 */
public final class RevenueContractOrderRecords implements Serializable {

    private final Map<Long, RevenueContractOrderDetailsRecord> orderDetailsById;
    private final Map<Long, RevenueContractOrderAttributesRecord> orderAttributesById;
    private final Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsById;
    private final Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsById;

    private final Map<Long, RevenueContractOrderAttributesRecord> orderAttributesByRevenueContractId;
    private final Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsByRevenueContractId;
    private final Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsByRevenueContractId;

    private RevenueContractOrderRecords(
            Map<Long, RevenueContractOrderDetailsRecord> orderDetailsById,
            Map<Long, RevenueContractOrderAttributesRecord> orderAttributesById,
            Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsById,
            Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsById,
            Map<Long, RevenueContractOrderAttributesRecord> orderAttributesByRevenueContractId,
            Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsByRevenueContractId,
            Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsByRevenueContractId) {
        this.orderDetailsById = Map.copyOf(orderDetailsById);
        this.orderAttributesById = Map.copyOf(orderAttributesById);
        this.orderAccountDetailsById = Map.copyOf(orderAccountDetailsById);
        this.allocationDetailsById = Map.copyOf(allocationDetailsById);
        this.orderAttributesByRevenueContractId = Map.copyOf(orderAttributesByRevenueContractId);
        this.orderAccountDetailsByRevenueContractId = Map.copyOf(orderAccountDetailsByRevenueContractId);
        this.allocationDetailsByRevenueContractId = Map.copyOf(allocationDetailsByRevenueContractId);
    }

    public static RevenueContractOrderRecords empty() {
        return fromLists(List.of(), List.of(), List.of(), List.of());
    }

    public static RevenueContractOrderRecords fromLists(
            List<RevenueContractOrderDetailsRecord> orderDetails,
            List<RevenueContractOrderAttributesRecord> orderAttributes,
            List<RevenueContractOrderAccountDetailsRecord> orderAccountDetails,
            List<RevenueContractAllocationDetailsRecord> allocationDetails) {
        return new RevenueContractOrderRecords(
                indexById(orderDetails, RevenueContractOrderDetailsRecord::id),
                indexById(orderAttributes, RevenueContractOrderAttributesRecord::id),
                indexById(orderAccountDetails, RevenueContractOrderAccountDetailsRecord::id),
                indexById(allocationDetails, RevenueContractAllocationDetailsRecord::id),
                indexByRevenueContractId(orderAttributes, RevenueContractOrderAttributesRecord::revenueContractId),
                indexByRevenueContractId(
                        orderAccountDetails, RevenueContractOrderAccountDetailsRecord::revenueContractId),
                indexByRevenueContractId(allocationDetails, RevenueContractAllocationDetailsRecord::revenueContractId));
    }

    public static RevenueContractOrderRecords forContract(
            RevenueContractOrderDetailsRecord orderDetails,
            RevenueContractOrderAttributesRecord orderAttributes,
            RevenueContractOrderAccountDetailsRecord orderAccountDetails,
            RevenueContractAllocationDetailsRecord allocationDetails) {
        return fromLists(
                List.of(orderDetails),
                orderAttributes != null ? List.of(orderAttributes) : List.of(),
                orderAccountDetails != null ? List.of(orderAccountDetails) : List.of(),
                allocationDetails != null ? List.of(allocationDetails) : List.of());
    }

    public Map<Long, RevenueContractOrderDetailsRecord> orderDetailsById() {
        return orderDetailsById;
    }

    public Map<Long, RevenueContractOrderAttributesRecord> orderAttributesById() {
        return orderAttributesById;
    }

    public Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsById() {
        return orderAccountDetailsById;
    }

    public Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsById() {
        return allocationDetailsById;
    }

    public Collection<RevenueContractOrderDetailsRecord> orderDetails() {
        return orderDetailsById.values();
    }

    public Optional<RevenueContractOrderDetailsRecord> getOrderDetails(long id) {
        return Optional.ofNullable(orderDetailsById.get(id));
    }

    public Optional<RevenueContractOrderAttributesRecord> getOrderAttributes(long id) {
        return Optional.ofNullable(orderAttributesById.get(id));
    }

    public Optional<RevenueContractOrderAccountDetailsRecord> getOrderAccountDetails(long id) {
        return Optional.ofNullable(orderAccountDetailsById.get(id));
    }

    public Optional<RevenueContractAllocationDetailsRecord> getAllocationDetails(long id) {
        return Optional.ofNullable(allocationDetailsById.get(id));
    }

    public Optional<RevenueContractOrderAttributesRecord> findOrderAttributesFor(
            RevenueContractOrderDetailsRecord orderDetail) {
        return resolve(orderDetail, orderAttributesById, orderAttributesByRevenueContractId);
    }

    public Optional<RevenueContractOrderAccountDetailsRecord> findOrderAccountDetailsFor(
            RevenueContractOrderDetailsRecord orderDetail) {
        return resolve(orderDetail, orderAccountDetailsById, orderAccountDetailsByRevenueContractId);
    }

    public Optional<RevenueContractAllocationDetailsRecord> findAllocationDetailsFor(
            RevenueContractOrderDetailsRecord orderDetail) {
        return resolve(orderDetail, allocationDetailsById, allocationDetailsByRevenueContractId);
    }

    public boolean isEmpty() {
        return orderDetailsById.isEmpty()
                && orderAttributesById.isEmpty()
                && orderAccountDetailsById.isEmpty()
                && allocationDetailsById.isEmpty();
    }

    public Optional<Long> primaryRevenueContractId() {
        return orderDetailsById.values().stream()
                .map(RevenueContractOrderDetailsRecord::revenueContractId)
                .findFirst();
    }

    private static <T> Map<Long, T> indexById(List<T> records, Function<T, Long> idExtractor) {
        if (records == null || records.isEmpty()) {
            return Map.of();
        }
        return records.stream()
                .filter(record -> idExtractor.apply(record) != null)
                .collect(Collectors.toMap(idExtractor, Function.identity(), (left, right) -> left, LinkedHashMap::new));
    }

    private static <T> Map<Long, T> indexByRevenueContractId(
            List<T> records, Function<T, Long> revenueContractIdExtractor) {
        if (records == null || records.isEmpty()) {
            return Map.of();
        }
        return records.stream()
                .filter(record -> revenueContractIdExtractor.apply(record) != null)
                .collect(Collectors.toMap(
                        revenueContractIdExtractor, Function.identity(), (left, right) -> left, LinkedHashMap::new));
    }

    private static <T> Optional<T> resolve(
            RevenueContractOrderDetailsRecord orderDetail,
            Map<Long, T> byId,
            Map<Long, T> byRevenueContractId) {
        Objects.requireNonNull(orderDetail, "orderDetail");
        T byRecordId = byId.get(orderDetail.id());
        if (byRecordId != null) {
            return Optional.of(byRecordId);
        }
        if (orderDetail.revenueContractId() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(byRevenueContractId.get(orderDetail.revenueContractId()));
    }
}
