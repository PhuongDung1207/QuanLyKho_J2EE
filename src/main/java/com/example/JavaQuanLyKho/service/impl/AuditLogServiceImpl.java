package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.AuditLog;
import com.example.JavaQuanLyKho.repository.AuditLogRepository;
import com.example.JavaQuanLyKho.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public Page<AuditLog> findAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @Override
    public AuditLog findById(UUID id) {
        return auditLogRepository.findById(id).orElse(null);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void logAction(UUID actorUserId, String action, String entityType) {
        try {
            AuditLog log = new AuditLog();
            log.setActorUserId(actorUserId);
            log.setAction(action);
            log.setEntityType(entityType != null && entityType.length() > 128
                    ? entityType.substring(0, 128) : entityType);
            log.setCreatedAt(OffsetDateTime.now());
            auditLogRepository.save(log);
            System.out.println("[AUDIT] Saved: " + action + " by userId=" + actorUserId);
        } catch (Exception e) {
            System.err.println("[AUDIT] Failed to write audit log: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
