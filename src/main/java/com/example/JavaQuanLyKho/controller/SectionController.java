package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses/{warehouseId}/sections")
public class SectionController {

    private final LocationService locationService;

    public SectionController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getSections(@PathVariable("warehouseId") UUID warehouseId) {
        return ResponseEntity.ok(locationService.findByWarehouse(warehouseId));
    }

    @PostMapping
    public ResponseEntity<Location> createSection(@PathVariable("warehouseId") UUID warehouseId,
                                                  @Valid @RequestBody Location section) {
        section.setWarehouseId(warehouseId);
        return ResponseEntity.ok(locationService.create(section));
    }
}
