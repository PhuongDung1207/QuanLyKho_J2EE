package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Product create(Product product);
}

