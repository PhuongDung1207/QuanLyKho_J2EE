package com.example.JavaQuanLyKho.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
