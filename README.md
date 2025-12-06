

# Motorbike Shop - Clean Architecture

## Tổng quan dự án

Dự án quản lý cửa hàng xe máy theo kiến trúc Clean Architecture, tuân thủ SOLID, chuẩn hóa package/dependency, flow xử lý nghiệp vụ và dữ liệu. Toàn bộ chức năng đều được kiểm thử tự động với coverage 100%.

- **34 Use Cases**, 34 use case control, 30 test class, **228 unit tests**, coverage 100%
- **Clean Architecture**: Domain, Business, Adapters, Infrastructure tách biệt tuyệt đối
- **Best-Selling Products**: Sản phẩm bán chạy cho dashboard admin, tính toán từ đơn hàng xác nhận/giao thành công
- **Chuẩn hóa currency**: Backend trả BigDecimal, frontend tự format VND

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/motorbike/
│   │   ├── domain/                    # Entity, Exception, ValueObject
│   │   ├── business/                  # UseCaseControl, DTO, Repository Port
│   │   ├── adapters/                  # Controller, Presenter, ViewModel, RepositoryAdapter
│   │   └── infrastructure/            # JPA Entity/Repo, Config
│   └── resources/
│       ├── static/                    # Static resources (HTML, JS, CSS)
│       └── templates/                 # Thymeleaf templates
└── test/
    └── java/com/motorbike/            # Unit Tests (34 use case, 228 tests)
```

## API nổi bật

- `/admin/orders/stats/top-products`: Thống kê sản phẩm bán chạy cho dashboard admin

## Testing

- 34/34 Use Cases có unit test, 100% coverage

## Flow chuẩn

```
Controller → InputData → UseCaseControl → Entity
        ↓
      OutputData → Presenter → ViewModel
```

## Tính năng nổi bật

- **Sản phẩm bán chạy**: Tính toán từ các đơn hàng đã xác nhận/giao thành công, hiển thị dashboard admin
- **Chuẩn hóa currency**: Backend trả BigDecimal, frontend tự format VND

---

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/motorbike/
│   │   ├── domain/                    # Entity, Exception, ValueObject
│   │   ├── business/                  # UseCaseControl, DTO, Repository Port
│   │   ├── adapters/                  # Controller, Presenter, ViewModel, RepositoryAdapter
│   │   └── infrastructure/            # JPA Entity/Repo, Config
│   └── resources/
│       ├── static/                    # Static resources (HTML, JS, CSS)
│       └── templates/                 # Thymeleaf templates
└── test/
  └── java/com/motorbike/            # Unit Tests (34 use case, 228 tests)
```

## API nổi bật

- `/admin/orders/stats/top-products`: Thống kê sản phẩm bán chạy cho dashboard admin

## Testing

- 34/34 Use Cases có unit test, 100% coverage

## Flow chuẩn

```
Controller → InputData → UseCaseControl → Entity
        ↓
      OutputData → Presenter → ViewModel
```

## Tính năng nổi bật

- **Sản phẩm bán chạy**: Tính toán từ các đơn hàng đã xác nhận/giao thành công, hiển thị dashboard admin
- **Chuẩn hóa currency**: Backend trả BigDecimal, frontend tự format VND

---



### Công nghệ sử dụng

- **Backend Framework**: Spring Boot 3.5.6
- **Java Version**: 17
- **Build Tool**: Maven
- **Database**: MS SQL Server / H2 (in-memory for testing)
- **ORM**: Spring Data JPA
- **Testing**: JUnit 5
- **Architecture Pattern**: Clean Architecture
- **Design Pattern**: Repository Pattern, Presenter Pattern, Dependency Injection




## Chức năng hệ thống

- Quản lý người dùng, xe máy, phụ kiện, sản phẩm, giỏ hàng, đơn hàng
- Sản phẩm bán chạy cho dashboard admin
- Chuẩn hóa currency, flow nghiệp vụ, kiểm thử tự động





























