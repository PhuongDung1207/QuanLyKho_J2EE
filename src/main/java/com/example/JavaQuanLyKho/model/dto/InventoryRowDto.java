package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class InventoryRowDto {
    private String productName;
    private String productSku;
    private String warehouseName;
    private String locationCode;
    private BigDecimal qtyOnHand;
    private BigDecimal qtyReserved;
    private BigDecimal qtyAvailable;
    private BigDecimal minQty;
    private BigDecimal maxQty;
    private OffsetDateTime lastInboundDate;

    public InventoryRowDto() {
    }

    public InventoryRowDto(String productName, String productSku, String warehouseName, String locationCode, 
                           BigDecimal qtyOnHand, BigDecimal qtyReserved, BigDecimal qtyAvailable, 
                           BigDecimal minQty, BigDecimal maxQty, OffsetDateTime lastInboundDate) {
        this.productName = productName;
        this.productSku = productSku;
        this.warehouseName = warehouseName;
        this.locationCode = locationCode;
        this.qtyOnHand = qtyOnHand;
        this.qtyReserved = qtyReserved;
        this.qtyAvailable = qtyAvailable;
        this.minQty = minQty;
        this.maxQty = maxQty;
        this.lastInboundDate = lastInboundDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public BigDecimal getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public BigDecimal getQtyReserved() {
        return qtyReserved;
    }

    public void setQtyReserved(BigDecimal qtyReserved) {
        this.qtyReserved = qtyReserved;
    }

    public BigDecimal getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(BigDecimal qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public BigDecimal getMinQty() {
        return minQty;
    }

    public void setMinQty(BigDecimal minQty) {
        this.minQty = minQty;
    }

    public BigDecimal getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(BigDecimal maxQty) {
        this.maxQty = maxQty;
    }

    public OffsetDateTime getLastInboundDate() {
        return lastInboundDate;
    }

    public void setLastInboundDate(OffsetDateTime lastInboundDate) {
        this.lastInboundDate = lastInboundDate;
    }

    /** "Normal", "Low", "Out" */
    public String getStatus() {
        if (qtyOnHand == null || qtyOnHand.compareTo(BigDecimal.ZERO) <= 0) return "Out";
        if (minQty != null && qtyOnHand.compareTo(minQty) <= 0) return "Low";
        return "Normal";
    }

    public String getStatusClass() {
        return switch (getStatus()) {
            case "Out" -> "status-out";
            case "Low" -> "status-low";
            default -> "status-normal";
        };
    }

    public String getStatusLabel() {
        return switch (getStatus()) {
            case "Out" -> "Out Of Stock";
            case "Low" -> "Low Stock";
            default -> "Normal";
        };
    }
}
