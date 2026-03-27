package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.ScanResponseDTO;
import com.example.JavaQuanLyKho.model.dto.ScanInventoryDetailDTO;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.BatchService;
import com.example.JavaQuanLyKho.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inventory/scan")
public class InventoryScanController {

    private final ProductService productService;
    private final BatchService batchService;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocationRepository locationRepository;

    public InventoryScanController(ProductService productService, BatchService batchService,
                                   InventoryBalanceRepository inventoryBalanceRepository,
                                   WarehouseRepository warehouseRepository,
                                   LocationRepository locationRepository) {
        this.productService = productService;
        this.batchService = batchService;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.warehouseRepository = warehouseRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public ResponseEntity<ScanResponseDTO> scanCode(@RequestParam String code) {
        try {
            Batch batch = batchService.findByCode(code);
            Product product = productService.findById(batch.getProductId());
            return ResponseEntity.ok(buildResponse("BATCH", product, batch));
        } catch (Exception e1) {
            try {
                Product product = productService.findByBarcode(code);
                return ResponseEntity.ok(buildResponse("PRODUCT", product, null));
            } catch (Exception e2) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    private ScanResponseDTO buildResponse(String type, Product product, Batch batch) {
        List<InventoryBalance> balances = inventoryBalanceRepository.findByProductId(product.getId(), Pageable.unpaged())
                .getContent();

        Map<UUID, String> warehouseNames = warehouseRepository.findAllById(
                        balances.stream()
                                .map(InventoryBalance::getWarehouseId)
                                .distinct()
                                .toList()
                ).stream()
                .collect(Collectors.toMap(com.example.JavaQuanLyKho.model.entity.Warehouse::getId,
                        com.example.JavaQuanLyKho.model.entity.Warehouse::getName));

        Map<UUID, String> locationCodes = locationRepository.findAllById(
                        balances.stream()
                                .map(InventoryBalance::getLocationId)
                                .filter(java.util.Objects::nonNull)
                                .distinct()
                                .toList()
                ).stream()
                .collect(Collectors.toMap(com.example.JavaQuanLyKho.model.entity.Location::getId,
                        com.example.JavaQuanLyKho.model.entity.Location::getCode));

        List<ScanInventoryDetailDTO> inventoryDetails = balances.stream()
                .map(balance -> toInventoryDetail(balance, warehouseNames, locationCodes))
                .sorted(Comparator.comparing(ScanInventoryDetailDTO::getWarehouseName,
                                Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(ScanInventoryDetailDTO::getLocationCode,
                                Comparator.nullsLast(String::compareToIgnoreCase)))
                .toList();

        BigDecimal totalOnHand = balances.stream()
                .map(InventoryBalance::getQtyOnHand)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalReserved = balances.stream()
                .map(InventoryBalance::getQtyReserved)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAvailable = totalOnHand.subtract(totalReserved);

        return new ScanResponseDTO(type, product, batch, totalOnHand, totalReserved, totalAvailable, inventoryDetails);
    }

    private ScanInventoryDetailDTO toInventoryDetail(InventoryBalance balance,
                                                     Map<UUID, String> warehouseNames,
                                                     Map<UUID, String> locationCodes) {
        BigDecimal qtyOnHand = defaultZero(balance.getQtyOnHand());
        BigDecimal qtyReserved = defaultZero(balance.getQtyReserved());
        BigDecimal qtyAvailable = qtyOnHand.subtract(qtyReserved);
        String status = getStatus(qtyOnHand, balance.getMinQty());
        String statusLabel = switch (status) {
            case "Out" -> "Out of stock";
            case "Low" -> "Low stock";
            default -> "Normal";
        };

        return new ScanInventoryDetailDTO(
                warehouseNames.getOrDefault(balance.getWarehouseId(), "Unknown warehouse"),
                locationCodes.get(balance.getLocationId()),
                qtyOnHand,
                qtyReserved,
                qtyAvailable,
                balance.getMinQty(),
                balance.getMaxQty(),
                balance.getMfgDate(),
                balance.getExpDate(),
                balance.getLastInboundDate(),
                balance.getLastOutboundDate(),
                status,
                statusLabel
        );
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String getStatus(BigDecimal qtyOnHand, BigDecimal minQty) {
        if (qtyOnHand.compareTo(BigDecimal.ZERO) <= 0) {
            return "Out";
        }
        if (minQty != null && qtyOnHand.compareTo(minQty) <= 0) {
            return "Low";
        }
        return "Normal";
    }
}
