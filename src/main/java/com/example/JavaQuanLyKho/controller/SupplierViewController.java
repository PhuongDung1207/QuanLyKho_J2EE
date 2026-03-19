package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Supplier;
import com.example.JavaQuanLyKho.service.SupplierService;
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
@RequestMapping("/suppliers")
public class SupplierViewController {

    private final SupplierService supplierService;

    public SupplierViewController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.findAll(Pageable.unpaged()).getContent());
        return "suppliers/list";
    }

    @PostMapping
    public String createSupplier(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "contactPhone", required = false) String contactPhone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "status", required = false) String status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Supplier supplier = new Supplier();
            supplier.setCode(code);
            supplier.setName(name);
            supplier.setContactName(contactName);
            supplier.setContactEmail(contactEmail);
            supplier.setContactPhone(contactPhone);
            supplier.setAddress(address);
            supplier.setStatus(status);
            supplierService.create(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier \"" + name + "\" created successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create supplier: " + ex.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/update")
    public String updateSupplier(
            @PathVariable("id") UUID id,
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "contactPhone", required = false) String contactPhone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "status", required = false) String status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Supplier supplier = new Supplier();
            supplier.setCode(code);
            supplier.setName(name);
            supplier.setContactName(contactName);
            supplier.setContactEmail(contactEmail);
            supplier.setContactPhone(contactPhone);
            supplier.setAddress(address);
            supplier.setStatus(status);
            supplierService.update(id, supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update supplier: " + ex.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/delete")
    public String deleteSupplier(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete supplier: " + ex.getMessage());
        }
        return "redirect:/suppliers";
    }
}
