package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.TransactionPageDtos;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.model.entity.Supplier;
import com.example.JavaQuanLyKho.model.entity.Transfer;
import com.example.JavaQuanLyKho.model.entity.TransferLine;
import com.example.JavaQuanLyKho.model.entity.Uom;
import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.repository.InboundReceiptLineRepository;
import com.example.JavaQuanLyKho.repository.InboundReceiptRepository;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueLineRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueRepository;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.repository.SupplierRepository;
import com.example.JavaQuanLyKho.repository.TransferLineRepository;
import com.example.JavaQuanLyKho.repository.TransferRepository;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionPageQueryService {

    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final UomRepository uomRepository;
    private final InboundReceiptRepository inboundReceiptRepository;
    private final InboundReceiptLineRepository inboundReceiptLineRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final OutboundIssueRepository outboundIssueRepository;
    private final OutboundIssueLineRepository outboundIssueLineRepository;
    private final TransferRepository transferRepository;
    private final TransferLineRepository transferLineRepository;

    public TransactionPageQueryService(
            WarehouseRepository warehouseRepository,
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            UomRepository uomRepository,
            InboundReceiptRepository inboundReceiptRepository,
            InboundReceiptLineRepository inboundReceiptLineRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            OutboundIssueRepository outboundIssueRepository,
            OutboundIssueLineRepository outboundIssueLineRepository,
            TransferRepository transferRepository,
            TransferLineRepository transferLineRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
        this.inboundReceiptRepository = inboundReceiptRepository;
        this.inboundReceiptLineRepository = inboundReceiptLineRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.outboundIssueRepository = outboundIssueRepository;
        this.outboundIssueLineRepository = outboundIssueLineRepository;
        this.transferRepository = transferRepository;
        this.transferLineRepository = transferLineRepository;
    }

    public TransactionPageDtos.Payload getPayload() {
        List<TransactionPageDtos.WarehouseItem> warehouses = warehouseRepository.findAll().stream()
                .sorted(Comparator
                        .comparing((Warehouse w) -> safeText(w.getName()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(w -> safeText(w.getCode()), String.CASE_INSENSITIVE_ORDER))
                .map((w) -> new TransactionPageDtos.WarehouseItem(
                        w.getId(),
                        w.getCode(),
                        w.getName(),
                        w.getStatus()
                ))
                .collect(Collectors.toList());

        List<TransactionPageDtos.SupplierItem> suppliers = supplierRepository.findAll().stream()
                .filter((s) -> !"LOCKED".equalsIgnoreCase(safeText(s.getStatus())))
                .sorted(Comparator
                        .comparing((Supplier s) -> safeText(s.getName()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing((s) -> safeText(s.getCode()), String.CASE_INSENSITIVE_ORDER))
                .map((s) -> new TransactionPageDtos.SupplierItem(
                        s.getId(),
                        s.getCode(),
                        s.getName(),
                        s.getStatus()
                ))
                .collect(Collectors.toList());

        List<TransactionPageDtos.ProductItem> products = productRepository.findAll().stream()
                .filter((p) -> !"LOCKED".equalsIgnoreCase(safeText(p.getStatus())))
                .sorted(Comparator.comparing((Product p) -> safeText(p.getName()), String.CASE_INSENSITIVE_ORDER))
                .map((p) -> new TransactionPageDtos.ProductItem(
                        p.getId(),
                        p.getSku(),
                        p.getName(),
                        p.getBaseUomId(),
                        p.getStatus()
                ))
                .collect(Collectors.toList());

        List<TransactionPageDtos.UomItem> uoms = uomRepository.findAll().stream()
                .sorted(Comparator
                        .comparing((Uom u) -> safeText(u.getName()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing((u) -> safeText(u.getCode()), String.CASE_INSENSITIVE_ORDER))
                .map((u) -> new TransactionPageDtos.UomItem(
                        u.getId(),
                        u.getCode(),
                        u.getName()
                ))
                .collect(Collectors.toList());

        List<TransactionPageDtos.RecordItem> records = new ArrayList<>();

        List<InboundReceipt> inboundReceipts = inboundReceiptRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<UUID, List<InboundReceiptLine>> inboundLinesByReceiptId = loadInboundLinesByReceiptId(inboundReceipts);
        inboundReceipts.forEach((receipt) -> {
            List<TransactionPageDtos.RecordLineItem> lines = inboundLinesByReceiptId
                    .getOrDefault(receipt.getId(), List.of())
                    .stream()
                    .map((line) -> new TransactionPageDtos.RecordLineItem(
                            line.getId(),
                            line.getProductId(),
                            line.getUomId(),
                            line.getQuantity(),
                            line.getLocationId(),
                            null,
                            null
                    ))
                    .collect(Collectors.toList());
            records.add(new TransactionPageDtos.RecordItem(
                    receipt.getId(),
                    "INBOUND",
                    receipt.getCode(),
                    receipt.getType(),
                    receipt.getWarehouseId(),
                    null,
                    receipt.getCreatedAt(),
                    receipt.getCreatedBy(),
                    enumName(receipt.getStatus()),
                    lines,
                    lines.size()
            ));
        });

        List<OutboundIssue> outboundIssues = outboundIssueRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<UUID, List<OutboundIssueLine>> outboundLinesByIssueId = loadOutboundLinesByIssueId(outboundIssues);
        outboundIssues.forEach((issue) -> {
            List<TransactionPageDtos.RecordLineItem> lines = outboundLinesByIssueId
                    .getOrDefault(issue.getId(), List.of())
                    .stream()
                    .map((line) -> new TransactionPageDtos.RecordLineItem(
                            line.getId(),
                            line.getProductId(),
                            line.getUomId(),
                            line.getQuantity(),
                            line.getLocationId(),
                            null,
                            null
                    ))
                    .collect(Collectors.toList());
            records.add(new TransactionPageDtos.RecordItem(
                    issue.getId(),
                    "OUTBOUND",
                    issue.getCode(),
                    issue.getType(),
                    issue.getWarehouseId(),
                    null,
                    issue.getCreatedAt(),
                    issue.getCreatedBy(),
                    enumName(issue.getStatus()),
                    lines,
                    lines.size()
            ));
        });

        List<Transfer> transfers = transferRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<UUID, List<TransferLine>> transferLinesByTransferId = loadTransferLinesByTransferId(transfers);
        transfers.forEach((transfer) -> {
            List<TransactionPageDtos.RecordLineItem> lines = transferLinesByTransferId
                    .getOrDefault(transfer.getId(), List.of())
                    .stream()
                    .map((line) -> new TransactionPageDtos.RecordLineItem(
                            line.getId(),
                            line.getProductId(),
                            line.getUomId(),
                            line.getQuantity(),
                            null,
                            line.getFromLocationId(),
                            line.getToLocationId()
                    ))
                    .collect(Collectors.toList());
            records.add(new TransactionPageDtos.RecordItem(
                    transfer.getId(),
                    "TRANSFER",
                    transfer.getCode(),
                    "INTERNAL_TRANSFER",
                    transfer.getFromWarehouseId(),
                    transfer.getToWarehouseId(),
                    transfer.getCreatedAt(),
                    transfer.getCreatedBy(),
                    enumName(transfer.getStatus()),
                    lines,
                    lines.size()
            ));
        });

        records.sort((a, b) -> compareCreatedAtDesc(a.createdAt(), b.createdAt()));

        return new TransactionPageDtos.Payload(warehouses, suppliers, products, uoms, records);
    }

    public List<UUID> getOutboundSectionProductIds(UUID warehouseId, UUID sectionId) {
        if (warehouseId == null || sectionId == null) return List.of();
        LinkedHashSet<UUID> productIds = inventoryBalanceRepository
                .findByWarehouseId(warehouseId, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(this::hasPositiveOnHand)
                .filter((balance) -> sectionId.equals(balance.getLocationId()))
                .map(InventoryBalance::getProductId)
                .filter((id) -> id != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return List.copyOf(productIds);
    }

    public List<UUID> getTransferSourceProductIds(UUID warehouseId) {
        if (warehouseId == null) return List.of();
        LinkedHashSet<UUID> productIds = inventoryBalanceRepository
                .findByWarehouseId(warehouseId, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(this::hasPositiveOnHand)
                .map(InventoryBalance::getProductId)
                .filter((id) -> id != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return List.copyOf(productIds);
    }

    private Map<UUID, List<InboundReceiptLine>> loadInboundLinesByReceiptId(List<InboundReceipt> receipts) {
        List<UUID> receiptIds = receipts.stream().map(InboundReceipt::getId).collect(Collectors.toList());
        if (receiptIds.isEmpty()) return Map.of();
        return inboundReceiptLineRepository.findByReceiptIdIn(receiptIds).stream()
                .collect(Collectors.groupingBy(InboundReceiptLine::getReceiptId));
    }

    private Map<UUID, List<OutboundIssueLine>> loadOutboundLinesByIssueId(List<OutboundIssue> issues) {
        List<UUID> issueIds = issues.stream().map(OutboundIssue::getId).collect(Collectors.toList());
        if (issueIds.isEmpty()) return Map.of();
        return outboundIssueLineRepository.findByIssueIdIn(issueIds).stream()
                .collect(Collectors.groupingBy(OutboundIssueLine::getIssueId));
    }

    private Map<UUID, List<TransferLine>> loadTransferLinesByTransferId(List<Transfer> transfers) {
        List<UUID> transferIds = transfers.stream().map(Transfer::getId).collect(Collectors.toList());
        if (transferIds.isEmpty()) return Map.of();
        return transferLineRepository.findByTransferIdIn(transferIds).stream()
                .collect(Collectors.groupingBy(TransferLine::getTransferId));
    }

    private static int compareCreatedAtDesc(OffsetDateTime left, OffsetDateTime right) {
        if (left == null && right == null) return 0;
        if (left == null) return 1;
        if (right == null) return -1;
        return right.compareTo(left);
    }

    private static String safeText(String value) {
        return value == null ? "" : value;
    }

    private static String enumName(Enum<?> value) {
        return value == null ? "UNKNOWN" : value.name();
    }

    private boolean hasPositiveOnHand(InventoryBalance balance) {
        return balance.getQtyOnHand() != null && balance.getQtyOnHand().compareTo(BigDecimal.ZERO) > 0;
    }
}
