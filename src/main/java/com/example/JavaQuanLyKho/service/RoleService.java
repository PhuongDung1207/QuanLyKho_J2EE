package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    Page<Role> findAll(Pageable pageable);

    Role create(Role role);

    Role updatePermissions(UUID roleId, List<UUID> permissionIds);
}

