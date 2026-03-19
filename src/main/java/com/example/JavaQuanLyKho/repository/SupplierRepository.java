package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Optional<Supplier> findByCode(String code);

    boolean existsByCode(String code);
}
