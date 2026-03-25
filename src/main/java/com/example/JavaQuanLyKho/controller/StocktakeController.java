package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.StocktakeQRRequestDTO;
import com.example.JavaQuanLyKho.model.entity.Stocktake;
import com.example.JavaQuanLyKho.model.entity.StocktakeLine;
import com.example.JavaQuanLyKho.service.StocktakeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stocktakes")
public class StocktakeController {

    private final StocktakeService stocktakeService;

    public StocktakeController(StocktakeService stocktakeService) {
        this.stocktakeService = stocktakeService;
    }

    @GetMapping
    public ResponseEntity<Page<Stocktake>> getStocktakes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(stocktakeService.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<Stocktake> createStocktake(@Validated @RequestBody Stocktake stocktake) {
        return ResponseEntity.ok(stocktakeService.create(stocktake));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeStocktake(@PathVariable UUID id) {
        stocktakeService.close(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/lines")
    public ResponseEntity<List<StocktakeLine>> getStocktakeLines(@PathVariable UUID id) {
        return ResponseEntity.ok(stocktakeService.getLines(id));
    }

    @PostMapping("/{id}/scan-qr")
    public ResponseEntity<StocktakeLine> scanQRForStocktake(@PathVariable UUID id, @RequestBody StocktakeQRRequestDTO requestDTO) {
        return ResponseEntity.ok(stocktakeService.processQrScan(id, requestDTO));
    }
}
