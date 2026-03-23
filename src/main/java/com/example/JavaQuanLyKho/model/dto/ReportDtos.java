package com.example.JavaQuanLyKho.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ReportDtos {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WarehouseInventoryReport {
        private String warehouseName;
        private BigDecimal totalStock;
        private long uniqueProducts;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransactionTimelineReport {
        private String period; // e.g. "2023-10"
        private BigDecimal inboundQty;
        private BigDecimal outboundQty;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DashboardSummary {
        private long totalWarehouses;
        private long totalProducts;
        private long totalCategories;
        private BigDecimal overallStock;
        private long lowStockCount;
        private long expiringSoonCount;
        private BigDecimal recentWasteCount;
        
        // Trends
        private double totalProductsTrend;
        private double totalCategoriesTrend;
        private double totalWarehousesTrend;
        private double stockInTrend;
        private double stockOutTrend;
        
        // Month counts
        private long stockInCount;
        private long stockOutCount;
    }
}
