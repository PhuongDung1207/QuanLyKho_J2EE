package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}

