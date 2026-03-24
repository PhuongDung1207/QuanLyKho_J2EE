package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.dto.TimelineEventDTO;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import com.example.JavaQuanLyKho.model.entity.TransferLine;
import com.example.JavaQuanLyKho.repository.InboundReceiptLineRepository;
import com.example.JavaQuanLyKho.repository.InboundReceiptRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueLineRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueRepository;
import com.example.JavaQuanLyKho.repository.TransferLineRepository;
import com.example.JavaQuanLyKho.repository.TransferRepository;
import com.example.JavaQuanLyKho.repository.WarehouseRepository;
import com.example.JavaQuanLyKho.service.ProductTimelineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ProductTimelineServiceImpl implements ProductTimelineService {

    private final InboundReceiptRepository inboundRepository;
    private final InboundReceiptLineRepository inboundLineRepository;
    private final OutboundIssueRepository outboundRepository;
    private final OutboundIssueLineRepository outboundLineRepository;
    private final TransferRepository transferRepository;
    private final TransferLineRepository transferLineRepository;
    private final WarehouseRepository warehouseRepository;

    public ProductTimelineServiceImpl(InboundReceiptRepository inboundRepository,
                                      InboundReceiptLineRepository inboundLineRepository,
                                      OutboundIssueRepository outboundRepository,
                                      OutboundIssueLineRepository outboundLineRepository,
                                      TransferRepository transferRepository,
                                      TransferLineRepository transferLineRepository,
                                      WarehouseRepository warehouseRepository) {
        this.inboundRepository = inboundRepository;
        this.inboundLineRepository = inboundLineRepository;
        this.outboundRepository = outboundRepository;
        this.outboundLineRepository = outboundLineRepository;
        this.transferRepository = transferRepository;
        this.transferLineRepository = transferLineRepository;
        this.warehouseRepository = warehouseRepository;
    }

    private String getWarehouseName(UUID warehouseId) {
        if (warehouseId == null) return "Unknown";
        return warehouseRepository.findById(warehouseId).map(w -> w.getName()).orElse("Unknown");
    }

    @Override
    public List<TimelineEventDTO> getProductTimeline(UUID productId) {
        List<TimelineEventDTO> timeline = new ArrayList<>();

        // Inbounds
        List<InboundReceiptLine> inLines = inboundLineRepository.findAll().stream()
                .filter(l -> l.getProductId().equals(productId)).toList();
        for (InboundReceiptLine line : inLines) {
            inboundRepository.findById(line.getReceiptId()).ifPresent(receipt -> {
                String wName = "Nhập vào " + getWarehouseName(receipt.getWarehouseId());
                timeline.add(new TimelineEventDTO("INBOUND", receipt.getCode(), line.getQuantity(), receipt.getCreatedAt(), receipt.getCreatedBy(), wName));
            });
        }

        // Outbounds
        List<OutboundIssueLine> outLines = outboundLineRepository.findAll().stream()
                .filter(l -> l.getProductId().equals(productId)).toList();
        for (OutboundIssueLine line : outLines) {
            outboundRepository.findById(line.getIssueId()).ifPresent(issue -> {
                String wName = "Xuất từ " + getWarehouseName(issue.getWarehouseId());
                timeline.add(new TimelineEventDTO("OUTBOUND", issue.getCode(), line.getQuantity().negate(), issue.getCreatedAt(), issue.getCreatedBy(), wName));
            });
        }

        // Transfers
        List<TransferLine> transferLines = transferLineRepository.findAll().stream()
                .filter(l -> l.getProductId().equals(productId)).toList();
        for (TransferLine line : transferLines) {
            transferRepository.findById(line.getTransferId()).ifPresent(transfer -> {
                String wName = getWarehouseName(transfer.getFromWarehouseId()) + " -> " + getWarehouseName(transfer.getToWarehouseId());
                timeline.add(new TimelineEventDTO("TRANSFER", transfer.getCode(), line.getQuantity(), transfer.getCreatedAt(), transfer.getCreatedBy(), wName));
            });
        }

        // Sort ascending by date (oldest first for timeline view)
        timeline.sort(Comparator.comparing(TimelineEventDTO::getTimestamp));

        return timeline;
    }
}
