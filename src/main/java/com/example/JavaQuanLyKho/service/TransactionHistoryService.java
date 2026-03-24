package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.dto.TransactionReportDTO;
import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionHistoryService {
    List<TransactionReportDTO> getTransactionHistory(OffsetDateTime fromDate, OffsetDateTime toDate);
}
