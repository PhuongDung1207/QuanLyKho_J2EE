package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    Optional<Transfer> findByCode(String code);

    boolean existsByCode(String code);
}
