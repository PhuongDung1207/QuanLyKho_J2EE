package com.example.JavaQuanLyKho.dto;

import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.model.entity.Product;

public class ScanResponseDTO {
    private String type; // "PRODUCT" or "BATCH"
    private Product product;
    private Batch batch;

    public ScanResponseDTO(String type, Product product, Batch batch) {
        this.type = type;
        this.product = product;
        this.batch = batch;
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
}
