package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.InboundDtos;
import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import com.example.JavaQuanLyKho.service.InboundService;
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
@RequestMapping("/api/v1/inbounds")
public class InboundController {

    private final InboundService inboundService;

    public InboundController(InboundService inboundService) {
        this.inboundService = inboundService;
    }

    @GetMapping
    public ResponseEntity<Page<InboundDtos.Response>> getInbounds(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InboundDtos.Response> body = inboundService.findAll(pageable).map(this::toResponse);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<InboundDtos.Response> createInbound(
            @Valid @RequestBody InboundDtos.CreateRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        InboundReceipt created = inboundService.create(request, username);
        return ResponseEntity
                .created(URI.create("/api/v1/inbounds/" + created.getId()))
                .body(toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInbound(@PathVariable("id") UUID id) {
        inboundService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<InboundDtos.Response> submitInbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(inboundService.submit(id)));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<InboundDtos.Response> approveInbound(
            @PathVariable("id") UUID id,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(toResponse(inboundService.approve(id, username)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<InboundDtos.Response> rejectInbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(inboundService.reject(id)));
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<InboundDtos.Response> receiveInbound(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(inboundService.receive(id)));
    }

    private InboundDtos.Response toResponse(InboundReceipt receipt) {
        InboundDtos.Response response = new InboundDtos.Response();
        response.setId(receipt.getId());
        response.setCode(receipt.getCode());
        response.setType(receipt.getType());
        response.setWarehouseId(receipt.getWarehouseId());
        response.setSupplierId(receipt.getSupplierId());
        response.setStatus(receipt.getStatus().name());
        response.setCreatedBy(receipt.getCreatedBy());
        response.setApprovedBy(receipt.getApprovedBy());
        response.setCreatedAt(receipt.getCreatedAt());
        response.setApprovedAt(receipt.getApprovedAt());
        response.setCompletedAt(receipt.getCompletedAt());

        List<InboundDtos.LineResponse> lines = inboundService.findLines(receipt.getId()).stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
        response.setLines(lines);
        return response;
    }

    private InboundDtos.LineResponse toLineResponse(InboundReceiptLine line) {
        InboundDtos.LineResponse response = new InboundDtos.LineResponse();
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
