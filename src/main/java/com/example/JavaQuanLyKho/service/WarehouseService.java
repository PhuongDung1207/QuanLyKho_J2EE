package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface WarehouseService {

    Page<Warehouse> findAll(Pageable pageable);

    Warehouse findById(UUID id);

    Warehouse create(Warehouse warehouse);

    Warehouse update(UUID id, Warehouse warehouse);

    void delete(UUID id);

    /**
     * Tính số lượng hàng hiện tại trong kho dựa trên:
     * + Phiếu nhập (Inbound) COMPLETED
     * + Phiếu transfer vào kho (Transfer toWarehouse) COMPLETED
     * - Phiếu xuất (Outbound) COMPLETED
     * - Phiếu transfer từ kho đi (Transfer fromWarehouse) COMPLETED
     */
    BigDecimal calculateCurrentStock(UUID warehouseId);
}

