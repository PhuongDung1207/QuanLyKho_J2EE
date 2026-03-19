package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryService {

    Page<InventoryBalance> getBalances(UUID warehouseId, UUID productId, Pageable pageable);

    Page<InventoryBalance> getLowStock(UUID warehouseId, Pageable pageable);
}
