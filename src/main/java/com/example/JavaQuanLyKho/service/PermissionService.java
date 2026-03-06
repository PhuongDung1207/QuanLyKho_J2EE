package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {

    Page<Permission> findAll(Pageable pageable);
}

