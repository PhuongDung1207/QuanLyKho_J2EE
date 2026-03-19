package com.example.JavaQuanLyKho.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InboundDtos {

    public static class CreateRequest {

        @Size(max = 64)
        private String code;

        @NotNull
        private UUID warehouseId;

        private UUID supplierId;

        @Size(max = 64)
        private String type;

        @NotEmpty
        @Valid
        private List<LineRequest> lines = new ArrayList<>();

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public UUID getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(UUID warehouseId) {
            this.warehouseId = warehouseId;
        }

        public UUID getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(UUID supplierId) {
            this.supplierId = supplierId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<LineRequest> getLines() {
            return lines;
        }

        public void setLines(List<LineRequest> lines) {
            this.lines = lines;
        }
    }

    public static class LineRequest {

        @NotNull
        private UUID productId;

        @NotNull
        private UUID uomId;

        @NotNull
        @DecimalMin(value = "0.000001")
        private BigDecimal quantity;

        private UUID locationId;

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public UUID getUomId() {
            return uomId;
        }

        public void setUomId(UUID uomId) {
            this.uomId = uomId;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public UUID getLocationId() {
            return locationId;
        }

        public void setLocationId(UUID locationId) {
            this.locationId = locationId;
        }
    }

    public static class Response {
        private UUID id;
        private String code;
        private String type;
        private UUID warehouseId;
        private UUID supplierId;
        private String status;
        private UUID createdBy;
        private UUID approvedBy;
        private OffsetDateTime createdAt;
        private OffsetDateTime approvedAt;
        private OffsetDateTime completedAt;
        private List<LineResponse> lines = new ArrayList<>();

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public UUID getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(UUID warehouseId) {
            this.warehouseId = warehouseId;
        }

        public UUID getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(UUID supplierId) {
            this.supplierId = supplierId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public UUID getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(UUID createdBy) {
            this.createdBy = createdBy;
        }

        public UUID getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(UUID approvedBy) {
            this.approvedBy = approvedBy;
        }

        public OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public OffsetDateTime getApprovedAt() {
            return approvedAt;
        }

        public void setApprovedAt(OffsetDateTime approvedAt) {
            this.approvedAt = approvedAt;
        }

        public OffsetDateTime getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(OffsetDateTime completedAt) {
            this.completedAt = completedAt;
        }

        public List<LineResponse> getLines() {
            return lines;
        }

        public void setLines(List<LineResponse> lines) {
            this.lines = lines;
        }
    }

    public static class LineResponse {
        private UUID id;
        private UUID productId;
        private UUID uomId;
        private BigDecimal quantity;
        private UUID locationId;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public UUID getUomId() {
            return uomId;
        }

        public void setUomId(UUID uomId) {
            this.uomId = uomId;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public UUID getLocationId() {
            return locationId;
        }

        public void setLocationId(UUID locationId) {
            this.locationId = locationId;
        }
    }
}
