package com.example.JavaQuanLyKho.model.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "inventory", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "product_id", "warehouse_id" })
})
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID productId;
    private UUID warehouseId;

    // Đây là nơi thực thi quy tắc "chỉ thuộc 1 section"
    private UUID locationId;

    private Double quantity;
}
