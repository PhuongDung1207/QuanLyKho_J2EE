package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryBalanceRepository inventoryBalanceRepository;

    public InventoryServiceImpl(InventoryBalanceRepository inventoryBalanceRepository) {
        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }

    @Override
    public Page<InventoryBalance> getBalances(UUID warehouseId, UUID productId, Pageable pageable) {
        if (warehouseId != null && productId != null) {
            return inventoryBalanceRepository.findByWarehouseIdAndProductId(warehouseId, productId, pageable);
        }
        if (warehouseId != null) {
            return inventoryBalanceRepository.findByWarehouseId(warehouseId, pageable);
        }
        if (productId != null) {
            return inventoryBalanceRepository.findByProductId(productId, pageable);
        }
        return inventoryBalanceRepository.findAll(pageable);
    }

    @Override
    public Page<InventoryBalance> getLowStock(UUID warehouseId, Pageable pageable) {
        if (warehouseId != null) {
            return inventoryBalanceRepository.findLowStockByWarehouseId(warehouseId, pageable);
        }
        return inventoryBalanceRepository.findLowStock(pageable);
    }
}
