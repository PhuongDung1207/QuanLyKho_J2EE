package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SlowMovingResponse {
    private UUID productId;
    private String productName;
    private String sku;
    private UUID warehouseId;
    private String warehouseName;
    private UUID locationId;
    private String locationCode;
    private BigDecimal qtyOnHand;
    private OffsetDateTime lastOutboundDate;
    private Long daysSinceLastOutbound;

    public SlowMovingResponse(UUID productId, String productName, String sku, 
                             UUID warehouseId, String warehouseName, 
                             UUID locationId, String locationCode, 
                             BigDecimal qtyOnHand, OffsetDateTime lastOutboundDate) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.locationId = locationId;
        this.locationCode = locationCode;
        this.qtyOnHand = qtyOnHand;
        this.lastOutboundDate = lastOutboundDate;
        if (lastOutboundDate != null) {
            this.daysSinceLastOutbound = java.time.temporal.ChronoUnit.DAYS.between(lastOutboundDate.toLocalDate(), LocalDate.now());
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
    public OffsetDateTime getLastOutboundDate() { return lastOutboundDate; }
    public Long getDaysSinceLastOutbound() { return daysSinceLastOutbound; }
}
