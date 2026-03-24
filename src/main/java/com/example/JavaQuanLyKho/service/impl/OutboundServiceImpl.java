package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.exception.InvalidStateTransitionException;
import com.example.JavaQuanLyKho.exception.OutboundStockNotEnoughException;
import com.example.JavaQuanLyKho.model.dto.OutboundDtos;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueStatus;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueLineRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueRepository;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.OutboundService;
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
public class OutboundServiceImpl implements OutboundService {

    private static final DateTimeFormatter CODE_TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final OutboundIssueRepository outboundIssueRepository;
    private final OutboundIssueLineRepository outboundIssueLineRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UomRepository uomRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public OutboundServiceImpl(
            OutboundIssueRepository outboundIssueRepository,
            OutboundIssueLineRepository outboundIssueLineRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository,
            UomRepository uomRepository,
            LocationRepository locationRepository,
            UserRepository userRepository
    ) {
        this.outboundIssueRepository = outboundIssueRepository;
        this.outboundIssueLineRepository = outboundIssueLineRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<OutboundIssue> findAll(Pageable pageable) {
        return outboundIssueRepository.findAll(pageable);
    }

    @Override
    public List<OutboundIssueLine> findLines(UUID issueId) {
        return outboundIssueLineRepository.findByIssueId(issueId);
    }

    @Override
    @Transactional
    public OutboundIssue create(OutboundDtos.CreateRequest request, String actorUsername) {
        validateCreateRequest(request);
        warehouseRepository.findById(request.getWarehouseId()).orElseThrow(NoSuchElementException::new);

        OutboundIssue issue = new OutboundIssue();
        issue.setCode(resolveCode(request.getCode()));
        issue.setType(defaultIfBlank(request.getType(), "SALE"));
        issue.setWarehouseId(request.getWarehouseId());
        issue.setCustomerId(request.getCustomerId());
        issue.setStatus(OutboundIssueStatus.DRAFT);
        issue.setCreatedAt(OffsetDateTime.now());
        issue.setCreatedBy(findUserIdByUsername(actorUsername));
        OutboundIssue savedIssue = outboundIssueRepository.save(issue);

        List<OutboundIssueLine> lines = mapAndValidateLines(savedIssue.getId(), request.getWarehouseId(), request.getLines());
        outboundIssueLineRepository.saveAll(lines);
        return savedIssue;
    }

    @Override
    @Transactional
    public OutboundIssue submit(UUID issueId) {
        OutboundIssue issue = getRequiredIssue(issueId);
        if (issue.getStatus() != OutboundIssueStatus.DRAFT) {
            throw new InvalidStateTransitionException("Outbound issue must be DRAFT before SUBMITTED");
        }
        ensureHasLines(issueId);
        issue.setStatus(OutboundIssueStatus.SUBMITTED);
        return outboundIssueRepository.save(issue);
    }

    @Override
    @Transactional
    public OutboundIssue approve(UUID issueId, String actorUsername) {
        OutboundIssue issue = getRequiredIssue(issueId);
        if (issue.getStatus() != OutboundIssueStatus.SUBMITTED) {
            throw new InvalidStateTransitionException("Outbound issue must be SUBMITTED before APPROVED");
        }
        issue.setStatus(OutboundIssueStatus.APPROVED);
        issue.setApprovedBy(findUserIdByUsername(actorUsername));
        issue.setApprovedAt(OffsetDateTime.now());
        return outboundIssueRepository.save(issue);
    }

    @Override
    @Transactional
    public OutboundIssue reject(UUID issueId) {
        OutboundIssue issue = getRequiredIssue(issueId);
        OutboundIssueStatus current = issue.getStatus();
        if (current != OutboundIssueStatus.DRAFT
                && current != OutboundIssueStatus.SUBMITTED
                && current != OutboundIssueStatus.APPROVED) {
            throw new InvalidStateTransitionException("Outbound issue can only be rejected from DRAFT/SUBMITTED/APPROVED");
        }
        issue.setStatus(OutboundIssueStatus.CANCELLED);
        return outboundIssueRepository.save(issue);
    }

    @Override
    @Transactional
    public OutboundIssue complete(UUID issueId) {
        OutboundIssue issue = getRequiredIssue(issueId);
        if (issue.getStatus() != OutboundIssueStatus.APPROVED) {
            throw new InvalidStateTransitionException("Outbound issue must be APPROVED before COMPLETED");
        }

        List<OutboundIssueLine> lines = ensureHasLines(issueId);
        Map<StockKey, BigDecimal> requiredByStockKey = aggregateRequired(lines);
        Map<StockKey, InventoryBalance> balances = new HashMap<>();

        for (Map.Entry<StockKey, BigDecimal> entry : requiredByStockKey.entrySet()) {
            StockKey key = entry.getKey();
            BigDecimal required = entry.getValue();
            InventoryBalance balance = inventoryBalanceRepository
                    .findForUpdate(key.productId, issue.getWarehouseId(), key.locationId)
                    .orElseThrow(() -> new OutboundStockNotEnoughException(
                            "No stock for product " + key.productId + " at warehouse " + issue.getWarehouseId()
                    ));

            BigDecimal available = safe(balance.getQtyOnHand()).subtract(safe(balance.getQtyReserved()));
            if (available.compareTo(required) < 0) {
                throw new OutboundStockNotEnoughException(
                        "Not enough stock for product " + key.productId + ": required=" + required + ", available=" + available
                );
            }
            balances.put(key, balance);
        }

        for (Map.Entry<StockKey, BigDecimal> entry : requiredByStockKey.entrySet()) {
            StockKey key = entry.getKey();
            InventoryBalance balance = balances.get(key);
            balance.setQtyOnHand(safe(balance.getQtyOnHand()).subtract(entry.getValue()));
            balance.setLastOutboundDate(OffsetDateTime.now());
            inventoryBalanceRepository.save(balance);
        }

        issue.setStatus(OutboundIssueStatus.COMPLETED);
        issue.setCompletedAt(OffsetDateTime.now());
        return outboundIssueRepository.save(issue);
    }

    @Override
    @Transactional
    public void delete(UUID issueId) {
        OutboundIssue issue = getRequiredIssue(issueId);
        if (issue.getStatus() != OutboundIssueStatus.DRAFT) {
            throw new InvalidStateTransitionException("Only DRAFT outbound issue can be deleted");
        }
        outboundIssueLineRepository.deleteByIssueId(issueId);
        outboundIssueRepository.delete(issue);
    }

    private void validateCreateRequest(OutboundDtos.CreateRequest request) {
        if (request.getWarehouseId() == null) {
            throw new IllegalArgumentException("warehouseId must not be null");
        }
        if (request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("Outbound issue must have at least one line");
        }
    }

    private List<OutboundIssueLine> mapAndValidateLines(
            UUID issueId,
            UUID warehouseId,
            List<OutboundDtos.LineRequest> requests
    ) {
        List<OutboundIssueLine> lines = new ArrayList<>();
        for (OutboundDtos.LineRequest reqLine : requests) {
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

            OutboundIssueLine line = new OutboundIssueLine();
            line.setIssueId(issueId);
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

    private OutboundIssue getRequiredIssue(UUID issueId) {
        return outboundIssueRepository.findById(issueId).orElseThrow(NoSuchElementException::new);
    }

    private List<OutboundIssueLine> ensureHasLines(UUID issueId) {
        List<OutboundIssueLine> lines = outboundIssueLineRepository.findByIssueId(issueId);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Outbound issue must have at least one line");
        }
        return lines;
    }

    private Map<StockKey, BigDecimal> aggregateRequired(List<OutboundIssueLine> lines) {
        Map<StockKey, BigDecimal> required = new HashMap<>();
        for (OutboundIssueLine line : lines) {
            StockKey key = new StockKey(line.getProductId(), line.getLocationId());
            BigDecimal current = required.getOrDefault(key, BigDecimal.ZERO);
            required.put(key, current.add(line.getQuantity()));
        }
        return required;
    }

    private String resolveCode(String requestedCode) {
        if (requestedCode != null && !requestedCode.isBlank()) {
            String normalized = requestedCode.trim().toUpperCase();
            if (outboundIssueRepository.existsByCode(normalized)) {
                throw new IllegalArgumentException("Outbound code already exists");
            }
            return normalized;
        }
        String generated;
        do {
            generated = "OUT-" + CODE_TS.format(OffsetDateTime.now());
        } while (outboundIssueRepository.existsByCode(generated));
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

    private record StockKey(UUID productId, UUID locationId) {
    }
}