```
TaiKhoan (User Account)
├── VaiTro: CUSTOMER | ADMIN
├── Authentication: email, password (BCrypt hashed)
├── Status: hoatDong (boolean)
├── Profile: tenTaiKhoan, hoTen, soDienThoai
└── Relationships:
    └── 1:1 → GioHang (Shopping Cart)

GioHang (Shopping Cart)
├── maTaiKhoan (FK to TaiKhoan)
├── tongTien (calculated)
├── ngayTao, ngayCapNhat
└── danhSachSanPham: List<ChiTietGioHang>
    └── ChiTietGioHang
        ├── maSanPham, tenSanPham
        ├── giaSanPham, soLuong
        └── tamTinh (calculated)

SanPham (Product) - Abstract Base Class
├── Common: maSanPham, tenSanPham, moTa
├── Price: gia, giamGia (%)
├── Stock: soLuongTonKho
├── Status: hienThi (boolean)
├── Timestamps: ngayTao, ngayCapNhat
└── Subclasses:
    ├── XeMay (Motorbike)
    │   ├── hangXe, dongXe, mauSac
    │   ├── namSanXuat, dungTich (CC)
    │   └── Discount logic: giảm giá tự động
    └── PhuKienXeMay (Accessory)
        ├── loaiPhuKien, thuongHieu
        ├── chatLieu, kichThuoc
        └── Compatible với nhiều loại xe

DonHang (Order)
├── maTaiKhoan (FK to TaiKhoan)
├── trangThai: CHO_XAC_NHAN | DANG_GIAO | HOAN_THANH | HUY
├── thongTinGiaoHang: diaChiGiaoHang, sdtNguoiNhan, ghiChu
├── tongTien (snapshot at checkout time)
├── ngayDatHang, ngayCapNhat
└── chiTietDonHang: List<ChiTietDonHang>
    └── ChiTietDonHang
        ├── maSanPham, tenSanPham
        ├── giaSanPham, soLuong
        └── tamTinh
## Chi tiết Use Cases

### 1. Authentication & User Management (7 Use Cases)

#### RegisterUseCase
- Validate: email format, username uniqueness, password strength, phone format
- Business rule: Check email không trùng lặp
- Action: Hash password (BCrypt), tạo tài khoản, tạo giỏ hàng mới
- Output: Success với userId hoặc error với code cụ thể

#### LoginUseCase  
- Validate: Email và password required
- Business rule: Xác thực credentials, kiểm tra account active
- Action: Verify password, merge guest cart vào user cart
- Output: Login success với user info hoặc error

#### CRUD Operations (Create/Get/Search/Update/Delete User)
- Full CRUD cho quản lý người dùng
- Admin-only features
- Soft delete support

### 2. Motorbike Management (5 Use Cases)

#### GetAllMotorbikes & SearchMotorbikes
- **GetAll**: Trả về toàn bộ xe máy đang hiển thị
- **Search**: Stream-based filtering
  - Criteria: keyword (tên), brand (hãng xe), model (dòng xe), color, CC range
  - Optimized với Java Streams
  
#### CRUD Operations (Create/Update/Delete Motorbike)
- Validate thông tin xe máy
- Quản lý tồn kho
- Admin authorization

### 3. Accessory Management (5 Use Cases)

#### GetAllAccessories & SearchAccessories
- **GetAll**: Danh sách tất cả phụ kiện
- **Search**: Multi-criteria filtering
  - keyword, type (loại), brand, material (chất liệu), price range
  - Stream API với null-safe checks
  
#### CRUD Operations (Create/Update/Delete Accessory)
- Validate thông tin phụ kiện
- Category management
- Stock control

### 4. Product & Cart Management (4 Use Cases)

#### GetProductDetail
- Polymorphic: Hỗ trợ cả XeMay và PhuKienXeMay
- Calculate: giá sau giảm giá, % discount
- Display: Full product info với stock availability

#### AddToCart
- Step 1: Validate productId, accountId, quantity
- Step 2: Check product exists và còn hàng
- Step 3: Nếu sản phẩm đã có → cộng dồn quantity, else → thêm mới
- Step 4: Update tổng tiền giỏ hàng
- Always present: Success hoặc error với message cụ thể

#### ViewCart
- Lấy giỏ hàng của user
- Kiểm tra từng item: stock availability
- Warning nếu item nào vượt tồn kho
- Calculate: tổng tiền realtime

#### UpdateCartQuantity
- Validate: quantity >= 0
- Special case: quantity = 0 → xóa item
- Update: số lượng và tổng tiền
- Check: tồn kho đủ cho quantity mới

### 5. Order Management (5 Use Cases)

#### Checkout (Complex Flow)
```
1. Validate thông tin giao hàng (địa chỉ, SĐT)
2. Validate giỏ hàng không empty
3. Check tồn kho cho TẤT CẢ items trong giỏ
4. Create đơn hàng với snapshot data (giá tại thời điểm đặt)
5. Giảm tồn kho cho mỗi sản phẩm
6. Xóa giỏ hàng sau khi đặt thành công
7. Present: Order confirmation hoặc error
```

#### ListAllOrders
- Lấy tất cả đơn hàng của user
- Sort: ngày đặt DESC (mới nhất trước)
- Include: order items details

#### SearchOrders
- Filter theo: status, date range
- Admin có thể search all orders
- Customer chỉ thấy orders của mình

#### UpdateOrder (Admin)
- Cập nhật trạng thái đơn hàng
- Validate state transition logic
- Log thời gian cập nhật

#### CancelOrder
- Check: User ownership hoặc admin
- Validate: Chỉ hủy được đơn ở trạng thái CHO_XAC_NHAN
- Action: Update status → HUY, hoàn lại tồn kho
- Present: Success hoặc error message
## 🚀 Getting Started

### Yêu cầu hệ thống

- **Java**: 17 or higher
- **Maven**: 3.6+
- **Database**: MS SQL Server hoặc H2 (in-memory)
- **IDE**: IntelliJ IDEA / Eclipse / VS Code (recommended)

### Cài đặt

```bash
# 1. Clone repository
git clone https://github.com/hayamij/JSB-CleanArchitecture-MotorbikeShop.git
cd JSB-CleanArchitecture-MotorbikeShop

