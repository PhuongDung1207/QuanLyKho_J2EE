package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class InventoryDtos {

    public static class BalanceResponse {
        private UUID id;
        private UUID productId;
        private UUID warehouseId;
        private UUID locationId;
        private BigDecimal qtyOnHand;
        private BigDecimal qtyReserved;
        private BigDecimal minQty;
        private BigDecimal maxQty;

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

        public UUID getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(UUID warehouseId) {
            this.warehouseId = warehouseId;
        }

        public UUID getLocationId() {
            return locationId;
        }

        public void setLocationId(UUID locationId) {
            this.locationId = locationId;
        }

        public BigDecimal getQtyOnHand() {
            return qtyOnHand;
        }

        public void setQtyOnHand(BigDecimal qtyOnHand) {
            this.qtyOnHand = qtyOnHand;
        }

        public BigDecimal getQtyReserved() {
            return qtyReserved;
        }

        public void setQtyReserved(BigDecimal qtyReserved) {
            this.qtyReserved = qtyReserved;
        }

        public BigDecimal getMinQty() {
            return minQty;
        }

        public void setMinQty(BigDecimal minQty) {
            this.minQty = minQty;
        }

        public BigDecimal getMaxQty() {
            return maxQty;
        }

        public void setMaxQty(BigDecimal maxQty) {
            this.maxQty = maxQty;
        }
    }
}
