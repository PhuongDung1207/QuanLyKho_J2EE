package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Uom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UomRepository extends JpaRepository<Uom, UUID> {

    Optional<Uom> findByCode(String code);

    boolean existsByCode(String code);
}

