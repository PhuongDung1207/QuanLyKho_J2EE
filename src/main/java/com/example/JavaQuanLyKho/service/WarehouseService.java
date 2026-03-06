package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WarehouseService {

    Page<Warehouse> findAll(Pageable pageable);

    Warehouse findById(UUID id);

    Warehouse create(Warehouse warehouse);

    Warehouse update(UUID id, Warehouse warehouse);

    void delete(UUID id);
}

