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
import java.util.stream.Stream;

/**
 * Aggregate of {@code RevenueContractOrder} table rows for a single revenue contract, indexed by
 * {@code id} for O(1) lookup.
 *
 * <p>Build from streams (typical for one RC loaded from the database):
 *
 * <pre>{@code
 * RevenueContractOrderRecords records = RevenueContractOrderRecords.fromStreams(
 *         orderDetailsStream,
 *         orderAttributesStream,
 *         orderAccountDetailsStream,
 *         allocationDetailsStream);
 *
 * for (RevenueContractOrderDetailsRecord detail : records.orderDetails()) {
 *     RevenueContractAllocationDetailsRecord allocation =
 *             records.getAllocationDetails(detail.id()).orElse(null);
 * }
 * }</pre>
 */
public final class RevenueContractOrderRecords implements Serializable {

    private final Map<Long, RevenueContractOrderDetailsRecord> orderDetailsById;
    private final Map<Long, RevenueContractOrderAttributesRecord> orderAttributesById;
    private final Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsById;
    private final Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsById;

    private RevenueContractOrderRecords(
            Map<Long, RevenueContractOrderDetailsRecord> orderDetailsById,
            Map<Long, RevenueContractOrderAttributesRecord> orderAttributesById,
            Map<Long, RevenueContractOrderAccountDetailsRecord> orderAccountDetailsById,
            Map<Long, RevenueContractAllocationDetailsRecord> allocationDetailsById) {
        this.orderDetailsById = Map.copyOf(orderDetailsById);
        this.orderAttributesById = Map.copyOf(orderAttributesById);
        this.orderAccountDetailsById = Map.copyOf(orderAccountDetailsById);
        this.allocationDetailsById = Map.copyOf(allocationDetailsById);
    }

    /**
     * Indexes the four record streams by {@code id}. When duplicate ids appear, the first record wins.
     */
    public static RevenueContractOrderRecords fromStreams(
            Stream<RevenueContractOrderDetailsRecord> orderDetails,
            Stream<RevenueContractOrderAttributesRecord> orderAttributes,
            Stream<RevenueContractOrderAccountDetailsRecord> orderAccountDetails,
            Stream<RevenueContractAllocationDetailsRecord> allocationDetails) {
        Objects.requireNonNull(orderDetails, "orderDetails");
        Objects.requireNonNull(orderAttributes, "orderAttributes");
        Objects.requireNonNull(orderAccountDetails, "orderAccountDetails");
        Objects.requireNonNull(allocationDetails, "allocationDetails");
        return new RevenueContractOrderRecords(
                indexById(orderDetails, RevenueContractOrderDetailsRecord::id),
                indexById(orderAttributes, RevenueContractOrderAttributesRecord::id),
                indexById(orderAccountDetails, RevenueContractOrderAccountDetailsRecord::id),
                indexById(allocationDetails, RevenueContractAllocationDetailsRecord::id));
    }

    public static RevenueContractOrderRecords empty() {
        return fromLists(List.of(), List.of(), List.of(), List.of());
    }

    public static RevenueContractOrderRecords fromLists(
            List<RevenueContractOrderDetailsRecord> orderDetails,
            List<RevenueContractOrderAttributesRecord> orderAttributes,
            List<RevenueContractOrderAccountDetailsRecord> orderAccountDetails,
            List<RevenueContractAllocationDetailsRecord> allocationDetails) {
        return fromStreams(
                streamOrEmpty(orderDetails),
                streamOrEmpty(orderAttributes),
                streamOrEmpty(orderAccountDetails),
                streamOrEmpty(allocationDetails));
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

    public Map<Long, RevenueContractOrderDetailsRecord> getRevenueContractOrderDetailsRecord() {
        return orderDetailsById;
    }

    public Map<Long, RevenueContractOrderAttributesRecord> getRevenueContractOrderAttributesRecord() {
        return orderAttributesById;
    }

    public Map<Long, RevenueContractOrderAccountDetailsRecord> getRevenueContractOrderAccountDetailsRecord() {
        return orderAccountDetailsById;
    }

    public Map<Long, RevenueContractAllocationDetailsRecord> getRevenueContractAllocationDetailsRecord() {
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
        Objects.requireNonNull(orderDetail, "orderDetail");
        return getOrderAttributes(orderDetail.id());
    }

    public Optional<RevenueContractOrderAccountDetailsRecord> findOrderAccountDetailsFor(
            RevenueContractOrderDetailsRecord orderDetail) {
        Objects.requireNonNull(orderDetail, "orderDetail");
        return getOrderAccountDetails(orderDetail.id());
    }

    public Optional<RevenueContractAllocationDetailsRecord> findAllocationDetailsFor(
            RevenueContractOrderDetailsRecord orderDetail) {
        Objects.requireNonNull(orderDetail, "orderDetail");
        return getAllocationDetails(orderDetail.id());
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

    private static <T> Stream<T> streamOrEmpty(List<T> records) {
        return records == null ? Stream.empty() : records.stream();
    }

    private static <T> Map<Long, T> indexById(Stream<T> records, Function<T, Long> idExtractor) {
        return records
                .filter(record -> idExtractor.apply(record) != null)
                .collect(Collectors.toMap(idExtractor, Function.identity(), (left, right) -> left, LinkedHashMap::new));
    }
}
