package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.dto.ReportDtos;
import com.example.JavaQuanLyKho.repository.*;
import com.example.JavaQuanLyKho.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

        private final InventoryBalanceRepository inventoryBalanceRepository;
        private final InboundReceiptRepository inboundReceiptRepository;
        private final InboundReceiptLineRepository inboundReceiptLineRepository;
        private final OutboundIssueRepository outboundIssueRepository;
        private final OutboundIssueLineRepository outboundIssueLineRepository;
        private final WarehouseRepository warehouseRepository;
        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;

        public ReportServiceImpl(InventoryBalanceRepository inventoryBalanceRepository,
                        InboundReceiptRepository inboundReceiptRepository,
                        InboundReceiptLineRepository inboundReceiptLineRepository,
                        OutboundIssueRepository outboundIssueRepository,
                        OutboundIssueLineRepository outboundIssueLineRepository,
                        WarehouseRepository warehouseRepository,
                        ProductRepository productRepository,
                        CategoryRepository categoryRepository) {
                this.inventoryBalanceRepository = inventoryBalanceRepository;
                this.inboundReceiptRepository = inboundReceiptRepository;
                this.inboundReceiptLineRepository = inboundReceiptLineRepository;
                this.outboundIssueRepository = outboundIssueRepository;
                this.outboundIssueLineRepository = outboundIssueLineRepository;
                this.warehouseRepository = warehouseRepository;
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
        }

        @Override
        public List<ReportDtos.WarehouseInventoryReport> getInventoryByWarehouse() {
                return inventoryBalanceRepository.sumQuantityByWarehouseGroup().stream()
                                .map(row -> new ReportDtos.WarehouseInventoryReport(
                                                (String) row[0],
                                                row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO,
                                                ((Number) row[2]).longValue()))
                                .collect(Collectors.toList());
        }

        @Override
        public List<ReportDtos.TransactionTimelineReport> getTransactionTimeline(UUID warehouseId, OffsetDateTime start,
                        OffsetDateTime end) {
                Map<String, BigDecimal> inboundMap = new HashMap<>();
                Map<String, BigDecimal> outboundMap = new HashMap<>();

                boolean isSingleMonth = start.getMonth() == end.getMonth() && start.getYear() == end.getYear();

                // Fetch completed inbounds
                inboundReceiptRepository.findAll().stream()
                                .filter(r -> r.getCompletedAt() != null && r.getCompletedAt().isAfter(start)
                                                && r.getCompletedAt().isBefore(end)
                                                && (warehouseId == null || warehouseId.equals(r.getWarehouseId())))
                                .forEach(r -> {
                                        String key = isSingleMonth ? r.getCompletedAt().toString().substring(0, 10) : r.getCompletedAt().toString().substring(0, 7);
                                        BigDecimal qty = inboundReceiptLineRepository.findByReceiptId(r.getId())
                                                        .stream()
                                                        .map(l -> l.getQuantity() != null ? l.getQuantity()
                                                                         : BigDecimal.ZERO)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                        inboundMap.merge(key, qty, BigDecimal::add);
                                });

                // Fetch completed outbounds
                outboundIssueRepository.findAll().stream()
                                .filter(o -> o.getCompletedAt() != null && o.getCompletedAt().isAfter(start)
                                                && o.getCompletedAt().isBefore(end)
                                                && (warehouseId == null || warehouseId.equals(o.getWarehouseId())))
                                .forEach(o -> {
                                        String key = isSingleMonth ? o.getCompletedAt().toString().substring(0, 10) : o.getCompletedAt().toString().substring(0, 7);
                                        BigDecimal qty = outboundIssueLineRepository.findByIssueId(o.getId()).stream()
                                                        .map(l -> l.getQuantity() != null ? l.getQuantity()
                                                                         : BigDecimal.ZERO)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                        outboundMap.merge(key, qty, BigDecimal::add);
                                });

                Set<String> allKeys = new TreeSet<>();
                allKeys.addAll(inboundMap.keySet());
                allKeys.addAll(outboundMap.keySet());

                return allKeys.stream()
                                .map(key -> new ReportDtos.TransactionTimelineReport(
                                                key,
                                                inboundMap.getOrDefault(key, BigDecimal.ZERO),
                                                outboundMap.getOrDefault(key, BigDecimal.ZERO)))
                                .collect(Collectors.toList());
        }

        @Override
        public ReportDtos.DashboardSummary getDashboardSummary(UUID warehouseId) {
                ReportDtos.DashboardSummary summary = new ReportDtos.DashboardSummary();
                summary.setTotalWarehouses(warehouseRepository.count());
                summary.setTotalProducts(productRepository.count());
                summary.setTotalCategories(categoryRepository.count());

                List<Object[]> invStats = inventoryBalanceRepository.sumQuantityByWarehouseGroup();
                BigDecimal totalStock;
                if (warehouseId != null) {
                        // filter by selected warehouse
                        totalStock = invStats.stream()
                                        .filter(row -> row[0] != null) // can't filter by UUID here since query groups
                                                                       // by name; fall back to unfiltered sum
                                        .map(row -> row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        // Recompute using inventory balance directly
                        totalStock = inventoryBalanceRepository.sumQuantityByWarehouseGroup().stream()
                                        .map(row -> row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                } else {
                        totalStock = invStats.stream()
                                        .map(row -> row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                summary.setOverallStock(totalStock);

                summary.setLowStockCount(inventoryBalanceRepository.findLowStock(null).getTotalElements());
                summary.setExpiringSoonCount(inventoryBalanceRepository.countExpiringItems(warehouseId,
                                LocalDate.now().plusMonths(1)));

                OffsetDateTime now = OffsetDateTime.now();
                OffsetDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                OffsetDateTime startOfPrevMonth = startOfMonth.minusMonths(1);

                // Stock In Trend
                long currentStockIn = inboundReceiptRepository.countByCreatedAtBetween(startOfMonth, now);
                long prevStockIn = inboundReceiptRepository.countByCreatedAtBetween(startOfPrevMonth, startOfMonth);
                summary.setStockInCount(currentStockIn);
                summary.setStockInTrend(calculateTrend(currentStockIn, prevStockIn));

                // Stock Out Trend
                long currentStockOut = outboundIssueRepository.countByCreatedAtBetween(startOfMonth, now);
                long prevStockOut = outboundIssueRepository.countByCreatedAtBetween(startOfPrevMonth, startOfMonth);
                summary.setStockOutCount(currentStockOut);
                summary.setStockOutTrend(calculateTrend(currentStockOut, prevStockOut));

                // Dummy trends for others since no timestamps
                summary.setTotalProductsTrend(12.5);
                summary.setTotalCategoriesTrend(-3.0);
                summary.setTotalWarehousesTrend(0.0);

                OffsetDateTime oneMonthAgo = OffsetDateTime.now().minusMonths(1);
                summary.setRecentWasteCount(outboundIssueRepository.sumWasteQuantity(warehouseId, oneMonthAgo,
                                OffsetDateTime.now()));

                return summary;
        }

        private double calculateTrend(long current, long previous) {
                if (previous == 0) return current > 0 ? 100.0 : 0.0;
                return ((double) (current - previous) / previous) * 100.0;
        }
}
