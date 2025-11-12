# Clean Architecture - Motorbike Shop

## 📋 Tổng quan đề tài

Website bán xe máy và phụ kiện trực tuyến được xây dựng theo kiến trúc Clean Architecture, đảm bảo tính module hóa, dễ bảo trì và mở rộng.

### 🎯 Mục tiêu

Xây dựng hệ thống thương mại điện tử cho phép:
- Khách hàng xem, tìm kiếm và mua xe máy, phụ kiện xe máy
- Quản lý giỏ hàng và thanh toán trực tuyến
- Quản trị viên quản lý sản phẩm, đơn hàng và tài khoản

### 👥 Người dùng hệ thống

1. **Guest (Khách không đăng nhập)**
   - Xem danh sách sản phẩm
   - Xem chi tiết sản phẩm
   - Thêm sản phẩm vào giỏ hàng
   - Chỉnh sửa giỏ hàng
   - Đăng ký tài khoản

2. **Customer (Khách hàng đã đăng nhập)**
   - Tất cả chức năng của Guest
   - Đăng nhập/Đăng xuất
   - Thanh toán đơn hàng
   - Xem lịch sử đơn hàng

3. **Admin (Quản trị viên)**
   - Tất cả chức năng của Customer
   - Quản lý sản phẩm (CRUD)
   - Quản lý tài khoản người dùng
   - Quản lý đơn hàng
   - Tìm kiếm và báo cáo

## 🏗️ Kiến trúc Clean Architecture

### Cấu trúc dự án

```
src/main/java/com/motorbike/
├── domain/                    # Enterprise Business Rules
│   ├── entities/             # Domain Entities
│   │   ├── TaiKhoan.java    # User Account
│   │   ├── GioHang.java     # Shopping Cart
│   │   ├── SanPham.java     # Product (Abstract)
│   │   ├── XeMay.java       # Motorbike
│   │   ├── PhuKienXeMay.java # Accessory
│   │   └── Order.java       # Order
│   └── exceptions/           # Domain Exceptions
│
├── business/                  # Application Business Rules
│   ├── usecase/              # Use Case Interfaces & Implementations
│   │   ├── impl/            # Use Case Implementations
│   │   │   ├── LoginUseCaseImpl.java
│   │   │   ├── RegisterUseCaseImpl.java
│   │   │   ├── GetProductDetailUseCaseImpl.java
│   │   │   ├── AddToCartUseCaseImpl.java
│   │   │   ├── ViewCartUseCaseImpl.java
│   │   │   ├── UpdateCartQuantityUseCaseImpl.java
│   │   │   └── CheckoutUseCaseImpl.java
│   ├── dto/                  # Data Transfer Objects (Input/Output)
│   └── ports/repository/     # Repository Interfaces (Ports)
│
├── adapters/                  # Interface Adapters
│   ├── controllers/          # Web & API Controllers
│   ├── presenters/           # Output Data Presenters
│   ├── viewmodels/          # View Models
│   └── repositories/         # Repository Adapters (Implementations)
│
└── infrastructure/           # Frameworks & Drivers
    ├── persistence/         # JPA Entities & Repositories
    │   └── jpa/
    │       ├── entities/    # JPA Entity Classes
    │       └── repositories/ # Spring JPA Repositories
    └── config/              # Spring Configuration
```

### Nguyên tắc Clean Architecture

1. **Dependency Rule**: Dependencies chỉ hướng vào trong (Domain ← Business ← Adapters ← Infrastructure)
2. **Independence**: Business logic không phụ thuộc framework, UI, database
3. **Testability**: Mỗi layer có thể test độc lập
4. **Separation of Concerns**: Mỗi layer có trách nhiệm riêng biệt

## 🔧 Công nghệ sử dụng

### Backend
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - ORM
- **Hibernate** - JPA Implementation
- **SQL Server** - Database
- **Maven** - Build tool

### Frontend
- **Thymeleaf** - Template engine
- **HTML/CSS/JavaScript** - UI

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing

## 📊 Cơ sở dữ liệu

### Schema chính

