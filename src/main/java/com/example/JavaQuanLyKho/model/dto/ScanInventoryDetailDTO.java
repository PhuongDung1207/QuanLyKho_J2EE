package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class ScanInventoryDetailDTO {
    private String warehouseName;
    private String locationCode;
    private BigDecimal qtyOnHand;
    private BigDecimal qtyReserved;
    private BigDecimal qtyAvailable;
    private BigDecimal minQty;
    private BigDecimal maxQty;
    private LocalDate mfgDate;
    private LocalDate expDate;
    private OffsetDateTime lastInboundDate;
    private OffsetDateTime lastOutboundDate;
    private String status;
    private String statusLabel;

    public ScanInventoryDetailDTO(String warehouseName, String locationCode, BigDecimal qtyOnHand, BigDecimal qtyReserved,
                                  BigDecimal qtyAvailable, BigDecimal minQty, BigDecimal maxQty, LocalDate mfgDate,
                                  LocalDate expDate, OffsetDateTime lastInboundDate, OffsetDateTime lastOutboundDate,
                                  String status, String statusLabel) {
        this.warehouseName = warehouseName;
        this.locationCode = locationCode;
        this.qtyOnHand = qtyOnHand;
        this.qtyReserved = qtyReserved;
        this.qtyAvailable = qtyAvailable;
        this.minQty = minQty;
        this.maxQty = maxQty;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
        this.lastInboundDate = lastInboundDate;
        this.lastOutboundDate = lastOutboundDate;
        this.status = status;
        this.statusLabel = statusLabel;
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

    public LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public OffsetDateTime getLastInboundDate() {
        return lastInboundDate;
    }

    public void setLastInboundDate(OffsetDateTime lastInboundDate) {
        this.lastInboundDate = lastInboundDate;
    }

    public OffsetDateTime getLastOutboundDate() {
        return lastOutboundDate;
    }

    public void setLastOutboundDate(OffsetDateTime lastOutboundDate) {
        this.lastOutboundDate = lastOutboundDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
