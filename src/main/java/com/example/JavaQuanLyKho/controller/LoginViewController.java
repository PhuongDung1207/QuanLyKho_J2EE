package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.dto.AuthDtos;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.example.JavaQuanLyKho.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginViewController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static class LoginForm {
        @NotBlank(message = "Vui long nhap tai khoan.")
        private String username;

        @NotBlank(message = "Vui long nhap mat khau.")
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
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                          BindingResult bindingResult,
                          HttpSession session) {
        if (form.getUsername() != null) {
            form.setUsername(form.getUsername().trim());
        }

        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        User user = userRepository.findByUsername(form.getUsername()).orElse(null);
        if (user == null) {
            bindingResult.rejectValue("username", "login.username.invalid", "Tai khoan khong ton tai.");
            return "auth/login";
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            bindingResult.reject("login.account.locked", "Tai khoan da bi khoa hoac ngung hoat dong.");
            return "auth/login";
        }

        if (!passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            bindingResult.rejectValue("password", "login.password.invalid", "Mat khau khong dung.");
            return "auth/login";
        }

        AuthDtos.LoginRequest req = new AuthDtos.LoginRequest();
        req.setUsername(form.getUsername());
        req.setPassword(form.getPassword());

        try {
            AuthDtos.LoginResponse res = authService.login(req);
            session.setAttribute("AUTH_TOKEN", res.getAccessToken());
            session.setAttribute("AUTH_USERNAME", form.getUsername());
            return "redirect:/dashboard";
        } catch (AuthenticationException ex) {
            bindingResult.reject("login.failed", "Dang nhap that bai. Vui long thu lai.");
            return "auth/login";
        } catch (Exception ex) {
            bindingResult.reject("login.failed", "He thong dang ban. Vui long thu lai sau.");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
