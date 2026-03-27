package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuditLogService {
    Page<AuditLog> findAll(Pageable pageable);
    AuditLog findById(UUID id);
    void logAction(UUID actorUserId, String action, String entityType);
}
