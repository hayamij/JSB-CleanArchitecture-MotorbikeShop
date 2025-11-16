# Motorbike Shop - Clean Architecture

## Tổng quan dự án

Hệ thống quản lý cửa hàng xe máy được xây dựng theo kiến trúc Clean Architecture, tuân thủ các nguyên tắc SOLID và đảm bảo tính độc lập giữa các layer.

### Công nghệ sử dụng

- **Backend Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: SQL
- **Testing**: JUnit 5
- **Architecture Pattern**: Clean Architecture

### Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/motorbike/
│   │   ├── domain/           # Entities và Business Logic
│   │   ├── business/         # Use Cases và DTOs
│   │   ├── adapters/         # Controllers, Presenters, ViewModels
│   │   └── infrastructure/   # Database, Config
│   └── resources/
│       ├── static/           # CSS, JS
│       └── templates/        # HTML
└── test/
    └── java/com/motorbike/   # Unit Tests
```

## Chức năng hệ thống

### 1. Quản lý sản phẩm (Customer)

#### Xe máy
- ✅ Xem chi tiết sản phẩm
- ✅ Tìm kiếm xe máy
- ✅ Xem danh sách xe máy

#### Giỏ hàng
- ✅ Thêm vào giỏ hàng
- ✅ Xem giỏ hàng
- ✅ Chỉnh số lượng sản phẩm trong giỏ hàng

#### Tài khoản
- ✅ Đăng ký tài khoản
- ✅ Đăng nhập

#### Đơn hàng
- ✅ Thanh toán (Checkout)

### 2. Quản lý xe máy (Admin)

- 🔄 Tìm kiếm xe máy
- 🔄 Thêm xe máy
- 🔄 Xem danh sách xe máy
- 🔄 Sửa thông tin xe máy
- 🔄 Xóa xe máy

### 3. Quản lý phụ kiện xe máy (Admin)

- 🔄 Tìm kiếm phụ kiện xe máy
- 🔄 Thêm phụ kiện xe máy
- 🔄 Xem danh sách phụ kiện xe máy
- 🔄 Sửa thông tin phụ kiện xe máy
- 🔄 Xóa thông tin phụ kiện xe máy

### 4. Quản lý tài khoản (Admin)

- 🔄 Tìm kiếm tài khoản
- 🔄 Thêm người dùng
- 🔄 Xem danh sách người dùng
- 🔄 Sửa thông tin người dùng
- 🔄 Xóa người dùng

### 5. Quản lý đơn hàng (Admin)

- 🔄 Tìm kiếm đơn hàng
- 🔄 Thêm đơn hàng
- 🔄 Xem danh sách đơn hàng
- 🔄 Sửa đơn hàng
- 🔄 Xóa đơn hàng

**Chú thích**: ✅ = Đã hoàn thành | 🔄 = Đang phát triển

## Kiến trúc Clean Architecture

### Layer Structure

```
┌─────────────────────────────────────────┐
│         Adapters (Controllers)          │ ← External Interface
├─────────────────────────────────────────┤
│      Use Cases (Business Logic)         │ ← Application Logic
├─────────────────────────────────────────┤
│         Domain (Entities)               │ ← Core Business Rules
├─────────────────────────────────────────┤
│      Infrastructure (Database)          │ ← External Services
└─────────────────────────────────────────┘
```

### Các thành phần chính

#### 1. Domain Layer
- **Entities**: `TaiKhoan`, `SanPham`, `XeMay`, `GioHang`, `DonHang`
- **Value Objects**: `VaiTro`, `TrangThaiDonHang`
- **Exceptions**: Custom domain exceptions

#### 2. Business Layer
- **Use Cases**: Logic nghiệp vụ độc lập
- **Input/Output DTOs**: Truyền dữ liệu giữa các layer
- **Ports (Interfaces)**: Repository interfaces

#### 3. Adapters Layer
- **Controllers**: Nhận request từ client
- **Presenters**: Format dữ liệu cho UI
- **ViewModels**: Dữ liệu hiển thị
- **Repositories**: Implement ports

#### 4. Infrastructure Layer
- **Database**: JPA entities và repositories
- **Configuration**: Spring Boot config

## Use Case Implementation

Mỗi use case được triển khai theo flow:

```
Controller → InputData → UseCaseControl → Entity
                ↓
            OutputData → Presenter → ViewModel
