package com.example.JavaQuanLyKho.config;

import com.example.JavaQuanLyKho.model.entity.Permission;
import com.example.JavaQuanLyKho.model.entity.Role;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.PermissionRepository;
import com.example.JavaQuanLyKho.repository.RoleRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedAuthData(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByUsername("admin")) {
                return;
            }
            Permission pWarehouseView = new Permission();
            pWarehouseView.setCode("WAREHOUSE_VIEW");
            pWarehouseView.setModule("WAREHOUSES");
            pWarehouseView.setName("Xem danh sách kho");
            Permission savedWarehouseView = permissionRepository.save(pWarehouseView);

            Permission pUomView = new Permission();
            pUomView.setCode("UOM_VIEW");
            pUomView.setModule("MASTER_DATA");
            pUomView.setName("Xem đơn vị tính");
            Permission savedUomView = permissionRepository.save(pUomView);

            Permission pCategoryView = new Permission();
            pCategoryView.setCode("CATEGORY_VIEW");
            pCategoryView.setModule("MASTER_DATA");
            pCategoryView.setName("Xem danh mục");
            Permission savedCategoryView = permissionRepository.save(pCategoryView);

            Permission pProductView = new Permission();
            pProductView.setCode("PRODUCT_VIEW");
            pProductView.setModule("MASTER_DATA");
            pProductView.setName("Xem sản phẩm");
            Permission savedProductView = permissionRepository.save(pProductView);

            Permission pProductCreate = new Permission();
            pProductCreate.setCode("PRODUCT_CREATE");
            pProductCreate.setModule("MASTER_DATA");
            pProductCreate.setName("Tạo sản phẩm");
            Permission savedProductCreate = permissionRepository.save(pProductCreate);

            Permission pLocationView = new Permission();
            pLocationView.setCode("LOCATION_VIEW");
            pLocationView.setModule("MASTER_DATA");
            pLocationView.setName("Xem vị trí kho");
            Permission savedLocationView = permissionRepository.save(pLocationView);

            Permission pLocationCreate = new Permission();
            pLocationCreate.setCode("LOCATION_CREATE");
            pLocationCreate.setModule("MASTER_DATA");
            pLocationCreate.setName("Tạo vị trí kho");
            Permission savedLocationCreate = permissionRepository.save(pLocationCreate);

            Role adminRole = new Role();
            adminRole.setCode("ADMIN");
            adminRole.setName("Administrator");
            Set<Permission> adminPermissions = new HashSet<>(List.of(
                    savedWarehouseView,
                    savedUomView,
                    savedCategoryView,
                    savedProductView,
                    savedProductCreate,
                    savedLocationView,
                    savedLocationCreate
            ));
            adminRole.setPermissions(adminPermissions);
            Role savedAdminRole = roleRepository.save(adminRole);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setStatus("ACTIVE");
            admin.setCreatedAt(OffsetDateTime.now());
            admin.setRoles(new HashSet<>(Set.of(savedAdminRole)));
            userRepository.save(admin);
        };
    }
}
