package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Role;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.RoleRepository;
import com.example.JavaQuanLyKho.service.UserService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UsersViewController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UsersViewController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    public static class CreateUserForm {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String status;
        private List<UUID> roleIds;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<UUID> getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(List<UUID> roleIds) {
            this.roleIds = roleIds;
        }
    }

    @GetMapping
    public String list(Model model) {
        Page<User> page = userService.findAll(Pageable.unpaged());
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("users", page.getContent());
        model.addAttribute("roles", roles);
        model.addAttribute("createForm", new CreateUserForm());
        return "users/list";
    }

    @PostMapping
    public String create(@ModelAttribute("createForm") CreateUserForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Page<User> page = userService.findAll(Pageable.unpaged());
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("users", page.getContent());
            model.addAttribute("roles", roles);
            return "users/list";
        }
        User user = new User();
        user.setUsername(form.getUsername());
        user.setStatus(form.getStatus());
        Set<UUID> roleIds = form.getRoleIds() != null ? new HashSet<>(form.getRoleIds()) : new HashSet<>();
        userService.create(user, roleIds, form.getPassword());
        return "redirect:/users";
    }
}

