package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);

    Optional<Product> findByBarcode(String barcode);
}

