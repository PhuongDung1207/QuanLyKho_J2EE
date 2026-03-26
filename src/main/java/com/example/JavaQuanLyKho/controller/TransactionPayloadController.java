package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.TransactionPageDtos;
import com.example.JavaQuanLyKho.service.TransactionPageQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionPayloadController {

    private final TransactionPageQueryService transactionPageQueryService;

    public TransactionPayloadController(TransactionPageQueryService transactionPageQueryService) {
        this.transactionPageQueryService = transactionPageQueryService;
    }

    @GetMapping("/payload")
    public ResponseEntity<TransactionPageDtos.Payload> payload() {
        return ResponseEntity.ok(transactionPageQueryService.getPayload());
    }

    @GetMapping("/outbound-section-products")
    public ResponseEntity<List<UUID>> outboundSectionProducts(
            @RequestParam("warehouseId") UUID warehouseId,
            @RequestParam("sectionId") UUID sectionId
    ) {
        return ResponseEntity.ok(transactionPageQueryService.getOutboundSectionProductIds(warehouseId, sectionId));
    }

    @GetMapping("/transfer-source-products")
    public ResponseEntity<List<UUID>> transferSourceProducts(
            @RequestParam("warehouseId") UUID warehouseId
    ) {
        return ResponseEntity.ok(transactionPageQueryService.getTransferSourceProductIds(warehouseId));
    }
}
