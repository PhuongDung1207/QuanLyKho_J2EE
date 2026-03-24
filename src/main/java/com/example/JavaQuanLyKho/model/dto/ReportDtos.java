package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;

public class ReportDtos {

    public static class WarehouseInventoryReport {
        private String warehouseName;
        private BigDecimal totalStock;
        private long uniqueProducts;

        public WarehouseInventoryReport() {
        }

        public WarehouseInventoryReport(String warehouseName, BigDecimal totalStock, long uniqueProducts) {
            this.warehouseName = warehouseName;
            this.totalStock = totalStock;
            this.uniqueProducts = uniqueProducts;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public BigDecimal getTotalStock() {
            return totalStock;
        }

        public void setTotalStock(BigDecimal totalStock) {
            this.totalStock = totalStock;
        }

        public long getUniqueProducts() {
            return uniqueProducts;
        }

        public void setUniqueProducts(long uniqueProducts) {
            this.uniqueProducts = uniqueProducts;
        }
    }

    public static class TransactionTimelineReport {
        private String period; // e.g. "2023-10"
        private BigDecimal inboundQty;
        private BigDecimal outboundQty;

        public TransactionTimelineReport() {
        }

        public TransactionTimelineReport(String period, BigDecimal inboundQty, BigDecimal outboundQty) {
            this.period = period;
            this.inboundQty = inboundQty;
            this.outboundQty = outboundQty;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public BigDecimal getInboundQty() {
            return inboundQty;
        }

        public void setInboundQty(BigDecimal inboundQty) {
            this.inboundQty = inboundQty;
        }

        public BigDecimal getOutboundQty() {
            return outboundQty;
        }

        public void setOutboundQty(BigDecimal outboundQty) {
            this.outboundQty = outboundQty;
        }
    }

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

        public DashboardSummary() {
        }

        public DashboardSummary(long totalWarehouses, long totalProducts, long totalCategories, BigDecimal overallStock, 
                               long lowStockCount, long expiringSoonCount, BigDecimal recentWasteCount, 
                               double totalProductsTrend, double totalCategoriesTrend, double totalWarehousesTrend, 
                               double stockInTrend, double stockOutTrend, long stockInCount, long stockOutCount) {
            this.totalWarehouses = totalWarehouses;
            this.totalProducts = totalProducts;
            this.totalCategories = totalCategories;
            this.overallStock = overallStock;
            this.lowStockCount = lowStockCount;
            this.expiringSoonCount = expiringSoonCount;
            this.recentWasteCount = recentWasteCount;
            this.totalProductsTrend = totalProductsTrend;
            this.totalCategoriesTrend = totalCategoriesTrend;
            this.totalWarehousesTrend = totalWarehousesTrend;
            this.stockInTrend = stockInTrend;
            this.stockOutTrend = stockOutTrend;
            this.stockInCount = stockInCount;
            this.stockOutCount = stockOutCount;
        }

        public long getTotalWarehouses() {
            return totalWarehouses;
        }

        public void setTotalWarehouses(long totalWarehouses) {
            this.totalWarehouses = totalWarehouses;
        }

        public long getTotalProducts() {
            return totalProducts;
        }

        public void setTotalProducts(long totalProducts) {
            this.totalProducts = totalProducts;
        }

        public long getTotalCategories() {
            return totalCategories;
        }

        public void setTotalCategories(long totalCategories) {
            this.totalCategories = totalCategories;
        }

        public BigDecimal getOverallStock() {
            return overallStock;
        }

        public void setOverallStock(BigDecimal overallStock) {
            this.overallStock = overallStock;
        }

        public long getLowStockCount() {
            return lowStockCount;
        }

        public void setLowStockCount(long lowStockCount) {
            this.lowStockCount = lowStockCount;
        }

        public long getExpiringSoonCount() {
            return expiringSoonCount;
        }

        public void setExpiringSoonCount(long expiringSoonCount) {
            this.expiringSoonCount = expiringSoonCount;
        }

        public BigDecimal getRecentWasteCount() {
            return recentWasteCount;
        }

        public void setRecentWasteCount(BigDecimal recentWasteCount) {
            this.recentWasteCount = recentWasteCount;
        }

        public double getTotalProductsTrend() {
            return totalProductsTrend;
        }

        public void setTotalProductsTrend(double totalProductsTrend) {
            this.totalProductsTrend = totalProductsTrend;
        }

        public double getTotalCategoriesTrend() {
            return totalCategoriesTrend;
        }

        public void setTotalCategoriesTrend(double totalCategoriesTrend) {
            this.totalCategoriesTrend = totalCategoriesTrend;
        }

        public double getTotalWarehousesTrend() {
            return totalWarehousesTrend;
        }

        public void setTotalWarehousesTrend(double totalWarehousesTrend) {
            this.totalWarehousesTrend = totalWarehousesTrend;
        }

        public double getStockInTrend() {
            return stockInTrend;
        }

        public void setStockInTrend(double stockInTrend) {
            this.stockInTrend = stockInTrend;
        }

        public double getStockOutTrend() {
            return stockOutTrend;
        }

        public void setStockOutTrend(double stockOutTrend) {
            this.stockOutTrend = stockOutTrend;
        }

        public long getStockInCount() {
            return stockInCount;
        }

        public void setStockInCount(long stockInCount) {
            this.stockInCount = stockInCount;
        }

        public long getStockOutCount() {
            return stockOutCount;
        }

        public void setStockOutCount(long stockOutCount) {
            this.stockOutCount = stockOutCount;
        }
    }
}
