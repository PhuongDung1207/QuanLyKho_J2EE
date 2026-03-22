package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BatchService {
    Page<Batch> findAll(Pageable pageable);
    Batch findById(UUID id);
    Batch findByCode(String code);
    Batch create(Batch batch);
    Batch update(UUID id, Batch batch);
    void delete(UUID id);
}
