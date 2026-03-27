from __future__ import annotations

from datetime import datetime, timezone
from pathlib import Path
from zipfile import ZIP_DEFLATED, ZipFile
from xml.sax.saxutils import escape


ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "database" / "web_manual_test_data.xlsx"


README_ROWS = [
    ["Step", "Module", "Notes"],
    ["1", "UOMs", "Create units first so product dropdowns have values."],
    ["2", "Categories", "Create root categories first, then sub-categories."],
    ["3", "Suppliers", "Use uppercase codes for consistency."],
    ["4", "Warehouses", "Status values should be ACTIVE, INACTIVE, or MAINTENANCE."],
    ["5", "Sections", "Open /sections and select a warehouse before adding sections."],
    ["6", "Products", "Products require an existing sub-category and base UOM."],
]


UOM_ROWS = [
    ["Code", "Name"],
    ["CASE", "Case"],
    ["TRAY", "Tray"],
    ["CAN", "Can"],
    ["BAG", "Bag"],
    ["CUP", "Cup"],
    ["JAR", "Jar"],
    ["ROLL", "Roll"],
    ["SET", "Set"],
    ["TIN", "Tin"],
    ["TUBE", "Tube"],
    ["PACK", "Pack"],
    ["BOX", "Box"],
    ["BOTTLE", "Bottle"],
]


CATEGORY_ROWS = [
    ["Code", "Name", "Parent Code"],
    ["SNACKS", "Snacks", ""],
    ["HOUSEHOLD", "Household", ""],
    ["PERSONAL_CARE", "Personal Care", ""],
    ["STATIONERY", "Stationery", ""],
    ["CANNED_GOODS", "Canned Goods", ""],
    ["CHIPS", "Chips", "SNACKS"],
    ["BISCUITS", "Biscuits", "SNACKS"],
    ["INSTANT_NOODLES", "Instant Noodles", "SNACKS"],
    ["CLEANING", "Cleaning Supplies", "HOUSEHOLD"],
    ["TISSUE_PAPER", "Tissue Paper", "HOUSEHOLD"],
    ["SOAP_BODYWASH", "Soap and Body Wash", "PERSONAL_CARE"],
    ["SHAMPOO", "Shampoo", "PERSONAL_CARE"],
    ["NOTEBOOKS", "Notebooks", "STATIONERY"],
    ["WRITING_TOOLS", "Writing Tools", "STATIONERY"],
    ["CANNED_FISH", "Canned Fish", "CANNED_GOODS"],
    ["CANNED_MEAT", "Canned Meat", "CANNED_GOODS"],
]


SUPPLIER_ROWS = [
    ["Code", "Supplier Name", "Contact Person", "Contact Email", "Contact Phone", "Address", "Status"],
    ["SUP-SNACK-MART", "Snack Mart Distribution", "Huy Tran", "huy@snackmart.vn", "0905111001", "120 Nguyen Van Linh, Da Nang", "ACTIVE"],
    ["SUP-HOUSE-CARE", "House Care Trading", "Mai Le", "mai@housecare.vn", "0905111002", "88 Le Van Viet, Thu Duc, HCM", "ACTIVE"],
    ["SUP-PURE-LIFE", "Pure Life Consumer Goods", "An Bui", "an@purelifegoods.vn", "0905111003", "15 Tran Hung Dao, Hai Chau, Da Nang", "ACTIVE"],
    ["SUP-OFFICE-HUB", "Office Hub Supply", "Quang Pham", "quang@officehub.vn", "0905111004", "210 Cach Mang Thang 8, District 10, HCM", "UNDER_REVIEW"],
    ["SUP-SEA-FOOD", "Sea Food Canning Co", "Vy Nguyen", "vy@seafoodcanning.vn", "0905111005", "42 Vo Nguyen Giap, Son Tra, Da Nang", "ACTIVE"],
    ["SUP-LEGACY", "Legacy Wholesale", "Dung Ho", "dung@legacywholesale.vn", "0905111006", "6 Tran Phu, Nha Trang", "INACTIVE"],
]


