## Project Rules (Quy ước dự án)

### 1) Coding & Architecture Rules
- **R1.1** Áp dụng kiến trúc 5 tầng: `controller → service → repository → model → view`.
- **R1.2** `Controller` không chứa business logic; chỉ nhận request, validate input, gọi service, trả response/view.
- **R1.3** `Service` chứa toàn bộ nghiệp vụ (state transition, kiểm tra tồn, approve/complete…).
- **R1.4** `Repository` chỉ làm việc với database thông qua Spring Data JPA (không viết SQL ở Controller).
- **R1.5** `Model` gồm Entity + Enum trạng thái + DTO (nếu tách DTO khỏi Entity).
- **R1.6** `View` dùng Thymeleaf: file đặt tại `src/main/resources/templates/`, static tại `src/main/resources/static/`.

### 2) Naming Convention
- **R2.1** Package theo chuẩn: `com.example.<project>.<layer>`  
  Ví dụ: `com.example.JavaQuanLyKho.controller`, `...service`, `...repository`, `...model`.
- **R2.2** REST endpoints versioning: `/api/v1/...`.
- **R2.3** Tên bảng/column trong DB: `snake_case`; tên entity/class: `PascalCase`.

### 3) Database & Transaction Rules
- **R3.1** PostgreSQL dùng `UUID` làm khóa chính cho bảng nghiệp vụ & danh mục.
- **R3.2** Các thao tác ảnh hưởng tồn kho phải chạy trong `@Transactional` (service layer).
- **R3.3** Chỉ trạng thái `COMPLETED` mới cộng/trừ tồn kho.
- **R3.4** Transfer bắt buộc 2 bước: `ISSUE` (kho A trừ) → `RECEIVE` (kho B cộng).

### 4) Validation & Error Handling
- **R4.1** Validate input bằng Bean Validation (`@NotNull`, `@Size`, …) ở DTO/request.
- **R4.2** Lỗi trả về theo schema thống nhất:
  - `401` unauthenticated
  - `403` forbidden
  - `404` not found
  - `409` conflict (trùng code / thiếu tồn)
  - `422` invalid state transition / validation error
- **R4.3** Không expose stacktrace ra client.

### 5) Security Rules (nếu bật Spring Security)
- **R5.1** Xác thực JWT Bearer.
- **R5.2** Phân quyền RBAC theo `permission code`.
- **R5.3** Audit log ghi lại hành động quan trọng: create/update/delete/approve/complete/login.

### 6) Git & Branching Rules
- **R6.1** Branch chính: `main`. Branch dev: `develop`.
- **R6.2** Feature branch: `feature/<name>`.
- **R6.3** Commit message: `type: short message`  
  Ví dụ: `feat: add inbound receipt approve flow`

### 7) Definition of Done
- **R7.1** Xong chức năng khi:
  - Có API/Screen hoạt động
  - Có validate + handle error
  - Có test case tối thiểu (manual checklist hoặc unit/integration test)
  - Có cập nhật SRS/README (nếu cần)