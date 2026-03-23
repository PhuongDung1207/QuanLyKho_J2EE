package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.*;
import com.example.JavaQuanLyKho.service.GreenWarehouseService;
import com.example.JavaQuanLyKho.service.WarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Controller
@RequestMapping("/green-warehouse")
public class GreenWarehouseViewController {

    private final GreenWarehouseService greenWarehouseService;
    private final WarehouseService warehouseService;

    public GreenWarehouseViewController(
            GreenWarehouseService greenWarehouseService,
            WarehouseService warehouseService
    ) {
        this.greenWarehouseService = greenWarehouseService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            Model model
    ) {
        model.addAttribute("summary", greenWarehouseService.getHealthSummary(warehouseId));
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        return "green-warehouse/dashboard";
    }

    @GetMapping("/aging")
    public String aging(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryAgingResponse> balances = greenWarehouseService.getInventoryAging(warehouseId, pageable);
        
        model.addAttribute("balances", balances);
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        return "green-warehouse/aging";
    }

    @GetMapping("/slow-moving")
    public String slowMoving(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "thresholdDays", defaultValue = "90") int thresholdDays,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SlowMovingResponse> balances = greenWarehouseService.getSlowMovingItems(warehouseId, thresholdDays, pageable);
        
        model.addAttribute("balances", balances);
        model.addAttribute("thresholdDays", thresholdDays);
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        return "green-warehouse/slow-moving";
    }

    @GetMapping("/expiring")
    public String expiring(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "thresholdDays", defaultValue = "30") int thresholdDays,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExpiringResponse> balances = greenWarehouseService.getExpiringItems(warehouseId, thresholdDays, pageable);
        
        model.addAttribute("balances", balances);
        model.addAttribute("thresholdDays", thresholdDays);
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        return "green-warehouse/expiring";
    }

    @GetMapping("/waste")
    public String waste(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime start,
            @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime end,
            Model model
    ) {
        if (start == null) start = LocalDateTime.now().minusMonths(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (end == null) end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        OffsetDateTime startODT = start.atOffset(ZoneOffset.UTC);
        OffsetDateTime endODT = end.atOffset(ZoneOffset.UTC);

        model.addAttribute("stats", greenWarehouseService.getWasteStatisticsByType(warehouseId, startODT, endODT));
        model.addAttribute("details", greenWarehouseService.getWasteDetails(warehouseId, startODT, endODT));
        model.addAttribute("total", greenWarehouseService.getWasteTotalQuantity(warehouseId, startODT, endODT));
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "green-warehouse/waste";
    }
}
