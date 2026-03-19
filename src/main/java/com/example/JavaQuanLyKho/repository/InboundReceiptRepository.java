package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InboundReceiptRepository extends JpaRepository<InboundReceipt, UUID> {

    Optional<InboundReceipt> findByCode(String code);

    boolean existsByCode(String code);
}
