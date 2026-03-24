package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.ReportDtos;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    List<ReportDtos.WarehouseInventoryReport> getInventoryByWarehouse();

    List<ReportDtos.TransactionTimelineReport> getTransactionTimeline(UUID warehouseId, OffsetDateTime start, OffsetDateTime end);

    ReportDtos.DashboardSummary getDashboardSummary(UUID warehouseId);
}