WAREHOUSE_ROWS = [
    ["Code", "Status", "Name", "Capacity"],
    ["WH-HCM-DRY", "ACTIVE", "HCM Dry Storage Warehouse", "350"],
    ["WH-HCM-RETAIL", "ACTIVE", "HCM Retail Fulfillment Hub", "220"],
    ["WH-DN-CONSUMER", "ACTIVE", "Da Nang Consumer Goods Warehouse", "300"],
    ["WH-BD-RESERVE", "ACTIVE", "Binh Duong Reserve Warehouse", "500"],
    ["WH-CT-TRANSIT", "MAINTENANCE", "Can Tho Transit Warehouse", "180"],
    ["WH-QN-OLD", "INACTIVE", "Quang Nam Legacy Warehouse", "140"],
]


SECTION_ROWS = [
    ["Warehouse Code", "Section Code", "Type"],
    ["WH-HCM-DRY", "D1-A01", "STORAGE"],
    ["WH-HCM-DRY", "D1-A02", "STORAGE"],
    ["WH-HCM-DRY", "D1-B01", "PICKING"],
    ["WH-HCM-DRY", "D1-B02", "PICKING"],
    ["WH-HCM-DRY", "D1-RCV", "RECEIVING"],
    ["WH-HCM-DRY", "D1-QC", "QC"],
    ["WH-HCM-RETAIL", "R1-A01", "PICKING"],
    ["WH-HCM-RETAIL", "R1-A02", "PICKING"],
    ["WH-HCM-RETAIL", "R1-B01", "PACKING"],
    ["WH-HCM-RETAIL", "R1-B02", "PACKING"],
    ["WH-HCM-RETAIL", "R1-RCV", "RECEIVING"],
    ["WH-HCM-RETAIL", "R1-RTN", "RETURNS"],
    ["WH-DN-CONSUMER", "DN-A01", "STORAGE"],
    ["WH-DN-CONSUMER", "DN-A02", "STORAGE"],
    ["WH-DN-CONSUMER", "DN-B01", "PICKING"],
    ["WH-DN-CONSUMER", "DN-B02", "PICKING"],
    ["WH-DN-CONSUMER", "DN-RCV", "RECEIVING"],
    ["WH-DN-CONSUMER", "DN-QC", "QC"],
    ["WH-BD-RESERVE", "BD-BULK-01", "BULK"],
    ["WH-BD-RESERVE", "BD-BULK-02", "BULK"],
    ["WH-BD-RESERVE", "BD-RES-01", "RESERVE"],
    ["WH-BD-RESERVE", "BD-RES-02", "RESERVE"],
    ["WH-BD-RESERVE", "BD-RCV", "RECEIVING"],
    ["WH-CT-TRANSIT", "CT-TMP-01", "TRANSIT"],
    ["WH-CT-TRANSIT", "CT-TMP-02", "TRANSIT"],
    ["WH-CT-TRANSIT", "CT-RCV", "RECEIVING"],
    ["WH-CT-TRANSIT", "CT-SHP", "SHIPPING"],
]


