# KIẾN TRÚC DỰ ÁN MOTORBIKE SHOP - CLEAN ARCHITECTURE

## 📋 TỔNG QUAN

Dự án **MotorbikeShop** là hệ thống bán xe máy và phụ kiện trực tuyến, được xây dựng theo kiến trúc **Clean Architecture** với Spring Boot.

### Công nghệ sử dụng
- **Backend**: Java 17, Spring Boot 3.5.6
- **Database**: SQL Server (JPA/Hibernate)
- **Architecture**: Clean Architecture (4 layers)
- **Testing**: JUnit 5 (228 tests, 100% coverage)

---

## 🏗️ CẤU TRÚC 4 LỚP

```
┌─────────────────────────────────────────────┐
│         4. Infrastructure Layer             │
│    (JPA, Spring Config, Frameworks)         │
└──────────────┬──────────────────────────────┘
               │ implements
               ↓
┌─────────────────────────────────────────────┐
│           3. Adapters Layer                 │
│  (Controllers, Presenters, Repositories)    │
└──────────────┬──────────────────────────────┘
               │ uses
               ↓
┌─────────────────────────────────────────────┐
│          2. Business Layer                  │
│    (Use Cases, DTOs, Interfaces)            │
└──────────────┬──────────────────────────────┘
               │ uses
               ↓
┌─────────────────────────────────────────────┐
│           1. Domain Layer                   │
│    (Entities, Business Logic, Rules)        │
└─────────────────────────────────────────────┘
```

### Dependency Rule
✅ **Outer layers** phụ thuộc vào **Inner layers**  
❌ **Inner layers** KHÔNG được phụ thuộc vào **Outer layers**

---

## 📁 CẤU TRÚC THƯ MỤC

```
src/main/java/com/motorbike/
├── domain/                          # Layer 1: Domain
│   ├── entities/                    # 16 files
│   │   ├── TaiKhoan.java
│   │   ├── SanPham.java (abstract)
│   │   ├── XeMay.java (extends SanPham)
│   │   ├── PhuKienXeMay.java (extends SanPham)
│   │   ├── GioHang.java
│   │   ├── DonHang.java
│   │   ├── ChiTietGioHang.java
│   │   ├── ChiTietDonHang.java
│   │   └── enums: VaiTro, TrangThaiDonHang, PhuongThucThanhToan
│   └── exceptions/
│       ├── DomainException.java
│       ├── ValidationException.java
│       └── SystemException.java
│
├── business/                        # Layer 2: Business
│   ├── usecase/
│   │   ├── input/                   # Input Boundaries (interfaces)
│   │   ├── output/                  # Output Boundaries (interfaces)
│   │   └── control/                 # Use Case Controls (34 files)
│   ├── dto/                         # Data Transfer Objects (60+ files)
│   │   ├── login/, register/
│   │   ├── product/, motorbike/, accessory/
│   │   ├── cart/, checkout/
│   │   └── order/, user/
│   └── ports/repository/            # Repository Interfaces
│       ├── UserRepository.java
│       ├── ProductRepository.java
│       ├── CartRepository.java
│       └── OrderRepository.java
│
├── adapters/                        # Layer 3: Adapters
│   ├── controllers/                 # REST Controllers (11 files)
│   │   ├── AuthController.java
│   │   ├── ProductController.java
│   │   ├── CartController.java
│   │   ├── OrderController.java
│   │   └── Admin*.java (5 controllers)
│   ├── presenters/                  # Output Presenters (34 files)
│   ├── viewmodels/                  # View Models (34 files)
│   └── repositories/                # Repository Adapters (6 files)
│       └── *RepositoryAdapter.java
│
└── infrastructure/                  # Layer 4: Infrastructure
    ├── config/
    │   └── UseCaseConfig.java       # Spring Bean Configuration
    └── persistence/jpa/
        ├── entities/                # JPA Entities (8 files)
        └── repositories/            # JPA Repositories (5 files)
```

---

## 🔄 LUỒNG DỮ LIỆU (DATA FLOW)

### Request Flow (Client → Server)
```
HTTP Request
    ↓
1. Controller (Adapters)
    ↓
2. Use Case Control (Business)
    ↓
3. Domain Entity (Business Logic)
    ↓
4. Repository Interface (Business)
    ↓
5. Repository Adapter (Adapters)
    ↓
6. JPA Repository (Infrastructure)
    ↓
Database
```

### Response Flow (Server → Client)
```
Database
    ↓
1. JPA Entity (Infrastructure)
    ↓
2. Domain Entity (Domain)
    ↓
3. Use Case Control → Output Data (Business)
    ↓
4. Presenter → ViewModel (Adapters)
    ↓
5. Controller → HTTP Response (Adapters)
    ↓
JSON Response
```

