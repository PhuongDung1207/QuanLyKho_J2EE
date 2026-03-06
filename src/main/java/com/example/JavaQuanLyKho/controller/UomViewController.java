package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Uom;
import com.example.JavaQuanLyKho.service.UomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uoms")
public class UomViewController {

    private final UomService uomService;

    public UomViewController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public String listUoms(Model model) {
        Page<Uom> page = uomService.findAll(Pageable.unpaged());
        model.addAttribute("uoms", page.getContent());
        return "uoms/list";
    }
}

