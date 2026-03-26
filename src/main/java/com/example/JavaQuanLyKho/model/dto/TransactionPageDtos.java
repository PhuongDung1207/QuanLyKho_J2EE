package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionPageDtos {

    public record Payload(
            List<WarehouseItem> warehouses,
            List<SupplierItem> suppliers,
            List<ProductItem> products,
            List<UomItem> uoms,
            List<RecordItem> records
    ) {
    }

    public record WarehouseItem(
            UUID id,
            String code,
            String name,
            String status
    ) {
    }

    public record SupplierItem(
            UUID id,
            String code,
            String name,
            String status
    ) {
    }

    public record ProductItem(
            UUID id,
            String sku,
            String name,
            UUID baseUomId,
            String status
    ) {
    }

    public record UomItem(
            UUID id,
            String code,
            String name
    ) {
    }

    public record RecordItem(
            UUID id,
            String source,
            String code,
            String businessType,
            UUID warehouseId,
            UUID toWarehouseId,
            OffsetDateTime createdAt,
            UUID createdBy,
            String status,
            List<RecordLineItem> lines,
            int lineCount
    ) {
    }

    public record RecordLineItem(
            UUID id,
            UUID productId,
            UUID uomId,
            BigDecimal quantity,
            UUID locationId,
            UUID fromLocationId,
            UUID toLocationId
    ) {
    }
}

