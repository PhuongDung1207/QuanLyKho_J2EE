package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ExpiringResponse {
    private UUID productId;
    private String productName;
    private String sku;
    private UUID warehouseId;
    private String warehouseName;
    private UUID locationId;
    private String locationCode;
    private BigDecimal qtyOnHand;
    private LocalDate expDate;
    private Long daysRemaining;

    public ExpiringResponse(UUID productId, String productName, String sku, 
                           UUID warehouseId, String warehouseName, 
                           UUID locationId, String locationCode, 
                           BigDecimal qtyOnHand, LocalDate expDate) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.locationId = locationId;
        this.locationCode = locationCode;
        this.qtyOnHand = qtyOnHand;
        this.expDate = expDate;
        if (expDate != null) {
            this.daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expDate);
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
    public LocalDate getExpDate() { return expDate; }
    public Long getDaysRemaining() { return daysRemaining; }
}
