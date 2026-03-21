package com.example.JavaQuanLyKho.config;

import com.example.JavaQuanLyKho.model.entity.*;
import com.example.JavaQuanLyKho.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            WarehouseRepository warehouseRepository,
            CategoryRepository categoryRepository,
            UomRepository uomRepository,
            ProductRepository productRepository,
            LocationRepository locationRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            OutboundIssueRepository outboundIssueRepository,
            OutboundIssueLineRepository outboundIssueLineRepository,
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
            Warehouse wh1 = new Warehouse();
            wh1.setCode("WH-NORTH");
            wh1.setName("North Distribution Center");
            wh1.setStatus("ACTIVE");
            Warehouse savedWh1 = warehouseRepository.save(wh1);

            Warehouse wh2 = new Warehouse();
            wh2.setCode("WH-SOUTH");
            wh2.setName("South Fulfillment Hub");
            wh2.setStatus("ACTIVE");
            Warehouse savedWh2 = warehouseRepository.save(wh2);

            Category cat1 = new Category();
            cat1.setCode("ELECTRONICS");
            cat1.setName("Electronics");
            Category savedCat1 = categoryRepository.save(cat1);

            Category cat2 = new Category();
            cat2.setCode("FOOD");
            cat2.setName("Food & Beverage");
            Category savedCat2 = categoryRepository.save(cat2);

            Uom uom1 = new Uom();
            uom1.setCode("PCS");
            uom1.setName("Pieces");
            Uom savedUom1 = uomRepository.save(uom1);

            Uom uom2 = new Uom();
            uom2.setCode("KG");
            uom2.setName("Kilograms");
            Uom savedUom2 = uomRepository.save(uom2);

            Product prod1 = new Product();
            prod1.setSku("LAP-DELL-XPS");
            prod1.setName("Dell XPS 15 Laptop");
            prod1.setCategoryId(savedCat1.getId());
            prod1.setBaseUomId(savedUom1.getId());
            prod1.setStatus("ACTIVE");
            Product savedProd1 = productRepository.save(prod1);

            Product prod2 = new Product();
            prod2.setSku("IPH-15-PRO");
            prod2.setName("iPhone 15 Pro");
            prod2.setCategoryId(savedCat1.getId());
            prod2.setBaseUomId(savedUom1.getId());
            prod2.setStatus("ACTIVE");
            Product savedProd2 = productRepository.save(prod2);

            Product prod3 = new Product();
            prod3.setSku("MILK-VINAMILK");
            prod3.setName("Vinamilk Fresh Milk 1L");
            prod3.setCategoryId(savedCat2.getId());
            prod3.setBaseUomId(savedUom1.getId());
            prod3.setStatus("ACTIVE");
            Product savedProd3 = productRepository.save(prod3);

            Location loc1 = new Location();
            loc1.setWarehouseId(savedWh1.getId());
            loc1.setCode("A1-01-01");
            loc1.setType("PICKING");
            Location savedLoc1 = locationRepository.save(loc1);

            Location loc2 = new Location();
            loc2.setWarehouseId(savedWh2.getId());
            loc2.setCode("S1-05-02");
            loc2.setType("STORAGE");
            Location savedLoc2 = locationRepository.save(loc2);

            // --- NORTH WAREHOUSE DATA ---
            // Aging Data (95 days ago)
            InventoryBalance bal1 = new InventoryBalance();
            bal1.setProductId(savedProd1.getId());
            bal1.setWarehouseId(savedWh1.getId());
            bal1.setLocationId(savedLoc1.getId());
            bal1.setQtyOnHand(new BigDecimal("50"));
            bal1.setLastInboundDate(OffsetDateTime.now().minusDays(95));
            bal1.setExpiryDate(LocalDate.now().plusDays(10)); // Expiring soon
            inventoryBalanceRepository.save(bal1);

            // Slow Moving Data (No outbound for 100 days)
            InventoryBalance bal2 = new InventoryBalance();
            bal2.setProductId(savedProd2.getId());
            bal2.setWarehouseId(savedWh1.getId());
            bal2.setLocationId(savedLoc1.getId());
            bal2.setQtyOnHand(new BigDecimal("120"));
            bal2.setLastInboundDate(OffsetDateTime.now().minusDays(110));
            bal2.setLastOutboundDate(OffsetDateTime.now().minusDays(100));
            bal2.setExpiryDate(LocalDate.now().plusMonths(6));
            inventoryBalanceRepository.save(bal2);

            // --- SOUTH WAREHOUSE DATA ---
            // Expired Data
            InventoryBalance bal3 = new InventoryBalance();
            bal3.setProductId(savedProd3.getId());
            bal3.setWarehouseId(savedWh2.getId());
            bal3.setLocationId(savedLoc2.getId());
            bal3.setQtyOnHand(new BigDecimal("200"));
            bal3.setLastInboundDate(OffsetDateTime.now().minusDays(40));
            bal3.setExpiryDate(LocalDate.now().minusDays(5)); // Already expired
            inventoryBalanceRepository.save(bal3);

            // Aging Data (15 days ago - New Stock)
            InventoryBalance bal4 = new InventoryBalance();
            bal4.setProductId(savedProd1.getId());
            bal4.setWarehouseId(savedWh2.getId());
            bal4.setLocationId(savedLoc2.getId());
            bal4.setQtyOnHand(new BigDecimal("30"));
            bal4.setLastInboundDate(OffsetDateTime.now().minusDays(15));
            bal4.setExpiryDate(LocalDate.now().plusYears(1));
            inventoryBalanceRepository.save(bal4);

            // --- WASTE STATISTICS DATA ---
            // North Warehouse Waste
            OutboundIssue waste1 = new OutboundIssue();
            waste1.setCode("WST-001");
            waste1.setType("WASTE");
            waste1.setWarehouseId(savedWh1.getId());
            waste1.setStatus(OutboundIssueStatus.COMPLETED);
            waste1.setCreatedAt(OffsetDateTime.now().minusDays(10));
            waste1.setCompletedAt(OffsetDateTime.now().minusDays(10));
            OutboundIssue savedWaste1 = outboundIssueRepository.save(waste1);

            OutboundIssueLine line1 = new OutboundIssueLine();
            line1.setIssueId(savedWaste1.getId());
            line1.setProductId(savedProd3.getId());
            line1.setUomId(savedUom1.getId());
            line1.setQuantity(new BigDecimal("15"));
            outboundIssueLineRepository.save(line1);

            // South Warehouse Damaged
            OutboundIssue waste2 = new OutboundIssue();
            waste2.setCode("DMG-001");
            waste2.setType("DAMAGED");
            waste2.setWarehouseId(savedWh2.getId());
            waste2.setStatus(OutboundIssueStatus.COMPLETED);
            waste2.setCreatedAt(OffsetDateTime.now().minusDays(2));
            waste2.setCompletedAt(OffsetDateTime.now().minusDays(2));
            OutboundIssue savedWaste2 = outboundIssueRepository.save(waste2);

            OutboundIssueLine line2 = new OutboundIssueLine();
            line2.setIssueId(savedWaste2.getId());
            line2.setProductId(savedProd1.getId());
            line2.setUomId(savedUom1.getId());
            line2.setQuantity(new BigDecimal("2"));
            outboundIssueLineRepository.save(line2);
        };
    }
}
