package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.InventoryDtos;
import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/balances")
    public ResponseEntity<Page<InventoryDtos.BalanceResponse>> getBalances(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "productId", required = false) UUID productId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryDtos.BalanceResponse> body = inventoryService.getBalances(warehouseId, productId, pageable)
                .map(this::toBalanceResponse);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Page<InventoryDtos.BalanceResponse>> getLowStock(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryDtos.BalanceResponse> body = inventoryService.getLowStock(warehouseId, pageable)
                .map(this::toBalanceResponse);
        return ResponseEntity.ok(body);
    }

    private InventoryDtos.BalanceResponse toBalanceResponse(InventoryBalance balance) {
        InventoryDtos.BalanceResponse response = new InventoryDtos.BalanceResponse();
        response.setId(balance.getId());
        response.setProductId(balance.getProductId());
        response.setWarehouseId(balance.getWarehouseId());
        response.setLocationId(balance.getLocationId());
        response.setQtyOnHand(balance.getQtyOnHand());
        response.setQtyReserved(balance.getQtyReserved());
        response.setMinQty(balance.getMinQty());
        response.setMaxQty(balance.getMaxQty());
        return response;
    }
}