```

### Ví dụ: Add To Cart Use Case

```java
1. Controller nhận request
2. Tạo AddToCartInputData
3. UseCaseControl thực thi business logic
4. Tạo AddToCartOutputData
5. Presenter format dữ liệu
6. ViewModel trả về cho UI
```

## Testing

### Test Coverage

Tất cả Use Cases đều có Unit Tests:
- ✅ `AddToCartUseCaseControlTest`
- ✅ `CheckoutUseCaseControlTest`
- ✅ `GetProductDetailUseCaseControlTest`
- ✅ `LoginUseCaseControlTest`
- ✅ `RegisterUseCaseControlTest`
- ✅ `UpdateCartQuantityUseCaseControlTest`
- ✅ `ViewCartUseCaseControlTest`

### Test Strategy

- **Valid Cases**: Kiểm tra luồng thành công
- **Invalid Cases**: Kiểm tra validation và error handling
- **Edge Cases**: Kiểm tra boundary conditions
- **Mock Objects**: Sử dụng mock repositories

### Chạy tests

```bash
mvn test
```

## Cài đặt và chạy

### Yêu cầu hệ thống

- Java 11 or higher
- Maven 3.6+
- MySQL/PostgreSQL

### Cài đặt

```bash
# Clone repository
git clone https://github.com/hayamij/JSB-CleanArchitecture-MotorbikeShop.git

# Di chuyển vào thư mục dự án
cd JSB-CleanArchitecture-MotorbikeShop

# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

### Cấu hình Database

Chỉnh sửa file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/motorbike_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Chạy script SQL:
```bash
mysql -u username -p < database-setup.sql
```

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

## Domain Model

### Core Entities

```
TaiKhoan (User Account)
├── VaiTro: CUSTOMER | ADMIN
└── GioHang (Shopping Cart)

SanPham (Product)
├── XeMay (Motorbike)
└── PhuKien (Accessory)

DonHang (Order)
├── TrangThai: CHO_XAC_NHAN | DANG_GIAO | HOAN_THANH | HUY
└── ChiTietDonHang (Order Items)
```

## API Endpoints (Planned)

### Customer APIs
```
GET    /products/{id}           - Xem chi tiết sản phẩm
POST   /cart/add                - Thêm vào giỏ hàng
GET    /cart                    - Xem giỏ hàng
PUT    /cart/update             - Cập nhật giỏ hàng
POST   /auth/register           - Đăng ký
POST   /auth/login              - Đăng nhập
POST   /checkout                - Thanh toán
```

### Admin APIs
```
GET    /admin/products          - Danh sách sản phẩm
POST   /admin/products          - Thêm sản phẩm
PUT    /admin/products/{id}     - Sửa sản phẩm
DELETE /admin/products/{id}     - Xóa sản phẩm
GET    /admin/orders            - Danh sách đơn hàng
GET    /admin/users             - Danh sách người dùng
```

## Contributing

Khi contribute vào dự án, vui lòng tuân thủ:

1. **Code Style**: Follow Java conventions
2. **Testing**: Viết tests cho mọi use case
3. **Documentation**: Comment code khi cần thiết
4. **Clean Architecture**: Tuân thủ nguyên tắc phân tầng

## License

This project is licensed under the MIT License.

## Team

- **Project Type**: University Course Project
- **Architecture**: Clean Architecture Pattern
- **Focus**: Learning best practices in software design

---

**Note**: Đây là dự án học tập, tập trung vào việc áp dụng Clean Architecture và SOLID principles trong thực tế.
