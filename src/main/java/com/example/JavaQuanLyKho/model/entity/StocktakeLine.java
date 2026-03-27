package com.example.JavaQuanLyKho.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "stocktake_lines")
public class StocktakeLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "stocktake_id", nullable = false)
    private UUID stocktakeId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "system_qty", precision = 19, scale = 6)
    private BigDecimal systemQty = BigDecimal.ZERO;

    @Column(name = "counted_qty", precision = 19, scale = 6)
    private BigDecimal countedQty = BigDecimal.ZERO;

    @Column(name = "diff", precision = 19, scale = 6)
    private BigDecimal diff = BigDecimal.ZERO;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(UUID stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(BigDecimal systemQty) {
        this.systemQty = systemQty;
    }

    public BigDecimal getCountedQty() {
        return countedQty;
    }

    public void setCountedQty(BigDecimal countedQty) {
        this.countedQty = countedQty;
    }

    public BigDecimal getDiff() {
        return diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }
}
