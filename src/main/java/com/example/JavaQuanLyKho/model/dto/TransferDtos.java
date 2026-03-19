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

public class TransferDtos {

    public static class CreateRequest {

        @Size(max = 64)
        private String code;

        @NotNull
        private UUID fromWarehouseId;

        @NotNull
        private UUID toWarehouseId;

        @NotEmpty
        @Valid
        private List<LineRequest> lines = new ArrayList<>();

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public UUID getFromWarehouseId() {
            return fromWarehouseId;
        }

        public void setFromWarehouseId(UUID fromWarehouseId) {
            this.fromWarehouseId = fromWarehouseId;
        }

        public UUID getToWarehouseId() {
            return toWarehouseId;
        }

        public void setToWarehouseId(UUID toWarehouseId) {
            this.toWarehouseId = toWarehouseId;
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

        private UUID fromLocationId;

        private UUID toLocationId;

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

        public UUID getFromLocationId() {
            return fromLocationId;
        }

        public void setFromLocationId(UUID fromLocationId) {
            this.fromLocationId = fromLocationId;
        }

        public UUID getToLocationId() {
            return toLocationId;
        }

        public void setToLocationId(UUID toLocationId) {
            this.toLocationId = toLocationId;
        }
    }

    public static class Response {
        private UUID id;
        private String code;
        private UUID fromWarehouseId;
        private UUID toWarehouseId;
        private String status;
        private UUID createdBy;
        private UUID approvedBy;
        private OffsetDateTime createdAt;
        private OffsetDateTime approvedAt;
        private OffsetDateTime issuedAt;
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

        public UUID getFromWarehouseId() {
            return fromWarehouseId;
        }

        public void setFromWarehouseId(UUID fromWarehouseId) {
            this.fromWarehouseId = fromWarehouseId;
        }

        public UUID getToWarehouseId() {
            return toWarehouseId;
        }

        public void setToWarehouseId(UUID toWarehouseId) {
            this.toWarehouseId = toWarehouseId;
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

        public OffsetDateTime getIssuedAt() {
            return issuedAt;
        }

        public void setIssuedAt(OffsetDateTime issuedAt) {
            this.issuedAt = issuedAt;
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
        private UUID fromLocationId;
        private UUID toLocationId;

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

        public UUID getFromLocationId() {
            return fromLocationId;
        }

        public void setFromLocationId(UUID fromLocationId) {
            this.fromLocationId = fromLocationId;
        }

        public UUID getToLocationId() {
            return toLocationId;
        }

        public void setToLocationId(UUID toLocationId) {
            this.toLocationId = toLocationId;
        }
    }
}
