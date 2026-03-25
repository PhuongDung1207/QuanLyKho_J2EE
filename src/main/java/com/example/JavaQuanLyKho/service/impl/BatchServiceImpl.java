package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.repository.BatchRepository;
import com.example.JavaQuanLyKho.service.BatchService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    public BatchServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public Page<Batch> findAll(Pageable pageable) {
        return batchRepository.findAll(pageable);
    }

    @Override
    public Batch findById(UUID id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found: " + id));
    }

    @Override
    public Batch findByCode(String code) {
        return batchRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Batch not found with code: " + code));
    }

    @Override
    @Transactional
    public Batch create(Batch batch) {
        if (batch.getCode() == null || batch.getCode().trim().isEmpty()) {
            String generatedCode;
            do {
                generatedCode = "BATCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            } while (batchRepository.existsByCode(generatedCode));
            batch.setCode(generatedCode);
        } else if (batchRepository.existsByCode(batch.getCode())) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
        batch.setStatus("ACTIVE");
        return batchRepository.save(batch);
    }

    @Override
    @Transactional
    public Batch update(UUID id, Batch payload) {
        Batch existing = findById(id);
        existing.setProductId(payload.getProductId());
        existing.setMfgDate(payload.getMfgDate());
        existing.setExpDate(payload.getExpDate());
        return batchRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!batchRepository.existsById(id)) {
            throw new RuntimeException("Batch not found: " + id);
        }
        batchRepository.deleteById(id);
    }
}
