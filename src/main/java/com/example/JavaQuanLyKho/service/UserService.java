package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User create(User user, Set<UUID> roleIds, String rawPassword);
}

