package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.service.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    @Transactional
    public Product create(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        if (product.getBarcode() != null && productRepository.existsByBarcode(product.getBarcode())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        product.setStatus("ACTIVE");
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(UUID id, Product payload) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        existing.setName(payload.getName());
        existing.setCategoryId(payload.getCategoryId());
        existing.setBaseUomId(payload.getBaseUomId());
        existing.setBarcode(payload.getBarcode());
        existing.setAttributesJson(payload.getAttributesJson());
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Product lock(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        if ("ACTIVE".equals(product.getStatus())) {
            product.setStatus("LOCKED");
        } else {
            product.setStatus("ACTIVE");
        }
        return productRepository.save(product);
    }
}
