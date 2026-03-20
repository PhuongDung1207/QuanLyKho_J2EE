package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.TransferLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransferLineRepository extends JpaRepository<TransferLine, UUID> {

    List<TransferLine> findByTransferId(UUID transferId);

    void deleteByTransferId(UUID transferId);
}
