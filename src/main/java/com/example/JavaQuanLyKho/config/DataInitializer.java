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
            // ─── WAREHOUSES ──────────────────────────────────────────────────
            Permission pWarehouseView = new Permission();
            pWarehouseView.setCode("WAREHOUSE_VIEW");
            pWarehouseView.setModule("WAREHOUSES");
            pWarehouseView.setName("Xem danh sách kho");
            Permission savedWarehouseView = permissionRepository.save(pWarehouseView);

            Permission pWarehouseCreate = new Permission();
            pWarehouseCreate.setCode("WAREHOUSE_CREATE");
            pWarehouseCreate.setModule("WAREHOUSES");
            pWarehouseCreate.setName("Tạo kho");
            Permission savedWarehouseCreate = permissionRepository.save(pWarehouseCreate);

            Permission pWarehouseUpdate = new Permission();
            pWarehouseUpdate.setCode("WAREHOUSE_UPDATE");
            pWarehouseUpdate.setModule("WAREHOUSES");
            pWarehouseUpdate.setName("Cập nhật kho");
            Permission savedWarehouseUpdate = permissionRepository.save(pWarehouseUpdate);

            Permission pWarehouseDelete = new Permission();
            pWarehouseDelete.setCode("WAREHOUSE_DELETE");
            pWarehouseDelete.setModule("WAREHOUSES");
            pWarehouseDelete.setName("Xóa kho");
            Permission savedWarehouseDelete = permissionRepository.save(pWarehouseDelete);

            // ─── MASTER_DATA (Products) ──────────────────────────────────────
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

            // ─── MASTER_DATA (Categories) ────────────────────────────────────
            Permission pCategoryView = new Permission();
            pCategoryView.setCode("CATEGORY_VIEW");
            pCategoryView.setModule("MASTER_DATA");
            pCategoryView.setName("Xem danh mục");
            Permission savedCategoryView = permissionRepository.save(pCategoryView);

            Permission pCategoryCreate = new Permission();
            pCategoryCreate.setCode("CATEGORY_CREATE");
            pCategoryCreate.setModule("MASTER_DATA");
            pCategoryCreate.setName("Tạo danh mục");
            Permission savedCategoryCreate = permissionRepository.save(pCategoryCreate);

            Permission pCategoryUpdate = new Permission();
            pCategoryUpdate.setCode("CATEGORY_UPDATE");
            pCategoryUpdate.setModule("MASTER_DATA");
            pCategoryUpdate.setName("Cập nhật danh mục");
            Permission savedCategoryUpdate = permissionRepository.save(pCategoryUpdate);

            Permission pCategoryDelete = new Permission();
            pCategoryDelete.setCode("CATEGORY_DELETE");
            pCategoryDelete.setModule("MASTER_DATA");
            pCategoryDelete.setName("Xóa danh mục");
            Permission savedCategoryDelete = permissionRepository.save(pCategoryDelete);

            // ─── MASTER_DATA (UOM) ───────────────────────────────────────────
            Permission pUomView = new Permission();
            pUomView.setCode("UOM_VIEW");
            pUomView.setModule("MASTER_DATA");
            pUomView.setName("Xem đơn vị tính");
            Permission savedUomView = permissionRepository.save(pUomView);

            Permission pUomCreate = new Permission();
            pUomCreate.setCode("UOM_CREATE");
            pUomCreate.setModule("MASTER_DATA");
            pUomCreate.setName("Tạo đơn vị tính");
            Permission savedUomCreate = permissionRepository.save(pUomCreate);

            Permission pUomUpdate = new Permission();
            pUomUpdate.setCode("UOM_UPDATE");
            pUomUpdate.setModule("MASTER_DATA");
            pUomUpdate.setName("Cập nhật đơn vị tính");
            Permission savedUomUpdate = permissionRepository.save(pUomUpdate);

            Permission pUomDelete = new Permission();
            pUomDelete.setCode("UOM_DELETE");
            pUomDelete.setModule("MASTER_DATA");
            pUomDelete.setName("Xóa đơn vị tính");
            Permission savedUomDelete = permissionRepository.save(pUomDelete);

            // ─── MASTER_DATA (Locations) ─────────────────────────────────────
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

            Permission pLocationUpdate = new Permission();
            pLocationUpdate.setCode("LOCATION_UPDATE");
            pLocationUpdate.setModule("MASTER_DATA");
            pLocationUpdate.setName("Cập nhật vị trí kho");
            Permission savedLocationUpdate = permissionRepository.save(pLocationUpdate);

            Permission pLocationDelete = new Permission();
            pLocationDelete.setCode("LOCATION_DELETE");
            pLocationDelete.setModule("MASTER_DATA");
            pLocationDelete.setName("Xóa vị trí kho");
            Permission savedLocationDelete = permissionRepository.save(pLocationDelete);

            // ─── MASTER_DATA (Suppliers) ─────────────────────────────────────
            Permission pSupplierView = new Permission();
            pSupplierView.setCode("SUPPLIER_VIEW");
            pSupplierView.setModule("MASTER_DATA");
            pSupplierView.setName("Xem nhà cung cấp");
            Permission savedSupplierView = permissionRepository.save(pSupplierView);

            Permission pSupplierCreate = new Permission();
            pSupplierCreate.setCode("SUPPLIER_CREATE");
            pSupplierCreate.setModule("MASTER_DATA");
            pSupplierCreate.setName("Tạo nhà cung cấp");
            Permission savedSupplierCreate = permissionRepository.save(pSupplierCreate);

            Permission pSupplierUpdate = new Permission();
            pSupplierUpdate.setCode("SUPPLIER_UPDATE");
            pSupplierUpdate.setModule("MASTER_DATA");
            pSupplierUpdate.setName("Cập nhật nhà cung cấp");
            Permission savedSupplierUpdate = permissionRepository.save(pSupplierUpdate);

            Permission pSupplierDelete = new Permission();
            pSupplierDelete.setCode("SUPPLIER_DELETE");
            pSupplierDelete.setModule("MASTER_DATA");
            pSupplierDelete.setName("Xóa nhà cung cấp");
            Permission savedSupplierDelete = permissionRepository.save(pSupplierDelete);

            // ─── ROLES ───────────────────────────────────────────────────────
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

            // ─── USERS ───────────────────────────────────────────────────────
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

            // ─── INBOUND_RECEIPTS ────────────────────────────────────────────
            Permission pInboundCreate = new Permission();
            pInboundCreate.setCode("INBOUND_RECEIPT_CREATE");
            pInboundCreate.setModule("INBOUND_RECEIPTS");
            pInboundCreate.setName("Tạo phiếu nhập kho");
            Permission savedInboundCreate = permissionRepository.save(pInboundCreate);

            Permission pInboundUpdate = new Permission();
            pInboundUpdate.setCode("INBOUND_RECEIPT_UPDATE");
            pInboundUpdate.setModule("INBOUND_RECEIPTS");
            pInboundUpdate.setName("Cập nhật phiếu nhập kho");
            Permission savedInboundUpdate = permissionRepository.save(pInboundUpdate);

            Permission pInboundDelete = new Permission();
            pInboundDelete.setCode("INBOUND_RECEIPT_DELETE");
            pInboundDelete.setModule("INBOUND_RECEIPTS");
            pInboundDelete.setName("Xóa phiếu nhập kho");
            Permission savedInboundDelete = permissionRepository.save(pInboundDelete);

            Permission pInboundApprove = new Permission();
            pInboundApprove.setCode("INBOUND_RECEIPT_APPROVE");
            pInboundApprove.setModule("INBOUND_RECEIPTS");
            pInboundApprove.setName("Duyệt phiếu nhập kho");
            Permission savedInboundApprove = permissionRepository.save(pInboundApprove);

            Permission pInboundDecline = new Permission();
            pInboundDecline.setCode("INBOUND_RECEIPT_DECLINE");
            pInboundDecline.setModule("INBOUND_RECEIPTS");
            pInboundDecline.setName("Từ chối phiếu nhập kho");
            Permission savedInboundDecline = permissionRepository.save(pInboundDecline);

            // ─── OUTBOUND_RECEIPTS ───────────────────────────────────────────
            Permission pOutboundCreate = new Permission();
            pOutboundCreate.setCode("OUTBOUND_RECEIPT_CREATE");
            pOutboundCreate.setModule("OUTBOUND_RECEIPTS");
            pOutboundCreate.setName("Tạo phiếu xuất kho");
            Permission savedOutboundCreate = permissionRepository.save(pOutboundCreate);

            Permission pOutboundUpdate = new Permission();
            pOutboundUpdate.setCode("OUTBOUND_RECEIPT_UPDATE");
            pOutboundUpdate.setModule("OUTBOUND_RECEIPTS");
            pOutboundUpdate.setName("Cập nhật phiếu xuất kho");
            Permission savedOutboundUpdate = permissionRepository.save(pOutboundUpdate);

            Permission pOutboundDelete = new Permission();
            pOutboundDelete.setCode("OUTBOUND_RECEIPT_DELETE");
            pOutboundDelete.setModule("OUTBOUND_RECEIPTS");
            pOutboundDelete.setName("Xóa phiếu xuất kho");
            Permission savedOutboundDelete = permissionRepository.save(pOutboundDelete);

            Permission pOutboundApprove = new Permission();
            pOutboundApprove.setCode("OUTBOUND_RECEIPT_APPROVE");
            pOutboundApprove.setModule("OUTBOUND_RECEIPTS");
            pOutboundApprove.setName("Duyệt phiếu xuất kho");
            Permission savedOutboundApprove = permissionRepository.save(pOutboundApprove);

            Permission pOutboundDecline = new Permission();
            pOutboundDecline.setCode("OUTBOUND_RECEIPT_DECLINE");
            pOutboundDecline.setModule("OUTBOUND_RECEIPTS");
            pOutboundDecline.setName("Từ chối phiếu xuất kho");
            Permission savedOutboundDecline = permissionRepository.save(pOutboundDecline);

            // ─── INVENTORY ───────────────────────────────────────────────────
            Permission pInventoryCreate = new Permission();
            pInventoryCreate.setCode("INVENTORY_CREATE");
            pInventoryCreate.setModule("INVENTORY");
            pInventoryCreate.setName("Tạo kiểm kê");
            Permission savedInventoryCreate = permissionRepository.save(pInventoryCreate);

            Permission pInventoryUpdate = new Permission();
            pInventoryUpdate.setCode("INVENTORY_UPDATE");
            pInventoryUpdate.setModule("INVENTORY");
            pInventoryUpdate.setName("Cập nhật kiểm kê");
            Permission savedInventoryUpdate = permissionRepository.save(pInventoryUpdate);

            Permission pInventoryDelete = new Permission();
            pInventoryDelete.setCode("INVENTORY_DELETE");
            pInventoryDelete.setModule("INVENTORY");
            pInventoryDelete.setName("Xóa kiểm kê");
            Permission savedInventoryDelete = permissionRepository.save(pInventoryDelete);

            Role adminRole = new Role();
            adminRole.setCode("ADMIN");
            adminRole.setName("Administrator");
            Set<Permission> adminPermissions = new HashSet<>(List.of(
                    savedWarehouseView, savedWarehouseCreate, savedWarehouseUpdate, savedWarehouseDelete,
                    savedUomView, savedUomCreate, savedUomUpdate, savedUomDelete,
                    savedCategoryView, savedCategoryCreate, savedCategoryUpdate, savedCategoryDelete,
                    savedProductView, savedProductCreate, savedProductUpdate, savedProductDelete, savedProductLock,
                    savedLocationView, savedLocationCreate, savedLocationUpdate, savedLocationDelete,
                    savedRoleView, savedRoleCreate, savedRoleUpdate,
                    savedUserView, savedUserCreate, savedUserUpdate, savedUserLock,
                    savedSupplierView, savedSupplierCreate, savedSupplierUpdate, savedSupplierDelete,
                    savedInboundCreate, savedInboundUpdate, savedInboundDelete, savedInboundApprove, savedInboundDecline,
                    savedOutboundCreate, savedOutboundUpdate, savedOutboundDelete, savedOutboundApprove, savedOutboundDecline,
                    savedInventoryCreate, savedInventoryUpdate, savedInventoryDelete
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
