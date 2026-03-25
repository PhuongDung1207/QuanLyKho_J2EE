package com.example.JavaQuanLyKho.model.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TimelineEventDTO {
    private String eventType; // INBOUND, OUTBOUND, TRANSFER
    private String referenceCode;
    private BigDecimal quantityChange;
    private OffsetDateTime timestamp;
    private UUID actor;
    private String warehouseName;

    public TimelineEventDTO(String eventType, String referenceCode, BigDecimal quantityChange, OffsetDateTime timestamp, UUID actor, String warehouseName) {
        this.eventType = eventType;
        this.referenceCode = referenceCode;
        this.quantityChange = quantityChange;
        this.timestamp = timestamp;
        this.actor = actor;
        this.warehouseName = warehouseName;
    }

    public String getEventType() { return eventType; }
    public String getReferenceCode() { return referenceCode; }
    public BigDecimal getQuantityChange() { return quantityChange; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public UUID getActor() { return actor; }
    public String getWarehouseName() { return warehouseName; }
}
