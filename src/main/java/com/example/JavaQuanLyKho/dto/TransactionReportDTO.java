package com.example.JavaQuanLyKho.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TransactionReportDTO {
    private UUID transactionId;
    private String transactionCode;
    private String type; // INBOUND, OUTBOUND, TRANSFER
    private String status;
    private UUID createdBy;
    private OffsetDateTime createdAt;

    public TransactionReportDTO(UUID transactionId, String transactionCode, String type, String status, UUID createdBy, OffsetDateTime createdAt) {
        this.transactionId = transactionId;
        this.transactionCode = transactionCode;
        this.type = type;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() { return transactionId; }
    public String getTransactionCode() { return transactionCode; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public UUID getCreatedBy() { return createdBy; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
