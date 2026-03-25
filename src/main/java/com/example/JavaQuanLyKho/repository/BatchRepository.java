package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BatchRepository extends JpaRepository<Batch, UUID> {
    Optional<Batch> findByCode(String code);
    boolean existsByCode(String code);
}
