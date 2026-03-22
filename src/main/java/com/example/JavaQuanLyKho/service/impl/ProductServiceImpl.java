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

import com.example.JavaQuanLyKho.repository.UomRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UomRepository uomRepository;

    public ProductServiceImpl(ProductRepository productRepository, UomRepository uomRepository) {
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
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
    public Product findByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Product not found with barcode: " + barcode));
        if (product.getBaseUomId() != null) {
            uomRepository.findById(product.getBaseUomId()).ifPresent(u -> product.setUomName(u.getName()));
        }
        return product;
    }

    @Override
    @Transactional
    public Product create(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        if (product.getBarcode() == null || product.getBarcode().trim().isEmpty()) {
            String generatedBarcode;
            do {
                generatedBarcode = "PRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            } while (productRepository.existsByBarcode(generatedBarcode));
            product.setBarcode(generatedBarcode);
        } else if (productRepository.existsByBarcode(product.getBarcode())) {
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
