package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.dto.TransactionReportDTO;
import com.example.JavaQuanLyKho.repository.InboundReceiptRepository;
import com.example.JavaQuanLyKho.repository.OutboundIssueRepository;
import com.example.JavaQuanLyKho.repository.TransferRepository;
import com.example.JavaQuanLyKho.service.TransactionHistoryService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final InboundReceiptRepository inboundRepository;
    private final OutboundIssueRepository outboundRepository;
    private final TransferRepository transferRepository;

    public TransactionHistoryServiceImpl(InboundReceiptRepository inboundRepository,
                                         OutboundIssueRepository outboundRepository,
                                         TransferRepository transferRepository) {
        this.inboundRepository = inboundRepository;
        this.outboundRepository = outboundRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    public List<TransactionReportDTO> getTransactionHistory(OffsetDateTime fromDate, OffsetDateTime toDate) {
        List<TransactionReportDTO> transactions = new ArrayList<>();

        inboundRepository.findAll().stream()
                .filter(r -> (fromDate == null || !r.getCreatedAt().isBefore(fromDate)) &&
                             (toDate == null || !r.getCreatedAt().isAfter(toDate)))
                .forEach(r -> transactions.add(new TransactionReportDTO(r.getId(), r.getCode(), "INBOUND", r.getStatus().name(), r.getCreatedBy(), r.getCreatedAt())));

        outboundRepository.findAll().stream()
                .filter(r -> (fromDate == null || !r.getCreatedAt().isBefore(fromDate)) &&
                             (toDate == null || !r.getCreatedAt().isAfter(toDate)))
                .forEach(r -> transactions.add(new TransactionReportDTO(r.getId(), r.getCode(), "OUTBOUND", r.getStatus().name(), r.getCreatedBy(), r.getCreatedAt())));

        transferRepository.findAll().stream()
                .filter(r -> (fromDate == null || !r.getCreatedAt().isBefore(fromDate)) &&
                             (toDate == null || !r.getCreatedAt().isAfter(toDate)))
                .forEach(r -> transactions.add(new TransactionReportDTO(r.getId(), r.getCode(), "TRANSFER", r.getStatus().name(), r.getCreatedBy(), r.getCreatedAt())));

        // Sort descending by date (newest first)
        transactions.sort(Comparator.comparing(TransactionReportDTO::getCreatedAt).reversed());

        return transactions;
    }
}
