package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.OutboundDtos;
import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OutboundService {

    Page<OutboundIssue> findAll(Pageable pageable);

    List<OutboundIssueLine> findLines(UUID issueId);

    OutboundIssue create(OutboundDtos.CreateRequest request, String actorUsername);

    OutboundIssue submit(UUID issueId);

    OutboundIssue approve(UUID issueId, String actorUsername);

    OutboundIssue complete(UUID issueId);
}
