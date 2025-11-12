# Clean Architecture - Motorbike Shop Project Structure

## ✅ Đã Setup

### 📁 Cấu trúc thư mục hoàn chỉnh

```
src/main/java/com/motorbike/
│
├── domain/                              ✅ LAYER 1: ENTITIES (Hoàn thành)
│   ├── entities/
│   │   ├── User.java                    ✅ Entity với business logic
│   │   ├── UserRole.java                ✅ Enum
│   │   ├── Product.java                 ✅ Entity với business logic
│   │   ├── ProductCategory.java         ✅ Enum
│   │   ├── Cart.java                    ✅ Entity với business logic
│   │   └── CartItem.java                ✅ Entity với business logic
│   │
│   └── exceptions/
│       ├── BusinessException.java       ✅ Base exception
│       ├── InvalidUserException.java    ✅
│       ├── InvalidProductException.java ✅
│       ├── InvalidCartException.java    ✅
│       └── AuthenticationException.java ✅
│
├── business/                            🔄 LAYER 2: USE CASES (Cần implement)
│   ├── dto/                             📝 Tạo DTOs cho từng use case
│   │   ├── login/
│   │   ├── register/
│   │   ├── productdetail/
│   │   ├── addtocart/
│   │   ├── viewcart/
│   │   ├── updatecart/
│   │   └── checkout/
│   │
│   ├── usecase/                         📝 Interfaces (Boundaries)
│   │   ├── LoginInputBoundary.java
│   │   ├── LoginOutputBoundary.java
│   │   └── ... (các boundaries khác)
│   │
│   ├── usecase/impl/                    📝 Use Case implementations
│   │   ├── LoginUseCaseImpl.java
│   │   ├── RegisterUseCaseImpl.java
│   │   ├── GetProductDetailUseCaseImpl.java
│   │   ├── AddToCartUseCaseImpl.java
│   │   ├── ViewCartUseCaseImpl.java
│   │   ├── UpdateCartQuantityUseCaseImpl.java
│   │   └── CheckoutUseCaseImpl.java
│   │
│   └── ports/repository/                📝 Repository interfaces
│       ├── UserRepository.java
│       ├── ProductRepository.java
│       └── CartRepository.java
│
├── adapters/                            🔄 LAYER 3: INTERFACE ADAPTERS
│   ├── presenters/                      📝 Presentation logic
│   │   ├── LoginPresenter.java
│   │   └── ... (các presenters khác)
│   │
│   ├── viewmodels/                      📝 View models
│   │   ├── LoginViewModel.java
│   │   └── ... (các viewmodels khác)
│   │
│   ├── controllers/
│   │   ├── web/                         📝 Servlet controllers
│   │   │   ├── LoginServlet.java
│   │   │   └── ... (các servlets khác)
│   │   └── api/                         📝 REST API controllers
│   │       ├── LoginRestController.java
│   │       └── ... (các REST APIs khác)
│   │
│   └── repositories/                    📝 Repository implementations
│       ├── UserRepositoryImpl.java
│       ├── ProductRepositoryImpl.java
│       └── CartRepositoryImpl.java
│
├── infrastructure/                      🔄 LAYER 4: FRAMEWORKS & DRIVERS
│   ├── persistence/jpa/
│   │   ├── entities/                    📝 JPA Entities
│   │   │   ├── UserJpaEntity.java
│   │   │   ├── ProductJpaEntity.java
│   │   │   ├── CartJpaEntity.java
│   │   │   └── CartItemJpaEntity.java
│   │   │
│   │   └── repositories/                📝 Spring Data JPA Repositories
│   │       ├── UserJpaRepository.java
│   │       ├── ProductJpaRepository.java
│   │       └── CartJpaRepository.java
│   │
│   └── config/                          📝 Configuration
│       ├── DatabaseConfig.java
│       └── DependencyInjection.java
│
└── main/
    └── MainApplication.java             📝 Application entry point
```

---

## 📊 Database Schema (Đã có)

### Tables:
1. **users**
   - id, email, username, password, phone_number
   - role (CUSTOMER/ADMIN), active
   - created_at, updated_at, last_login_at

2. **products**
   - id, name, description, price (DECIMAL 15,2)
   - image_url, specifications, category
   - stock_quantity, available
   - created_at, updated_at

3. **carts**
   - id, user_id, total_amount (DECIMAL 15,2)
   - created_at, updated_at

4. **cart_items**
   - id, cart_id, product_id
   - product_price (DECIMAL 15,2), quantity
   - subtotal (DECIMAL 15,2)

---

## 🎯 7 Use Cases Cần Implement

### ✅ Domain Entities (Hoàn thành)
- [x] User entity với validation logic
- [x] Product entity với stock management
- [x] Cart entity với item management
- [x] CartItem entity với quantity logic
- [x] All business exceptions

### 📝 Use Cases Cần Làm (7 use cases)

#### 1️⃣ **Use Case: Xem chi tiết sản phẩm**
**Business Rules:**
- Sản phẩm phải tồn tại
- Hiển thị đầy đủ thông tin
- Không yêu cầu đăng nhập

**Cần tạo:**
- `GetProductDetailInputData`, `GetProductDetailOutputData`
- `GetProductDetailInputBoundary`, `GetProductDetailOutputBoundary`
- `GetProductDetailUseCaseImpl`
- `GetProductDetailPresenter`, `GetProductDetailViewModel`
- `ProductDetailServlet` hoặc `ProductDetailRestController`

