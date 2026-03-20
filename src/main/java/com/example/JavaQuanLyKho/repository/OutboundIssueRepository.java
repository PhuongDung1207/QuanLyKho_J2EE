package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface OutboundIssueRepository extends JpaRepository<OutboundIssue, UUID> {

    Optional<OutboundIssue> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM OutboundIssueLine l " +
           "JOIN OutboundIssue o ON l.issueId = o.id " +
           "WHERE o.warehouseId = :warehouseId AND o.status = :status")
    BigDecimal sumQuantityByWarehouseIdAndStatus(@Param("warehouseId") UUID warehouseId,
                                                 @Param("status") OutboundIssueStatus status);
}
