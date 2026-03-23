package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.InventoryBalance;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InventoryBalanceRepository extends JpaRepository<InventoryBalance, UUID> {

    Page<InventoryBalance> findByWarehouseId(UUID warehouseId, Pageable pageable);

    Page<InventoryBalance> findByProductId(UUID productId, Pageable pageable);

    Page<InventoryBalance> findByWarehouseIdAndProductId(UUID warehouseId, UUID productId, Pageable pageable);

    @Query("""
            select b from InventoryBalance b
            where b.minQty is not null
              and b.qtyOnHand <= b.minQty
            """)
    Page<InventoryBalance> findLowStock(Pageable pageable);

    @Query("""
            select b from InventoryBalance b
            where b.warehouseId = :warehouseId
              and b.minQty is not null
              and b.qtyOnHand <= b.minQty
            """)
    Page<InventoryBalance> findLowStockByWarehouseId(@Param("warehouseId") UUID warehouseId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select b from InventoryBalance b
            where b.productId = :productId
              and b.warehouseId = :warehouseId
              and (
                   (:locationId is null and b.locationId is null)
                   or b.locationId = :locationId
              )
            """)
    Optional<InventoryBalance> findForUpdate(
            @Param("productId") UUID productId,
            @Param("warehouseId") UUID warehouseId,
            @Param("locationId") UUID locationId
    );

    // 18. Tuổi tồn kho (sắp xếp theo ngày nhập cũ nhất)
    @Query("""
            select new com.example.JavaQuanLyKho.model.dto.InventoryAgingResponse(
                b.productId, p.name, p.sku, b.warehouseId, w.name, b.locationId, l.code, b.qtyOnHand, b.lastInboundDate
            )
            from InventoryBalance b
            join Product p on b.productId = p.id
            join Warehouse w on b.warehouseId = w.id
            left join Location l on b.locationId = l.id
            where (:warehouseId is null or b.warehouseId = :warehouseId)
              and b.qtyOnHand > 0
            order by b.lastInboundDate asc nulls last
            """)
    Page<com.example.JavaQuanLyKho.model.dto.InventoryAgingResponse> findInventoryAgingDetailed(@Param("warehouseId") UUID warehouseId, Pageable pageable);

    // 19. Hàng tồn lâu (không có xuất trong X ngày)
    @Query("""
            select new com.example.JavaQuanLyKho.model.dto.SlowMovingResponse(
                b.productId, p.name, p.sku, b.warehouseId, w.name, b.locationId, l.code, b.qtyOnHand, b.lastOutboundDate
            )
            from InventoryBalance b
            join Product p on b.productId = p.id
            join Warehouse w on b.warehouseId = w.id
            left join Location l on b.locationId = l.id
            where (:warehouseId is null or b.warehouseId = :warehouseId)
              and b.qtyOnHand > 0
              and (b.lastOutboundDate is null or b.lastOutboundDate <= :thresholdDate)
            """)
    Page<com.example.JavaQuanLyKho.model.dto.SlowMovingResponse> findSlowMovingDetailed(@Param("warehouseId") UUID warehouseId, @Param("thresholdDate") java.time.OffsetDateTime thresholdDate, Pageable pageable);

    // 20. Hàng sắp hết hạn
    @Query("""
            select new com.example.JavaQuanLyKho.model.dto.ExpiringResponse(
                b.productId, p.name, p.sku, b.warehouseId, w.name, b.locationId, l.code, b.qtyOnHand, b.expiryDate
            )
            from InventoryBalance b
            join Product p on b.productId = p.id
            join Warehouse w on b.warehouseId = w.id
            left join Location l on b.locationId = l.id
            where (:warehouseId is null or b.warehouseId = :warehouseId)
              and b.qtyOnHand > 0
              and b.expiryDate is not null
              and b.expiryDate <= :thresholdDate
            """)
    Page<com.example.JavaQuanLyKho.model.dto.ExpiringResponse> findExpiringDetailed(@Param("warehouseId") UUID warehouseId, @Param("thresholdDate") java.time.LocalDate thresholdDate, Pageable pageable);

    @Query("select count(b) from InventoryBalance b where (:warehouseId is null or b.warehouseId = :warehouseId) and b.qtyOnHand > 0")
    long countAgedItems(@Param("warehouseId") UUID warehouseId);

    @Query("select count(b) from InventoryBalance b where (:warehouseId is null or b.warehouseId = :warehouseId) and b.qtyOnHand > 0 and (b.lastOutboundDate is null or b.lastOutboundDate <= :thresholdDate)")
    long countSlowMovingItems(@Param("warehouseId") UUID warehouseId, @Param("thresholdDate") java.time.OffsetDateTime thresholdDate);

    @Query("select count(b) from InventoryBalance b where (:warehouseId is null or b.warehouseId = :warehouseId) and b.qtyOnHand > 0 and b.expiryDate is not null and b.expiryDate <= :thresholdDate")
    long countExpiringItems(@Param("warehouseId") UUID warehouseId, @Param("thresholdDate") java.time.LocalDate thresholdDate);

    @Query("SELECT w.name, SUM(b.qtyOnHand), count(distinct b.productId) FROM InventoryBalance b JOIN Warehouse w ON b.warehouseId = w.id GROUP BY w.name")
    java.util.List<Object[]> sumQuantityByWarehouseGroup();

    @Query("""
            select new com.example.JavaQuanLyKho.model.dto.InventoryRowDto(
                p.name, p.sku,
                w.name,
                l.code,
                b.qtyOnHand, b.qtyReserved,
                b.qtyOnHand - b.qtyReserved,
                b.minQty, b.maxQty,
                b.lastInboundDate
            )
            from InventoryBalance b
            join Product p on b.productId = p.id
            join Warehouse w on b.warehouseId = w.id
            left join Location l on b.locationId = l.id
            where (:warehouseId is null or b.warehouseId = :warehouseId)
            order by p.name asc
            """)
    org.springframework.data.domain.Page<com.example.JavaQuanLyKho.model.dto.InventoryRowDto> findInventoryRows(
            @Param("warehouseId") UUID warehouseId,
            org.springframework.data.domain.Pageable pageable);
}
