package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.InventoryRowDto;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.service.WarehouseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/inventory")
public class InventoryViewController {

    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final WarehouseService warehouseService;
    private final LocationRepository locationRepository;

    public InventoryViewController(InventoryBalanceRepository inventoryBalanceRepository,
            WarehouseService warehouseService, LocationRepository locationRepository) {
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.warehouseService = warehouseService;
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            Model model,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryRowDto> rows = inventoryBalanceRepository.findInventoryRows(warehouseId, pageable);

        // Summary stats
        long totalSkus = inventoryBalanceRepository.count();
        BigDecimal totalOnHand = inventoryBalanceRepository.sumQuantityByWarehouseGroup().stream()
                .map(r -> r[1] != null ? (BigDecimal) r[1] : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long lowStock = inventoryBalanceRepository.findLowStock(pageable).getTotalElements();
        long outOfStock = rows.stream().filter(r -> "Out".equals(r.getStatus())).count();

        model.addAttribute("rows", rows);
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("selectedWarehouseId", warehouseId);
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("totalSkus", totalSkus);
        model.addAttribute("totalOnHand", totalOnHand);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("outOfStock", outOfStock);

        return "inventory/list";
    }
}
