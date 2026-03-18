package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryViewController {

    private final CategoryService categoryService;

    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
        return "categories/list";
    }

    @PostMapping
    public String createCategory(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam(value = "parentId", required = false) String parentIdStr,
            RedirectAttributes redirectAttrs) {
        try {
            UUID parentId = (parentIdStr != null && !parentIdStr.isBlank())
                    ? UUID.fromString(parentIdStr) : null;
            categoryService.save(code, name, parentId);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Category \"" + name + "\" created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to create category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    @PostMapping("/{id}/update")
    public String updateCategory(
            @PathVariable("id") UUID id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttrs) {
        try {
            categoryService.update(id, code, name);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Category updated successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to update category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(
            @PathVariable("id") UUID id,
            RedirectAttributes redirectAttrs) {
        try {
            categoryService.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to delete category: " + e.getMessage());
        }
        return "redirect:/categories";
    }
}