```sql
-- User Account
tai_khoan (ma_tai_khoan, email, ten_dang_nhap, mat_khau, 
           so_dien_thoai, dia_chi, vai_tro, hoat_dong, ...)

-- Product (Inheritance: JOINED strategy)
san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, ...)
xe_may (ma_san_pham, hang_xe, dong_xe, mau_sac, nam_san_xuat, ...)
phu_kien_xe_may (ma_san_pham, loai_phu_kien, hang_san_xuat, ...)

-- Shopping Cart
gio_hang (ma_gio_hang, ma_tai_khoan, tong_tien, ...)
chi_tiet_gio_hang (ma_chi_tiet, ma_gio_hang, ma_san_pham, so_luong, ...)

-- Order
don_hang (ma_don_hang, ma_tai_khoan, trang_thai, tong_tien, ...)
chi_tiet_don_hang (ma_chi_tiet, ma_don_hang, ma_san_pham, so_luong, ...)
```

## ✨ Tính năng chính

### 7 Use Cases đã implement

1. **Đăng nhập** - Authentication với password hashing, merge guest cart
2. **Đăng ký** - Registration với email validation
3. **Xem chi tiết sản phẩm** - Product detail với inheritance (XeMay/PhuKienXeMay)
4. **Thêm vào giỏ hàng** - Add to cart cho cả guest và customer
5. **Xem giỏ hàng** - View cart với total calculation
6. **Cập nhật số lượng** - Update cart quantity, remove if quantity = 0
7. **Thanh toán** - Checkout với stock validation, order creation

### Business Rules được enforce

- Email phải unique khi đăng ký
- Mật khẩu được hash trước khi lưu (BCrypt)
- Số lượng thêm vào giỏ không vượt quá tồn kho
- Giỏ hàng guest được merge khi login
- Thanh toán chỉ cho user đã login
- Trừ số lượng tồn kho sau checkout
- Xóa giỏ hàng sau checkout thành công

## 🧪 Testing

### Test Coverage

```
Total Tests: 85
├── Domain Entity Tests: 56 ✅
│   ├── TaiKhoanTest: 16 tests
│   ├── GioHangTest: 15 tests
│   ├── XeMayTest: 12 tests
│   └── PhuKienXeMayTest: 13 tests
│
└── Use Case Tests: 29 ✅
    ├── LoginUseCaseImplTest: 8 tests
    ├── RegisterUseCaseImplTest: 5 tests
    ├── GetProductDetailUseCaseImplTest: 3 tests
    ├── AddToCartUseCaseImplTest: 3 tests
    ├── ViewCartUseCaseImplTest: 3 tests
    ├── UpdateCartQuantityUseCaseImplTest: 3 tests
    └── CheckoutUseCaseImplTest: 4 tests

Pass Rate: 100% (85/85 tests)
```

### Chạy tests

```bash
# Chạy tất cả tests
mvn test

# Chạy test specific class
mvn test -Dtest=LoginUseCaseImplTest

# Compile và skip tests
mvn clean compile -DskipTests
```

## 🚀 Cài đặt và chạy

### Yêu cầu

- JDK 17+
- Maven 3.6+
- SQL Server 2019+

### Cấu hình Database

1. Tạo database `MotorcycleShop` trong SQL Server
2. Chạy script `database-setup-new.sql` để tạo schema
3. Cấu hình trong `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MotorcycleShop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Chạy ứng dụng

```bash
# Build project
mvn clean install

# Run Spring Boot
mvn spring-boot:run
```

Truy cập: `http://localhost:8080`

## 📁 File quan trọng

- `database-setup.sql` - SQL Server schema setup
- `ARCHITECTURE.md` - Chi tiết kiến trúc Clean Architecture
- `usecases.md` - Đặc tả use cases và business rules
- `pom.xml` - Maven dependencies

## 🎓 Điểm nổi bật

### Clean Architecture Benefits

1. **Testability**: 85 unit tests với 100% pass rate, không cần database/UI
2. **Independence**: Business logic tách biệt hoàn toàn khỏi framework
3. **Maintainability**: Dễ sửa đổi, mở rộng từng layer
4. **Flexibility**: Có thể thay đổi database/UI mà không ảnh hưởng business logic

### Design Patterns

- **Dependency Injection** (Spring)
- **Repository Pattern** (Data access abstraction)
- **Adapter Pattern** (Infrastructure adapters)
- **Factory Pattern** (Entity creation)
- **Strategy Pattern** (Business rules)

## 📝 License

MIT License