#### 2️⃣ **Use Case: Đăng nhập**
**Business Rules:**
- Email phải tồn tại
- Mật khẩu phải khớp
- Tạo session sau login
- Phân biệt role (customer/admin)

**Cần tạo:**
- `LoginInputData`, `LoginOutputData`
- `LoginInputBoundary`, `LoginOutputBoundary`
- `LoginUseCaseImpl`
- `LoginPresenter`, `LoginViewModel`
- `LoginServlet` hoặc `LoginRestController`

#### 3️⃣ **Use Case: Đăng ký tài khoản**
**Business Rules:**
- Email unique
- Password mã hóa
- Validation đầy đủ
- Default role = CUSTOMER

**Cần tạo:**
- `RegisterInputData`, `RegisterOutputData`
- `RegisterInputBoundary`, `RegisterOutputBoundary`
- `RegisterUseCaseImpl`
- `RegisterPresenter`, `RegisterViewModel`
- `RegisterServlet` hoặc `RegisterRestController`

#### 4️⃣ **Use Case: Thêm vào giỏ hàng**
**Business Rules:**
- Sản phẩm phải tồn tại và còn hàng
- Số lượng > 0 và <= tồn kho
- Nếu đã có trong giỏ thì cộng dồn
- Guest: lưu session, Customer: lưu DB

**Cần tạo:**
- `AddToCartInputData`, `AddToCartOutputData`
- `AddToCartInputBoundary`, `AddToCartOutputBoundary`
- `AddToCartUseCaseImpl`
- `AddToCartPresenter`, `AddToCartViewModel`
- `AddToCartServlet` hoặc `AddToCartRestController`

#### 5️⃣ **Use Case: Thanh toán**
**Business Rules:**
- **BẮT BUỘC đăng nhập**
- Giỏ hàng phải có sản phẩm
- Kiểm tra tồn kho
- Tính tổng tiền
- Trừ tồn kho
- Xóa giỏ hàng

**Cần tạo:**
- `CheckoutInputData`, `CheckoutOutputData`
- `CheckoutInputBoundary`, `CheckoutOutputBoundary`
- `CheckoutUseCaseImpl`
- `CheckoutPresenter`, `CheckoutViewModel`
- `CheckoutServlet` hoặc `CheckoutRestController`

#### 6️⃣ **Use Case: Xem giỏ hàng**
**Business Rules:**
- Guest: hiển thị từ session
- Customer: hiển thị từ DB
- Hiển thị thông tin chi tiết
- Tính tổng tiền

**Cần tạo:**
- `ViewCartInputData`, `ViewCartOutputData`
- `ViewCartInputBoundary`, `ViewCartOutputBoundary`
- `ViewCartUseCaseImpl`
- `ViewCartPresenter`, `ViewCartViewModel`
- `ViewCartServlet` hoặc `ViewCartRestController`

#### 7️⃣ **Use Case: Chỉnh số lượng sản phẩm trong giỏ hàng**
**Business Rules:**
- Số lượng >= 0
- Nếu = 0 thì xóa sản phẩm
- Kiểm tra tồn kho
- Tự động tính lại tổng tiền

**Cần tạo:**
- `UpdateCartQuantityInputData`, `UpdateCartQuantityOutputData`
- `UpdateCartQuantityInputBoundary`, `UpdateCartQuantityOutputBoundary`
- `UpdateCartQuantityUseCaseImpl`
- `UpdateCartQuantityPresenter`, `UpdateCartQuantityViewModel`
- `UpdateCartQuantityServlet` hoặc `UpdateCartQuantityRestController`

---

## 🔄 Data Flow Pattern

```
Request (HTTP/JSON)
    ↓
Controller/Servlet (Parse request)
    ↓ [InputData]
Use Case (Business logic orchestration)
    ↓ Call Entity methods
Entity (Pure business logic)
    ↓ Return result
Use Case (Create OutputData)
    ↓ [OutputData]
Presenter (Format for UI)
    ↓ Update ViewModel
Controller (Forward to view)
    ↓
Response (HTML/JSON)
```

---

## 📋 Next Steps

### Bước 1: Repository Interfaces (Use Case Layer)
Tạo interfaces trong `business/ports/repository/`:
- `UserRepository.java`
- `ProductRepository.java`
- `CartRepository.java`

### Bước 2: DTOs cho từng Use Case
Tạo InputData & OutputData cho 7 use cases

### Bước 3: Boundaries (Interfaces)
Tạo InputBoundary & OutputBoundary cho 7 use cases

### Bước 4: Use Case Implementations
Implement 7 use cases trong `business/usecase/impl/`

### Bước 5: Presenters & ViewModels
Tạo presenters và viewmodels cho từng use case

### Bước 6: Controllers
Tạo Servlets hoặc REST Controllers

### Bước 7: Infrastructure
- JPA Entities
- Repository Implementations
- Database Configuration

### Bước 8: Testing
- Unit tests cho Entities
- Integration tests cho Use Cases
- Controller tests

---

## 🎓 Clean Architecture Principles Applied

✅ **Dependency Rule**: Entities không phụ thuộc bất kỳ layer nào
✅ **Separation of Concerns**: Mỗi layer có trách nhiệm riêng
✅ **Business Logic in Entities**: User, Product, Cart có đầy đủ validation
✅ **Rich Domain Model**: Entities có behaviors, không phải chỉ data containers
✅ **Exception Handling**: Business exceptions riêng biệt cho từng domain

---

**Document Version**: 1.0  
**Last Updated**: 2025-11-12  
**Status**: Domain Layer Complete ✅ | Use Case Layer In Progress 🔄
