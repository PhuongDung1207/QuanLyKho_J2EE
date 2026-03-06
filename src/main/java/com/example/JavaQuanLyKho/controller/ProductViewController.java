package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        Page<Product> page = productService.findAll(Pageable.unpaged());
        model.addAttribute("products", page.getContent());
        model.addAttribute("createForm", new Product());
        return "products/list";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("createForm") Product createForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Page<Product> page = productService.findAll(Pageable.unpaged());
            model.addAttribute("products", page.getContent());
            return "products/list";
        }
        productService.create(createForm);
        return "redirect:/products";
    }
}

