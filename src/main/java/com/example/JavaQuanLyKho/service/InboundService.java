package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.InboundDtos;
import com.example.JavaQuanLyKho.model.entity.InboundReceipt;
import com.example.JavaQuanLyKho.model.entity.InboundReceiptLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InboundService {

    Page<InboundReceipt> findAll(Pageable pageable);

    List<InboundReceiptLine> findLines(UUID receiptId);

    InboundReceipt create(InboundDtos.CreateRequest request, String actorUsername);

    InboundReceipt submit(UUID receiptId);

    InboundReceipt approve(UUID receiptId, String actorUsername);

    InboundReceipt receive(UUID receiptId);
}
