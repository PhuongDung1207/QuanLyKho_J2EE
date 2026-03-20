package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.WarehouseDtos;
import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.service.LocationService;
import com.example.JavaQuanLyKho.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/warehouses")
public class WarehouseViewController {

    private final WarehouseService warehouseService;
    private final LocationService  locationService;

    public WarehouseViewController(WarehouseService warehouseService,
                                   LocationService locationService) {
        this.warehouseService = warehouseService;
        this.locationService  = locationService;
    }

    /* ── helpers ── */
    private List<Location> collectAllLocations(List<Warehouse> warehouses) {
        List<Location> all = new ArrayList<>();
        for (Warehouse w : warehouses) {
            all.addAll(locationService.findByWarehouse(w.getId()));
        }
        return all;
    }

    private void populateModel(Model model) {
        List<Warehouse> warehouses = warehouseService.findAll(Pageable.unpaged()).getContent();
        model.addAttribute("warehouses",   warehouses);
        model.addAttribute("allLocations", collectAllLocations(warehouses));
        model.addAttribute("createForm",   new WarehouseDtos.CreateRequest());

        // Tính currentStock và % tồn kho cho từng warehouse
        Map<UUID, BigDecimal> stockMap  = new HashMap<>();
        Map<UUID, Integer>    pctMap    = new HashMap<>();
        for (Warehouse w : warehouses) {
            BigDecimal stock = warehouseService.calculateCurrentStock(w.getId());
            stockMap.put(w.getId(), stock);
            if (w.getCapacity() != null && w.getCapacity().compareTo(BigDecimal.ZERO) > 0) {
                int pct = stock.multiply(BigDecimal.valueOf(100))
                               .divide(w.getCapacity(), 0, java.math.RoundingMode.HALF_UP)
                               .intValue();
                pctMap.put(w.getId(), Math.min(pct, 100));
            } else {
                pctMap.put(w.getId(), -1); // không có capacity
            }
        }
        model.addAttribute("warehouseStockMap", stockMap);
        model.addAttribute("warehouseStockPctMap", pctMap);
    }

    /* ── GET /warehouses ── */
    @GetMapping
    public String listWarehouses(Model model) {
        populateModel(model);
        return "warehouses/list";
    }

    /* ── POST /warehouses  (create) ── */
    @PostMapping
    public String createWarehouse(
            @Valid @ModelAttribute("createForm") WarehouseDtos.CreateRequest form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            populateModel(model);
            return "warehouses/list";
        }
        try {
            Warehouse w = new Warehouse();
            w.setCode(form.getCode());
            w.setName(form.getName());
            w.setStatus(form.getStatus());
            w.setCapacity(form.getCapacity());
            warehouseService.create(w);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Warehouse \"" + form.getName() + "\" created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to create warehouse: " + e.getMessage());
        }
        return "redirect:/warehouses";
    }

    /* ── POST /warehouses/{id}/update ── */
    @PostMapping("/{id}/update")
    public String updateWarehouse(
            @PathVariable UUID id,
            @RequestParam("code")     String code,
            @RequestParam("name")     String name,
            @RequestParam("status")   String status,
            @RequestParam(value = "capacity", required = false) BigDecimal capacity,
            RedirectAttributes redirectAttrs) {
        try {
            Warehouse w = new Warehouse();
            w.setCode(code.toUpperCase().trim());
            w.setName(name.trim());
            w.setStatus(status);
            w.setCapacity(capacity);
            warehouseService.update(id, w);
            redirectAttrs.addFlashAttribute("successMessage", "Warehouse updated successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to update warehouse: " + e.getMessage());
        }
        return "redirect:/warehouses";
    }

    /* ── POST /warehouses/{id}/delete ── */
    @PostMapping("/{id}/delete")
    public String deleteWarehouse(
            @PathVariable UUID id,
            RedirectAttributes redirectAttrs) {
        try {
            warehouseService.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "Warehouse deleted successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to delete warehouse: " + e.getMessage());
        }
        return "redirect:/warehouses";
    }
}

