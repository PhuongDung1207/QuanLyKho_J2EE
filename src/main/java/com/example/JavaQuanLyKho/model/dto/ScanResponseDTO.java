package com.example.JavaQuanLyKho.model.dto;

import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.model.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public class ScanResponseDTO {
    private String type; // "PRODUCT" or "BATCH"
    private Product product;
    private Batch batch;
    private BigDecimal totalOnHand;
    private BigDecimal totalReserved;
    private BigDecimal totalAvailable;
    private List<ScanInventoryDetailDTO> inventoryDetails;

    public ScanResponseDTO(String type, Product product, Batch batch,
                           BigDecimal totalOnHand, BigDecimal totalReserved, BigDecimal totalAvailable,
                           List<ScanInventoryDetailDTO> inventoryDetails) {
        this.type = type;
        this.product = product;
        this.batch = batch;
        this.totalOnHand = totalOnHand;
        this.totalReserved = totalReserved;
        this.totalAvailable = totalAvailable;
        this.inventoryDetails = inventoryDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public BigDecimal getTotalOnHand() {
        return totalOnHand;
    }

    public void setTotalOnHand(BigDecimal totalOnHand) {
        this.totalOnHand = totalOnHand;
    }

    public BigDecimal getTotalReserved() {
        return totalReserved;
    }

    public void setTotalReserved(BigDecimal totalReserved) {
        this.totalReserved = totalReserved;
    }

    public BigDecimal getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(BigDecimal totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public List<ScanInventoryDetailDTO> getInventoryDetails() {
        return inventoryDetails;
    }

    public void setInventoryDetails(List<ScanInventoryDetailDTO> inventoryDetails) {
        this.inventoryDetails = inventoryDetails;
    }
}
