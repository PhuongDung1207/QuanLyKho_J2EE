package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class GreenWarehouseDtos {

    public static class HealthSummary {
        private long totalAgedItems;
        private long totalSlowMovingItems;
        private long totalExpiringItems;
        private BigDecimal totalWasteQty;
        private Map<String, BigDecimal> wasteByType;

        public long getTotalAgedItems() { return totalAgedItems; }
        public void setTotalAgedItems(long totalAgedItems) { this.totalAgedItems = totalAgedItems; }
        public long getTotalSlowMovingItems() { return totalSlowMovingItems; }
        public void setTotalSlowMovingItems(long totalSlowMovingItems) { this.totalSlowMovingItems = totalSlowMovingItems; }
        public long getTotalExpiringItems() { return totalExpiringItems; }
        public void setTotalExpiringItems(long totalExpiringItems) { this.totalExpiringItems = totalExpiringItems; }
        public BigDecimal getTotalWasteQty() { return totalWasteQty; }
        public void setTotalWasteQty(BigDecimal totalWasteQty) { this.totalWasteQty = totalWasteQty; }
        public Map<String, BigDecimal> getWasteByType() { return wasteByType; }
        public void setWasteByType(Map<String, BigDecimal> wasteByType) { this.wasteByType = wasteByType; }
        
        private BigDecimal turnoverRate;
        private BigDecimal wastePercentage;
        
        public BigDecimal getTurnoverRate() { return turnoverRate; }
        public void setTurnoverRate(BigDecimal turnoverRate) { this.turnoverRate = turnoverRate; }
        public BigDecimal getWastePercentage() { return wastePercentage; }
        public void setWastePercentage(BigDecimal wastePercentage) { this.wastePercentage = wastePercentage; }
    }
}
