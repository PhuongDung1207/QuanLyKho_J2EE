package com.example.JavaQuanLyKho.controller;

import com.example.JavaQuanLyKho.model.entity.Permission;
import com.example.JavaQuanLyKho.model.entity.Role;
import com.example.JavaQuanLyKho.service.PermissionService;
import com.example.JavaQuanLyKho.service.RoleService;
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

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/roles")
public class RolesViewController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public RolesViewController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    public static class CreateRoleForm {
        @NotBlank
        private String code;
        @NotBlank
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class AssignPermissionsForm {
        private UUID roleId;
        private List<UUID> permissionIds;

        public UUID getRoleId() {
            return roleId;
        }

        public void setRoleId(UUID roleId) {
            this.roleId = roleId;
        }

        public List<UUID> getPermissionIds() {
            return permissionIds;
        }

        public void setPermissionIds(List<UUID> permissionIds) {
            this.permissionIds = permissionIds;
        }
    }

    @GetMapping
    public String list(Model model) {
        Page<Role> rolesPage = roleService.findAll(Pageable.unpaged());
        Page<Permission> permissionsPage = permissionService.findAll(Pageable.unpaged());
        model.addAttribute("roles", rolesPage.getContent());
        model.addAttribute("permissions", permissionsPage.getContent());
        model.addAttribute("createForm", new CreateRoleForm());
        model.addAttribute("assignForm", new AssignPermissionsForm());
        return "roles/list";
    }

    @PostMapping
    public String create(@ModelAttribute("createForm") CreateRoleForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Page<Role> rolesPage = roleService.findAll(Pageable.unpaged());
            Page<Permission> permissionsPage = permissionService.findAll(Pageable.unpaged());
            model.addAttribute("roles", rolesPage.getContent());
            model.addAttribute("permissions", permissionsPage.getContent());
            return "roles/list";
        }
        Role role = new Role();
        role.setCode(form.getCode());
        role.setName(form.getName());
        roleService.create(role);
        return "redirect:/roles";
    }

    @PostMapping("/assign-permissions")
    public String assignPermissions(@ModelAttribute("assignForm") AssignPermissionsForm form) {
        roleService.updatePermissions(form.getRoleId(), form.getPermissionIds());
        return "redirect:/roles";
    }
}

