package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class WasteDetailResponse {
    private String productCode;
    private String productName;
    private String sku;
    private BigDecimal quantity;
    private String uomName;
    private String type;
    private OffsetDateTime completedAt;
    private String warehouseName;

    public WasteDetailResponse(String productCode, String productName, String sku, 
                               BigDecimal quantity, String uomName, String type, 
                               OffsetDateTime completedAt, String warehouseName) {
        this.productCode = productCode;
        this.productName = productName;
        this.sku = sku;
        this.quantity = quantity;
        this.uomName = uomName;
        this.type = type;
        this.completedAt = completedAt;
        this.warehouseName = warehouseName;
    }

    // Getters
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }
    public String getSku() { return sku; }
    public BigDecimal getQuantity() { return quantity; }
    public String getUomName() { return uomName; }
    public String getType() { return type; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public String getWarehouseName() { return warehouseName; }
}
