package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.service.WarehouseService;
import com.example.JavaQuanLyKho.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sections")
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

        // If a warehouse is selected, load its sections
        if (warehouseId != null) {
            model.addAttribute("selectedWarehouseId", warehouseId);
            List<Location> sections = locationService.findByWarehouse(warehouseId);
            model.addAttribute("sections", sections);
            
            Warehouse selectedWarehouse = warehouseService.findById(warehouseId);
            model.addAttribute("selectedWarehouse", selectedWarehouse);
        }

        return "sections/list";
    }

    @PostMapping
    public String createSection(@RequestParam("warehouseId") UUID warehouseId,
                                @RequestParam("code") String code,
                                @RequestParam(name = "type", required = false) String type,
                                RedirectAttributes redirectAttributes) {
        String normalizedCode = code == null ? "" : code.trim().toUpperCase();
        if (normalizedCode.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Section code is required.");
            return "redirect:/sections?warehouseId=" + warehouseId;
        }

        Location section = new Location();
        section.setWarehouseId(warehouseId);
        section.setCode(normalizedCode);

        String normalizedType = type == null ? "" : type.trim();
        section.setType(normalizedType.isBlank() ? null : normalizedType);

        try {
            locationService.create(section);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Section \"" + normalizedCode + "\" has been created.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Section code already exists in this warehouse.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not create section: " + ex.getMessage());
        }

        return "redirect:/sections?warehouseId=" + warehouseId;
    }
}
