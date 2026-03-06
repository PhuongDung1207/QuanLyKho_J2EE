package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Permission;
import com.example.JavaQuanLyKho.service.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/permissions")
public class PermissionsViewController {

    private final PermissionService permissionService;

    public PermissionsViewController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public String list(Model model) {
        Page<Permission> page = permissionService.findAll(Pageable.unpaged());
        model.addAttribute("permissions", page.getContent());
        return "permissions/list";
    }
}

