package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InboundReceiptLineRepository extends JpaRepository<InboundReceiptLine, UUID> {

    List<InboundReceiptLine> findByReceiptId(UUID receiptId);

    void deleteByReceiptId(UUID receiptId);
}
