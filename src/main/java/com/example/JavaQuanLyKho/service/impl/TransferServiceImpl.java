package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.exception.InvalidStateTransitionException;
import com.example.JavaQuanLyKho.exception.OutboundStockNotEnoughException;
import com.example.JavaQuanLyKho.model.dto.TransferDtos;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.model.entity.Transfer;
import com.example.JavaQuanLyKho.model.entity.TransferLine;
import com.example.JavaQuanLyKho.model.entity.TransferStatus;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.repository.TransferLineRepository;
import com.example.JavaQuanLyKho.repository.TransferRepository;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class TransferServiceImpl implements TransferService {

    private static final DateTimeFormatter CODE_TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final TransferRepository transferRepository;
    private final TransferLineRepository transferLineRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UomRepository uomRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public TransferServiceImpl(
            TransferRepository transferRepository,
            TransferLineRepository transferLineRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository,
            UomRepository uomRepository,
            LocationRepository locationRepository,
            UserRepository userRepository
    ) {
        this.transferRepository = transferRepository;
        this.transferLineRepository = transferLineRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Transfer> findAll(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Override
    public List<TransferLine> findLines(UUID transferId) {
        return transferLineRepository.findByTransferId(transferId);
    }

    @Override
    @Transactional
    public Transfer create(TransferDtos.CreateRequest request, String actorUsername) {
        validateCreateRequest(request);
        warehouseRepository.findById(request.getFromWarehouseId()).orElseThrow(NoSuchElementException::new);
        warehouseRepository.findById(request.getToWarehouseId()).orElseThrow(NoSuchElementException::new);

        Transfer transfer = new Transfer();
        transfer.setCode(resolveCode(request.getCode()));
        transfer.setFromWarehouseId(request.getFromWarehouseId());
        transfer.setToWarehouseId(request.getToWarehouseId());
        transfer.setStatus(TransferStatus.DRAFT);
        transfer.setCreatedAt(OffsetDateTime.now());
        transfer.setCreatedBy(findUserIdByUsername(actorUsername));
        Transfer saved = transferRepository.save(transfer);

        List<TransferLine> lines = mapAndValidateLines(saved.getId(), request.getFromWarehouseId(), request.getToWarehouseId(), request.getLines());
        transferLineRepository.saveAll(lines);
        return saved;
    }

    @Override
    @Transactional
    public Transfer approve(UUID transferId, String actorUsername) {
        Transfer transfer = getRequiredTransfer(transferId);
        if (transfer.getStatus() != TransferStatus.DRAFT) {
            throw new InvalidStateTransitionException("Transfer must be DRAFT before APPROVED");
        }
        ensureHasLines(transferId);
        transfer.setStatus(TransferStatus.APPROVED);
        transfer.setApprovedBy(findUserIdByUsername(actorUsername));
        transfer.setApprovedAt(OffsetDateTime.now());
        return transferRepository.save(transfer);
    }

    @Override
    @Transactional
    public Transfer reject(UUID transferId) {
        Transfer transfer = getRequiredTransfer(transferId);
        TransferStatus current = transfer.getStatus();
        if (current != TransferStatus.DRAFT && current != TransferStatus.APPROVED) {
            throw new InvalidStateTransitionException("Transfer can only be rejected from DRAFT/APPROVED");
        }
        transfer.setStatus(TransferStatus.CANCELLED);
        return transferRepository.save(transfer);
    }

    @Override
    @Transactional
    public Transfer issue(UUID transferId) {
        Transfer transfer = getRequiredTransfer(transferId);
        if (transfer.getStatus() != TransferStatus.APPROVED) {
            throw new InvalidStateTransitionException("Transfer must be APPROVED before ISSUED");
        }

        List<TransferLine> lines = ensureHasLines(transferId);
        Map<StockKey, BigDecimal> requiredByStockKey = aggregateRequired(lines, true);
        Map<StockKey, InventoryBalance> fromBalances = new HashMap<>();

        for (Map.Entry<StockKey, BigDecimal> entry : requiredByStockKey.entrySet()) {
            StockKey key = entry.getKey();
            BigDecimal required = entry.getValue();
            InventoryBalance fromBalance = inventoryBalanceRepository
                    .findForUpdate(key.productId, transfer.getFromWarehouseId(), key.locationId)
                    .orElseThrow(() -> new OutboundStockNotEnoughException(
                            "No stock for product " + key.productId + " at source warehouse " + transfer.getFromWarehouseId()
                    ));

            BigDecimal available = safe(fromBalance.getQtyOnHand()).subtract(safe(fromBalance.getQtyReserved()));
            if (available.compareTo(required) < 0) {
                throw new OutboundStockNotEnoughException(
                        "Not enough source stock for product " + key.productId + ": required=" + required + ", available=" + available
                );
            }
            fromBalances.put(key, fromBalance);
        }

        for (Map.Entry<StockKey, BigDecimal> entry : requiredByStockKey.entrySet()) {
            StockKey key = entry.getKey();
            InventoryBalance fromBalance = fromBalances.get(key);
            fromBalance.setQtyOnHand(safe(fromBalance.getQtyOnHand()).subtract(entry.getValue()));
            inventoryBalanceRepository.save(fromBalance);
        }

        transfer.setStatus(TransferStatus.ISSUED);
        transfer.setIssuedAt(OffsetDateTime.now());
        return transferRepository.save(transfer);
    }

    @Override
    @Transactional
    public Transfer receive(UUID transferId) {
        Transfer transfer = getRequiredTransfer(transferId);
        if (transfer.getStatus() != TransferStatus.ISSUED) {
            throw new InvalidStateTransitionException("Transfer must be ISSUED before COMPLETED");
        }
        List<TransferLine> lines = ensureHasLines(transferId);

        for (TransferLine line : lines) {
            InventoryBalance toBalance = inventoryBalanceRepository
                    .findForUpdate(line.getProductId(), transfer.getToWarehouseId(), line.getToLocationId())
                    .orElseGet(() -> {
                        InventoryBalance created = new InventoryBalance();
                        created.setProductId(line.getProductId());
                        created.setWarehouseId(transfer.getToWarehouseId());
                        created.setLocationId(line.getToLocationId());
                        created.setQtyOnHand(BigDecimal.ZERO);
                        created.setQtyReserved(BigDecimal.ZERO);
                        return created;
                    });

            toBalance.setQtyOnHand(safe(toBalance.getQtyOnHand()).add(line.getQuantity()));
            inventoryBalanceRepository.save(toBalance);
        }

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setCompletedAt(OffsetDateTime.now());
        return transferRepository.save(transfer);
    }

    @Override
    @Transactional
    public void delete(UUID transferId) {
        Transfer transfer = getRequiredTransfer(transferId);
        if (transfer.getStatus() != TransferStatus.DRAFT) {
            throw new InvalidStateTransitionException("Only DRAFT transfer can be deleted");
        }
        transferLineRepository.deleteByTransferId(transferId);
        transferRepository.delete(transfer);
    }

    private void validateCreateRequest(TransferDtos.CreateRequest request) {
        if (request.getFromWarehouseId() == null || request.getToWarehouseId() == null) {
            throw new IllegalArgumentException("fromWarehouseId and toWarehouseId must not be null");
        }
        if (request.getFromWarehouseId().equals(request.getToWarehouseId())) {
            throw new IllegalArgumentException("fromWarehouseId and toWarehouseId must be different");
        }
        if (request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("Transfer must have at least one line");
        }
    }

    private List<TransferLine> mapAndValidateLines(
            UUID transferId,
            UUID fromWarehouseId,
            UUID toWarehouseId,
            List<TransferDtos.LineRequest> requests
    ) {
        List<TransferLine> lines = new ArrayList<>();
        for (TransferDtos.LineRequest reqLine : requests) {
            if (reqLine.getQuantity() == null || reqLine.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("line.quantity must be greater than 0");
            }
            productRepository.findById(reqLine.getProductId()).orElseThrow(NoSuchElementException::new);
            uomRepository.findById(reqLine.getUomId()).orElseThrow(NoSuchElementException::new);
            if (reqLine.getFromLocationId() != null) {
                var fromLocation = locationRepository.findById(reqLine.getFromLocationId()).orElseThrow(NoSuchElementException::new);
                if (!fromWarehouseId.equals(fromLocation.getWarehouseId())) {
                    throw new IllegalArgumentException("fromLocationId does not belong to fromWarehouseId");
                }
            }
            if (reqLine.getToLocationId() != null) {
                var toLocation = locationRepository.findById(reqLine.getToLocationId()).orElseThrow(NoSuchElementException::new);
                if (!toWarehouseId.equals(toLocation.getWarehouseId())) {
                    throw new IllegalArgumentException("toLocationId does not belong to toWarehouseId");
                }
            }

            TransferLine line = new TransferLine();
            line.setTransferId(transferId);
            line.setProductId(reqLine.getProductId());
            line.setUomId(reqLine.getUomId());
            line.setQuantity(reqLine.getQuantity());
            line.setFromLocationId(reqLine.getFromLocationId());
            line.setToLocationId(reqLine.getToLocationId());
            lines.add(line);
        }
        return lines;
    }

    private Transfer getRequiredTransfer(UUID transferId) {
        return transferRepository.findById(transferId).orElseThrow(NoSuchElementException::new);
    }

    private List<TransferLine> ensureHasLines(UUID transferId) {
        List<TransferLine> lines = transferLineRepository.findByTransferId(transferId);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Transfer must have at least one line");
        }
        return lines;
    }

    private Map<StockKey, BigDecimal> aggregateRequired(List<TransferLine> lines, boolean source) {
        Map<StockKey, BigDecimal> required = new HashMap<>();
        for (TransferLine line : lines) {
            StockKey key = source
                    ? new StockKey(line.getProductId(), line.getFromLocationId())
                    : new StockKey(line.getProductId(), line.getToLocationId());
            BigDecimal current = required.getOrDefault(key, BigDecimal.ZERO);
            required.put(key, current.add(line.getQuantity()));
        }
        return required;
    }

    private String resolveCode(String requestedCode) {
        if (requestedCode != null && !requestedCode.isBlank()) {
            String normalized = requestedCode.trim().toUpperCase();
            if (transferRepository.existsByCode(normalized)) {
                throw new IllegalArgumentException("Transfer code already exists");
            }
            return normalized;
        }
        String generated;
        do {
            generated = "TRF-" + CODE_TS.format(OffsetDateTime.now());
        } while (transferRepository.existsByCode(generated));
        return generated;
    }

    private UUID findUserIdByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return userRepository.findByUsername(username).map(User::getId).orElse(null);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private record StockKey(UUID productId, UUID locationId) {
    }
}
