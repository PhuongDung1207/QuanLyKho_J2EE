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

    @Query("""
            select coalesce(sum(l.quantity), 0) from OutboundIssueLine l
            join OutboundIssue o on l.issueId = o.id
            where (:warehouseId is null or o.warehouseId = :warehouseId)
              and o.status = 'COMPLETED'
              and o.type in ('WASTE', 'DAMAGED')
              and o.completedAt >= :start and o.completedAt <= :end
            """)
    BigDecimal sumWasteQuantity(
            @Param("warehouseId") UUID warehouseId,
            @Param("start") java.time.OffsetDateTime start,
            @Param("end") java.time.OffsetDateTime end
    );

    @Query("""
            select o.type, coalesce(sum(l.quantity), 0) from OutboundIssueLine l
            join OutboundIssue o on l.issueId = o.id
            where (:warehouseId is null or o.warehouseId = :warehouseId)
              and o.status = 'COMPLETED'
              and o.type in ('WASTE', 'DAMAGED')
              and o.completedAt >= :start and o.completedAt <= :end
            group by o.type
            """)
    java.util.List<Object[]> sumWasteStatisticsByType(
            @Param("warehouseId") UUID warehouseId,
            @Param("start") java.time.OffsetDateTime start,
            @Param("end") java.time.OffsetDateTime end
    );

    @Query("""
            select new com.example.JavaQuanLyKho.model.dto.WasteDetailResponse(
                o.code, p.name, p.sku, l.quantity, u.name, o.type, o.completedAt, w.name
            )
            from OutboundIssueLine l
            join OutboundIssue o on l.issueId = o.id
            join Product p on l.productId = p.id
            join Uom u on l.uomId = u.id
            join Warehouse w on o.warehouseId = w.id
            where (:warehouseId is null or o.warehouseId = :warehouseId)
              and o.status = 'COMPLETED'
              and o.type in ('WASTE', 'DAMAGED')
              and o.completedAt >= :start and o.completedAt <= :end
            order by o.completedAt desc
            """)
    java.util.List<com.example.JavaQuanLyKho.model.dto.WasteDetailResponse> findWasteDetails(
            @Param("warehouseId") UUID warehouseId,
            @Param("start") java.time.OffsetDateTime start,
            @Param("end") java.time.OffsetDateTime end
    );
}
