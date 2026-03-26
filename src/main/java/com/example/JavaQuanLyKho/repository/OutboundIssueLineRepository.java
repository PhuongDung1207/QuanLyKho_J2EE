package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboundIssueLineRepository extends JpaRepository<OutboundIssueLine, UUID> {

    List<OutboundIssueLine> findByIssueId(UUID issueId);

    List<OutboundIssueLine> findByIssueIdIn(List<UUID> issueIds);

    void deleteByIssueId(UUID issueId);
}
