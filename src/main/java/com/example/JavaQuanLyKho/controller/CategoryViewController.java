package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Category;
import com.example.JavaQuanLyKho.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryViewController {

    private final CategoryService categoryService;

    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        Page<Category> page = categoryService.findAll(Pageable.unpaged());
        model.addAttribute("categories", page.getContent());
        return "categories/list";
    }
}

