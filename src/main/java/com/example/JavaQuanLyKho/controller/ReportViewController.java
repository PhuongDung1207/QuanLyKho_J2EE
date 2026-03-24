package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.ReportDtos;
import com.example.JavaQuanLyKho.service.GreenWarehouseService;
import com.example.JavaQuanLyKho.service.ReportService;
import com.example.JavaQuanLyKho.service.WarehouseService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reports")
public class ReportViewController {

    private final ReportService reportService;
    private final WarehouseService warehouseService;
    private final GreenWarehouseService greenWarehouseService;

    public ReportViewController(ReportService reportService, WarehouseService warehouseService,
            GreenWarehouseService greenWarehouseService) {
        this.reportService = reportService;
        this.warehouseService = warehouseService;
        this.greenWarehouseService = greenWarehouseService;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "warehouseId", required = false) UUID warehouseId,
            @RequestParam(name = "months", defaultValue = "6") int months,
            Model model
    ) {
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.minusMonths(months);

        var summary = reportService.getDashboardSummary(warehouseId);
        var warehouseStats = reportService.getInventoryByWarehouse();
        List<ReportDtos.TransactionTimelineReport> timeline = reportService.getTransactionTimeline(warehouseId, start, end);

        model.addAttribute("summary", summary);
        model.addAttribute("warehouseStats", warehouseStats);
        model.addAttribute("timeline", timeline);
        model.addAttribute("warehouses", warehouseService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("selectedWarehouseId", warehouseId);
        model.addAttribute("selectedMonths", months);
        
        // Green report
        model.addAttribute("wasteStats", greenWarehouseService.getWasteStatisticsByType(warehouseId, start, end));
        model.addAttribute("health", greenWarehouseService.getHealthSummary(warehouseId));

        // Prepare chart lists
        model.addAttribute("timelineLabels", timeline.stream().map(ReportDtos.TransactionTimelineReport::getPeriod).toList());
        model.addAttribute("inboundData", timeline.stream().map(ReportDtos.TransactionTimelineReport::getInboundQty).toList());
        model.addAttribute("outboundData", timeline.stream().map(ReportDtos.TransactionTimelineReport::getOutboundQty).toList());

        model.addAttribute("warehouseLabels", warehouseStats.stream().map(ReportDtos.WarehouseInventoryReport::getWarehouseName).toList());
        model.addAttribute("warehouseData", warehouseStats.stream().map(ReportDtos.WarehouseInventoryReport::getTotalStock).toList());

        return "reports/list";
    }
}
