package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.TransferDtos;
import com.example.JavaQuanLyKho.model.entity.Transfer;
import com.example.JavaQuanLyKho.model.entity.TransferLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransferService {

    Page<Transfer> findAll(Pageable pageable);

    List<TransferLine> findLines(UUID transferId);

    Transfer create(TransferDtos.CreateRequest request, String actorUsername);

    Transfer approve(UUID transferId, String actorUsername);

    Transfer reject(UUID transferId);

    Transfer issue(UUID transferId);

    Transfer receive(UUID transferId);

    void delete(UUID transferId);
}
