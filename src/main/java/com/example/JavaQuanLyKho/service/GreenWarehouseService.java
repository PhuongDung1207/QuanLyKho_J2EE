package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public interface GreenWarehouseService {
    
    // 18. Theo dõi tuổi tồn kho
    Page<InventoryAgingResponse> getInventoryAging(UUID warehouseId, Pageable pageable);
    
    // 19. Cảnh báo hàng tồn lâu
    Page<SlowMovingResponse> getSlowMovingItems(UUID warehouseId, int thresholdDays, Pageable pageable);
    
    // 20. Cảnh báo hàng sắp hết hạn
    Page<ExpiringResponse> getExpiringItems(UUID warehouseId, int thresholdDays, Pageable pageable);
    
    // 21. Thống kê hàng hư hỏng / hủy bỏ
    BigDecimal getWasteTotalQuantity(UUID warehouseId, OffsetDateTime start, OffsetDateTime end);
    
    Map<String, BigDecimal> getWasteStatisticsByType(UUID warehouseId, OffsetDateTime start, OffsetDateTime end);

    java.util.List<WasteDetailResponse> getWasteDetails(UUID warehouseId, OffsetDateTime start, OffsetDateTime end);

    GreenWarehouseDtos.HealthSummary getHealthSummary(UUID warehouseId);
}
