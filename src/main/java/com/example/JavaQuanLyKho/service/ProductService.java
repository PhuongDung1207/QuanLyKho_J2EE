package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Product findById(UUID id);

    Product findByBarcode(String barcode);

    Product create(Product product);

    Product update(UUID id, Product product);

    void delete(UUID id);

    Product lock(UUID id);
}

