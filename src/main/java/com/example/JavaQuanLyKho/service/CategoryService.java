package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    Category save(String code, String name, UUID parentId);

    Category update(UUID id, String code, String name);

    void delete(UUID id);
}

