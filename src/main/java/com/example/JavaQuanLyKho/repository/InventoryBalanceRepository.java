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
}