---

## 🎯 CÁC LAYER CHI TIẾT

### 1️⃣ DOMAIN LAYER
**Trách nhiệm**: Chứa business logic thuần túy, không phụ thuộc framework

**Thành phần chính**:
- **Entities**: Đối tượng nghiệp vụ với logic validation
  - `TaiKhoan`: Quản lý thông tin user, authentication
  - `SanPham`: Abstract class cho sản phẩm
  - `XeMay`, `PhuKienXeMay`: Concrete products
  - `GioHang`: Quản lý giỏ hàng, tính tổng tiền
  - `DonHang`: Quản lý đơn hàng, chuyển trạng thái
  
- **Value Objects**: Enums cho trạng thái
  - `VaiTro`: ADMIN, CUSTOMER
  - `TrangThaiDonHang`: CHO_XAC_NHAN, DANG_GIAO, HOAN_THANH, DA_HUY
  - `PhuongThucThanhToan`: THANH_TOAN_TRUC_TIEP, CHUYEN_KHOAN

- **Exceptions**: Domain-specific exceptions

**Ví dụ logic trong Entity**:
```java
// TaiKhoan.java
public void doiMatKhau(String matKhauCu, String matKhauMoi) {
    if (!this.matKhau.equals(matKhauCu)) {
        throw ValidationException.wrongPassword();
    }
    this.matKhau = matKhauMoi;
}

// DonHang.java
public void chuyenTrangThai(TrangThaiDonHang trangThaiMoi) {
    if (!this.trangThai.coTheChuyenSang(trangThaiMoi)) {
        throw DomainException.invalidOrderStatusTransition(...);
    }
    this.trangThai = trangThaiMoi;
}
```

---

### 2️⃣ BUSINESS LAYER
**Trách nhiệm**: Chứa use cases và business rules, định nghĩa interfaces

**Thành phần chính**:

#### A. Use Cases (34 use cases)
Mỗi use case theo pattern:
```
Input Boundary (interface) ← Use Case Control ← Output Boundary (interface)
```

**Authentication & User Management**:
- Login, Register, CreateUser, UpdateUser, DeleteUser
- GetAllUsers, SearchUsers

**Product Management**:
- GetProductDetail, GetAllProducts, GetAllMotorbikes, GetAllAccessories
- SearchMotorbikes, SearchAccessories
- CreateMotorbike, UpdateMotorbike, DeleteMotorbike
- CreateAccessory, UpdateAccessory, DeleteAccessory
- ToggleProductVisibility

**Shopping Cart**:
- AddToCart, ViewCart, UpdateCartQuantity

**Order Management**:
- Checkout, ListAllOrders, GetOrderDetail
- CancelOrder, UpdateOrder, SearchOrders
- GetValidOrderStatuses, GetTopProducts

#### B. DTOs (Data Transfer Objects)
Mỗi use case có Input/Output Data:
```java
// Input Data
public class LoginInputData {
    public final String username;
    public final String password;
}

// Output Data
public class LoginOutputData {
    private boolean success;
    private Integer userId;
    private String errorCode;
    private String message;
}
```

#### C. Repository Ports (Interfaces)
```java
public interface UserRepository {
    Optional<TaiKhoan> findByUsername(String username);
    TaiKhoan save(TaiKhoan taiKhoan);
    void delete(Integer id);
}
```

---

### 3️⃣ ADAPTERS LAYER
**Trách nhiệm**: Chuyển đổi giữa external world và business logic

#### A. Controllers (REST API)
```java
@RestController
@RequestMapping("/api")
public class ProductController {
    private final GetProductDetailInputBoundary getProductDetailUseCase;
    
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Integer id) {
        GetProductDetailInputData inputData = new GetProductDetailInputData(id);
        getProductDetailUseCase.execute(inputData);
        
        ProductDetailViewModel viewModel = productDetailPresenter.getViewModel();
        return ResponseEntity.ok(viewModel);
    }
}
```

