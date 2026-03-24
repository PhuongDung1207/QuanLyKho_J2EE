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
            wh1.setCode("WH-GREEN-NORTH");
            wh1.setName("Green Warehouse North (Fresh Food)");
            wh1.setStatus("ACTIVE");
            Warehouse savedWh1 = warehouseRepository.save(wh1);

            Warehouse wh2 = new Warehouse();
            wh2.setCode("WH-GREEN-SOUTH");
            wh2.setName("Green Warehouse South (Dairy & Bakery)");
            wh2.setStatus("ACTIVE");
            Warehouse savedWh2 = warehouseRepository.save(wh2);

            Category catFood = new Category();
            catFood.setCode("FRESH_FOOD");
            catFood.setName("Fresh Food");
            Category savedCatFood = categoryRepository.save(catFood);

            Category catDairy = new Category();
            catDairy.setCode("DAIRY_BAKERY");
            catDairy.setName("Dairy & Bakery");
            Category savedCatDairy = categoryRepository.save(catDairy);

            Uom uomKg = new Uom();
            uomKg.setCode("KG");
            uomKg.setName("Kilograms");
            Uom savedUomKg = uomRepository.save(uomKg);

            Uom uomPack = new Uom();
            uomPack.setCode("PACK");
            uomPack.setName("Pack");
            Uom savedUomPack = uomRepository.save(uomPack);

            // --- NORTH WAREHOUSE PRODUCTS (Fresh Food) ---
            Product prod1 = createProduct(productRepository, "VEG-001", "Organic Spinach", savedCatFood.getId(), savedUomKg.getId());
            Product prod2 = createProduct(productRepository, "VEG-002", "Red Tomatoes", savedCatFood.getId(), savedUomKg.getId());
            Product prod3 = createProduct(productRepository, "FRT-001", "Fresh Bananas", savedCatFood.getId(), savedUomKg.getId());
            Product prod4 = createProduct(productRepository, "FRT-002", "Sweet Grapes", savedCatFood.getId(), savedUomKg.getId());
            Product prod5 = createProduct(productRepository, "VEG-003", "Broccoli", savedCatFood.getId(), savedUomKg.getId());

            // --- SOUTH WAREHOUSE PRODUCTS (Dairy & Bakery) ---
            Product prod6 = createProduct(productRepository, "DRY-001", "Whole Milk 1L", savedCatDairy.getId(), savedUomPack.getId());
            Product prod7 = createProduct(productRepository, "DRY-002", "Greek Yogurt", savedCatDairy.getId(), savedUomPack.getId());
            Product prod8 = createProduct(productRepository, "BKY-001", "Whole Wheat Bread", savedCatDairy.getId(), savedUomPack.getId());
            Product prod9 = createProduct(productRepository, "BKY-002", "Butter Croissants", savedCatDairy.getId(), savedUomPack.getId());
            Product prod10 = createProduct(productRepository, "DRY-003", "Cheddar Cheese", savedCatDairy.getId(), savedUomPack.getId());

            Location loc1 = createLocation(locationRepository, savedWh1.getId(), "N1-VEG-01", "PICKING");
            Location loc2 = createLocation(locationRepository, savedWh2.getId(), "S1-DRY-01", "STORAGE");

            // --- NORTH WAREHOUSE INVENTORY (Fresh Food - Fast Aging) ---
            // Aging: Spinach (3 days old), Last out: 1 day ago
            createInventory(inventoryBalanceRepository, prod1, savedWh1, loc1, "15.5", 3, 2, 1); 
            // Aging: Tomatoes (5 days old), Last out: 8 days ago (Slow Moving)
            createInventory(inventoryBalanceRepository, prod2, savedWh1, loc1, "42.0", 5, 1, 8);
            // Expiring: Bananas (Expires in 1 day), Last out: 2 days ago
            createInventory(inventoryBalanceRepository, prod3, savedWh1, loc1, "25.8", 4, 1, 2);
            // Slow Moving: Broccoli (No movement for 12 days)
            createInventory(inventoryBalanceRepository, prod5, savedWh1, loc1, "12.4", 15, 10, 12);

            // --- SOUTH WAREHOUSE INVENTORY (Dairy & Bakery) ---
            // Expired: Milk (Expired 2 days ago), Last out: 5 days ago
            createInventory(inventoryBalanceRepository, prod6, savedWh2, loc2, "100.0", 10, -2, 5);
            // Expiring Soon: Bread (Expires tomorrow), Last out: Never (null)
            createInventory(inventoryBalanceRepository, prod8, savedWh2, loc2, "45.5", 1, 1, null);
            // Healthy Stock: Cheese, Last out: 10 days ago (Slow Moving for food)
            createInventory(inventoryBalanceRepository, prod10, savedWh2, loc2, "30.2", 15, 30, 10);

            // --- WASTE DATA ---
            createWaste(outboundIssueRepository, outboundIssueLineRepository, savedWh1, prod4, savedUomKg, "5.2", "SPOILAGE", 2);
            createWaste(outboundIssueRepository, outboundIssueLineRepository, savedWh2, prod9, savedUomPack, "3.0", "EXPIRED", 1);
        };
    }

    private Product createProduct(ProductRepository repo, String sku, String name, UUID catId, UUID uomId) {
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setCategoryId(catId);
        p.setBaseUomId(uomId);
        p.setStatus("ACTIVE");
        return repo.save(p);
    }

    private Location createLocation(LocationRepository repo, UUID whId, String code, String type) {
        Location l = new Location();
        l.setWarehouseId(whId);
        l.setCode(code);
        l.setType(type);
        return repo.save(l);
    }

    private void createInventory(InventoryBalanceRepository repo, Product p, Warehouse wh, Location loc, String qty, int daysAgoInbound, int daysToExpiry) {
        createInventory(repo, p, wh, loc, qty, daysAgoInbound, daysToExpiry, null);
    }

    private void createInventory(InventoryBalanceRepository repo, Product p, Warehouse wh, Location loc, String qty, int daysAgoInbound, int daysToExpiry, Integer daysAgoOutbound) {
        InventoryBalance bal = new InventoryBalance();
        bal.setProductId(p.getId());
        bal.setWarehouseId(wh.getId());
        bal.setLocationId(loc.getId());
        bal.setQtyOnHand(new BigDecimal(qty));
        bal.setLastInboundDate(OffsetDateTime.now().minusDays(daysAgoInbound));
        bal.setExpDate(LocalDate.now().plusDays(daysToExpiry));
        bal.setMfgDate(LocalDate.now().minusDays(daysAgoInbound + 2));
        if (daysAgoOutbound != null) {
            bal.setLastOutboundDate(OffsetDateTime.now().minusDays(daysAgoOutbound));
        }
        repo.save(bal);
    }

    private void createWaste(OutboundIssueRepository outRepo, OutboundIssueLineRepository lineRepo, Warehouse wh, Product p, Uom uom, String qty, String reason, int daysAgo) {
        OutboundIssue waste = new OutboundIssue();
        waste.setCode(reason.substring(0, 3) + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        waste.setType("WASTE");
        waste.setWarehouseId(wh.getId());
        waste.setStatus(OutboundIssueStatus.COMPLETED);
        waste.setCreatedAt(OffsetDateTime.now().minusDays(daysAgo));
        waste.setCompletedAt(OffsetDateTime.now().minusDays(daysAgo));
        OutboundIssue saved = outRepo.save(waste);

        OutboundIssueLine line = new OutboundIssueLine();
        line.setIssueId(saved.getId());
        line.setProductId(p.getId());
        line.setUomId(uom.getId());
        line.setQuantity(new BigDecimal(qty));
        lineRepo.save(line);
    }
}