# 2. Build project
mvn clean install

# 3. Run tests (optional)
mvn test

# 4. Chạy ứng dụng
mvn spring-boot:run
```

### Cấu hình Database

#### Option 1: MS SQL Server (Production)
Chỉnh sửa `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=motorbike_shop;encrypt=true;trustServerCertificate=true
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```

#### Option 2: H2 Database (Development/Testing)
```properties
# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

### Database Schema
Chạy file `database-setup.sql` để khởi tạo schema và sample data:
```bash
# Với SQL Server
sqlcmd -S localhost -U your_username -P your_password -i database-setup.sql
```

### Truy cập ứng dụng
- **Application**: `http://localhost:8080`
- **H2 Console** (nếu dùng H2): `http://localhost:8080/h2-console`

## Nguyên tắc thiết kế

### SOLID Principles

- **Single Responsibility**: Mỗi class có một trách nhiệm duy nhất
- **Open/Closed**: Mở cho mở rộng, đóng cho sửa đổi
- **Liskov Substitution**: Subclass có thể thay thế class cha
- **Interface Segregation**: Interface nhỏ và tập trung
- **Dependency Inversion**: Phụ thuộc vào abstraction, không phải concrete

### Clean Architecture Benefits

- ✅ **Testability**: Dễ dàng viết unit tests
- ✅ **Maintainability**: Code dễ bảo trì và mở rộng
- ✅ **Independence**: Các layer độc lập với nhau
- ✅ **Flexibility**: Dễ thay đổi UI, database, framework














## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.


## 👥 Team & Project Info

- **Project Type**: University Course Final Project (Cuối kỳ JSB)
- **Institution**: FPT University
- **Course**: Java Spring Boot Development
- **Architecture**: Clean Architecture Pattern
- **Focus**: Learning software design best practices
- **Status**: Completed ✅ (34/34 Use Cases, 228/228 Tests)

---


## 📊 Project Statistics

| Metric | Count |
|--------|-------|
| Use Cases | 34 |
| Unit Tests | 228 |
| Test Classes | 30 |
| Domain Entities | 13 |
| Controllers | 11 |
| Repository Adapters | 6 |
| Presenters | 34 |
| ViewModels | 34 |
| DTOs | 60+ |
| Lines of Code | ~15,000+ |
| Test Coverage | 100% |
| Build Success Rate | ✅ 100% |

---

**⭐ If you find this project helpful, please star it on GitHub!**

**📧 Questions? Open an issue or contact the team.**

**🎓 Đây là dự án học tập, tập trung vào việc áp dụng Clean Architecture và SOLID principles trong thực tế.**
