# JSB Clean Architecture - Motorbike Shop

Hệ thống quản lý cửa hàng xe máy được xây dựng theo **Clean Architecture** của Uncle Bob, tuân thủ nguyên lý **SOLID** và các best practices trong phát triển phần mềm doanh nghiệp.

## 📋 Tổng quan đề tài

Dự án xây dựng backend cho hệ thống bán hàng trực tuyến chuyên về xe máy và phụ kiện, với 7 use cases chính:

1. **Get Product Detail** - Xem chi tiết sản phẩm
2. **Login** - Đăng nhập với merge giỏ hàng
3. **Register** - Đăng ký tài khoản mới
4. **Add To Cart** - Thêm sản phẩm vào giỏ
5. **Checkout** - Thanh toán và tạo đơn hàng
6. **View Cart** - Xem giỏ hàng với cảnh báo tồn kho
7. **Update Cart Quantity** - Cập nhật số lượng trong giỏ

## 🏗️ Kiến trúc Clean Architecture

Dự án tuân thủ nghiêm ngặt **Clean Architecture** với 4 layers:

```
┌─────────────────────────────────────────┐
│          Presentation Layer             │
│     (Controllers, Views, DTOs)          │
└─────────────────────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│         Application Layer               │
│  (Use Cases, Input/Output Boundaries)   │
└─────────────────────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│           Domain Layer                  │
│  (Entities, Business Rules, Ports)      │
└─────────────────────────────────────────┘
               ↓
┌─────────────────────────────────────────┐
│        Infrastructure Layer             │
│  (Database, External Services, Web)     │
└─────────────────────────────────────────┘
```

### Dependency Rule
> "Dependencies chỉ trỏ vào trong, không bao giờ trỏ ra ngoài"

- ✅ Domain Layer **không phụ thuộc** vào bất kỳ layer nào
- ✅ Use Cases chỉ phụ thuộc vào Domain
- ✅ Infrastructure phụ thuộc vào Domain (thông qua interfaces)

## 🎯 SOLID Principles Applied

### 1️⃣ Single Responsibility Principle (SRP)
- Mỗi Use Case chỉ xử lý 1 nghiệp vụ duy nhất
- Entities chỉ chứa business logic, không có framework code
- Presenters chỉ lo format dữ liệu cho UI

### 2️⃣ Open/Close Principle (OCP)
- **ProductCategory**: Abstract class cho phép mở rộng (MotorcycleCategory, AccessoryCategory)
- **UserRole**: Class-based system với Registry, dễ dàng thêm role mới
- Không cần sửa code cũ khi thêm loại sản phẩm hoặc role mới

### 3️⃣ Liskov Substitution Principle (LSP)
- Các concrete categories có thể thay thế ProductCategory
- Repository implementations có thể thay thế interfaces

### 4️⃣ Interface Segregation Principle (ISP)
- Input/Output Boundaries tách biệt cho từng use case
- Repository interfaces chỉ define methods cần thiết

### 5️⃣ Dependency Inversion Principle (DIP)
- Use Cases phụ thuộc vào Repository **interfaces** (ports)
- Infrastructure implement các interfaces này
- Domain không biết gì về database, framework

## 📦 Cấu trúc Project

