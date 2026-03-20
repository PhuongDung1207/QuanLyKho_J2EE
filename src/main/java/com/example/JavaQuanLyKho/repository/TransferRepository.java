package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Transfer;
import com.example.JavaQuanLyKho.model.entity.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    Optional<Transfer> findByCode(String code);

    boolean existsByCode(String code);

    /** Tổng SL chuyển VÀO kho (nhận hàng) từ transfer COMPLETED */
    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM TransferLine l " +
           "JOIN Transfer t ON l.transferId = t.id " +
           "WHERE t.toWarehouseId = :warehouseId AND t.status = :status")
    BigDecimal sumIncomingQuantityByWarehouseIdAndStatus(@Param("warehouseId") UUID warehouseId,
                                                         @Param("status") TransferStatus status);

    /** Tổng SL chuyển ĐI khỏi kho (xuất hàng) từ transfer COMPLETED */
    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM TransferLine l " +
           "JOIN Transfer t ON l.transferId = t.id " +
           "WHERE t.fromWarehouseId = :warehouseId AND t.status = :status")
    BigDecimal sumOutgoingQuantityByWarehouseIdAndStatus(@Param("warehouseId") UUID warehouseId,
                                                          @Param("status") TransferStatus status);
}
