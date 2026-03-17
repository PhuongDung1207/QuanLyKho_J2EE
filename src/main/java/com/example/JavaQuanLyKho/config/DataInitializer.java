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

            Permission pRoleView = new Permission();
            pRoleView.setCode("ROLE_VIEW");
            pRoleView.setModule("ROLES");
            pRoleView.setName("Xem danh sách role");
            Permission savedRoleView = permissionRepository.save(pRoleView);

            Permission pRoleCreate = new Permission();
            pRoleCreate.setCode("ROLE_CREATE");
            pRoleCreate.setModule("ROLES");
            pRoleCreate.setName("Tạo role");
            Permission savedRoleCreate = permissionRepository.save(pRoleCreate);

            Permission pRoleUpdate = new Permission();
            pRoleUpdate.setCode("ROLE_UPDATE");
            pRoleUpdate.setModule("ROLES");
            pRoleUpdate.setName("Cập nhật role (gán permission)");
            Permission savedRoleUpdate = permissionRepository.save(pRoleUpdate);

            Permission pUserUpdate = new Permission();
            pUserUpdate.setCode("USER_UPDATE");
            pUserUpdate.setModule("USERS");
            pUserUpdate.setName("Cập nhật thông tin user");
            Permission savedUserUpdate = permissionRepository.save(pUserUpdate);

            Permission pUserLock = new Permission();
            pUserLock.setCode("USER_LOCK");
            pUserLock.setModule("USERS");
            pUserLock.setName("Khóa / Mở khóa user");
            Permission savedUserLock = permissionRepository.save(pUserLock);

            Permission pUserView = new Permission();
            pUserView.setCode("USER_VIEW");
            pUserView.setModule("USERS");
            pUserView.setName("Xem danh sách user");
            Permission savedUserView = permissionRepository.save(pUserView);

            Permission pUserCreate = new Permission();
            pUserCreate.setCode("USER_CREATE");
            pUserCreate.setModule("USERS");
            pUserCreate.setName("Tạo user mới");
            Permission savedUserCreate = permissionRepository.save(pUserCreate);

            Permission pProductUpdate = new Permission();
            pProductUpdate.setCode("PRODUCT_UPDATE");
            pProductUpdate.setModule("MASTER_DATA");
            pProductUpdate.setName("Cập nhật sản phẩm");
            Permission savedProductUpdate = permissionRepository.save(pProductUpdate);

            Permission pProductDelete = new Permission();
            pProductDelete.setCode("PRODUCT_DELETE");
            pProductDelete.setModule("MASTER_DATA");
            pProductDelete.setName("Xóa sản phẩm");
            Permission savedProductDelete = permissionRepository.save(pProductDelete);

            Permission pProductLock = new Permission();
            pProductLock.setCode("PRODUCT_LOCK");
            pProductLock.setModule("MASTER_DATA");
            pProductLock.setName("Khóa / Mở khóa sản phẩm");
            Permission savedProductLock = permissionRepository.save(pProductLock);

            Role adminRole = new Role();
            adminRole.setCode("ADMIN");
            adminRole.setName("Administrator");
            Set<Permission> adminPermissions = new HashSet<>(List.of(
                    savedWarehouseView,
                    savedUomView,
                    savedCategoryView,
                    savedProductView,
                    savedProductCreate,
                    savedProductUpdate,
                    savedProductDelete,
                    savedProductLock,
                    savedLocationView,
                    savedLocationCreate,
                    savedRoleView,
                    savedRoleCreate,
                    savedRoleUpdate,
                    savedUserView,
                    savedUserCreate,
                    savedUserUpdate,
                    savedUserLock
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
