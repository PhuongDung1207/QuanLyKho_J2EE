package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.service.CategoryService;
import com.example.JavaQuanLyKho.service.ProductService;
import com.example.JavaQuanLyKho.service.UomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UomService uomService;

    public ProductViewController(ProductService productService, CategoryService categoryService, UomService uomService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.uomService = uomService;
    }

    @GetMapping
    public String listProducts(Model model) {
        Page<Product> page = productService.findAll(Pageable.unpaged());
        model.addAttribute("products", page.getContent());
        model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("uoms", uomService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("createForm", new Product());
        return "products/list";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("createForm") Product createForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Page<Product> page = productService.findAll(Pageable.unpaged());
            model.addAttribute("products", page.getContent());
            model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
            model.addAttribute("uoms", uomService.findAll(Pageable.unpaged()).getContent());
            return "products/list";
        }
        productService.create(createForm);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable UUID id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("updateForm", product);
        model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
        model.addAttribute("uoms", uomService.findAll(Pageable.unpaged()).getContent());
        return "products/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable UUID id, @Valid @ModelAttribute("updateForm") Product updateForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
            model.addAttribute("uoms", uomService.findAll(Pageable.unpaged()).getContent());
            return "products/edit";
        }
        productService.update(id, updateForm);
        return "redirect:/products";
    }

    @PostMapping("/{id}/lock")
    public String lockProduct(@PathVariable UUID id) {
        productService.lock(id);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
