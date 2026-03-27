package com.example.JavaQuanLyKho.config;

import com.example.JavaQuanLyKho.model.entity.*;
import com.example.JavaQuanLyKho.repository.*;
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
    public CommandLineRunner seedData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            WarehouseRepository warehouseRepository,
            SampleDataSeeder sampleDataSeeder,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Check if data already exists
            if (warehouseRepository.count() > 0) {
                return;
            }

            // ─── PERMISSIONS & ROLES ────────────────────────────────────────
            Permission pWarehouseView = new Permission();
            pWarehouseView.setCode("WAREHOUSE_VIEW");
            pWarehouseView.setModule("WAREHOUSES");
            pWarehouseView.setName("View Warehouse List");
            Permission savedWarehouseView = permissionRepository.save(pWarehouseView);

            Permission pWarehouseCreate = new Permission();
            pWarehouseCreate.setCode("WAREHOUSE_CREATE");
            pWarehouseCreate.setModule("WAREHOUSES");
            pWarehouseCreate.setName("Create Warehouse");
            Permission savedWarehouseCreate = permissionRepository.save(pWarehouseCreate);

            Permission pWarehouseUpdate = new Permission();
            pWarehouseUpdate.setCode("WAREHOUSE_UPDATE");
            pWarehouseUpdate.setModule("WAREHOUSES");
            pWarehouseUpdate.setName("Update Warehouse");
            Permission savedWarehouseUpdate = permissionRepository.save(pWarehouseUpdate);

            Permission pWarehouseDelete = new Permission();
            pWarehouseDelete.setCode("WAREHOUSE_DELETE");
            pWarehouseDelete.setModule("WAREHOUSES");
            pWarehouseDelete.setName("Delete Warehouse");
            Permission savedWarehouseDelete = permissionRepository.save(pWarehouseDelete);

            // ─── MASTER_DATA (Products) ──────────────────────────────────────
            Permission pProductView = new Permission();
            pProductView.setCode("PRODUCT_VIEW");
            pProductView.setModule("MASTER_DATA");
            pProductView.setName("View Products");
            Permission savedProductView = permissionRepository.save(pProductView);

            Permission pProductCreate = new Permission();
            pProductCreate.setCode("PRODUCT_CREATE");
            pProductCreate.setModule("MASTER_DATA");
            pProductCreate.setName("Create Product");
            Permission savedProductCreate = permissionRepository.save(pProductCreate);

            Permission pProductUpdate = new Permission();
            pProductUpdate.setCode("PRODUCT_UPDATE");
            pProductUpdate.setModule("MASTER_DATA");
            pProductUpdate.setName("Update Product");
            Permission savedProductUpdate = permissionRepository.save(pProductUpdate);

            Permission pProductDelete = new Permission();
            pProductDelete.setCode("PRODUCT_DELETE");
            pProductDelete.setModule("MASTER_DATA");
            pProductDelete.setName("Delete Product");
            Permission savedProductDelete = permissionRepository.save(pProductDelete);

            Permission pProductLock = new Permission();
            pProductLock.setCode("PRODUCT_LOCK");
            pProductLock.setModule("MASTER_DATA");
            pProductLock.setName("Lock/Unlock Product");
            Permission savedProductLock = permissionRepository.save(pProductLock);

            // ─── MASTER_DATA (Categories) ────────────────────────────────────
            Permission pCategoryView = new Permission();
            pCategoryView.setCode("CATEGORY_VIEW");
            pCategoryView.setModule("MASTER_DATA");
            pCategoryView.setName("View Categories");
            Permission savedCategoryView = permissionRepository.save(pCategoryView);

            Permission pCategoryCreate = new Permission();
            pCategoryCreate.setCode("CATEGORY_CREATE");
            pCategoryCreate.setModule("MASTER_DATA");
            pCategoryCreate.setName("Create Category");
            Permission savedCategoryCreate = permissionRepository.save(pCategoryCreate);

            Permission pCategoryUpdate = new Permission();
            pCategoryUpdate.setCode("CATEGORY_UPDATE");
            pCategoryUpdate.setModule("MASTER_DATA");
            pCategoryUpdate.setName("Update Category");
            Permission savedCategoryUpdate = permissionRepository.save(pCategoryUpdate);

            Permission pCategoryDelete = new Permission();
            pCategoryDelete.setCode("CATEGORY_DELETE");
            pCategoryDelete.setModule("MASTER_DATA");
            pCategoryDelete.setName("Delete Category");
            Permission savedCategoryDelete = permissionRepository.save(pCategoryDelete);

            // ─── MASTER_DATA (UOM) ───────────────────────────────────────────
            Permission pUomView = new Permission();
            pUomView.setCode("UOM_VIEW");
            pUomView.setModule("MASTER_DATA");
            pUomView.setName("View UOMs");
            Permission savedUomView = permissionRepository.save(pUomView);

            Permission pUomCreate = new Permission();
            pUomCreate.setCode("UOM_CREATE");
            pUomCreate.setModule("MASTER_DATA");
            pUomCreate.setName("Create UOM");
            Permission savedUomCreate = permissionRepository.save(pUomCreate);

            Permission pUomUpdate = new Permission();
            pUomUpdate.setCode("UOM_UPDATE");
            pUomUpdate.setModule("MASTER_DATA");
            pUomUpdate.setName("Update UOM");
            Permission savedUomUpdate = permissionRepository.save(pUomUpdate);

            Permission pUomDelete = new Permission();
            pUomDelete.setCode("UOM_DELETE");
            pUomDelete.setModule("MASTER_DATA");
            pUomDelete.setName("Delete UOM");
            Permission savedUomDelete = permissionRepository.save(pUomDelete);

            // ─── MASTER_DATA (Locations) ─────────────────────────────────────
            Permission pLocationView = new Permission();
            pLocationView.setCode("LOCATION_VIEW");
            pLocationView.setModule("MASTER_DATA");
            pLocationView.setName("View Locations");
            Permission savedLocationView = permissionRepository.save(pLocationView);

            Permission pLocationCreate = new Permission();
            pLocationCreate.setCode("LOCATION_CREATE");
            pLocationCreate.setModule("MASTER_DATA");
            pLocationCreate.setName("Create Location");
            Permission savedLocationCreate = permissionRepository.save(pLocationCreate);

            Permission pLocationUpdate = new Permission();
            pLocationUpdate.setCode("LOCATION_UPDATE");
            pLocationUpdate.setModule("MASTER_DATA");
            pLocationUpdate.setName("Update Location");
            Permission savedLocationUpdate = permissionRepository.save(pLocationUpdate);

            Permission pLocationDelete = new Permission();
            pLocationDelete.setCode("LOCATION_DELETE");
            pLocationDelete.setModule("MASTER_DATA");
            pLocationDelete.setName("Delete Location");
            Permission savedLocationDelete = permissionRepository.save(pLocationDelete);

            // ─── MASTER_DATA (Suppliers) ─────────────────────────────────────
            Permission pSupplierView = new Permission();
            pSupplierView.setCode("SUPPLIER_VIEW");
            pSupplierView.setModule("MASTER_DATA");
            pSupplierView.setName("View Suppliers");
            Permission savedSupplierView = permissionRepository.save(pSupplierView);

            Permission pSupplierCreate = new Permission();
            pSupplierCreate.setCode("SUPPLIER_CREATE");
            pSupplierCreate.setModule("MASTER_DATA");
            pSupplierCreate.setName("Create Supplier");
            Permission savedSupplierCreate = permissionRepository.save(pSupplierCreate);

            Permission pSupplierUpdate = new Permission();
            pSupplierUpdate.setCode("SUPPLIER_UPDATE");
            pSupplierUpdate.setModule("MASTER_DATA");
            pSupplierUpdate.setName("Update Supplier");
            Permission savedSupplierUpdate = permissionRepository.save(pSupplierUpdate);

            Permission pSupplierDelete = new Permission();
            pSupplierDelete.setCode("SUPPLIER_DELETE");
            pSupplierDelete.setModule("MASTER_DATA");
            pSupplierDelete.setName("Delete Supplier");
            Permission savedSupplierDelete = permissionRepository.save(pSupplierDelete);

            Permission pSupplierLock = new Permission();
            pSupplierLock.setCode("SUPPLIER_LOCK");
            pSupplierLock.setModule("MASTER_DATA");
            pSupplierLock.setName("Lock/Unlock Supplier");
            Permission savedSupplierLock = permissionRepository.save(pSupplierLock);

            // ─── ROLES ───────────────────────────────────────────────────────
            Permission pRoleView = new Permission();
            pRoleView.setCode("ROLE_VIEW");
            pRoleView.setModule("ROLES");
            pRoleView.setName("View Roles");
            Permission savedRoleView = permissionRepository.save(pRoleView);

            Permission pRoleCreate = new Permission();
            pRoleCreate.setCode("ROLE_CREATE");
            pRoleCreate.setModule("ROLES");
            pRoleCreate.setName("Create Role");
            Permission savedRoleCreate = permissionRepository.save(pRoleCreate);

            Permission pRoleUpdate = new Permission();
            pRoleUpdate.setCode("ROLE_UPDATE");
            pRoleUpdate.setModule("ROLES");
            pRoleUpdate.setName("Update Role");
            Permission savedRoleUpdate = permissionRepository.save(pRoleUpdate);

            // ─── USERS ───────────────────────────────────────────────────────
            Permission pUserView = new Permission();
            pUserView.setCode("USER_VIEW");
            pUserView.setModule("USERS");
            pUserView.setName("View Users");
            Permission savedUserView = permissionRepository.save(pUserView);

            Permission pUserCreate = new Permission();
            pUserCreate.setCode("USER_CREATE");
            pUserCreate.setModule("USERS");
            pUserCreate.setName("Create User");
            Permission savedUserCreate = permissionRepository.save(pUserCreate);

            Permission pUserUpdate = new Permission();
            pUserUpdate.setCode("USER_UPDATE");
            pUserUpdate.setModule("USERS");
            pUserUpdate.setName("Update User");
            Permission savedUserUpdate = permissionRepository.save(pUserUpdate);

            Permission pUserLock = new Permission();
            pUserLock.setCode("USER_LOCK");
            pUserLock.setModule("USERS");
            pUserLock.setName("Lock/Unlock User");
            Permission savedUserLock = permissionRepository.save(pUserLock);

            // ─── INBOUND_RECEIPTS ────────────────────────────────────────────
            Permission pInboundCreate = new Permission();
            pInboundCreate.setCode("INBOUND_RECEIPT_CREATE");
            pInboundCreate.setModule("INBOUND_RECEIPTS");
            pInboundCreate.setName("Create Inbound Receipt");
            Permission savedInboundCreate = permissionRepository.save(pInboundCreate);

            Permission pInboundUpdate = new Permission();
            pInboundUpdate.setCode("INBOUND_RECEIPT_UPDATE");
            pInboundUpdate.setModule("INBOUND_RECEIPTS");
            pInboundUpdate.setName("Update Inbound Receipt");
            Permission savedInboundUpdate = permissionRepository.save(pInboundUpdate);

            Permission pInboundDelete = new Permission();
            pInboundDelete.setCode("INBOUND_RECEIPT_DELETE");
            pInboundDelete.setModule("INBOUND_RECEIPTS");
            pInboundDelete.setName("Delete Inbound Receipt");
            Permission savedInboundDelete = permissionRepository.save(pInboundDelete);

            Permission pInboundApprove = new Permission();
            pInboundApprove.setCode("INBOUND_RECEIPT_APPROVE");
            pInboundApprove.setModule("INBOUND_RECEIPTS");
            pInboundApprove.setName("Approve Inbound Receipt");
            Permission savedInboundApprove = permissionRepository.save(pInboundApprove);

            Permission pInboundDecline = new Permission();
            pInboundDecline.setCode("INBOUND_RECEIPT_DECLINE");
            pInboundDecline.setModule("INBOUND_RECEIPTS");
            pInboundDecline.setName("Decline Inbound Receipt");
            Permission savedInboundDecline = permissionRepository.save(pInboundDecline);

            // ─── OUTBOUND_RECEIPTS ───────────────────────────────────────────
            Permission pOutboundCreate = new Permission();
            pOutboundCreate.setCode("OUTBOUND_RECEIPT_CREATE");
            pOutboundCreate.setModule("OUTBOUND_RECEIPTS");
            pOutboundCreate.setName("Create Outbound Receipt");
            Permission savedOutboundCreate = permissionRepository.save(pOutboundCreate);

            Permission pOutboundUpdate = new Permission();
            pOutboundUpdate.setCode("OUTBOUND_RECEIPT_UPDATE");
            pOutboundUpdate.setModule("OUTBOUND_RECEIPTS");
            pOutboundUpdate.setName("Update Outbound Receipt");
            Permission savedOutboundUpdate = permissionRepository.save(pOutboundUpdate);

            Permission pOutboundDelete = new Permission();
            pOutboundDelete.setCode("OUTBOUND_RECEIPT_DELETE");
            pOutboundDelete.setModule("OUTBOUND_RECEIPTS");
            pOutboundDelete.setName("Delete Outbound Receipt");
            Permission savedOutboundDelete = permissionRepository.save(pOutboundDelete);

            Permission pOutboundApprove = new Permission();
            pOutboundApprove.setCode("OUTBOUND_RECEIPT_APPROVE");
            pOutboundApprove.setModule("OUTBOUND_RECEIPTS");
            pOutboundApprove.setName("Approve Outbound Receipt");
            Permission savedOutboundApprove = permissionRepository.save(pOutboundApprove);

            Permission pOutboundDecline = new Permission();
            pOutboundDecline.setCode("OUTBOUND_RECEIPT_DECLINE");
            pOutboundDecline.setModule("OUTBOUND_RECEIPTS");
            pOutboundDecline.setName("Decline Outbound Receipt");
            Permission savedOutboundDecline = permissionRepository.save(pOutboundDecline);

            // ─── INVENTORY ───────────────────────────────────────────────────
            Permission pInventoryView = new Permission();
            pInventoryView.setCode("INVENTORY_VIEW");
            pInventoryView.setModule("INVENTORY");
            pInventoryView.setName("General Inventory View");
            Permission savedInventoryView = permissionRepository.save(pInventoryView);

            Permission pInventoryDetail = new Permission();
            pInventoryDetail.setCode("INVENTORY_DETAIL");
            pInventoryDetail.setModule("INVENTORY");
            pInventoryDetail.setName("Section/Location Detailed View");
            Permission savedInventoryDetail = permissionRepository.save(pInventoryDetail);

            // ─── REPORTS ─────────────────────────────────────────────────────
            Permission pReportView = new Permission();
            pReportView.setCode("REPORT_VIEW");
            pReportView.setModule("REPORTS");
            pReportView.setName("View Reports");
            Permission savedReportView = permissionRepository.save(pReportView);

            // --- GREEN WAREHOUSE PERMISSIONS ---
            Permission pGreenAging = new Permission();
            pGreenAging.setCode("GREEN_AGING_VIEW");
            pGreenAging.setModule("GREEN_WAREHOUSE");
            pGreenAging.setName("View Inventory Aging");
            Permission savedGreenAging = permissionRepository.save(pGreenAging);

            Permission pGreenSlow = new Permission();
            pGreenSlow.setCode("GREEN_SLOW_VIEW");
            pGreenSlow.setModule("GREEN_WAREHOUSE");
            pGreenSlow.setName("View Slow Moving Items");
            Permission savedGreenSlow = permissionRepository.save(pGreenSlow);

            Permission pGreenExpiry = new Permission();
            pGreenExpiry.setCode("GREEN_EXPIRY_VIEW");
            pGreenExpiry.setModule("GREEN_WAREHOUSE");
            pGreenExpiry.setName("View Expiry Alerts");
            Permission savedGreenExpiry = permissionRepository.save(pGreenExpiry);

            Permission pGreenWaste = new Permission();
            pGreenWaste.setCode("GREEN_WASTE_VIEW");
            pGreenWaste.setModule("GREEN_WAREHOUSE");
            pGreenWaste.setName("View Waste Statistics");
            Permission savedGreenWaste = permissionRepository.save(pGreenWaste);

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
                    savedSupplierView, savedSupplierCreate, savedSupplierUpdate, savedSupplierDelete, savedSupplierLock,
                    savedInboundCreate, savedInboundUpdate, savedInboundDelete, savedInboundApprove, savedInboundDecline,
                    savedOutboundCreate, savedOutboundUpdate, savedOutboundDelete, savedOutboundApprove, savedOutboundDecline,
                    savedInventoryView, savedInventoryDetail, savedReportView,
                    savedGreenAging, savedGreenSlow, savedGreenExpiry, savedGreenWaste
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

            // ─── SAMPLE DATA ────────────────────────────────────────────────
            sampleDataSeeder.seed();
        };
    }
}
