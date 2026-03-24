package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.exception.InvalidStateTransitionException;
import com.example.JavaQuanLyKho.model.dto.InboundDtos;
import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptStatus;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.InboundReceiptLineRepository;
import com.example.JavaQuanLyKho.repository.InboundReceiptRepository;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.repository.SupplierRepository;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.InboundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class InboundServiceImpl implements InboundService {

    private static final DateTimeFormatter CODE_TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final InboundReceiptRepository inboundReceiptRepository;
    private final InboundReceiptLineRepository inboundReceiptLineRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final UomRepository uomRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public InboundServiceImpl(
            InboundReceiptRepository inboundReceiptRepository,
            InboundReceiptLineRepository inboundReceiptLineRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            WarehouseRepository warehouseRepository,
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            UomRepository uomRepository,
            LocationRepository locationRepository,
            UserRepository userRepository
    ) {
        this.inboundReceiptRepository = inboundReceiptRepository;
        this.inboundReceiptLineRepository = inboundReceiptLineRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.warehouseRepository = warehouseRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<InboundReceipt> findAll(Pageable pageable) {
        return inboundReceiptRepository.findAll(pageable);
    }

    @Override
    public List<InboundReceiptLine> findLines(UUID receiptId) {
        return inboundReceiptLineRepository.findByReceiptId(receiptId);
    }

    @Override
    @Transactional
    public InboundReceipt create(InboundDtos.CreateRequest request, String actorUsername) {
        validateCreateRequest(request);
        warehouseRepository.findById(request.getWarehouseId()).orElseThrow(NoSuchElementException::new);
        if (request.getSupplierId() != null) {
            supplierRepository.findById(request.getSupplierId()).orElseThrow(NoSuchElementException::new);
        }

        InboundReceipt receipt = new InboundReceipt();
        receipt.setCode(resolveCode(request.getCode()));
        receipt.setType(defaultIfBlank(request.getType(), "PURCHASE"));
        receipt.setWarehouseId(request.getWarehouseId());
        receipt.setSupplierId(request.getSupplierId());
        receipt.setStatus(InboundReceiptStatus.DRAFT);
        receipt.setCreatedAt(OffsetDateTime.now());
        receipt.setCreatedBy(findUserIdByUsername(actorUsername));
        InboundReceipt savedReceipt = inboundReceiptRepository.save(receipt);

        List<InboundReceiptLine> lines = mapAndValidateLines(savedReceipt.getId(), request.getWarehouseId(), request.getLines());
        inboundReceiptLineRepository.saveAll(lines);
        return savedReceipt;
    }

    @Override
    @Transactional
    public InboundReceipt submit(UUID receiptId) {
        InboundReceipt receipt = getRequiredReceipt(receiptId);
        if (receipt.getStatus() != InboundReceiptStatus.DRAFT) {
            throw new InvalidStateTransitionException("Inbound receipt must be DRAFT before SUBMITTED");
        }
        ensureHasLines(receiptId);
        receipt.setStatus(InboundReceiptStatus.SUBMITTED);
        return inboundReceiptRepository.save(receipt);
    }

    @Override
    @Transactional
    public InboundReceipt approve(UUID receiptId, String actorUsername) {
        InboundReceipt receipt = getRequiredReceipt(receiptId);
        if (receipt.getStatus() != InboundReceiptStatus.SUBMITTED) {
            throw new InvalidStateTransitionException("Inbound receipt must be SUBMITTED before APPROVED");
        }
        receipt.setStatus(InboundReceiptStatus.APPROVED);
        receipt.setApprovedBy(findUserIdByUsername(actorUsername));
        receipt.setApprovedAt(OffsetDateTime.now());
        return inboundReceiptRepository.save(receipt);
    }

    @Override
    @Transactional
    public InboundReceipt reject(UUID receiptId) {
        InboundReceipt receipt = getRequiredReceipt(receiptId);
        InboundReceiptStatus current = receipt.getStatus();
        if (current != InboundReceiptStatus.DRAFT
                && current != InboundReceiptStatus.SUBMITTED
                && current != InboundReceiptStatus.APPROVED) {
            throw new InvalidStateTransitionException("Inbound receipt can only be rejected from DRAFT/SUBMITTED/APPROVED");
        }
        receipt.setStatus(InboundReceiptStatus.CANCELLED);
        return inboundReceiptRepository.save(receipt);
    }

    @Override
    @Transactional
    public InboundReceipt receive(UUID receiptId) {
        InboundReceipt receipt = getRequiredReceipt(receiptId);
        if (receipt.getStatus() != InboundReceiptStatus.APPROVED) {
            throw new InvalidStateTransitionException("Inbound receipt must be APPROVED before COMPLETED");
        }
        List<InboundReceiptLine> lines = ensureHasLines(receiptId);

        for (InboundReceiptLine line : lines) {
            InventoryBalance balance = inventoryBalanceRepository
                    .findForUpdate(line.getProductId(), receipt.getWarehouseId(), line.getLocationId())
                    .orElseGet(() -> {
                        InventoryBalance created = new InventoryBalance();
                        created.setProductId(line.getProductId());
                        created.setWarehouseId(receipt.getWarehouseId());
                        created.setLocationId(line.getLocationId());
                        created.setQtyOnHand(BigDecimal.ZERO);
                        created.setQtyReserved(BigDecimal.ZERO);
                        return created;
                    });

            balance.setQtyOnHand(safe(balance.getQtyOnHand()).add(line.getQuantity()));
            inventoryBalanceRepository.save(balance);
        }

        receipt.setStatus(InboundReceiptStatus.COMPLETED);
        receipt.setCompletedAt(OffsetDateTime.now());
        return inboundReceiptRepository.save(receipt);
    }

    @Override
    @Transactional
    public void delete(UUID receiptId) {
        InboundReceipt receipt = getRequiredReceipt(receiptId);
        if (receipt.getStatus() != InboundReceiptStatus.DRAFT) {
            throw new InvalidStateTransitionException("Only DRAFT inbound receipt can be deleted");
        }
        inboundReceiptLineRepository.deleteByReceiptId(receiptId);
        inboundReceiptRepository.delete(receipt);
    }

    private void validateCreateRequest(InboundDtos.CreateRequest request) {
        if (request.getWarehouseId() == null) {
            throw new IllegalArgumentException("warehouseId must not be null");
        }
        if (request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("Inbound receipt must have at least one line");
        }
    }

    private List<InboundReceiptLine> mapAndValidateLines(
            UUID receiptId,
            UUID warehouseId,
            List<InboundDtos.LineRequest> requests
    ) {
        List<InboundReceiptLine> lines = new ArrayList<>();
        for (InboundDtos.LineRequest reqLine : requests) {
            if (reqLine.getQuantity() == null || reqLine.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("line.quantity must be greater than 0");
            }
            productRepository.findById(reqLine.getProductId()).orElseThrow(NoSuchElementException::new);
            uomRepository.findById(reqLine.getUomId()).orElseThrow(NoSuchElementException::new);
            if (reqLine.getLocationId() != null) {
                var location = locationRepository.findById(reqLine.getLocationId()).orElseThrow(NoSuchElementException::new);
                if (!warehouseId.equals(location.getWarehouseId())) {
                    throw new IllegalArgumentException("locationId does not belong to warehouseId");
                }
            }

            InboundReceiptLine line = new InboundReceiptLine();
            line.setReceiptId(receiptId);
            line.setProductId(reqLine.getProductId());
            line.setUomId(reqLine.getUomId());
            line.setQuantity(reqLine.getQuantity());
            line.setLocationId(reqLine.getLocationId());
            line.setMfgDate(reqLine.getMfgDate());
            line.setShelfLife(reqLine.getShelfLife());
            line.setExpDate(reqLine.getExpDate());
            lines.add(line);
        }
        return lines;
    }

    private InboundReceipt getRequiredReceipt(UUID receiptId) {
        return inboundReceiptRepository.findById(receiptId).orElseThrow(NoSuchElementException::new);
    }

    private List<InboundReceiptLine> ensureHasLines(UUID receiptId) {
        List<InboundReceiptLine> lines = inboundReceiptLineRepository.findByReceiptId(receiptId);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Inbound receipt must have at least one line");
        }
        return lines;
    }

    private String resolveCode(String requestedCode) {
        if (requestedCode != null && !requestedCode.isBlank()) {
            String normalized = requestedCode.trim().toUpperCase();
            if (inboundReceiptRepository.existsByCode(normalized)) {
                throw new IllegalArgumentException("Inbound code already exists");
            }
            return normalized;
        }
        String generated;
        do {
            generated = "INB-" + CODE_TS.format(OffsetDateTime.now());
        } while (inboundReceiptRepository.existsByCode(generated));
        return generated;
    }

    private UUID findUserIdByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return userRepository.findByUsername(username).map(User::getId).orElse(null);
    }

    private String defaultIfBlank(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim().toUpperCase();
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
