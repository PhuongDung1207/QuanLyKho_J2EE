package com.example.JavaQuanLyKho.config;

import com.example.JavaQuanLyKho.model.entity.*;
import com.example.JavaQuanLyKho.repository.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class SampleDataSeeder {

    private final WarehouseRepository warehouseRepository;
    private final CategoryRepository categoryRepository;
    private final UomRepository uomRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final OutboundIssueRepository outboundIssueRepository;
    private final OutboundIssueLineRepository outboundIssueLineRepository;

    public SampleDataSeeder(
            WarehouseRepository warehouseRepository,
            CategoryRepository categoryRepository,
            UomRepository uomRepository,
            ProductRepository productRepository,
            LocationRepository locationRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            OutboundIssueRepository outboundIssueRepository,
            OutboundIssueLineRepository outboundIssueLineRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
        this.uomRepository = uomRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.outboundIssueRepository = outboundIssueRepository;
        this.outboundIssueLineRepository = outboundIssueLineRepository;
    }

    public void seed() {
        // ─── WAREHOUSES ──────────────────────────────────────────────────
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

        // ─── CATEGORIES ──────────────────────────────────────────────────
        Category catFood = new Category();
        catFood.setCode("FRESH_FOOD");
        catFood.setName("Fresh Food");
        Category savedCatFood = categoryRepository.save(catFood);

        Category catDairy = new Category();
        catDairy.setCode("DAIRY_BAKERY");
        catDairy.setName("Dairy & Bakery");
        Category savedCatDairy = categoryRepository.save(catDairy);

        // ─── UOMs ────────────────────────────────────────────────────────
        Uom uomKg = new Uom();
        uomKg.setCode("KG");
        uomKg.setName("Kilograms");
        Uom savedUomKg = uomRepository.save(uomKg);

        Uom uomPack = new Uom();
        uomPack.setCode("PACK");
        uomPack.setName("Pack");
        Uom savedUomPack = uomRepository.save(uomPack);

        // ─── PRODUCTS ────────────────────────────────────────────────────
        Product prod1  = createProduct("VEG-001", "Organic Spinach",      savedCatFood.getId(),  savedUomKg.getId());
        Product prod2  = createProduct("VEG-002", "Red Tomatoes",          savedCatFood.getId(),  savedUomKg.getId());
        Product prod3  = createProduct("FRT-001", "Fresh Bananas",         savedCatFood.getId(),  savedUomKg.getId());
        Product prod4  = createProduct("FRT-002", "Sweet Grapes",          savedCatFood.getId(),  savedUomKg.getId());
        Product prod5  = createProduct("VEG-003", "Broccoli",              savedCatFood.getId(),  savedUomKg.getId());
        Product prod6  = createProduct("DRY-001", "Whole Milk 1L",         savedCatDairy.getId(), savedUomPack.getId());
        createProduct("DRY-002", "Greek Yogurt",                           savedCatDairy.getId(), savedUomPack.getId());
        Product prod8  = createProduct("BKY-001", "Whole Wheat Bread",     savedCatDairy.getId(), savedUomPack.getId());
        Product prod9  = createProduct("BKY-002", "Butter Croissants",     savedCatDairy.getId(), savedUomPack.getId());
        Product prod10 = createProduct("DRY-003", "Cheddar Cheese",        savedCatDairy.getId(), savedUomPack.getId());

        // ─── LOCATIONS ───────────────────────────────────────────────────
        Location loc1 = createLocation(savedWh1.getId(), "N1-VEG-01", "PICKING");
        Location loc2 = createLocation(savedWh2.getId(), "S1-DRY-01", "STORAGE");

        // ─── INVENTORY ───────────────────────────────────────────────────
        // North – Fresh Food (fast aging)
        createInventory(prod1, savedWh1, loc1, "15.5",  3,  2,  1);   // Spinach: 3 days old, last out 1d ago
        createInventory(prod2, savedWh1, loc1, "42.0",  5,  1,  8);   // Tomatoes: 5d old, slow moving (8d no out)
        createInventory(prod3, savedWh1, loc1, "25.8",  4,  1,  2);   // Bananas: expires in 1d
        createInventory(prod5, savedWh1, loc1, "12.4", 15, 10, 12);   // Broccoli: slow moving (12d no out)

        // South – Dairy & Bakery
        createInventory(prod6, savedWh2, loc2, "100.0", 10, -2,  5);  // Milk: expired 2d ago
        createInventory(prod8, savedWh2, loc2, "45.5",   1,  1, null); // Bread: expires tomorrow, never out
        createInventory(prod10, savedWh2, loc2, "30.2", 15, 30, 10);  // Cheese: slow moving (10d no out)

        // ─── WASTE ───────────────────────────────────────────────────────
        createWaste(savedWh1, prod4, savedUomKg,   "5.2", "SPOILAGE", 2);
        createWaste(savedWh2, prod9, savedUomPack, "3.0", "EXPIRED",  1);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────

    private Product createProduct(String sku, String name, UUID catId, UUID uomId) {
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setCategoryId(catId);
        p.setBaseUomId(uomId);
        p.setStatus("ACTIVE");
        return productRepository.save(p);
    }

    private Location createLocation(UUID whId, String code, String type) {
        Location l = new Location();
        l.setWarehouseId(whId);
        l.setCode(code);
        l.setType(type);
        return locationRepository.save(l);
    }

    private void createInventory(Product p, Warehouse wh, Location loc,
                                 String qty, int daysAgoInbound,
                                 int daysToExpiry, Integer daysAgoOutbound) {
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
        inventoryBalanceRepository.save(bal);
    }

    private void createWaste(Warehouse wh, Product p, Uom uom,
                              String qty, String reason, int daysAgo) {
        OutboundIssue waste = new OutboundIssue();
        waste.setCode(reason.substring(0, 3) + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        waste.setType("WASTE");
        waste.setWarehouseId(wh.getId());
        waste.setStatus(OutboundIssueStatus.COMPLETED);
        waste.setCreatedAt(OffsetDateTime.now().minusDays(daysAgo));
        waste.setCompletedAt(OffsetDateTime.now().minusDays(daysAgo));
        OutboundIssue saved = outboundIssueRepository.save(waste);

        OutboundIssueLine line = new OutboundIssueLine();
        line.setIssueId(saved.getId());
        line.setProductId(p.getId());
        line.setUomId(uom.getId());
        line.setQuantity(new BigDecimal(qty));
        outboundIssueLineRepository.save(line);
    }
}
