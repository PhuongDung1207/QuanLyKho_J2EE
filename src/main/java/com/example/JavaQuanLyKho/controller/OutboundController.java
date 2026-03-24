package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.OutboundDtos;
import com.example.JavaQuanLyKho.model.entity.OutboundIssue;
import com.example.JavaQuanLyKho.model.entity.OutboundIssueLine;
import com.example.JavaQuanLyKho.service.OutboundService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/outbounds")
public class OutboundController {

    private final OutboundService outboundService;

    public OutboundController(OutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @GetMapping
    public ResponseEntity<Page<OutboundDtos.Response>> getOutbounds(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OutboundDtos.Response> body = outboundService.findAll(pageable).map(this::toResponse);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<OutboundDtos.Response> createOutbound(
            @Valid @RequestBody OutboundDtos.CreateRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        OutboundIssue created = outboundService.create(request, username);
        return ResponseEntity
                .created(URI.create("/api/v1/outbounds/" + created.getId()))
                .body(toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutbound(@PathVariable("id") UUID id) {
        outboundService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<OutboundDtos.Response> submitOutbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(outboundService.submit(id)));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<OutboundDtos.Response> approveOutbound(
            @PathVariable("id") UUID id,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(toResponse(outboundService.approve(id, username)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<OutboundDtos.Response> rejectOutbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(outboundService.reject(id)));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<OutboundDtos.Response> completeOutbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(outboundService.complete(id)));
    }

    private OutboundDtos.Response toResponse(OutboundIssue issue) {
        OutboundDtos.Response response = new OutboundDtos.Response();
        response.setId(issue.getId());
        response.setCode(issue.getCode());
        response.setType(issue.getType());
        response.setWarehouseId(issue.getWarehouseId());
        response.setCustomerId(issue.getCustomerId());
        response.setStatus(issue.getStatus().name());
        response.setCreatedBy(issue.getCreatedBy());
        response.setApprovedBy(issue.getApprovedBy());
        response.setCreatedAt(issue.getCreatedAt());
        response.setApprovedAt(issue.getApprovedAt());
        response.setCompletedAt(issue.getCompletedAt());

        List<OutboundDtos.LineResponse> lines = outboundService.findLines(issue.getId()).stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
        response.setLines(lines);
        return response;
    }

    private OutboundDtos.LineResponse toLineResponse(OutboundIssueLine line) {
        OutboundDtos.LineResponse response = new OutboundDtos.LineResponse();
        response.setId(line.getId());
        response.setProductId(line.getProductId());
        response.setUomId(line.getUomId());
        response.setQuantity(line.getQuantity());
        response.setLocationId(line.getLocationId());
        response.setMfgDate(line.getMfgDate());
        response.setShelfLife(line.getShelfLife());
        response.setExpDate(line.getExpDate());
        return response;
    }
}
