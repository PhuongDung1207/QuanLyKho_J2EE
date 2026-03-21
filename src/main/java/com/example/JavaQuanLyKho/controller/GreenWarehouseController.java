package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.*;
import com.example.JavaQuanLyKho.service.GreenWarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/green-warehouse")
public class GreenWarehouseController {

    private final GreenWarehouseService greenWarehouseService;

    public GreenWarehouseController(GreenWarehouseService greenWarehouseService) {
        this.greenWarehouseService = greenWarehouseService;
    }

    @GetMapping("/aging")
    public Page<InventoryAgingResponse> getInventoryAging(
            @RequestParam(required = false) UUID warehouseId,
            Pageable pageable
    ) {
        return greenWarehouseService.getInventoryAging(warehouseId, pageable);
    }

    @GetMapping("/slow-moving")
    public Page<SlowMovingResponse> getSlowMoving(
            @RequestParam(required = false) UUID warehouseId,
            @RequestParam(defaultValue = "90") int thresholdDays,
            Pageable pageable
    ) {
        return greenWarehouseService.getSlowMovingItems(warehouseId, thresholdDays, pageable);
    }

    @GetMapping("/expiring")
    public Page<ExpiringResponse> getExpiring(
            @RequestParam(required = false) UUID warehouseId,
            @RequestParam(defaultValue = "30") int thresholdDays,
            Pageable pageable
    ) {
        return greenWarehouseService.getExpiringItems(warehouseId, thresholdDays, pageable);
    }

    @GetMapping("/waste/total")
    public BigDecimal getWasteTotal(
            @RequestParam(required = false) UUID warehouseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end
    ) {
        return greenWarehouseService.getWasteTotalQuantity(warehouseId, start, end);
    }

    @GetMapping("/waste/stats")
    public Map<String, BigDecimal> getWasteStats(
            @RequestParam(required = false) UUID warehouseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end
    ) {
        return greenWarehouseService.getWasteStatisticsByType(warehouseId, start, end);
    }
}
