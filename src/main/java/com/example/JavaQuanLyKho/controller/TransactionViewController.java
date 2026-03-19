package com.example.JavaQuanLyKho.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/transactions")
public class TransactionViewController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // Set currentUri for sidebar active state
        model.addAttribute("currentUri", request.getRequestURI());
        
        // Return the transactions view
        return "transactions/list";
    }

    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", "/transactions");
        return "transactions/create";
    }
}
