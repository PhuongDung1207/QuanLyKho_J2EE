package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.service.BatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batches")
public class BatchController {

    private final BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping
    public ResponseEntity<Page<Batch>> getBatches(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(batchService.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Batch> getBatchByCode(@PathVariable String code) {
        return ResponseEntity.ok(batchService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<Batch> createBatch(@Validated @RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.create(batch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(@PathVariable UUID id, @Validated @RequestBody Batch batch) {
        return ResponseEntity.ok(batchService.update(id, batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable UUID id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
