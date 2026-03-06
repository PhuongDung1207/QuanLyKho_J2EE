package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Warehouse;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.WarehouseService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Page<Warehouse> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    @Override
    public Warehouse findById(UUID id) {
        return warehouseRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public Warehouse create(Warehouse warehouse) {
        if (warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public Warehouse update(UUID id, Warehouse warehouse) {
        Warehouse existing = findById(id);
        if (!existing.getCode().equals(warehouse.getCode()) && warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        existing.setCode(warehouse.getCode());
        existing.setName(warehouse.getName());
        existing.setStatus(warehouse.getStatus());
        existing.setCapacity(warehouse.getCapacity());
        return warehouseRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Warehouse existing = findById(id);
        warehouseRepository.delete(existing);
    }
}

