package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InboundReceiptRepository extends JpaRepository<InboundReceipt, UUID> {

    Optional<InboundReceipt> findByCode(String code);

    boolean existsByCode(String code);

    List<InboundReceipt> findByWarehouseIdAndStatus(UUID warehouseId, InboundReceiptStatus status);

    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InboundReceiptLine l " +
           "JOIN InboundReceipt r ON l.receiptId = r.id " +
           "WHERE r.warehouseId = :warehouseId AND r.status = :status")
    BigDecimal sumQuantityByWarehouseIdAndStatus(@Param("warehouseId") UUID warehouseId,
                                                 @Param("status") InboundReceiptStatus status);

    long countByCreatedAtBetween(java.time.OffsetDateTime start, java.time.OffsetDateTime end);
}
