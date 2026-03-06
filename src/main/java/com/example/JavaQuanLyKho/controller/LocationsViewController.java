package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.service.LocationService;
import com.example.JavaQuanLyKho.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/warehouses/{warehouseId}/locations")
public class LocationsViewController {

    private final LocationService locationService;
    private final WarehouseService warehouseService;

    public LocationsViewController(LocationService locationService, WarehouseService warehouseService) {
        this.locationService = locationService;
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String list(@PathVariable("warehouseId") UUID warehouseId, Model model) {
        Warehouse warehouse = warehouseService.findById(warehouseId);
        List<Location> locations = locationService.findByWarehouse(warehouseId);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("locations", locations);
        model.addAttribute("createForm", new Location());
        return "locations/list";
    }

    @PostMapping
    public String create(@PathVariable("warehouseId") UUID warehouseId,
                         @Valid @ModelAttribute("createForm") Location createForm,
                         BindingResult bindingResult,
                         Model model) {
        Warehouse warehouse = warehouseService.findById(warehouseId);
        if (bindingResult.hasErrors()) {
            List<Location> locations = locationService.findByWarehouse(warehouseId);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("locations", locations);
            return "locations/list";
        }
        createForm.setWarehouseId(warehouseId);
        locationService.create(createForm);
        return "redirect:/warehouses/" + warehouseId + "/locations";
    }
}

