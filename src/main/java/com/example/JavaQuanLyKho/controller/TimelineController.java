package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.dto.TimelineEventDTO;
import com.example.JavaQuanLyKho.service.ProductTimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
public class TimelineController {

    private final ProductTimelineService productTimelineService;

    public TimelineController(ProductTimelineService productTimelineService) {
        this.productTimelineService = productTimelineService;
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<List<TimelineEventDTO>> getProductTimeline(@PathVariable UUID productId) {
        return ResponseEntity.ok(productTimelineService.getProductTimeline(productId));
    }
}
