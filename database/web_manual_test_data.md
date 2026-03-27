# Web Manual Test Data

Use this order in the web UI:

1. UOMs
2. Categories
3. Suppliers
4. Warehouses
5. Sections
6. Products

Notes:

- Product form needs an existing sub-category and UOM.
- Section form needs an existing warehouse selected first.
- Category form: create root categories first, then sub-categories.
- Supplier and warehouse codes should stay uppercase.

## UOMs

Form order: `Code | Name`

```text
CASE | Case
TRAY | Tray
CAN | Can
BAG | Bag
CUP | Cup
JAR | Jar
ROLL | Roll
SET | Set
TIN | Tin
TUBE | Tube
PACK | Pack
BOX | Box
BOTTLE | Bottle
```

## Categories

Form order: `Code | Name | Parent Category`

Create root categories first:

```text
SNACKS | Snacks | (none)
HOUSEHOLD | Household | (none)
PERSONAL_CARE | Personal Care | (none)
STATIONERY | Stationery | (none)
CANNED_GOODS | Canned Goods | (none)
```

Then create sub-categories:

```text
CHIPS | Chips | SNACKS
BISCUITS | Biscuits | SNACKS
INSTANT_NOODLES | Instant Noodles | SNACKS
CLEANING | Cleaning Supplies | HOUSEHOLD
TISSUE_PAPER | Tissue Paper | HOUSEHOLD
SOAP_BODYWASH | Soap and Body Wash | PERSONAL_CARE
SHAMPOO | Shampoo | PERSONAL_CARE
NOTEBOOKS | Notebooks | STATIONERY
WRITING_TOOLS | Writing Tools | STATIONERY
CANNED_FISH | Canned Fish | CANNED_GOODS
CANNED_MEAT | Canned Meat | CANNED_GOODS
```

## Suppliers

Form order:
`Code | Supplier Name | Contact Person | Contact Email | Contact Phone | Address | Status`

```text
SUP-SNACK-MART | Snack Mart Distribution | Huy Tran | huy@snackmart.vn | 0905111001 | 120 Nguyen Van Linh, Da Nang | ACTIVE
SUP-HOUSE-CARE | House Care Trading | Mai Le | mai@housecare.vn | 0905111002 | 88 Le Van Viet, Thu Duc, HCM | ACTIVE
SUP-PURE-LIFE | Pure Life Consumer Goods | An Bui | an@purelifegoods.vn | 0905111003 | 15 Tran Hung Dao, Hai Chau, Da Nang | ACTIVE
SUP-OFFICE-HUB | Office Hub Supply | Quang Pham | quang@officehub.vn | 0905111004 | 210 Cach Mang Thang 8, District 10, HCM | UNDER_REVIEW
SUP-SEA-FOOD | Sea Food Canning Co | Vy Nguyen | vy@seafoodcanning.vn | 0905111005 | 42 Vo Nguyen Giap, Son Tra, Da Nang | ACTIVE
SUP-LEGACY | Legacy Wholesale | Dung Ho | dung@legacywholesale.vn | 0905111006 | 6 Tran Phu, Nha Trang | INACTIVE
```

## Warehouses

Form order: `Code | Status | Name | Capacity`

```text
WH-HCM-DRY | ACTIVE | HCM Dry Storage Warehouse | 350
WH-HCM-RETAIL | ACTIVE | HCM Retail Fulfillment Hub | 220
WH-DN-CONSUMER | ACTIVE | Da Nang Consumer Goods Warehouse | 300
WH-BD-RESERVE | ACTIVE | Binh Duong Reserve Warehouse | 500
WH-CT-TRANSIT | MAINTENANCE | Can Tho Transit Warehouse | 180
WH-QN-OLD | INACTIVE | Quang Nam Legacy Warehouse | 140
```

## Sections

Form order: `Section Code | Type`

Create sections under `WH-HCM-DRY`:

```text
D1-A01 | STORAGE
D1-A02 | STORAGE
D1-B01 | PICKING
D1-B02 | PICKING
D1-RCV | RECEIVING
D1-QC | QC
```

