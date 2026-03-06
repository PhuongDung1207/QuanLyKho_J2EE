package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses/{warehouseId}/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Location>> getLocations(@PathVariable("warehouseId") UUID warehouseId) {
        return ResponseEntity.ok(locationService.findByWarehouse(warehouseId));
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@PathVariable("warehouseId") UUID warehouseId,
                                                   @Valid @RequestBody Location location) {
        location.setWarehouseId(warehouseId);
        return ResponseEntity.ok(locationService.create(location));
    }
}

