package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.dto.TimelineEventDTO;
import java.util.List;
import java.util.UUID;

public interface ProductTimelineService {
    List<TimelineEventDTO> getProductTimeline(UUID productId);
}