```
src/main/java/com/motorbike/
├── domain/                          # Domain Layer (Core Business)
│   ├── entities/                    # Business Entities
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderStatus.java
│   │   ├── ProductCategory.java    # Abstract class
│   │   ├── MotorcycleCategory.java
│   │   ├── AccessoryCategory.java
│   │   ├── ProductCategoryRegistry.java
│   │   ├── UserRole.java
│   │   └── UserRoleRegistry.java
│   ├── exceptions/                  # Business Exceptions
│   │   ├── InvalidUserException.java
│   │   ├── InvalidProductException.java
│   │   └── InvalidCartException.java
│   ├── repositories/                # Repository Interfaces (Ports)
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   ├── CartRepository.java
│   │   └── OrderRepository.java
│   ├── values/                      # Value Objects
│   │   └── Timestamp.java
│   └── validation/                  # Validation Abstractions
│       └── ValidationService.java
│
├── business/                        # Application Layer (Use Cases)
│   ├── dto/                         # Data Transfer Objects
│   │   ├── productdetail/
│   │   ├── login/
│   │   ├── register/
│   │   ├── addtocart/
│   │   ├── checkout/
│   │   ├── viewcart/
│   │   └── updatecart/
│   ├── usecase/                     # Use Case Interfaces (Boundaries)
│   │   ├── GetProductDetailInputBoundary.java
│   │   ├── GetProductDetailOutputBoundary.java
│   │   └── ... (các use cases khác)
│   ├── usecase/impl/                # Use Case Implementations
│   │   ├── GetProductDetailUseCaseImpl.java
│   │   ├── LoginUseCaseImpl.java
│   │   ├── RegisterUseCaseImpl.java
│   │   ├── AddToCartUseCaseImpl.java
│   │   ├── CheckoutUseCaseImpl.java
│   │   ├── ViewCartUseCaseImpl.java
│   │   └── UpdateCartQuantityUseCaseImpl.java
│   └── ports/                       # Business Layer Ports
│       └── repository/              # Repository interfaces for use cases
│
├── adapters/                        # Adapters Layer
│   ├── presenters/                  # Output adapters (format data)
│   │   ├── ProductDetailPresenter.java
│   │   ├── LoginPresenter.java
│   │   ├── RegisterPresenter.java
│   │   ├── AddToCartPresenter.java
│   │   ├── CheckoutPresenter.java
│   │   ├── ViewCartPresenter.java
│   │   └── UpdateCartQuantityPresenter.java
│   └── viewmodels/                  # View Models (UI-ready data)
│       ├── ProductDetailViewModel.java
│       ├── LoginViewModel.java
│       ├── RegisterViewModel.java
│       ├── AddToCartViewModel.java
│       ├── CheckoutViewModel.java
│       ├── ViewCartViewModel.java
│       └── UpdateCartQuantityViewModel.java
│
└── infrastructure/                  # Infrastructure Layer
    └── validation/
        └── RegexValidationService.java

src/test/java/com/motorbike/
└── business/usecase/impl/           # Unit Tests
    ├── GetProductDetailUseCaseImplTest.java    # 10 tests
    ├── LoginUseCaseImplTest.java               # 18 tests
    ├── RegisterUseCaseImplTest.java            # 23 tests
    ├── AddToCartUseCaseImplTest.java           # 16 tests
    ├── CheckoutUseCaseImplTest.java            # 16 tests
    ├── ViewCartUseCaseImplTest.java            # 14 tests
    └── UpdateCartQuantityUseCaseImplTest.java  # 15 tests
```

## 🎯 Chi tiết 7 Use Cases

### 1. Get Product Detail (10 tests)
**Mục đích**: Xem thông tin chi tiết sản phẩm

**Business Rules**:
- Kiểm tra product tồn tại
- Hiển thị đầy đủ thông tin: tên, giá, mô tả, hình ảnh, thông số kỹ thuật
- Hiển thị trạng thái tồn kho
- Format giá theo VND

**Input**: `productId`  
**Output**: Thông tin sản phẩm đầy đủ với format VND

---

### 2. Login (18 tests)
**Mục đích**: Đăng nhập và merge giỏ hàng guest vào user

**Business Rules**:
- Validate email và password
- Check user tồn tại và active
- Merge giỏ hàng guest vào giỏ user (nếu có)
- Xóa giỏ guest sau khi merge
- Cập nhật thời gian login cuối

**Input**: `email`, `password`, `guestCartId` (optional)  
**Output**: User info + access token + cart status

---

### 3. Register (23 tests)
**Mục đích**: Đăng ký tài khoản mới

**Business Rules**:
- Validate email format (regex)
- Validate username (3-50 ký tự)
- Validate password (>=6 ký tự)
- Validate số điện thoại VN (optional)
- Check email chưa tồn tại
- Check username chưa tồn tại
- Tạo user mới với role CUSTOMER
- Hash password (trong implementation)

**Input**: `email`, `username`, `password`, `phoneNumber`, `confirmPassword`  
**Output**: User info với role, createdAt

---

### 4. Add To Cart (16 tests)
**Mục đích**: Thêm sản phẩm vào giỏ hàng

**Business Rules**:
- Check product tồn tại và available
- Check stock đủ cho quantity yêu cầu
- Nếu product đã có trong cart → tăng quantity
- Nếu product chưa có → thêm mới
- Support cả logged-in user và guest user
- Tính toán lại tổng tiền giỏ hàng

**Input**: `userId/guestCartId`, `productId`, `quantity`  
**Output**: Cart info updated với tổng tiền VND

---

### 5. Checkout (16 tests)
**Mục đích**: Thanh toán và tạo đơn hàng

**Business Rules**:
- User phải đăng nhập
- Cart phải có items
- Check tất cả products tồn tại và available
- Validate stock cho tất cả items
- Require shipping address
- Tạo order với status PENDING
- Tạo order items từ cart items
- **Trừ stock** cho từng sản phẩm
- **Xóa giỏ hàng** sau khi đặt hàng thành công

