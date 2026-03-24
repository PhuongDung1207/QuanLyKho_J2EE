package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Batch;
import com.example.JavaQuanLyKho.model.entity.Product;
import com.example.JavaQuanLyKho.service.BatchService;
import com.example.JavaQuanLyKho.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/labels")
public class LabelPrintController {

    private final ProductService productService;
    private final BatchService batchService;

    public LabelPrintController(ProductService productService, BatchService batchService) {
        this.productService = productService;
        this.batchService = batchService;
    }

    @GetMapping("/products/{id}")
    public String printProductLabel(@PathVariable UUID id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "labels/print-product";
    }

    @GetMapping("/batches/{id}")
    public String printBatchLabel(@PathVariable UUID id, Model model) {
        Batch batch = batchService.findById(id);
        Product product = productService.findById(batch.getProductId());
        model.addAttribute("batch", batch);
        model.addAttribute("product", product);
        return "labels/print-batch";
    }
}
