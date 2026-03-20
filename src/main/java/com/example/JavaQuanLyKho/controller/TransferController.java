package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.TransferDtos;
import com.example.JavaQuanLyKho.model.entity.Transfer;
import com.example.JavaQuanLyKho.model.entity.TransferLine;
import com.example.JavaQuanLyKho.service.TransferService;
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
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public ResponseEntity<Page<TransferDtos.Response>> getTransfers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransferDtos.Response> body = transferService.findAll(pageable).map(this::toResponse);
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<TransferDtos.Response> createTransfer(
            @Valid @RequestBody TransferDtos.CreateRequest request,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        Transfer created = transferService.create(request, username);
        return ResponseEntity
                .created(URI.create("/api/v1/transfers/" + created.getId()))
                .body(toResponse(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable("id") UUID id) {
        transferService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<TransferDtos.Response> approveTransfer(
            @PathVariable("id") UUID id,
            Authentication authentication
    ) {
        String username = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(toResponse(transferService.approve(id, username)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<TransferDtos.Response> rejectTransfer(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(transferService.reject(id)));
    }

    @PostMapping("/{id}/issue")
    public ResponseEntity<TransferDtos.Response> issueTransfer(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(transferService.issue(id)));
    }

    @PostMapping("/{id}/receive")
    public ResponseEntity<TransferDtos.Response> receiveTransfer(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(transferService.receive(id)));
    }

    private TransferDtos.Response toResponse(Transfer transfer) {
        TransferDtos.Response response = new TransferDtos.Response();
        response.setId(transfer.getId());
        response.setCode(transfer.getCode());
        response.setFromWarehouseId(transfer.getFromWarehouseId());
        response.setToWarehouseId(transfer.getToWarehouseId());
        response.setStatus(transfer.getStatus().name());
        response.setCreatedBy(transfer.getCreatedBy());
        response.setApprovedBy(transfer.getApprovedBy());
        response.setCreatedAt(transfer.getCreatedAt());
        response.setApprovedAt(transfer.getApprovedAt());
        response.setIssuedAt(transfer.getIssuedAt());
        response.setCompletedAt(transfer.getCompletedAt());

        List<TransferDtos.LineResponse> lines = transferService.findLines(transfer.getId()).stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
        response.setLines(lines);
        return response;
    }

    private TransferDtos.LineResponse toLineResponse(TransferLine line) {
        TransferDtos.LineResponse response = new TransferDtos.LineResponse();
        response.setId(line.getId());
        response.setProductId(line.getProductId());
        response.setUomId(line.getUomId());
        response.setQuantity(line.getQuantity());
        response.setFromLocationId(line.getFromLocationId());
        response.setToLocationId(line.getToLocationId());
        return response;
    }
}
