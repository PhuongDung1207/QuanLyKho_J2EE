SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
Hệ thống Website Quản lý kho hàng (WMS)
Java Spring Boot • RESTful API • PostgreSQL

0.1 Thuật ngữ & viết tắt
Viết tắt	Giải thích
WMS	Warehouse Management System – Hệ thống quản lý kho
SKU	Stock Keeping Unit – Mã định danh sản phẩm
UoM	Unit of Measure – Đơn vị tính
RBAC	Role-Based Access Control – Phân quyền theo vai trò
JWT	JSON Web Token – Token xác thực
Inbound/Outbound	Nhập kho / Xuất kho
Transfer	Điều chuyển kho
Stocktake	Kiểm kê kho
1. Giới thiệu
Mục tiêu: quản lý nhập–xuất–tồn, điều chuyển, kiểm kê, phân quyền và báo cáo.
1.1 Đối tượng sử dụng
Vai trò	Mô tả
Admin	Toàn quyền hệ thống
Quản lý kho	Duyệt phiếu, điều chuyển, kiểm kê
Nhân viên kho	Tạo phiếu & thực hiện nhập/xuất
Kế toán/Báo cáo	Xem báo cáo & export
2. Tổng quan hệ thống
Kiến trúc: Client ↔ Spring Boot REST API ↔ PostgreSQL. Auth JWT; RBAC; transaction cho nghiệp vụ tồn kho.
3. Yêu cầu nghiệp vụ (Business Requirements)
Mã	Mô tả
BR-01	Quản lý nhiều kho và vị trí
BR-02	Phiếu có trạng thái, lịch sử, người duyệt
BR-03	Chỉ phiếu hoàn tất mới ảnh hưởng tồn
BR-04	RBAC + audit log
BR-05	Báo cáo tồn & nhập–xuất–tồn
4. Yêu cầu chức năng (Functional Requirements)
MUST/SHOULD/COULD.
4.1 Xác thực & Phân quyền
Mã	Ưu tiên	Mô tả
FR-AUTH-01	MUST	Đăng nhập, JWT
FR-AUTH-03	MUST	RBAC
FR-AUTH-04	MUST	Audit log
4.2 Master Data
Mã	Ưu tiên	Mô tả
FR-MD-01	MUST	Kho
FR-MD-02	MUST	Vị trí
FR-MD-03	MUST	Sản phẩm
FR-MD-05	MUST	NCC/KH
4.3 Tồn kho
Mã	Ưu tiên	Mô tả
FR-INV-01	MUST	Xem tồn
FR-INV-03	MUST	Điều chỉnh tồn
FR-INV-04	MUST	Kiểm kê
4.4 Nhập kho
Mã	Ưu tiên	Mô tả
FR-INB-01	MUST	Tạo phiếu nhập
FR-INB-03	MUST	Duyệt
FR-INB-04	MUST	COMPLETED cộng tồn
4.5 Xuất kho
Mã	Ưu tiên	Mô tả
FR-OUT-01	MUST	Tạo phiếu xuất
FR-OUT-03	MUST	Không vượt tồn
FR-OUT-04	MUST	COMPLETED trừ tồn
4.6 Điều chuyển
Mã	Ưu tiên	Mô tả
FR-TRF-02	MUST	2 bước ISSUE/RECEIVE
4.8 Báo cáo
Mã	Ưu tiên	Mô tả
FR-RPT-01	MUST	Tồn kho
FR-RPT-02	MUST	Nhập–xuất–tồn
FR-RPT-03	SHOULD	Export
5. Quy tắc nghiệp vụ
Mã	Quy tắc
RULE-01	Chỉ COMPLETED mới cập nhật tồn kho.
RULE-02	APPROVED mới được RECEIVE/ISSUE/COMPLETE.
RULE-03	Không cho xuất nếu tồn không đủ.
RULE-04	Transfer 2 bước: ISSUE trừ kho A, RECEIVE cộng kho B.
6. Yêu cầu phi chức năng
•	Hash mật khẩu (BCrypt/Argon2), JWT, RBAC.
•	Transaction & rollback.
•	Pagination/filter/sort.
•	Logging + error code thống nhất.
7. Mô hình dữ liệu (Data Model)
7.1 Data Dictionary (cốt lõi)
Bảng	Mô tả cột chính
users	id uuid PK; username unique; password_hash; status; created_at/updated_at
roles	id uuid PK; code unique; name
permissions	id uuid PK; code unique; module; name
audit_logs	actor_user_id; action; entity_type/entity_id; before_json/after_json; created_at
warehouses	id uuid PK; code unique; name; status; capacity(optional)
locations	id uuid PK; warehouse_id FK; code (unique per warehouse); type; parent_id FK
categories	id uuid PK; code unique; name; parent_id(optional)
uoms	id uuid PK; code unique; name
products	id uuid PK; sku unique; name; category_id; base_uom_id; barcode unique(optional); attributes jsonb
suppliers	id uuid PK; code unique; name; contact fields
customers	id uuid PK; code unique; name; contact fields
inventory_balances	unique(product_id, warehouse_id, location_id); qty_on_hand; qty_reserved; min/max
inbound_receipts	code unique; type; warehouse_id; supplier_id(optional); status; created_by; approved_by(optional)
inbound_receipt_lines	receipt_id FK; product_id; uom_id; quantity>0; location_id(optional)
outbound_issues	code unique; type; warehouse_id; customer_id(optional); status; created_by; approved_by(optional)
outbound_issue_lines	issue_id FK; product_id; uom_id; quantity>0; location_id(optional)
transfers	code unique; from_warehouse_id; to_warehouse_id; status
transfer_lines	transfer_id FK; product_id; uom_id; quantity>0; from/to location(optional)
stocktakes	code unique; warehouse_id; status; created_by
stocktake_lines	stocktake_id FK; product_id; system_qty; counted_qty; diff
adjustments	code unique; warehouse_id; reason; status; created_by; approved_by(optional)
adjustment_lines	adjustment_id FK; product_id; qty_change (+/-)
orders	code unique; customer_id(optional); warehouse_id(optional); status
order_lines	order_id FK; product_id; uom_id; quantity>0
8. Đặc tả API (REST)
8.1 Endpoint catalog
Module	Method	Path	Mô tả	Quyền
Auth	POST	/api/v1/auth/login	Đăng nhập, trả JWT	PUBLIC
Auth	POST	/api/v1/auth/refresh	Làm mới token (nếu có)	AUTH
Users	GET	/api/v1/users	Danh sách user	ADMIN
Users	POST	/api/v1/users	Tạo user	ADMIN
Users	GET	/api/v1/users/{id}	Chi tiết user	ADMIN
Users	PUT	/api/v1/users/{id}	Cập nhật user	ADMIN
Users	PATCH	/api/v1/users/{id}/lock	Khóa tài khoản	ADMIN
Roles	GET	/api/v1/roles	Danh sách role	ADMIN
Roles	POST	/api/v1/roles	Tạo role	ADMIN
Permissions	GET	/api/v1/permissions	Danh sách permission	ADMIN
Roles	PUT	/api/v1/roles/{id}/permissions	Gán permission cho role	ADMIN
Audit	GET	/api/v1/audit-logs	Tra cứu audit log	ADMIN/WM
Warehouses	GET	/api/v1/warehouses	Danh sách kho	ADMIN/WM/STAFF/ACC
Warehouses	POST	/api/v1/warehouses	Tạo kho	ADMIN/WM
Locations	GET	/api/v1/warehouses/{id}/locations	Danh sách vị trí theo kho	ADMIN/WM/STAFF
Locations	POST	/api/v1/warehouses/{id}/locations	Tạo vị trí	ADMIN/WM
Products	GET	/api/v1/products	Danh sách sản phẩm	ADMIN/WM/STAFF/ACC
Products	POST	/api/v1/products	Tạo sản phẩm	ADMIN/WM
Categories	GET	/api/v1/categories	Danh sách danh mục	ADMIN/WM/STAFF/ACC
UoMs	GET	/api/v1/uoms	Danh sách đơn vị	ADMIN/WM
Suppliers	GET	/api/v1/suppliers	Danh sách NCC	ADMIN/WM
Customers	GET	/api/v1/customers	Danh sách KH	ADMIN/WM
Inventory	GET	/api/v1/inventory/balances	Tồn kho	ADMIN/WM/STAFF/ACC
Inventory	GET	/api/v1/inventory/low-stock	Cảnh báo tồn thấp	ADMIN/WM/ACC
Adjustments	POST	/api/v1/inventory/adjustments	Tạo phiếu điều chỉnh	WM/STAFF
Adjustments	POST	/api/v1/inventory/adjustments/{id}/approve	Duyệt điều chỉnh	WM
Adjustments	POST	/api/v1/inventory/adjustments/{id}/post	Ghi sổ điều chỉnh (cập nhật tồn)	WM/STAFF
Inbounds	GET	/api/v1/inbounds	Danh sách phiếu nhập	WM/STAFF/ACC
Inbounds	POST	/api/v1/inbounds	Tạo phiếu nhập	WM/STAFF
Inbounds	POST	/api/v1/inbounds/{id}/submit	Gửi duyệt	WM/STAFF
Inbounds	POST	/api/v1/inbounds/{id}/approve	Duyệt phiếu nhập	WM
Inbounds	POST	/api/v1/inbounds/{id}/receive	Hoàn tất nhập (cộng tồn)	WM/STAFF
Inbounds	POST	/api/v1/inbounds/import-excel	Import Excel phiếu nhập	WM/STAFF
Outbounds	GET	/api/v1/outbounds	Danh sách phiếu xuất	WM/STAFF/ACC
Outbounds	POST	/api/v1/outbounds	Tạo phiếu xuất	WM/STAFF
Outbounds	POST	/api/v1/outbounds/{id}/submit	Gửi duyệt	WM/STAFF
Outbounds	POST	/api/v1/outbounds/{id}/approve	Duyệt phiếu xuất	WM
Outbounds	POST	/api/v1/outbounds/{id}/complete	Hoàn tất xuất (trừ tồn)	WM/STAFF
Transfers	GET	/api/v1/transfers	Danh sách điều chuyển	WM/STAFF/ACC
Transfers	POST	/api/v1/transfers	Tạo điều chuyển	WM/STAFF
Transfers	POST	/api/v1/transfers/{id}/approve	Duyệt điều chuyển	WM
Transfers	POST	/api/v1/transfers/{id}/issue	Xuất điều chuyển (trừ kho A)	WM/STAFF
Transfers	POST	/api/v1/transfers/{id}/receive	Nhận điều chuyển (cộng kho B)	WM/STAFF
Stocktakes	GET	/api/v1/stocktakes	Danh sách kiểm kê	WM/ACC
Stocktakes	POST	/api/v1/stocktakes	Tạo đợt kiểm kê	WM
Stocktakes	POST	/api/v1/stocktakes/{id}/start	Bắt đầu kiểm kê	WM
Stocktakes	POST	/api/v1/stocktakes/{id}/close	Chốt kiểm kê	WM
Orders	GET	/api/v1/orders	Danh sách đơn hàng	WM/STAFF/ACC
Orders	POST	/api/v1/orders	Tạo đơn hàng	WM/STAFF
Orders	POST	/api/v1/orders/{id}/confirm	Xác nhận đơn	WM/STAFF
Orders	POST	/api/v1/orders/{id}/create-outbound	Tạo phiếu xuất từ đơn	WM/STAFF
Reports	GET	/api/v1/reports/inventory	Báo cáo tồn kho	ACC/WM/ADMIN
Reports	GET	/api/v1/reports/inoutstock	Báo cáo nhập–xuất–tồn	ACC/WM/ADMIN
Reports	GET	/api/v1/reports/export	Export report (excel/pdf)	ACC/WM/ADMIN
9. Use Case & Ma trận phân quyền
Mã	Mô tả
UC-02	Inbound: tạo→duyệt→receive
UC-03	Outbound: tạo→duyệt→complete
UC-04	Transfer: issue/receive
UC-05	Stocktake: counting→close
10. Kiểm thử & Nghiệm thu
Mã	Mô tả	Kỳ vọng
TC-INB-01	Inbound completed	Tồn tăng đúng
TC-OUT-02	Outbound thiếu tồn	409/422 + error
TC-TRF-01	Transfer issue/receive	A giảm, B tăng
11. Triển khai & Vận hành
•	Java 17+, Spring Boot 3.x, PostgreSQL 14+. Migration Flyway/Liquibase.
12. Phụ lục
12.1 Error Catalog
Mã lỗi	HTTP	Mô tả
OUTBOUND_STOCK_NOT_ENOUGH	409	Không đủ tồn kho để xuất
INVALID_STATE_TRANSITION	422	Chuyển trạng thái không hợp lệ
CONFLICT_DUPLICATE_CODE	409	Trùng code/sku/barcode
