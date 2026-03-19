package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OutboundIssueRepository extends JpaRepository<OutboundIssue, UUID> {

    Optional<OutboundIssue> findByCode(String code);

    boolean existsByCode(String code);
}
