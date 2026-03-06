package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.AuthDtos;
import com.example.JavaQuanLyKho.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LoginViewController {

    private final AuthService authService;

    public LoginViewController(AuthService authService) {
        this.authService = authService;
    }

    public static class LoginForm {
        @NotBlank
        private String username;
        @NotBlank
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "auth/login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        AuthDtos.LoginRequest req = new AuthDtos.LoginRequest();
        req.setUsername(form.getUsername());
        req.setPassword(form.getPassword());
        try {
            AuthDtos.LoginResponse res = authService.login(req);
            session.setAttribute("AUTH_TOKEN", res.getAccessToken());
            session.setAttribute("AUTH_USERNAME", form.getUsername());
            return "redirect:/warehouses";
        } catch (Exception ex) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

