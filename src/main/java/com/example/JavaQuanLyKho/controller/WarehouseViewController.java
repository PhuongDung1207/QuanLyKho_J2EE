package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.WarehouseDtos;
import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/warehouses")
public class WarehouseViewController {

    private final WarehouseService warehouseService;

    public WarehouseViewController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String listWarehouses(Model model) {
        Page<Warehouse> page = warehouseService.findAll(Pageable.unpaged());
        List<Warehouse> warehouses = page.getContent();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("createForm", new WarehouseDtos.CreateRequest());
        return "warehouses/list";
    }

    @PostMapping
    public String createWarehouse(
            @Valid @ModelAttribute("createForm") WarehouseDtos.CreateRequest createForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            Page<Warehouse> page = warehouseService.findAll(Pageable.unpaged());
            List<Warehouse> warehouses = page.getContent();
            model.addAttribute("warehouses", warehouses);
            return "warehouses/list";
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(createForm.getCode());
        warehouse.setName(createForm.getName());
        warehouse.setStatus(createForm.getStatus());
        warehouse.setCapacity(createForm.getCapacity());
        warehouseService.create(warehouse);
        return "redirect:/warehouses";
    }
}

