package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SupplierService {

    Page<Supplier> findAll(Pageable pageable);

    Supplier findById(UUID id);

    Supplier create(Supplier supplier);

    Supplier update(UUID id, Supplier supplier);

    void delete(UUID id);
}
