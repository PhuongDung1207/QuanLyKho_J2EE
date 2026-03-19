package com.example.JavaQuanLyKho.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/dispatch")
public class DispatchViewController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // Set currentUri for sidebar active state
        model.addAttribute("currentUri", request.getRequestURI());
        
        // Return the dispatch view
        return "dispatch/list";
    }

    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", "/dispatch");
        return "dispatch/create";
    }
}
