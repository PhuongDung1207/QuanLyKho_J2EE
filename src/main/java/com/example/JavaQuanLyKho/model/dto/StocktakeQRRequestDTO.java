package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;

public class StocktakeQRRequestDTO {
    private String qrCode;
    private BigDecimal countedQty;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public BigDecimal getCountedQty() {
        return countedQty;
    }

    public void setCountedQty(BigDecimal countedQty) {
        this.countedQty = countedQty;
    }
}
