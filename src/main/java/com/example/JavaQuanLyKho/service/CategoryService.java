package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);
}

