package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Category;
import com.example.JavaQuanLyKho.repository.CategoryRepository;
import com.example.JavaQuanLyKho.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category save(String code, String name, UUID parentId) {
        Category cat = new Category();
        cat.setCode(code.toUpperCase().trim());
        cat.setName(name.trim());
        cat.setParentId(parentId);
        return categoryRepository.save(cat);
    }

    @Override
    public Category update(UUID id, String code, String name) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        cat.setCode(code.toUpperCase().trim());
        cat.setName(name.trim());
        return categoryRepository.save(cat);
    }

    @Override
    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}

