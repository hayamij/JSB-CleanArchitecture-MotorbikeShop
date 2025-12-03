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

### 1. Quản lý khách hàng (Customer Features)

#### Tài khoản
- ✅ Đăng ký tài khoản (Register)
- ✅ Đăng nhập (Login)
  - Tự động tạo giỏ hàng khi đăng ký
  - Gộp giỏ hàng guest vào giỏ hàng user khi đăng nhập

#### Sản phẩm xe máy
- ✅ Xem danh sách xe máy (Get All Motorbikes)
- ✅ Tìm kiếm xe máy (Search Motorbikes)
  - Tìm theo keyword, hãng xe, dòng xe, màu sắc, dung tích
- ✅ Xem chi tiết sản phẩm (Get Product Detail)
  - Hiển thị giá gốc, giá sau khuyến mãi, % giảm giá

#### Giỏ hàng
- ✅ Thêm vào giỏ hàng (Add To Cart)
  - Kiểm tra tồn kho trước khi thêm
- ✅ Xem giỏ hàng (View Cart)
  - Cảnh báo nếu số lượng trong giỏ vượt tồn kho
- ✅ Cập nhật số lượng (Update Cart Quantity)
  - Tự động xóa sản phẩm nếu số lượng = 0

#### Đơn hàng
- ✅ Thanh toán (Checkout)
  - Tạo đơn hàng
  - Tự động trừ tồn kho
  - Xóa giỏ hàng sau khi đặt hàng thành công
- ✅ Hủy đơn hàng (Cancel Order)
  - Hoàn lại tồn kho
  - Kiểm tra quyền hủy đơn
- ✅ Xem danh sách đơn hàng (List All Orders)
  - Sắp xếp theo ngày đặt

### 2. Quản lý xe máy (Admin) - 🔄 Đang phát triển

- 🔄 Tìm kiếm xe máy
- 🔄 Thêm xe máy
- 🔄 Sửa thông tin xe máy
- 🔄 Xóa xe máy

### 3. Quản lý phụ kiện (Admin) - 🔄 Đang phát triển

- 🔄 Tìm kiếm phụ kiện
- 🔄 Thêm phụ kiện
- 🔄 Sửa thông tin phụ kiện
- 🔄 Xóa phụ kiện

### 4. Quản lý tài khoản (Admin) - 🔄 Đang phát triển

- 🔄 Tìm kiếm tài khoản
- 🔄 Xem danh sách người dùng
- 🔄 Sửa thông tin người dùng
- 🔄 Xóa người dùng

**Chú thích**: ✅ = Đã hoàn thành | 🔄 = Đang phát triển

**Tổng kết triển khai**:
- **11 Use Cases đã hoàn thành**: Register, Login, AddToCart, GetProductDetail, ViewCart, UpdateCartQuantity, Checkout, CancelOrder, ListAllOrders, GetAllMotorbikes, SearchMotorbikes
- **Flow Pattern**: Tất cả Use Cases tuân thủ flow pattern - luôn thực thi đầy đủ các bước và present kết quả dù success hay error
- **Error Handling**: Sử dụng error-accumulation pattern thay vì throw exception

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

### Flow Pattern

Tất cả Use Cases tuân thủ flow pattern hoàn chỉnh:

```
Controller → InputData → UseCaseControl → Entity
                ↓
            OutputData → Presenter → ViewModel
```

### Use Case Execution Flow

Mỗi Use Case thực thi theo các bước:

```java
1. Validate Input (Bước 2)
   - Kiểm tra tính hợp lệ của dữ liệu đầu vào
   - Bắt exception nhưng không throw ra ngoài

2. Check Business Rules (Bước 3)
   - Kiểm tra nghiệp vụ (chỉ thực thi nếu bước 2 không có lỗi)
   - Ví dụ: kiểm tra email đã tồn tại, kiểm tra tồn kho

3. Execute Business Logic (Bước 4)
   - Thực thi logic nghiệp vụ (chỉ thực thi nếu không có lỗi)
   - Ví dụ: tạo tài khoản, thêm vào giỏ hàng, tạo đơn hàng

4. Build Error Response (Bước 4.1)
   - Nếu có lỗi từ các bước trước, tạo error response

5. Present Result (Bước 5)
   - Luôn present kết quả (success hoặc error)

6. User Receives Notification (Bước 6)
   - Người dùng nhận thông báo
```

### Ví dụ: Add To Cart Use Case

