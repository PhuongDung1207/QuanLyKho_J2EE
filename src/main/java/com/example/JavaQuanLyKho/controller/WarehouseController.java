package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.WarehouseDtos;
import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.service.WarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseDtos.Response>> getWarehouses(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Warehouse> warehouses = warehouseService.findAll(pageable);
        Page<WarehouseDtos.Response> body = warehouses.map(this::toResponse);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDtos.Response> getWarehouse(@PathVariable("id") UUID id) {
        Warehouse warehouse = warehouseService.findById(id);
        return ResponseEntity.ok(toResponse(warehouse));
    }

    @PostMapping
    public ResponseEntity<WarehouseDtos.Response> createWarehouse(
            @Validated @RequestBody WarehouseDtos.CreateRequest request
    ) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(request.getCode());
        warehouse.setName(request.getName());
        warehouse.setStatus(request.getStatus());
        warehouse.setCapacity(request.getCapacity());
        Warehouse saved = warehouseService.create(warehouse);
        WarehouseDtos.Response body = toResponse(saved);
        return ResponseEntity.created(URI.create("/api/v1/warehouses/" + saved.getId())).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDtos.Response> updateWarehouse(
            @PathVariable("id") UUID id,
            @Validated @RequestBody WarehouseDtos.UpdateRequest request
    ) {
        Warehouse warehouse = new Warehouse();
        warehouse.setCode(request.getCode());
        warehouse.setName(request.getName());
        warehouse.setStatus(request.getStatus());
        warehouse.setCapacity(request.getCapacity());
        Warehouse saved = warehouseService.update(id, warehouse);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable("id") UUID id) {
        warehouseService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private WarehouseDtos.Response toResponse(Warehouse warehouse) {
        WarehouseDtos.Response response = new WarehouseDtos.Response();
        response.setId(warehouse.getId());
        response.setCode(warehouse.getCode());
        response.setName(warehouse.getName());
        response.setStatus(warehouse.getStatus());
        response.setCapacity(warehouse.getCapacity());
        return response;
    }
}