PRODUCT_ROWS = [
    ["SKU", "Barcode", "Product Name", "Sub-Category Code", "Base UOM Code"],
    ["SNK-001", "893860100001", "Sea Salt Crackers", "BISCUITS", "CASE"],
    ["SNK-002", "893860100002", "Butter Cookies Tin", "BISCUITS", "TIN"],
    ["SNK-003", "893860100003", "Spicy Potato Chips", "CHIPS", "BAG"],
    ["SNK-004", "893860100004", "Sour Cream Chips", "CHIPS", "BAG"],
    ["SNK-005", "893860100005", "Instant Noodles Beef", "INSTANT_NOODLES", "CASE"],
    ["SNK-006", "893860100006", "Instant Noodles Seafood", "INSTANT_NOODLES", "CASE"],
    ["HOU-001", "893860100007", "Laundry Detergent 3L", "CLEANING", "JAR"],
    ["HOU-002", "893860100008", "Floor Cleaner Lemon", "CLEANING", "BOTTLE"],
    ["HOU-003", "893860100009", "Kitchen Tissue Deluxe", "TISSUE_PAPER", "ROLL"],
    ["HOU-004", "893860100010", "Facial Tissue Soft Pack", "TISSUE_PAPER", "PACK"],
    ["PER-001", "893860100011", "Herbal Shampoo 650ml", "SHAMPOO", "BOTTLE"],
    ["PER-002", "893860100012", "Anti Dandruff Shampoo", "SHAMPOO", "BOTTLE"],
    ["PER-003", "893860100013", "Moisturizing Body Wash", "SOAP_BODYWASH", "BOTTLE"],
    ["PER-004", "893860100014", "Antibacterial Hand Soap", "SOAP_BODYWASH", "BOTTLE"],
    ["STA-001", "893860100015", "A5 Student Notebook", "NOTEBOOKS", "PACK"],
    ["STA-002", "893860100016", "Spiral Notebook B5", "NOTEBOOKS", "PACK"],
    ["STA-003", "893860100017", "Blue Ballpoint Pen", "WRITING_TOOLS", "BOX"],
    ["STA-004", "893860100018", "Black Marker Pen", "WRITING_TOOLS", "BOX"],
    ["CAN-001", "893860100019", "Tuna in Brine", "CANNED_FISH", "CAN"],
    ["CAN-002", "893860100020", "Sardines Tomato Sauce", "CANNED_FISH", "CAN"],
    ["CAN-003", "893860100021", "Luncheon Meat Classic", "CANNED_MEAT", "CAN"],
    ["CAN-004", "893860100022", "Chicken Ham Canned", "CANNED_MEAT", "CAN"],
]


QUICK_START_ROWS = [
    ["Module", "Field 1", "Field 2", "Field 3", "Field 4", "Field 5", "Field 6", "Field 7"],
    ["UOM", "BAG", "Bag", "", "", "", "", ""],
    ["Category", "SNACKS", "Snacks", "", "", "", "", ""],
    ["Category", "CHIPS", "Chips", "SNACKS", "", "", "", ""],
    ["Supplier", "SUP-SNACK-MART", "Snack Mart Distribution", "Huy Tran", "huy@snackmart.vn", "0905111001", "120 Nguyen Van Linh, Da Nang", "ACTIVE"],
    ["Warehouse", "WH-HCM-DRY", "ACTIVE", "HCM Dry Storage Warehouse", "350", "", "", ""],
    ["Section", "WH-HCM-DRY", "D1-A01", "STORAGE", "", "", "", ""],
    ["Section", "WH-HCM-DRY", "D1-B01", "PICKING", "", "", "", ""],
    ["Product", "SNK-003", "893860100003", "Spicy Potato Chips", "CHIPS", "BAG", "", ""],
]


SHEETS = [
    ("README", README_ROWS),
    ("UOMs", UOM_ROWS),
    ("Categories", CATEGORY_ROWS),
    ("Suppliers", SUPPLIER_ROWS),
    ("Warehouses", WAREHOUSE_ROWS),
    ("Sections", SECTION_ROWS),
    ("Products", PRODUCT_ROWS),
    ("QuickStart", QUICK_START_ROWS),
]


def column_name(index: int) -> str:
    result = []
    while index > 0:
        index, rem = divmod(index - 1, 26)
        result.append(chr(65 + rem))
    return "".join(reversed(result))


def cell_xml(ref: str, value: str) -> str:
    text = escape(value)
    return f'<c r="{ref}" t="inlineStr"><is><t xml:space="preserve">{text}</t></is></c>'


def worksheet_xml(rows: list[list[str]]) -> str:
    xml_rows = []
    for row_index, row in enumerate(rows, start=1):
        cells = []
        for col_index, value in enumerate(row, start=1):
            ref = f"{column_name(col_index)}{row_index}"
            cells.append(cell_xml(ref, str(value)))
        xml_rows.append(f'<row r="{row_index}">{"".join(cells)}</row>')
    sheet_data = "".join(xml_rows)
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">'
        f"<sheetData>{sheet_data}</sheetData>"
        "</worksheet>"
    )


