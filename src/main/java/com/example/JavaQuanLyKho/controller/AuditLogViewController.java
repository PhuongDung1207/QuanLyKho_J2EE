package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.AuditLog;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/audit-logs")
public class AuditLogViewController {

    private static final ZoneOffset VN_OFFSET = ZoneOffset.ofHours(7);
    private static final DateTimeFormatter DISPLAY_FMT  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public AuditLogViewController(AuditLogService auditLogService, UserRepository userRepository) {
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLog> logPage = auditLogService.findAll(pageable);

        List<User> users = userRepository.findAll();
        Map<UUID, String> userMap = new HashMap<>();
        for (User u : users) {
            userMap.put(u.getId(), u.getUsername());
        }

        // Pre-compute display strings thay vì xử lý phức tạp trong Thymeleaf
        List<AuditLog> logs = logPage.getContent();
        Map<UUID, String> displayTimeMap = new HashMap<>();
        Map<UUID, String> displayDateMap = new HashMap<>();
        for (AuditLog log : logs) {
            if (log.getCreatedAt() != null) {
                var vnTime = log.getCreatedAt().withOffsetSameInstant(VN_OFFSET);
                displayTimeMap.put(log.getId(), vnTime.format(DISPLAY_FMT));
                displayDateMap.put(log.getId(), vnTime.format(DATE_ONLY_FMT));
            } else {
                displayTimeMap.put(log.getId(), "—");
                displayDateMap.put(log.getId(), "");
            }
        }

        model.addAttribute("logPage", logPage);
        model.addAttribute("userMap", userMap);
        model.addAttribute("displayTimeMap", displayTimeMap);
        model.addAttribute("displayDateMap", displayDateMap);
        model.addAttribute("logs", logs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", logPage.getTotalPages() == 0 ? 1 : logPage.getTotalPages());

        return "audit-logs/list";
    }
}
