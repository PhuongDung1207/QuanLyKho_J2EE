package com.example.JavaQuanLyKho.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "outbound_issue_lines")
public class OutboundIssueLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "issue_id", nullable = false)
    private UUID issueId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "uom_id", nullable = false)
    private UUID uomId;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "mfg_date")
    private java.time.LocalDate mfgDate;

    @Column(name = "shelf_life")
    private Integer shelfLife;

    @Column(name = "exp_date")
    private java.time.LocalDate expDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIssueId() {
        return issueId;
    }

    public void setIssueId(UUID issueId) {
        this.issueId = issueId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getUomId() {
        return uomId;
    }

    public void setUomId(UUID uomId) {
        this.uomId = uomId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public java.time.LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(java.time.LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public java.time.LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(java.time.LocalDate expDate) {
        this.expDate = expDate;
    }
}
