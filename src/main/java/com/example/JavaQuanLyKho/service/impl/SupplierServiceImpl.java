package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Supplier;
import com.example.JavaQuanLyKho.repository.SupplierRepository;
import com.example.JavaQuanLyKho.service.SupplierService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Page<Supplier> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @Override
    public Supplier findById(UUID id) {
        return supplierRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public Supplier create(Supplier supplier) {
        String code = normalizeCode(supplier.getCode());
        if (supplierRepository.existsByCode(code)) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }

        Supplier toSave = new Supplier();
        toSave.setCode(code);
        toSave.setName(cleanRequired(supplier.getName(), "name"));
        toSave.setContactName(cleanNullable(supplier.getContactName()));
        toSave.setContactEmail(cleanNullable(supplier.getContactEmail()));
        toSave.setContactPhone(cleanNullable(supplier.getContactPhone()));
        toSave.setAddress(cleanNullable(supplier.getAddress()));
        toSave.setStatus(normalizeStatus(supplier.getStatus()));
        toSave.setCreatedAt(OffsetDateTime.now());
        return supplierRepository.save(toSave);
    }

    @Override
    @Transactional
    public Supplier update(UUID id, Supplier supplier) {
        Supplier existing = findById(id);
        String code = normalizeCode(supplier.getCode());
        if (!existing.getCode().equals(code) && supplierRepository.existsByCode(code)) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }

        existing.setCode(code);
        existing.setName(cleanRequired(supplier.getName(), "name"));
        existing.setContactName(cleanNullable(supplier.getContactName()));
        existing.setContactEmail(cleanNullable(supplier.getContactEmail()));
        existing.setContactPhone(cleanNullable(supplier.getContactPhone()));
        existing.setAddress(cleanNullable(supplier.getAddress()));
        existing.setStatus(normalizeStatus(supplier.getStatus()));
        existing.setUpdatedAt(OffsetDateTime.now());
        return supplierRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Supplier supplier = findById(id);
        supplierRepository.delete(supplier);
    }

    private String normalizeCode(String code) {
        String normalized = cleanRequired(code, "code").toUpperCase();
        if (normalized.length() > 64) {
            throw new IllegalArgumentException("Code must not exceed 64 characters");
        }
        return normalized;
    }

    private String normalizeStatus(String status) {
        String normalized = cleanNullable(status);
        if (normalized == null) {
            return "ACTIVE";
        }
        return normalized.toUpperCase();
    }

    private String cleanRequired(String value, String fieldName) {
        String cleaned = cleanNullable(value);
        if (cleaned == null) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return cleaned;
    }

    private String cleanNullable(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim();
        return cleaned.isEmpty() ? null : cleaned;
    }
}
