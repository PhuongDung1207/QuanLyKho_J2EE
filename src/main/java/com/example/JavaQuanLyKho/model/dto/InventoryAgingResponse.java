package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class InventoryAgingResponse {
    private UUID productId;
    private String productName;
    private String sku;
    private UUID warehouseId;
    private String warehouseName;
    private UUID locationId;
    private String locationCode;
    private BigDecimal qtyOnHand;
    private OffsetDateTime lastInboundDate;
    private Long agingDays;

    public InventoryAgingResponse(UUID productId, String productName, String sku, 
                                 UUID warehouseId, String warehouseName, 
                                 UUID locationId, String locationCode, 
                                 BigDecimal qtyOnHand, OffsetDateTime lastInboundDate) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.locationId = locationId;
        this.locationCode = locationCode;
        this.qtyOnHand = qtyOnHand;
        this.lastInboundDate = lastInboundDate;
        if (lastInboundDate != null) {
            this.agingDays = java.time.temporal.ChronoUnit.DAYS.between(lastInboundDate.toLocalDate(), LocalDate.now());
        }
    }

    // Getters
    public UUID getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getSku() { return sku; }
    public UUID getWarehouseId() { return warehouseId; }
    public String getWarehouseName() { return warehouseName; }
    public UUID getLocationId() { return locationId; }
    public String getLocationCode() { return locationCode; }
    public BigDecimal getQtyOnHand() { return qtyOnHand; }
    public OffsetDateTime getLastInboundDate() { return lastInboundDate; }
    public Long getAgingDays() { return agingDays; }
}
