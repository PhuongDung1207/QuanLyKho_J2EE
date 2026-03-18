package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.service.UomService;
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
@RequestMapping("/uoms")
public class UomViewController {

    private final UomService uomService;

    public UomViewController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public String listUoms(Model model) {
        model.addAttribute("uoms", uomService.findAll(Pageable.unpaged()).getContent());
        return "uoms/list";
    }

    @PostMapping
    public String createUom(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttrs) {
        try {
            uomService.save(code, name);
            redirectAttrs.addFlashAttribute("successMessage",
                    "UOM \"" + name + "\" created successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to create UOM: " + e.getMessage());
        }
        return "redirect:/uoms";
    }

    @PostMapping("/{id}/update")
    public String updateUom(
            @PathVariable("id") UUID id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            RedirectAttributes redirectAttrs) {
        try {
            uomService.update(id, code, name);
            redirectAttrs.addFlashAttribute("successMessage", "UOM updated successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to update UOM: " + e.getMessage());
        }
        return "redirect:/uoms";
    }

    @PostMapping("/{id}/delete")
    public String deleteUom(
            @PathVariable("id") UUID id,
            RedirectAttributes redirectAttrs) {
        try {
            uomService.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "UOM deleted successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to delete UOM: " + e.getMessage());
        }
        return "redirect:/uoms";
    }
}


