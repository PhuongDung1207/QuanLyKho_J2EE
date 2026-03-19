package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.SupplierDtos;
import com.example.JavaQuanLyKho.model.entity.Supplier;
import com.example.JavaQuanLyKho.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDtos.Response>> getSuppliers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SupplierDtos.Response> body = supplierService.findAll(pageable).map(this::toResponse);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDtos.Response> getSupplier(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(supplierService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<SupplierDtos.Response> createSupplier(@Valid @RequestBody SupplierDtos.CreateRequest request) {
        Supplier supplier = new Supplier();
        supplier.setCode(request.getCode());
        supplier.setName(request.getName());
        supplier.setContactName(request.getContactName());
        supplier.setContactEmail(request.getContactEmail());
        supplier.setContactPhone(request.getContactPhone());
        supplier.setAddress(request.getAddress());
        supplier.setStatus(request.getStatus());

        Supplier created = supplierService.create(supplier);
        SupplierDtos.Response body = toResponse(created);
        return ResponseEntity.created(URI.create("/api/v1/suppliers/" + created.getId())).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDtos.Response> updateSupplier(
            @PathVariable("id") UUID id,
            @Valid @RequestBody SupplierDtos.UpdateRequest request
    ) {
        Supplier supplier = new Supplier();
        supplier.setCode(request.getCode());
        supplier.setName(request.getName());
        supplier.setContactName(request.getContactName());
        supplier.setContactEmail(request.getContactEmail());
        supplier.setContactPhone(request.getContactPhone());
        supplier.setAddress(request.getAddress());
        supplier.setStatus(request.getStatus());
        return ResponseEntity.ok(toResponse(supplierService.update(id, supplier)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable("id") UUID id) {
        supplierService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private SupplierDtos.Response toResponse(Supplier supplier) {
        SupplierDtos.Response response = new SupplierDtos.Response();
        response.setId(supplier.getId());
        response.setCode(supplier.getCode());
        response.setName(supplier.getName());
        response.setContactName(supplier.getContactName());
        response.setContactEmail(supplier.getContactEmail());
        response.setContactPhone(supplier.getContactPhone());
        response.setAddress(supplier.getAddress());
        response.setStatus(supplier.getStatus());
        response.setCreatedAt(supplier.getCreatedAt());
        response.setUpdatedAt(supplier.getUpdatedAt());
        return response;
    }
}
