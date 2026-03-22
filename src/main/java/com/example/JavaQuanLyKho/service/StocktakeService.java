package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.dto.StocktakeQRRequestDTO;
import com.example.JavaQuanLyKho.model.entity.Stocktake;
import com.example.JavaQuanLyKho.model.entity.StocktakeLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StocktakeService {
    Page<Stocktake> findAll(Pageable pageable);
    Stocktake findById(UUID id);
    Stocktake create(Stocktake stocktake);
    void close(UUID id);
    
    // QR Feature
    StocktakeLine processQrScan(UUID stocktakeId, StocktakeQRRequestDTO requestDTO);
    List<StocktakeLine> getLines(UUID stocktakeId);
}