```java
public void execute(AddToCartInputData inputData) {
    AddToCartOutputData outputData = null;
    Exception errorException = null;
    
    // Bước 2: Validate
    try {
        validateInput(inputData);
    } catch (Exception e) {
        errorException = e;
    }
    
    // Bước 3: Check business rules
    if (errorException == null) {
        try {
            checkProductAndStock(inputData);
        } catch (Exception e) {
            errorException = e;
        }
    }
    
    // Bước 4: Execute
    if (errorException == null) {
        try {
            outputData = addToCart(inputData);
        } catch (Exception e) {
            errorException = e;
        }
    }
    
    // Bước 4.1: Build error response
    if (errorException != null) {
        outputData = buildErrorResponse(errorException);
    }
    
    // Bước 5: Present (luôn thực thi)
    outputBoundary.present(outputData);
}
```

### Nguyên tắc quan trọng

- ✅ **No throw pattern**: Không bao giờ throw exception ra ngoài use case
- ✅ **Error accumulation**: Sử dụng biến `errorException` để theo dõi lỗi
- ✅ **Always present**: Luôn gọi `outputBoundary.present()` bất kể success hay error
- ✅ **Sequential flow**: Các bước thực thi tuần tự, bước sau chỉ chạy nếu bước trước không lỗi
- ✅ **Complete execution**: Luôn thực thi hết tất cả các bước (validate → check → execute → present)

## Testing

### Test Coverage

Tất cả Use Cases đều có Unit Tests:
- ✅ `RegisterUseCaseControlTest`
- ✅ `LoginUseCaseControlTest`
- ✅ `AddToCartUseCaseControlTest`
- ✅ `GetProductDetailUseCaseControlTest`
- ✅ `ViewCartUseCaseControlTest`
- ✅ `UpdateCartQuantityUseCaseControlTest`
- ✅ `CheckoutUseCaseControlTest`
- ✅ `CancelOrderUseCaseControlTest`
- ✅ `ListAllOrdersUseCaseControlTest`

**Domain Entities có Unit Tests**:
- ✅ `TaiKhoanTest`
- ✅ `GioHangTest`
- ✅ `XeMayTest`
### Core Entities

```
TaiKhoan (User Account)
├── VaiTro: CUSTOMER | ADMIN
├── Authentication: email, password (BCrypt hashed)
├── Status: hoatDong (boolean)
└── Relationships:
    └── 1:1 → GioHang (Shopping Cart)

SanPham (Product) - Abstract
├── XeMay (Motorbike)
│   ├── hangXe, dongXe, mauSac
│   ├── namSanXuat, dungTich
│   └── giảm giá logic
└── PhuKien (Accessory)
## Implemented Use Cases

### 1. Authentication & User Management
- **RegisterUseCase**: Đăng ký tài khoản mới
  - Validate email, username, password, phone
  - Kiểm tra email đã tồn tại
  - Tự động tạo giỏ hàng cho user mới
  
- **LoginUseCase**: Đăng nhập
  - Xác minh email và password
  - Kiểm tra trạng thái tài khoản
  - Gộp giỏ hàng guest vào giỏ hàng user

### 2. Product Management
- **GetAllMotorbikesUseCase**: Lấy danh sách tất cả xe máy
- **SearchMotorbikesUseCase**: Tìm kiếm xe máy theo tiêu chí
  - Filter: keyword, brand, model, color, CC range
- **GetProductDetailUseCase**: Xem chi tiết sản phẩm
  - Tính giá sau khuyến mãi
  - Tính % giảm giá

### 3. Shopping Cart Management
- **AddToCartUseCase**: Thêm sản phẩm vào giỏ
  - Validate input
  - Kiểm tra tồn kho
  - Cộng dồn nếu sản phẩm đã có trong giỏ
  
- **ViewCartUseCase**: Xem giỏ hàng
  - Hiển thị danh sách sản phẩm
  - Cảnh báo nếu số lượng vượt tồn kho
  - Tính tổng tiền
  
- **UpdateCartQuantityUseCase**: Cập nhật số lượng
  - Tự động xóa nếu quantity = 0
  - Validate số lượng

### 4. Order Management
- **CheckoutUseCase**: Thanh toán và tạo đơn hàng
  - Validate thông tin giao hàng
  - Kiểm tra giỏ hàng và tồn kho
  - Tạo đơn hàng
  - Trừ tồn kho tự động
  - Xóa giỏ hàng sau khi đặt thành công
  
- **CancelOrderUseCase**: Hủy đơn hàng
  - Kiểm tra quyền hủy đơn
  - Kiểm tra trạng thái đơn hàng
  - Hoàn lại tồn kho
  
- **ListAllOrdersUseCase**: Xem danh sách đơn hàng
  - Sắp xếp theo ngày đặt (mới nhất trước)
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