def workbook_xml() -> str:
    sheets_xml = []
    for index, (name, _) in enumerate(SHEETS, start=1):
        sheets_xml.append(
            f'<sheet name="{escape(name)}" sheetId="{index}" r:id="rId{index}"/>'
        )
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" '
        'xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">'
        f"<sheets>{''.join(sheets_xml)}</sheets>"
        "</workbook>"
    )


def workbook_rels_xml() -> str:
    rels = []
    for index, _sheet in enumerate(SHEETS, start=1):
        rels.append(
            '<Relationship '
            f'Id="rId{index}" '
            'Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" '
            f'Target="worksheets/sheet{index}.xml"/>'
        )
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
        f"{''.join(rels)}"
        "</Relationships>"
    )


def root_rels_xml() -> str:
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
        '<Relationship Id="rId1" '
        'Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" '
        'Target="xl/workbook.xml"/>'
        '<Relationship Id="rId2" '
        'Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" '
        'Target="docProps/core.xml"/>'
        '<Relationship Id="rId3" '
        'Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" '
        'Target="docProps/app.xml"/>'
        "</Relationships>"
    )


def content_types_xml() -> str:
    overrides = [
        '<Override PartName="/xl/workbook.xml" '
        'ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>'
    ]
    for index, _sheet in enumerate(SHEETS, start=1):
        overrides.append(
            f'<Override PartName="/xl/worksheets/sheet{index}.xml" '
            'ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>'
        )
    overrides.append(
        '<Override PartName="/docProps/core.xml" '
        'ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>'
    )
    overrides.append(
        '<Override PartName="/docProps/app.xml" '
        'ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>'
    )
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">'
        '<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>'
        '<Default Extension="xml" ContentType="application/xml"/>'
        f"{''.join(overrides)}"
        "</Types>"
    )


def app_xml() -> str:
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" '
        'xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">'
        "<Application>Codex</Application>"
        f"<TitlesOfParts><vt:vector size=\"{len(SHEETS)}\" baseType=\"lpstr\">"
        + "".join(f"<vt:lpstr>{escape(name)}</vt:lpstr>" for name, _ in SHEETS)
        + "</vt:vector></TitlesOfParts>"
        f"<HeadingPairs><vt:vector size=\"2\" baseType=\"variant\">"
        "<vt:variant><vt:lpstr>Worksheets</vt:lpstr></vt:variant>"
        f"<vt:variant><vt:i4>{len(SHEETS)}</vt:i4></vt:variant>"
        "</vt:vector></HeadingPairs>"
        "</Properties>"
    )


def core_xml() -> str:
    created = datetime.now(timezone.utc).replace(microsecond=0).isoformat().replace("+00:00", "Z")
    return (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" '
        'xmlns:dc="http://purl.org/dc/elements/1.1/" '
        'xmlns:dcterms="http://purl.org/dc/terms/" '
        'xmlns:dcmitype="http://purl.org/dc/dcmitype/" '
        'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">'
        "<dc:title>Web Manual Test Data</dc:title>"
        "<dc:creator>Codex</dc:creator>"
        "<cp:lastModifiedBy>Codex</cp:lastModifiedBy>"
        f'<dcterms:created xsi:type="dcterms:W3CDTF">{created}</dcterms:created>'
        f'<dcterms:modified xsi:type="dcterms:W3CDTF">{created}</dcterms:modified>'
        "</cp:coreProperties>"
    )


def generate_xlsx() -> None:
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    with ZipFile(OUTPUT, "w", compression=ZIP_DEFLATED) as zf:
        zf.writestr("[Content_Types].xml", content_types_xml())
        zf.writestr("_rels/.rels", root_rels_xml())
        zf.writestr("docProps/app.xml", app_xml())
        zf.writestr("docProps/core.xml", core_xml())
        zf.writestr("xl/workbook.xml", workbook_xml())
        zf.writestr("xl/_rels/workbook.xml.rels", workbook_rels_xml())
        for index, (_name, rows) in enumerate(SHEETS, start=1):
            zf.writestr(f"xl/worksheets/sheet{index}.xml", worksheet_xml(rows))


if __name__ == "__main__":
    generate_xlsx()
    print(f"Created: {OUTPUT}")
