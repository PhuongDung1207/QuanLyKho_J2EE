package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.dto.ScanResponseDTO;
import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.service.BatchService;
import com.example.JavaQuanLyKho.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory/scan")
public class InventoryScanController {

    private final ProductService productService;
    private final BatchService batchService;

    public InventoryScanController(ProductService productService, BatchService batchService) {
        this.productService = productService;
        this.batchService = batchService;
    }

    @GetMapping
    public ResponseEntity<ScanResponseDTO> scanCode(@RequestParam String code) {
        // Try exact match for Batch code
        try {
            Batch batch = batchService.findByCode(code);
            // In a real application, you might want to fetch the associated Product here too
            Product product = productService.findById(batch.getProductId());
            return ResponseEntity.ok(new ScanResponseDTO("BATCH", product, batch));
        } catch (Exception e1) {
            // Not a Batch code, try Product barcode
            try {
                Product product = productService.findByBarcode(code);
                return ResponseEntity.ok(new ScanResponseDTO("PRODUCT", product, null));
            } catch (Exception e2) {
                // Return 404 if neither matched
                return ResponseEntity.notFound().build();
            }
        }
    }
}
