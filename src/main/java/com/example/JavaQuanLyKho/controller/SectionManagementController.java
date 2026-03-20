package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.service.WarehouseService;
import com.example.JavaQuanLyKho.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/locations")
public class SectionManagementController {

    private final WarehouseService warehouseService;
    private final LocationService locationService;

    public SectionManagementController(WarehouseService warehouseService, LocationService locationService) {
        this.warehouseService = warehouseService;
        this.locationService = locationService;
    }

    @GetMapping
    public String index(@RequestParam(name = "warehouseId", required = false) UUID warehouseId,
                        Model model, HttpServletRequest request) {
        // Active link on sidebar config
        model.addAttribute("currentUri", request.getRequestURI());

        // Get all warehouses to populate the dropdown
        Page<Warehouse> warehousePage = warehouseService.findAll(PageRequest.of(0, 100));
        model.addAttribute("warehouses", warehousePage.getContent());

        // If a warehouse is selected, load its locations
        if (warehouseId != null) {
            model.addAttribute("selectedWarehouseId", warehouseId);
            List<Location> locations = locationService.findByWarehouse(warehouseId);
            model.addAttribute("locations", locations);
            
            Warehouse selectedWarehouse = warehouseService.findById(warehouseId);
            model.addAttribute("selectedWarehouse", selectedWarehouse);
        }

        return "sections/list";
    }
}