**Input**: `userId`, `shippingAddress`, `customerPhone`  
**Output**: Order info đầy đủ với items, totals, shipping info

---

### 6. View Cart (14 tests)
**Mục đích**: Xem giỏ hàng với cảnh báo tồn kho

**Business Rules**:
- Support cả guest và logged-in user
- Hiển thị danh sách items
- Tính tổng số lượng và tổng tiền
- **Cảnh báo stock**: out of stock, insufficient stock, low stock
- Handle empty cart gracefully

**Input**: `userId` hoặc `guestCartId`  
**Output**: Cart items với stock warnings, tổng tiền VND

---

### 7. Update Cart Quantity (15 tests)
**Mục đích**: Cập nhật số lượng sản phẩm trong giỏ

**Business Rules**:
- Quantity phải >= 0
- **Quantity = 0** → xóa item khỏi cart
- Check stock availability
- Quantity không được vượt quá stock
- Update cart và recalculate totals
- Support cả logged-in và guest

**Input**: `userId/guestCartId`, `productId`, `newQuantity`  
**Output**: Updated cart với old/new quantity, totals

---

## ✅ Test Coverage

**Tổng số tests**: **112 tests**

| Use Case | Tests | Status |
|----------|-------|--------|
| Get Product Detail | 10 | ✅ Passing |
| Login | 18 | ✅ Passing |
| Register | 23 | ✅ Passing |
| Add To Cart | 16 | ✅ Passing |
| Checkout | 16 | ✅ Passing |
| View Cart | 14 | ✅ Passing |
| Update Cart Quantity | 15 | ✅ Passing |

```bash
mvn test
# Tests run: 112, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## 🛠️ Technologies

- **Java 17**
- **Maven** - Build tool
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Clean Architecture** - Architectural pattern
- **SOLID Principles** - Design principles

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build & Test
```bash
# Compile
mvn clean compile

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=LoginUseCaseImplTest

# Package
mvn package
```

## 📐 Design Patterns Used

1. **Repository Pattern** - Abstraction cho data access
2. **Factory Pattern** - Registry cho Categories và Roles
3. **Dependency Injection** - Inject dependencies qua constructor
4. **Boundary Pattern** - Input/Output boundaries cho use cases
5. **Presenter Pattern** - Format data cho UI layer
6. **DTO Pattern** - Transfer data between layers

## 🎨 Code Style

- **Comments**: Tiếng Việt thường, đơn giản, có thể viết tắt
- **Naming**: camelCase cho methods/variables, PascalCase cho classes
- **Package**: Phân chia rõ ràng theo layers
- **No Framework Dependencies** trong Domain layer

## 📈 Key Features

✅ **Pure Domain Layer** - Không phụ thuộc framework  
✅ **Testability** - 112 unit tests với Mockito  
✅ **Extensibility** - Open/Close Principle cho categories và roles  
✅ **Maintainability** - Clear separation of concerns  
✅ **Business-focused** - Use cases phản ánh đúng business requirements  
✅ **Vietnamese Localization** - VND currency, Vietnamese messages  

## 📝 Business Rules Summary

### Product Rules
- Xe máy: 5 triệu - 500 triệu VND, bắt buộc có thông số kỹ thuật
- Phụ kiện: 10k - 50 triệu VND, không bắt buộc thông số

### Cart Rules
- Guest user có thể có giỏ hàng riêng
- Khi login, merge giỏ guest vào giỏ user
- Auto-remove item khi quantity = 0
- Cảnh báo khi stock < 10 (low stock)

### Order Rules
- Chỉ logged-in user mới checkout được
- Order status: PENDING → CONFIRMED → PROCESSING → SHIPPING → DELIVERED
- Trừ stock ngay khi đặt hàng thành công
- Xóa cart sau khi tạo order

### User Rules
- Email unique
- Username unique, 3-50 ký tự
- Password >= 6 ký tự
- Số điện thoại VN format (optional)
- Default role: CUSTOMER

## 🔮 Future Enhancements

- [ ] Add Payment Gateway integration
- [ ] Implement Order status tracking
- [ ] Add Product search and filtering
- [ ] Implement Voucher/Discount system
- [ ] Add Email notification service
- [ ] Implement Admin dashboard
- [ ] Add API layer (REST/GraphQL)

## 👨‍💻 Author

**Hayamij (Nguyen Quang Tuan Phuong)**  
Clean Architecture Implementation - Motorbike Shop System

## 📄 License

MIT License - see LICENSE file for details

---

⭐ **Built with Clean Architecture principles and SOLID design patterns**