**Controllers phân loại**:
- `AuthController`: /api/login, /api/register
- `ProductController`: /api/products/*
- `CartController`: /api/cart/*
- `OrderController`: /api/orders/*
- `AdminUserController`: /api/admin/users/*
- `AdminProductController`: /api/admin/products/*
- `AdminOrderController`: /api/admin/orders/*

#### B. Presenters
Chuyển đổi Output Data → ViewModel:
```java
public class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel viewModel;
    
    @Override
    public void present(LoginOutputData outputData) {
        viewModel.setSuccess(outputData.isSuccess());
        viewModel.setUserId(outputData.getUserId());
        // ...
    }
}
```

#### C. Repository Adapters
Implement business repository interfaces:
```java
public class UserRepositoryAdapter implements UserRepository {
    private final TaiKhoanJpaRepository jpaRepository;
    
    @Override
    public Optional<TaiKhoan> findByUsername(String username) {
        return jpaRepository.findByTenDangNhap(username)
            .map(this::toDomain);
    }
    
    private TaiKhoan toDomain(TaiKhoanJpaEntity jpaEntity) {
        // Convert JPA Entity → Domain Entity
    }
}
```

---

### 4️⃣ INFRASTRUCTURE LAYER
**Trách nhiệm**: Cấu hình framework, database, external services

#### A. JPA Entities (Persistence)
```java
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoanJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTaiKhoan;
    
    @Column(unique = true)
    private String tenDangNhap;
    
    // JPA annotations, no business logic
}
```

#### B. Spring Configuration
```java
@Configuration
public class UseCaseConfig {
    @Bean
    public LoginInputBoundary loginUseCase(
            LoginOutputBoundary presenter,
            UserRepository userRepository) {
        return new LoginUseCaseControl(presenter, userRepository);
    }
    
    // 34+ bean definitions for all use cases
}
```

---

## 🔨 QUY TRÌNH XÂY DỰNG

### Bước 1: Thiết kế Domain Layer
1. Xác định các Entity chính: TaiKhoan, SanPham, GioHang, DonHang
2. Định nghĩa business rules trong entities
3. Tạo Value Objects (enums, immutable objects)
4. Định nghĩa Domain Exceptions

### Bước 2: Thiết kế Business Layer
1. Xác định các Use Cases từ requirements
2. Tạo Input/Output Boundaries (interfaces)
3. Tạo Input/Output DTOs
4. Định nghĩa Repository Ports (interfaces)
5. Implement Use Case Controls

### Bước 3: Thiết kế Adapters Layer
1. Tạo REST Controllers (API endpoints)
2. Implement Presenters (convert output)
3. Tạo ViewModels (for presentation)
4. Implement Repository Adapters

### Bước 4: Thiết kế Infrastructure Layer
1. Tạo JPA Entities (database mapping)
2. Tạo JPA Repositories (Spring Data)
3. Configure Spring Beans (UseCaseConfig)
4. Setup database connection

### Bước 5: Testing
1. Unit test cho Domain entities
2. Unit test cho Use Cases (mock repositories)
3. Integration test cho Controllers
4. End-to-end test

---

## 🎨 DESIGN PATTERNS SỬ DỤNG

1. **Dependency Inversion Principle**
   - Business định nghĩa interfaces
   - Infrastructure implement interfaces

2. **Repository Pattern**
   - Abstract data access
   - Business không biết database implementation

3. **Use Case Pattern (Interactor)**
   - Mỗi use case = 1 business operation
   - Single Responsibility

4. **Presenter Pattern**
   - Tách biệt presentation logic
   - ViewModels cho UI

5. **DTO Pattern**
   - Data transfer giữa các layers
   - Tránh coupling với entities

6. **Factory Pattern**
   - Spring Bean Factory (UseCaseConfig)
   - Tạo dependencies tự động

---

## ✅ ƯU ĐIỂM KIẾN TRÚC

1. **Testability**: Business logic độc lập, dễ test
2. **Flexibility**: Dễ thay đổi database/framework
3. **Maintainability**: Code rõ ràng, dễ maintain
4. **Scalability**: Dễ mở rộng tính năng mới
5. **Independence**: Domain không phụ thuộc framework
6. **Separation of Concerns**: Mỗi layer có trách nhiệm rõ ràng

---

## 📊 THỐNG KÊ DỰ ÁN

| Metric | Count |
|--------|-------|
| Total Java Files | 303 |
| Domain Entities | 16 |
| Use Cases | 34 |
| DTOs | 60+ |
| Controllers | 11 |
| Presenters | 34 |
| ViewModels | 34 |
| Repository Adapters | 6 |
| JPA Entities | 8 |
| Unit Tests | 228 |
| Test Coverage | 100% |

---

## 🚀 CHẠY ỨNG DỤNG

### Prerequisites
- Java 17+
- SQL Server
- Maven 3.6+

### Build & Run
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test
```

### API Endpoints
- `http://localhost:8080` - Main application
- `/api/login` - Authentication
- `/api/products` - Product listing
- `/api/cart` - Shopping cart
- `/api/orders` - Order management
- `/api/admin/*` - Admin operations
