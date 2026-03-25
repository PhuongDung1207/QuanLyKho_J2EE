package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.dto.StocktakeQRRequestDTO;
import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.model.entity.Stocktake;
import com.example.JavaQuanLyKho.model.entity.StocktakeLine;
import com.example.JavaQuanLyKho.repository.ProductRepository;
import com.example.JavaQuanLyKho.repository.StocktakeLineRepository;
import com.example.JavaQuanLyKho.repository.StocktakeRepository;
import com.example.JavaQuanLyKho.service.StocktakeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class StocktakeServiceImpl implements StocktakeService {

    private final StocktakeRepository stocktakeRepository;
    private final StocktakeLineRepository stocktakeLineRepository;
    private final ProductRepository productRepository;

    public StocktakeServiceImpl(StocktakeRepository stocktakeRepository,
                                StocktakeLineRepository stocktakeLineRepository,
                                ProductRepository productRepository) {
        this.stocktakeRepository = stocktakeRepository;
        this.stocktakeLineRepository = stocktakeLineRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<Stocktake> findAll(Pageable pageable) {
        return stocktakeRepository.findAll(pageable);
    }

    @Override
    public Stocktake findById(UUID id) {
        return stocktakeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stocktake not found: " + id));
    }

    @Override
    @Transactional
    public Stocktake create(Stocktake stocktake) {
        if (stocktake.getCode() == null || stocktake.getCode().isEmpty()) {
            stocktake.setCode("STK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        stocktake.setStatus("IN_PROGRESS");
        return stocktakeRepository.save(stocktake);
    }

    @Override
    @Transactional
    public void close(UUID id) {
        Stocktake stocktake = findById(id);
        stocktake.setStatus("CLOSED");
        stocktakeRepository.save(stocktake);
    }

    @Override
    @Transactional
    public StocktakeLine processQrScan(UUID stocktakeId, StocktakeQRRequestDTO requestDTO) {
        Stocktake stocktake = findById(stocktakeId);
        if (!"IN_PROGRESS".equals(stocktake.getStatus())) {
            throw new RuntimeException("Cannot modify lines for closed Stocktake.");
        }

        // Find product by barcode (since batch tracking requirement was dropped, qrCode maps to Product barcode)
        Product product = productRepository.findByBarcode(requestDTO.getQrCode())
                .orElseThrow(() -> new RuntimeException("Product not found by QR/Barcode: " + requestDTO.getQrCode()));

        StocktakeLine line = stocktakeLineRepository.findByStocktakeIdAndProductId(stocktakeId, product.getId())
                .orElseGet(() -> {
                    StocktakeLine newLine = new StocktakeLine();
                    newLine.setStocktakeId(stocktakeId);
                    newLine.setProductId(product.getId());
                    newLine.setSystemQty(BigDecimal.ZERO); // In a real system, compute system qty
                    newLine.setCountedQty(BigDecimal.ZERO);
                    return newLine;
                });

        BigDecimal newCount = line.getCountedQty().add(requestDTO.getCountedQty());
        line.setCountedQty(newCount);
        line.setDiff(newCount.subtract(line.getSystemQty()));

        return stocktakeLineRepository.save(line);
    }

    @Override
    public List<StocktakeLine> getLines(UUID stocktakeId) {
        return stocktakeLineRepository.findByStocktakeId(stocktakeId);
    }
}
