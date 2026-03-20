package com.example.JavaQuanLyKho.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
public class InventoryViewController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "inventory/list";
    }
}