Create sections under `WH-HCM-RETAIL`:

```text
R1-A01 | PICKING
R1-A02 | PICKING
R1-B01 | PACKING
R1-B02 | PACKING
R1-RCV | RECEIVING
R1-RTN | RETURNS
```

Create sections under `WH-DN-CONSUMER`:

```text
DN-A01 | STORAGE
DN-A02 | STORAGE
DN-B01 | PICKING
DN-B02 | PICKING
DN-RCV | RECEIVING
DN-QC | QC
```

Create sections under `WH-BD-RESERVE`:

```text
BD-BULK-01 | BULK
BD-BULK-02 | BULK
BD-RES-01 | RESERVE
BD-RES-02 | RESERVE
BD-RCV | RECEIVING
```

Create sections under `WH-CT-TRANSIT`:

```text
CT-TMP-01 | TRANSIT
CT-TMP-02 | TRANSIT
CT-RCV | RECEIVING
CT-SHP | SHIPPING
```

## Products

Form order:
`SKU | Barcode | Product Name | Sub-Category | Base UOM`

Use the sub-category and UOM names/codes already created above when selecting from dropdowns.

```text
SNK-001 | 893860100001 | Sea Salt Crackers | BISCUITS | CASE
SNK-002 | 893860100002 | Butter Cookies Tin | BISCUITS | TIN
SNK-003 | 893860100003 | Spicy Potato Chips | CHIPS | BAG
SNK-004 | 893860100004 | Sour Cream Chips | CHIPS | BAG
SNK-005 | 893860100005 | Instant Noodles Beef | INSTANT_NOODLES | CASE
SNK-006 | 893860100006 | Instant Noodles Seafood | INSTANT_NOODLES | CASE
HOU-001 | 893860100007 | Laundry Detergent 3L | CLEANING | JAR
HOU-002 | 893860100008 | Floor Cleaner Lemon | CLEANING | BOTTLE
HOU-003 | 893860100009 | Kitchen Tissue Deluxe | TISSUE_PAPER | ROLL
HOU-004 | 893860100010 | Facial Tissue Soft Pack | TISSUE_PAPER | PACK
PER-001 | 893860100011 | Herbal Shampoo 650ml | SHAMPOO | BOTTLE
PER-002 | 893860100012 | Anti Dandruff Shampoo | SHAMPOO | BOTTLE
PER-003 | 893860100013 | Moisturizing Body Wash | SOAP_BODYWASH | BOTTLE
PER-004 | 893860100014 | Antibacterial Hand Soap | SOAP_BODYWASH | BOTTLE
STA-001 | 893860100015 | A5 Student Notebook | NOTEBOOKS | PACK
STA-002 | 893860100016 | Spiral Notebook B5 | NOTEBOOKS | PACK
STA-003 | 893860100017 | Blue Ballpoint Pen | WRITING_TOOLS | BOX
STA-004 | 893860100018 | Black Marker Pen | WRITING_TOOLS | BOX
CAN-001 | 893860100019 | Tuna in Brine | CANNED_FISH | CAN
CAN-002 | 893860100020 | Sardines Tomato Sauce | CANNED_FISH | CAN
CAN-003 | 893860100021 | Luncheon Meat Classic | CANNED_MEAT | CAN
CAN-004 | 893860100022 | Chicken Ham Canned | CANNED_MEAT | CAN
```

## Quick Small Dataset

If you only want a very fast smoke test on web, use this minimum set:

```text
UOM:
CASE | Case
BAG | Bag

Categories:
SNACKS | Snacks | (none)
CHIPS | Chips | SNACKS

Supplier:
SUP-SNACK-MART | Snack Mart Distribution | Huy Tran | huy@snackmart.vn | 0905111001 | 120 Nguyen Van Linh, Da Nang | ACTIVE

Warehouse:
WH-HCM-DRY | ACTIVE | HCM Dry Storage Warehouse | 350

Sections:
D1-A01 | STORAGE
D1-B01 | PICKING

Products:
SNK-003 | 893860100003 | Spicy Potato Chips | CHIPS | BAG
```
