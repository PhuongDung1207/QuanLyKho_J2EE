package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.repository.InboundReceiptRepository;
import com.example.JavaQuanLyKho.repository.InventoryBalanceRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueRepository;
import com.example.JavaQuanLyKho.service.GreenWarehouseService;
import com.example.JavaQuanLyKho.service.ReportService;
import com.example.JavaQuanLyKho.service.WarehouseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.OffsetDateTime;

@Controller
public class DashboardViewController {

    private final ReportService reportService;
    private final GreenWarehouseService greenWarehouseService;
    private final InboundReceiptRepository inboundReceiptRepository;
    private final OutboundIssueRepository outboundIssueRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;

    public DashboardViewController(ReportService reportService, GreenWarehouseService greenWarehouseService,
            InboundReceiptRepository inboundReceiptRepository, OutboundIssueRepository outboundIssueRepository,
            InventoryBalanceRepository inventoryBalanceRepository) {
        this.reportService = reportService;
        this.greenWarehouseService = greenWarehouseService;
        this.inboundReceiptRepository = inboundReceiptRepository;
        this.outboundIssueRepository = outboundIssueRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Summary data
        var summary = reportService.getDashboardSummary(null);
        var health = greenWarehouseService.getHealthSummary(null);
        
        model.addAttribute("summary", summary);
        model.addAttribute("health", health);

        // Charts data (Current month)
        OffsetDateTime end = OffsetDateTime.now();
        OffsetDateTime start = end.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        model.addAttribute("timeline", reportService.getTransactionTimeline(null, start, end));
        model.addAttribute("warehouseStats", reportService.getInventoryByWarehouse());
        
        // Recent Activities
        model.addAttribute("recentInbounds", inboundReceiptRepository.findAll(
                PageRequest.of(0, 5, Sort.by("createdAt").descending())).getContent());
        model.addAttribute("recentOutbounds", outboundIssueRepository.findAll(
                PageRequest.of(0, 5, Sort.by("createdAt").descending())).getContent());
        
        // Critical Stock (Low Stock)
        model.addAttribute("lowStockItems", inventoryBalanceRepository.findInventoryRows(null, 
                PageRequest.of(0, 5, Sort.by("qtyOnHand").ascending())).getContent()
                .stream().filter(i -> i.getMinQty() != null && i.getQtyOnHand().compareTo(i.getMinQty()) <= 0)
                .toList());

        return "dashboard";
    }
}
