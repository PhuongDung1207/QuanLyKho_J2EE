package com.example.JavaQuanLyKho.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/suppliers")
public class SupplierViewController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "suppliers/list";
    }

    @GetMapping("/view/{id}")
    public String viewSupplier(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        
        // Dummy Supplier Data
        Map<String, Object> supplier = new HashMap<>();
        supplier.put("id", id);
        supplier.put("code", "SUP-1002" + id);
        
        if (id == 1) {
            supplier.put("name", "PackBetter Solutions");
            supplier.put("status", "ACTIVE");
            supplier.put("email", "contact@packbetter.com");
            supplier.put("phone", "0901 111 222");
            supplier.put("address", "123 Đường Điện Biên Phủ, Bình Thạnh, TP.HCM");
            supplier.put("bankAccount", "0123456789 (VCB)");
            supplier.put("totalSpend", 450000000);
        } else if (id == 2) {
            supplier.put("name", "Midstate Maintenance");
            supplier.put("status", "LOCKED");
            supplier.put("email", "m.ross@midstate.net");
            supplier.put("phone", "0902 333 444");
            supplier.put("address", "456 Đường CMT8, Quận 3, TP.HCM");
            supplier.put("bankAccount", "9876543210 (TCB)");
            supplier.put("totalSpend", 120500000);
        } else {
            supplier.put("name", "Guardian Security");
            supplier.put("status", "ACTIVE");
            supplier.put("email", "d.miller@guardian.com");
            supplier.put("phone", "0903 555 666");
            supplier.put("address", "789 Đường Tôn Đức Thắng, Quận 1, TP.HCM");
            supplier.put("bankAccount", "4561237890 (ACB)");
            supplier.put("totalSpend", 80000000);
        }
        
        model.addAttribute("supplier", supplier);

        return "suppliers/view";
    }
}
