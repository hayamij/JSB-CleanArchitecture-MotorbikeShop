# Clean Architecture - 4 Layers Structure

## 📐 Kiến trúc tổng quan

Dự án được tổ chức theo Clean Architecture với 4 tầng rõ ràng:

```
com.motorbike/
├── business/           # Business Layer (Core)
├── interfaceadapters/  # Interface Adapters Layer  
├── persistence/        # Persistence Layer
└── frameworks/         # Frameworks Layer
```

## ✅ Implemented Use Cases

### Use Case 1: Xem chi tiết sản phẩm (Get Product Detail)
- **Endpoint**: `GET /api/products/{id}`
- **Actor**: Guest, Customer, Admin
- **Status**: ✅ Completed

### Use Case 2: Đăng nhập (Login)
- **Endpoint**: `POST /api/auth/login`
- **Actor**: Customer, Admin
- **Status**: ✅ Completed

### Use Case 3: Đăng ký tài khoản (Register)
- **Endpoint**: `POST /api/auth/register`
- **Actor**: Guest
- **Status**: ✅ Completed

### Use Case 4: Thêm vào giỏ hàng (Add to Cart)
- **Endpoint**: `POST /api/cart/add`
- **Actor**: Guest, Customer, Admin
- **Status**: ✅ Completed

### Use Case 6: Xem giỏ hàng (View Cart)
- **Endpoint**: `GET /api/cart/{userId}`
- **Actor**: Guest, Customer, Admin
- **Status**: ✅ Completed

### Use Case 7: Chỉnh số lượng sản phẩm trong giỏ hàng (Update Cart Quantity)
- **Endpoint**: `PUT /api/cart/update`
- **Actor**: Guest, Customer, Admin
- **Status**: ✅ Completed

### Use Case 5: Thanh toán (Checkout)
- **Endpoint**: `POST /api/orders/checkout`
- **Actor**: Customer
- **Status**: ✅ Completed

---

## 🏗️ Chi tiết từng tầng

### 1. **Business Layer** (`business/`)
**Mục đích**: Chứa toàn bộ business logic và domain entities

**Cấu trúc**:
```
business/
├── entity/              # Domain entities (Product, Order, Customer...)
├── repository/          # Repository interfaces (contracts)
└── usecase/            # Use cases và business logic
    ├── GetProductDetailUseCase.java
    └── impl/           # Use case implementations
```

**Đặc điểm**:
- ✅ Không phụ thuộc vào framework hay database
- ✅ Chứa business rules thuần túy
- ✅ Entities có business logic methods
- ✅ Repository chỉ là interfaces

**Ví dụ**:
- `Product.java` - Domain entity với methods: `isInStock()`, `canPurchase()`, `decreaseStock()`
- `ProductRepository.java` - Interface định nghĩa contract
- `GetProductDetailUseCase.java` - Use case để lấy chi tiết sản phẩm

---

### 2. **Interface Adapters Layer** (`interfaceadapters/`)
**Mục đích**: Chuyển đổi dữ liệu giữa use cases và external interfaces

**Cấu trúc**:
```
interfaceadapters/
├── controller/         # REST Controllers
├── dto/               # Data Transfer Objects
└── mapper/            # Mappers giữa DTO và Use Case responses
```

**Đặc điểm**:
- ✅ Xử lý HTTP requests/responses
- ✅ Convert giữa DTOs và domain objects
- ✅ Validation input từ client
- ✅ Format output cho client

**Ví dụ**:
- `ProductController.java` - REST API endpoints
- `ProductDTO.java` - DTO cho API responses
- `ProductMapper.java` - Convert giữa Use Case Response và DTO

---

### 3. **Persistence Layer** (`persistence/`)
**Mục đích**: Xử lý mọi thứ liên quan đến database

**Cấu trúc**:
```
persistence/
├── adapter/           # Repository implementations
├── entity/           # JPA entities (database mapping)
├── mapper/           # Mappers giữa JPA entities và domain entities
└── repository/       # Spring Data JPA repositories
```

**Đặc điểm**:
- ✅ JPA entities với annotations
- ✅ Spring Data JPA repositories
- ✅ Implement business repository interfaces
- ✅ Convert giữa JPA entities và domain entities

**Ví dụ**:
- `ProductJpaEntity.java` - JPA entity với `@Entity`, `@Table`
- `ProductJpaRepository.java` - Spring Data JPA repository
- `ProductEntityMapper.java` - Convert giữa JPA entity và domain entity
- `ProductRepositoryAdapter.java` - Implements `ProductRepository` interface

---

### 4. **Frameworks Layer** (`frameworks/`)
**Mục đích**: Cấu hình frameworks và các thành phần liên quan đến Spring Boot

**Cấu trúc**:
```
frameworks/
├── config/           # Spring configurations
└── spring/          # Spring Boot main application
```

**Đặc điểm**:
- ✅ Spring Boot configuration
- ✅ Main application class
- ✅ Data initialization
- ✅ Framework-specific settings

**Ví dụ**:
- `MainApplication.java` - Spring Boot entry point
- `ServletInitializer.java` - WAR deployment support
- `DataInitializer.java` - Sample data loading

---

## 🔄 Luồng dữ liệu (Data Flow)

```
HTTP Request
    ↓
[Interface Adapters Layer]
    Controller → DTO
    ↓
[Business Layer]  
    Use Case → Domain Entity → Repository Interface
    ↓
[Persistence Layer]
    Repository Adapter → JPA Entity → Database
    ↓
[Frameworks Layer]
    Spring Data JPA → Hibernate → SQL
```

### Ví dụ cụ thể: GET /api/products/{id}

1. **Client** gửi HTTP GET request
2. **ProductController** (Interface Adapters) nhận request
3. **Controller** tạo `ProductDetailRequest` và gọi Use Case
4. **GetProductDetailUseCaseImpl** (Business) xử lý business logic
5. **Use Case** gọi `ProductRepository.findById()`
6. **ProductRepositoryAdapter** (Persistence) implements method
7. **Adapter** gọi `ProductJpaRepository` (Spring Data)
8. **JPA Repository** query database và trả về `ProductJpaEntity`
9. **ProductEntityMapper** convert JPA entity → Domain entity
10. **Use Case** nhận domain entity và tạo Response
11. **ProductMapper** convert Response → DTO
12. **Controller** trả về ResponseEntity<ProductDTO> cho client

---

## 📦 Dependency Flow (Nguyên tắc phụ thuộc)

```
Frameworks Layer
    ↓ depends on
Interface Adapters Layer
    ↓ depends on
Business Layer ← Persistence Layer
    (Core - không phụ thuộc ai)
```

**Quy tắc vàng**: 
- Business Layer KHÔNG được phụ thuộc vào bất kỳ tầng nào khác
- Các tầng ngoài CHỈ được phụ thuộc vào Business Layer
- Sử dụng Dependency Inversion Principle (DIP)

---

## 🎯 Lợi ích của kiến trúc này

1. **Separation of Concerns**: Mỗi tầng có trách nhiệm riêng biệt
2. **Testability**: Dễ dàng test từng tầng độc lập
3. **Maintainability**: Dễ bảo trì và mở rộng
4. **Framework Independence**: Business logic không phụ thuộc framework
5. **Database Independence**: Có thể thay đổi database dễ dàng
6. **UI Independence**: Có thể thay đổi UI/API dễ dàng

---

## 🚀 Cách thêm tính năng mới

### Ví dụ: Thêm tính năng "Tìm kiếm sản phẩm"

1. **Business Layer**: Tạo use case
```java
business/usecase/SearchProductsUseCase.java
business/usecase/impl/SearchProductsUseCaseImpl.java
```

2. **Business Layer**: Thêm method vào repository interface
```java
business/repository/ProductRepository.java
    List<Product> searchByName(String keyword);
```

3. **Persistence Layer**: Implement repository method
```java
persistence/repository/ProductJpaRepository.java
    List<ProductJpaEntity> findByNameContaining(String keyword);

persistence/adapter/ProductRepositoryAdapter.java
    @Override
    public List<Product> searchByName(String keyword) {
        return jpaRepository.findByNameContaining(keyword).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
```

4. **Interface Adapters Layer**: Tạo controller endpoint
```java
interfaceadapters/controller/ProductController.java
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String q) {
        // Call use case and return results
    }
```

---

## 📝 Lưu ý quan trọng

- **Business entities** và **JPA entities** là KHÁC NHAU
  - Business entities: Pure Java, business logic
  - JPA entities: Có annotations, mapping database
  
- **Mappers** rất quan trọng để convert giữa các layers

- **Use Cases** chứa business logic, KHÔNG có framework code

- **Controllers** chỉ xử lý HTTP, không có business logic

---

## 🔧 Configuration

Main application class đã được di chuyển:
```
frameworks/spring/MainApplication.java
```

Ensure correct package scanning:
```java
@ComponentScan(basePackages = "com.motorbike")
@EnableJpaRepositories(basePackages = "com.motorbike.persistence.repository")
@EntityScan(basePackages = "com.motorbike.persistence.entity")
```

---

# 📚 USE CASES DOCUMENTATION

## Use Case 1: Xem chi tiết sản phẩm (Get Product Detail)

### Overview
- **Actor**: Guest, Customer, Admin
- **Preconditions**: Product ID must exist
- **Postconditions**: Product details are displayed

### API Endpoint
```http
GET /api/products/{id}
Accept: application/json
```

### Request Example
```bash
curl http://localhost:8080/api/products/1
```

### Success Response (200 OK)
```json
{
  "id": 1,
  "name": "Honda Wave RSX",
  "description": "Xe số tiết kiệm nhiên liệu...",
  "price": 38000000,
  "imageUrl": "/images/honda-wave-rsx.jpg",
  "specifications": "{\"engine\":\"110cc\",...}",
  "category": "MOTORCYCLE",
  "stockQuantity": 15,
  "available": true,
  "inStock": true
}
```

### Error Responses
- **404 Not Found**: Product does not exist
- **400 Bad Request**: Invalid product ID
- **500 Internal Server Error**: Server error

### Files Implemented
**Business Layer:**
- `business/entity/Product.java`
- `business/repository/ProductRepository.java`
- `business/usecase/GetProductDetailUseCase.java`
- `business/usecase/impl/GetProductDetailUseCaseImpl.java`
- `business/usecase/impl/ProductNotFoundException.java`

**Interface Adapters:**
- `interfaceadapters/controller/ProductController.java`
- `interfaceadapters/dto/ProductDTO.java`
- `interfaceadapters/mapper/ProductMapper.java`

**Persistence:**
- `persistence/entity/ProductJpaEntity.java`
- `persistence/repository/ProductJpaRepository.java`
- `persistence/mapper/ProductEntityMapper.java`
- `persistence/adapter/ProductRepositoryAdapter.java`

---

## Use Case 2: Đăng nhập (Login)

### Overview
- **Actor**: Customer, Admin (not logged in)
- **Preconditions**: User has valid account
- **Postconditions**: User is authenticated and receives token

### API Endpoint
```http
POST /api/auth/login
Content-Type: application/json
```

### Request Example
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@motorbike.com","password":"password123"}'
```

### Request Body
```json
{
  "email": "customer@motorbike.com",
  "password": "password123"
}
```

### Success Response (200 OK)
```json
{
  "userId": 1,
  "email": "customer@motorbike.com",
  "username": "customer1",
  "role": "CUSTOMER",
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "success": true,
  "message": "Login successful"
}
```

### Error Responses
- **401 Unauthorized**: Invalid email or password
- **403 Forbidden**: User account is not active
- **400 Bad Request**: Invalid input data
- **500 Internal Server Error**: Server error

### Test Credentials
| Email | Password | Role |
|-------|----------|------|
| customer@motorbike.com | password123 | CUSTOMER |
| admin@motorbike.com | admin123 | ADMIN |
| john.doe@example.com | john123 | CUSTOMER |

### Files Implemented
**Business Layer:**
- `business/entity/User.java`
- `business/repository/UserRepository.java`
- `business/usecase/LoginUseCase.java`
- `business/usecase/impl/LoginUseCaseImpl.java`
- `business/usecase/impl/InvalidCredentialsException.java`
- `business/usecase/impl/UserNotActiveException.java`

**Interface Adapters:**
- `interfaceadapters/controller/AuthController.java` (login method)
- `interfaceadapters/dto/LoginRequestDTO.java`
- `interfaceadapters/dto/LoginResponseDTO.java`
- `interfaceadapters/mapper/LoginMapper.java`

**Persistence:**
- `persistence/entity/UserJpaEntity.java`
- `persistence/repository/UserJpaRepository.java`
- `persistence/mapper/UserEntityMapper.java`
- `persistence/adapter/UserRepositoryAdapter.java`

### Business Logic
1. Find user by email
2. Check if user is active
3. Verify password (demo: plain text, production: BCrypt)
4. Update last login timestamp
5. Generate authentication token (demo: UUID, production: JWT)
6. Return user details and token

---

## Use Case 3: Đăng ký tài khoản (Register)

### Overview
- **Actor**: Guest (not logged in)
- **Preconditions**: User does not have an account
- **Postconditions**: New user account is created

### API Endpoint
```http
POST /api/auth/register
Content-Type: application/json
```

### Request Example
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "username": "newuser",
    "password": "password123",
    "phoneNumber": "0912345678"
  }'
```

### Request Body
```json
{
  "email": "newuser@example.com",
  "username": "newuser",
  "password": "password123",
  "phoneNumber": "0912345678"
}
```

### Validation Rules
- **Email**: Required, must be valid email format, must be unique
- **Username**: Required, must be unique
- **Password**: Required, minimum 6 characters
- **Phone Number**: Optional

### Success Response (201 Created)
```json
{
  "userId": 4,
  "email": "newuser@example.com",
  "username": "newuser",
  "role": "CUSTOMER",
  "success": true,
  "message": "Registration successful"
}
```

### Error Responses
- **409 Conflict**: Email or username already exists
  ```json
  {
    "success": false,
    "message": "Email already registered: newuser@example.com"
  }
  ```
  ```json
  {
    "success": false,
    "message": "Username already taken: newuser"
  }
  ```
- **400 Bad Request**: Invalid input data
  ```json
  {
    "success": false,
    "message": "Password must be at least 6 characters"
  }
  ```
- **500 Internal Server Error**: Server error

### Files Implemented
**Business Layer:**
- `business/usecase/RegisterUseCase.java`
- `business/usecase/impl/RegisterUseCaseImpl.java`
- `business/usecase/impl/EmailAlreadyExistsException.java`
- `business/usecase/impl/UsernameAlreadyExistsException.java`

**Interface Adapters:**
- `interfaceadapters/controller/AuthController.java` (register method)
- `interfaceadapters/dto/RegisterRequestDTO.java`
- `interfaceadapters/dto/RegisterResponseDTO.java`
- `interfaceadapters/mapper/RegisterMapper.java`

**Persistence:**
- Reuses User entities and repositories from Use Case 2

### Business Logic
1. Validate input (email format, password length, etc.)
2. Check if email already exists
3. Check if username already exists
4. Create new User entity with default role "CUSTOMER"
5. Hash password (demo: plain text, production: BCrypt)
6. Save user to database
7. Return registration result

### Flow
```
Guest → POST /api/auth/register
    ↓
AuthController validates input
    ↓
Create RegisterRequest
    ↓
RegisterUseCaseImpl.execute()
    ├─ Check email exists → throw EmailAlreadyExistsException (409)
    ├─ Check username exists → throw UsernameAlreadyExistsException (409)
    ├─ Create User entity
    ├─ Hash password
    └─ Save to database
    ↓
RegisterMapper.toDTO()
    ↓
Return 201 Created with user details
```

---

## 🔒 Security Notes

### Current Implementation (Demo)
⚠️ **WARNING**: Current implementation is for DEMONSTRATION purposes only!

- Passwords stored in **plain text**
- Simple **UUID** token generation
- No token **expiration**
- No **rate limiting**
- No **CSRF** protection
- No **email verification**

### Production Recommendations

#### 1. Password Hashing
```java
// Use BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(plainPassword);
boolean matches = encoder.matches(plainPassword, hashedPassword);
```

#### 2. JWT Tokens
```java
// Use JWT with expiration
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

String token = Jwts.builder()
    .setSubject(user.getEmail())
    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
    .signWith(SignatureAlgorithm.HS512, secretKey)
    .compact();
```

#### 3. Spring Security
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configure authentication, authorization, CORS, CSRF
}
```

#### 4. Email Verification
```java
// Send verification email after registration
// User account inactive until email verified
```

#### 5. Rate Limiting
```java
// Limit login/register attempts
@RateLimiter(name = "auth", fallbackMethod = "authFallback")
```

---

## Use Case 4: Thêm vào giỏ hàng (Add to Cart)

### Mô tả
Cho phép người dùng (Guest/Customer/Admin) thêm sản phẩm vào giỏ hàng với số lượng mong muốn.

### API Endpoint
```
POST /api/cart/add
Content-Type: application/json
```

### Request Body
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

### Response Examples

#### Success (200 OK)
```json
{
  "cart": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Honda Wave RSX",
        "productPrice": 38000000,
        "quantity": 2,
        "subtotal": 76000000,
        "addedAt": "2025-11-10T08:45:30"
      }
    ],
    "totalAmount": 76000000,
    "totalItems": 2,
    "createdAt": "2025-11-10T08:45:30",
    "updatedAt": "2025-11-10T08:45:30"
  },
  "message": "Added 2 x Honda Wave RSX to cart successfully",
  "success": true
}
```

#### Product Not Found (404)
```json
{
  "success": false,
  "message": "Product not found with ID: 999"
}
```

#### Out of Stock (409)
```json
{
  "success": false,
  "message": "Product 'Honda Wave RSX' is out of stock. Requested: 100, Available: 15"
}
```

#### Invalid Quantity (400)
```json
{
  "success": false,
  "message": "Quantity must be greater than 0"
}
```

### Business Logic

#### 1. Product Validation
- Kiểm tra product có tồn tại không
- Kiểm tra product có available không
- Kiểm tra stock quantity đủ không

#### 2. Cart Management
- Nếu user chưa có cart → tạo cart mới
- Nếu product đã có trong cart → tăng quantity
- Nếu product chưa có → thêm cart item mới

#### 3. Calculation
- Tính subtotal cho mỗi item: `price × quantity`
- Tính total amount của cart: `sum(all subtotals)`
- Cập nhật total item count

### Architecture Implementation

#### Business Layer
```
business/
├── entity/
│   ├── Cart.java                    # Domain entity với business logic
│   └── CartItem.java                # Domain entity cho item
├── repository/
│   └── CartRepository.java          # Interface (contract)
├── exception/
│   ├── ProductNotFoundException.java
│   └── ProductOutOfStockException.java
└── usecase/
    ├── AddToCartUseCase.java        # Interface
    └── impl/
        └── AddToCartUseCaseImpl.java # Implementation
```

**Cart.java - Business Methods**:
- `calculateTotalAmount()` - Tính tổng tiền
- `getTotalItemCount()` - Đếm tổng số item
- `isEmpty()` - Kiểm tra cart rỗng
- `hasProduct(productId)` - Kiểm tra product đã có
- `addItem(item)` - Thêm/merge item
- `removeItem(productId)` - Xóa item
- `clear()` - Xóa toàn bộ cart

**CartItem.java - Business Methods**:
- `calculateSubtotal()` - Tính subtotal
- `hasValidQuantity()` - Validate quantity
- `updateQuantity(newQuantity)` - Cập nhật số lượng
- `increaseQuantity(amount)` - Tăng số lượng

#### Interface Adapters Layer
```
interfaceadapters/
├── controller/
│   └── CartController.java           # REST API
├── dto/
│   ├── CartDTO.java
│   ├── CartItemDTO.java
│   ├── AddToCartRequestDTO.java
│   └── AddToCartResponseDTO.java
└── mapper/
    └── CartDTOMapper.java            # Domain ↔ DTO
```

#### Persistence Layer
```
persistence/
├── entity/
│   ├── CartJpaEntity.java           # JPA entity
│   └── CartItemJpaEntity.java       # JPA entity
├── repository/
│   └── CartJpaRepository.java       # Spring Data JPA
├── mapper/
│   └── CartMapper.java              # Domain ↔ JPA
└── adapter/
    └── CartRepositoryAdapter.java   # Repository implementation
```

#### Frameworks Layer
```
frameworks/
└── config/
    └── CartConfig.java              # Spring Bean configuration
```

### Database Design

#### Table: carts
```sql
CREATE TABLE carts (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id BIGINT NOT NULL UNIQUE,
    total_amount DECIMAL(10,2) DEFAULT 0,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### Table: cart_items
```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name NVARCHAR(255) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    subtotal DECIMAL(10,2) NOT NULL,
    added_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE (cart_id, product_id)
);
```

### Testing Commands

```bash
# Success - Add product to cart
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 2
  }'

# Success - Add same product again (merge quantity)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1
  }'

# Fail - Product not found
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 999,
    "quantity": 1
  }'

# Fail - Out of stock
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1000
  }'

# Fail - Invalid quantity
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 0
  }'
```

### Business Rules

1. **Guest Users**: 
   - Spec yêu cầu redirect to login
   - Implementation: Cần userId trong request (sau khi login)

2. **Stock Validation**:
   - Kiểm tra `product.canPurchase(quantity)`
   - Throw `ProductOutOfStockException` nếu không đủ hàng

3. **Cart Merging**:
   - Nếu product đã có trong cart → `increaseQuantity()`
   - Nếu chưa có → add new `CartItem`

4. **Auto Calculation**:
   - Subtotal tự động tính bằng `@PrePersist` và `@PreUpdate`
   - Total amount tự động aggregate từ các items

### Error Handling

| Error | HTTP Code | Message |
|-------|-----------|---------|
| Missing userId | 400 | "User ID is required" |
| Missing productId | 400 | "Product ID is required" |
| Invalid quantity | 400 | "Quantity must be greater than 0" |
| Product not found | 404 | "Product not found with ID: {id}" |
| Out of stock | 409 | "Product '{name}' is out of stock. Requested: {req}, Available: {avail}" |
| Server error | 500 | "An error occurred: {message}" |

---

## Use Case 6: Xem giỏ hàng (View Cart)

### Mô tả
Cho phép người dùng xem danh sách sản phẩm trong giỏ hàng của mình bao gồm thông tin chi tiết và tổng tiền.

### API Endpoint
```
GET /api/cart/{userId}
```

### Request Parameters
- **userId** (path parameter): ID của user cần xem giỏ hàng

### Response Examples

#### Success - Cart has items (200 OK)
```json
{
  "cart": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Honda Wave RSX",
        "productPrice": 38000000,
        "quantity": 2,
        "subtotal": 76000000,
        "addedAt": "2025-11-10T09:00:00"
      },
      {
        "id": 2,
        "productId": 2,
        "productName": "Yamaha Exciter 155",
        "productPrice": 47000000,
        "quantity": 1,
        "subtotal": 47000000,
        "addedAt": "2025-11-10T09:05:00"
      }
    ],
    "totalAmount": 123000000,
    "totalItems": 3,
    "createdAt": "2025-11-10T09:00:00",
    "updatedAt": "2025-11-10T09:05:00"
  },
  "isEmpty": false,
  "message": "Cart has 3 item(s), Total: 123000000 VND"
}
```

#### Success - Empty cart (200 OK)
```json
{
  "cart": {
    "id": null,
    "userId": 1,
    "items": [],
    "totalAmount": 0,
    "totalItems": 0,
    "createdAt": "2025-11-10T09:00:00",
    "updatedAt": "2025-11-10T09:00:00"
  },
  "isEmpty": true,
  "message": "Your cart is empty"
}
```

#### Invalid User ID (400)
```json
{
  "success": false,
  "message": "User ID is required"
}
```

### Business Logic

#### 1. Cart Retrieval
- Tìm cart theo userId
- Nếu không tìm thấy → tạo empty cart mới
- Load tất cả cart items với eager fetching

#### 2. Cart Information
- Tính tổng số lượng items
- Tính tổng tiền (sum of all subtotals)
- Kiểm tra cart rỗng hay không

#### 3. Response Generation
- Map Cart entity sang CartDTO
- Thêm isEmpty flag
- Thêm descriptive message

### Architecture Implementation

#### Business Layer
```
business/
└── usecase/
    ├── ViewCartUseCase.java          # Interface
    └── impl/
        └── ViewCartUseCaseImpl.java  # Implementation
```

**ViewCartUseCaseImpl Logic**:
1. Find cart by userId from repository
2. If not found → create empty cart in memory (not persisted)
3. Check if cart isEmpty()
4. Generate appropriate message
5. Return ViewCartResponse with cart and metadata

#### Interface Adapters Layer
```
interfaceadapters/
├── controller/
│   └── CartController.java           # GET /{userId} endpoint
└── dto/
    └── ViewCartResponseDTO.java      # Response DTO
```

**CartController.viewCart()**:
- Validate userId path parameter
- Execute ViewCartUseCase
- Map response to DTO
- Return HTTP 200 OK

#### Reused Components
- `CartRepository` - Find cart by userId
- `CartDTOMapper` - Convert Cart → CartDTO
- `CartDTO`, `CartItemDTO` - Already created for Use Case 4

### Testing Commands

```bash
# View cart with items
curl http://localhost:8080/api/cart/1

# View empty cart (new user)
curl http://localhost:8080/api/cart/999

# After adding items (Use Case 4)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "quantity": 2}'

# Then view cart
curl http://localhost:8080/api/cart/1
```

### Business Rules

1. **No Authentication Required**: 
   - In current implementation, anyone can view any cart by userId
   - Production: Should verify userId matches authenticated user

2. **Empty Cart Handling**:
   - Returns empty cart object (not 404)
   - Client can distinguish via `isEmpty` flag

3. **Eager Loading**:
   - All cart items loaded in single query
   - Good for small carts, consider pagination for large carts

4. **Read-Only Operation**:
   - No side effects
   - No cart creation/modification
   - Safe to call multiple times

### Error Handling

| Error | HTTP Code | Message |
|-------|-----------|---------|
| Missing userId | 400 | "User ID is required" |
| Server error | 500 | "An error occurred: {message}" |

### Performance Considerations

- **Eager Fetching**: `@OneToMany(fetch = FetchType.EAGER)` loads all items
- **No Pagination**: Loads entire cart in one query
- **Calculated Fields**: totalAmount and totalItems computed on-the-fly

### Security Notes

⚠️ **Current Implementation - Development Only:**
- No authentication check
- Anyone can view any cart with userId

🔒 **Production Requirements:**
- Add authentication/authorization
- Verify requesting user owns the cart
- Consider using session/token instead of userId in URL

---

## Use Case 7: Chỉnh số lượng sản phẩm trong giỏ hàng (Update Cart Quantity)

### Mô tả
Cho phép người dùng cập nhật số lượng sản phẩm trong giỏ hàng hoặc xóa sản phẩm (khi quantity = 0).

### API Endpoint
```
PUT /api/cart/update
```

### Request Body
```json
{
  "userId": 1,
  "productId": 1,
  "newQuantity": 3
}
```

### Response Examples

#### Success - Updated quantity (200 OK)
```json
{
  "cart": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Honda Wave RSX",
        "productPrice": 38000000,
        "quantity": 3,
        "subtotal": 114000000,
        "addedAt": "2025-11-10T09:00:00"
      }
    ],
    "totalAmount": 114000000,
    "totalItems": 3,
    "createdAt": "2025-11-10T09:00:00",
    "updatedAt": "2025-11-10T09:15:00"
  },
  "message": "Updated 'Honda Wave RSX' quantity to 3",
  "itemRemoved": false,
  "success": true
}
```

#### Success - Item removed (quantity = 0) (200 OK)
```json
{
  "cart": {
    "id": 1,
    "userId": 1,
    "items": [],
    "totalAmount": 0,
    "totalItems": 0,
    "createdAt": "2025-11-10T09:00:00",
    "updatedAt": "2025-11-10T09:20:00"
  },
  "message": "Product 'Honda Wave RSX' removed from cart",
  "itemRemoved": true,
  "success": true
}
```

#### Product not in cart (404)
```json
{
  "success": false,
  "message": "Product with ID 999 not found in cart of user 1"
}
```

#### Out of stock (409)
```json
{
  "success": false,
  "message": "Product 'Honda Wave RSX' is out of stock. Requested: 100, Available: 50"
}
```

#### Invalid quantity (400)
```json
{
  "success": false,
  "message": "Quantity cannot be negative"
}
```

### Business Logic

#### 1. Request Validation
- Validate userId, productId không null
- Validate newQuantity không null và >= 0
- Reject negative quantities

#### 2. Cart Retrieval
- Tìm cart theo userId
- Throw CartItemNotFoundException nếu cart không tồn tại

#### 3. Find Cart Item
- Tìm cart item theo productId trong cart
- Throw CartItemNotFoundException nếu product không có trong cart

#### 4. Quantity Update Logic
**Case 1: newQuantity = 0**
- Remove item khỏi cart
- Set itemRemoved = true
- Message: "Product '{name}' removed from cart"

**Case 2: newQuantity > 0**
- Validate product còn tồn tại
- Check stock availability
- Throw ProductOutOfStockException nếu vượt tồn kho
- Update quantity của cart item
- Recalculate subtotal automatically
- Message: "Updated '{name}' quantity to {qty}"

#### 5. Cart Persistence
- Save updated cart to database
- updatedAt timestamp tự động update
- Recalculate totalAmount và totalItems

### Architecture Implementation

#### Business Layer
```
business/
├── entity/
│   ├── Cart.java                    # removeItem(), findItemByProductId()
│   └── CartItem.java                # updateQuantity()
├── exception/
│   └── CartItemNotFoundException.java  # New exception
├── repository/
│   └── CartRepository.java          # findByUserId(), save()
└── usecase/
    ├── UpdateCartQuantityUseCase.java      # Interface
    └── impl/
        └── UpdateCartQuantityUseCaseImpl.java  # Implementation (7 steps)
```

**UpdateCartQuantityUseCaseImpl Logic**:
1. Validate request (userId, productId, newQuantity)
2. Find cart by userId (throw if not found)
3. Find cart item by productId (throw if not found)
4. Handle quantity update:
   - If qty = 0: Remove item from cart
   - If qty > 0: Validate stock and update quantity
5. Save cart (timestamps updated automatically)
6. Return UpdateCartQuantityResponse

#### Interface Adapters Layer
```
interfaceadapters/
├── controller/
│   └── CartController.java                    # PUT /update endpoint
└── dto/
    ├── UpdateCartQuantityRequestDTO.java      # Request DTO
    └── UpdateCartQuantityResponseDTO.java     # Response DTO
```

**CartController.updateCartQuantity()**:
- Validate request body fields
- Execute UpdateCartQuantityUseCase
- Map response to DTO
- Handle exceptions: 400, 404, 409, 500

#### Reused Components
- `CartRepository` - Find and save cart
- `ProductRepository` - Validate product and check stock
- `CartDTOMapper` - Convert Cart → CartDTO
- `CartDTO`, `CartItemDTO` - Already created

#### Frameworks Layer
```
frameworks/
└── config/
    └── CartConfig.java    # updateCartQuantityUseCase bean
```

### Testing Commands

```bash
# Add product to cart first
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "quantity": 2}'

# Update quantity to 5
curl -X PUT http://localhost:8080/api/cart/update \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "newQuantity": 5}'

# Update quantity to 1
curl -X PUT http://localhost:8080/api/cart/update \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "newQuantity": 1}'

# Remove item (set quantity to 0)
curl -X PUT http://localhost:8080/api/cart/update \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "newQuantity": 0}'

# View cart after update
curl http://localhost:8080/api/cart/1
```

### Business Rules

1. **Quantity = 0 means Remove**:
   - Setting quantity to 0 removes the item completely
   - Cart automatically recalculates totals
   - Returns itemRemoved = true flag

2. **Stock Validation**:
   - Always check product stock before increasing quantity
   - Prevent overselling
   - Clear error message with available stock

3. **Automatic Calculations**:
   - CartItem subtotal recalculated on quantity update
   - Cart totalAmount recalculated when item changes
   - updatedAt timestamp updated automatically

4. **Item Must Exist**:
   - Cannot update quantity of product not in cart
   - Use Add to Cart endpoint to add new products

5. **No Negative Quantities**:
   - Reject negative values (< 0)
   - Use 0 to remove, positive integers to update

### Error Handling

| Error | HTTP Code | Message |
|-------|-----------|---------|
| Missing userId | 400 | "User ID is required" |
| Missing productId | 400 | "Product ID is required" |
| Missing quantity | 400 | "New quantity is required" |
| Negative quantity | 400 | "Quantity cannot be negative" |
| Cart not found | 404 | "Cart not found for user {id}" |
| Item not in cart | 404 | "Product with ID {id} not found in cart of user {userId}" |
| Product deleted | 404 | "Product with ID {id} not found" |
| Out of stock | 409 | "Product '{name}' is out of stock. Requested: {req}, Available: {avail}" |
| Server error | 500 | "An error occurred: {message}" |

### State Transitions

```
Cart with items → Update quantity > 0 → Cart with updated quantity
Cart with items → Update quantity = 0 → Item removed, cart updated
Cart with items → Update to invalid qty → Error, cart unchanged
Single item cart → Remove item → Empty cart (not deleted)
```

### Performance Considerations

- **Single Transaction**: All operations in one database transaction
- **Eager Loading**: Cart items loaded with cart (one query)
- **Automatic Calculation**: No manual recalculation needed
- **Optimistic Updates**: Client can update UI before server response

### Security Notes

⚠️ **Current Implementation - Development Only:**
- No authentication check
- Anyone can update any cart with userId
- No rate limiting

🔒 **Production Requirements:**
- Add authentication/authorization
- Verify requesting user owns the cart
- Rate limit update requests
- Validate userId from session/token

### Integration with Other Use Cases

- **Use Case 4 (Add to Cart)**: Adds items that can be updated here
- **Use Case 6 (View Cart)**: Shows updated quantities
- **Use Case 5 (Checkout)**: Uses final quantities for order
- **Frontend**: Can implement +/- buttons, direct input, or remove button

---

## Use Case 5: Thanh toán (Checkout)

### Mô tả
Cho phép khách hàng đặt hàng từ giỏ hàng hiện tại, cung cấp thông tin giao hàng và chọn phương thức thanh toán (COD/Online).

### API Endpoint
```
POST /api/orders/checkout
```

### Request Body
```json
{
  "userId": 1,
  "shippingAddress": "123 Nguyen Trai Street, Thanh Xuan",
  "shippingCity": "Hanoi",
  "shippingPhone": "0901234567",
  "paymentMethod": "COD"
}
```

### Response Examples

#### Success - Order created (201 CREATED)
```json
{
  "order": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Honda Wave RSX",
        "productPrice": 38000000,
        "quantity": 2,
        "subtotal": 76000000
      },
      {
        "id": 2,
        "productId": 2,
        "productName": "Yamaha Exciter 155",
        "productPrice": 47000000,
        "quantity": 1,
        "subtotal": 47000000
      }
    ],
    "totalAmount": 123000000,
    "shippingAddress": "123 Nguyen Trai Street, Thanh Xuan",
    "shippingCity": "Hanoi",
    "shippingPhone": "0901234567",
    "paymentMethod": "COD",
    "status": "PENDING",
    "orderDate": "2025-11-10T14:00:00",
    "updatedAt": "2025-11-10T14:00:00"
  },
  "message": "Order placed successfully! Order ID: 1, Total: 123000000 VND, Payment: COD",
  "success": true
}
```

#### Empty cart (400)
```json
{
  "success": false,
  "message": "Cart is empty for user 1. Cannot proceed with checkout."
}
```

#### Missing shipping info (400)
```json
{
  "success": false,
  "message": "Shipping address is required"
}
```

#### Invalid payment method (400)
```json
{
  "success": false,
  "message": "Payment method must be COD or ONLINE"
}
```

### Business Logic

#### 1. Request Validation
- Validate userId, shipping address, city, phone
- Validate payment method (COD or ONLINE only)
- Throw IllegalArgumentException for invalid data

#### 2. Cart Retrieval & Validation
- Find cart by userId
- Throw EmptyCartException if cart not found or empty
- Cannot checkout with empty cart

#### 3. Convert Cart Items to Order Items
- Map each CartItem to OrderItem
- Preserve product info, quantity, price, subtotal
- Use stream mapping for conversion

#### 4. Create Order Entity
- Build Order with shipping info
- Set initial status as "PENDING"
- Set order date and updated timestamp
- Calculate total amount from items

#### 5. Order Validation
- Validate order has items
- Validate all required fields present
- Validate payment method format

#### 6. Persist Order
- Save order to database
- JPA cascade saves order items automatically
- Transaction ensures atomic operation

#### 7. Clear Cart
- Remove all items from cart after successful order
- Save empty cart to database
- Prevents duplicate orders

#### 8. Generate Response
- Include order ID, total amount, payment method
- Provide user-friendly success message
- Return created order details

### Architecture Implementation

#### Business Layer
```
business/
├── entity/
│   ├── Order.java                     # Domain entity with business logic
│   └── OrderItem.java                 # Order line item entity
├── exception/
│   └── EmptyCartException.java        # Custom exception
├── repository/
│   └── OrderRepository.java           # Repository interface
└── usecase/
    ├── CheckoutUseCase.java           # Interface with Request/Response
    └── impl/
        └── CheckoutUseCaseImpl.java   # Implementation (10 steps)
```

**CheckoutUseCaseImpl Logic**:
1. Validate request (userId, shipping info, payment method)
2. Get cart by userId (throw if not found)
3. Check if cart is empty (throw if empty)
4. Convert cart items to order items
5. Create order entity with shipping info
6. Validate order entity
7. Save order to database
8. Clear cart after successful order
9. Generate success message
10. Return CheckoutResponse

**Order Entity Business Methods**:
- `calculateTotalAmount()` - Sum all item subtotals
- `getTotalItemCount()` - Count total items
- `isEmpty()` - Check if order has items
- `addItem()` - Add order item
- `validate()` - Validate all order data
- `canBeCancelled()` - Check if order can be cancelled

#### Persistence Layer
```
persistence/
├── entity/
│   ├── OrderJpaEntity.java            # JPA entity for orders table
│   └── OrderItemJpaEntity.java        # JPA entity for order_items table
├── repository/
│   └── OrderJpaRepository.java        # Spring Data JPA repository
├── mapper/
│   └── OrderMapper.java               # Domain ↔ JPA entity mapping
└── adapter/
    └── OrderRepositoryAdapter.java    # Repository implementation
```

**OrderJpaEntity Features**:
- `@OneToMany` relationship with OrderItemJpaEntity
- `@PrePersist` auto-set timestamps and status
- `@PreUpdate` auto-update timestamp and total
- Cascade ALL operations to items
- Orphan removal enabled

**OrderItemJpaEntity Features**:
- `@ManyToOne` relationship with OrderJpaEntity
- `@PrePersist` auto-calculate subtotal
- DECIMAL(15,2) for large price values
- Bidirectional mapping with parent

#### Interface Adapters Layer
```
interfaceadapters/
├── controller/
│   └── OrderController.java           # POST /checkout endpoint
├── dto/
│   ├── CheckoutRequestDTO.java        # Request DTO
│   ├── CheckoutResponseDTO.java       # Response DTO
│   ├── OrderDTO.java                  # Order presentation DTO
│   └── OrderItemDTO.java              # Order item DTO
└── mapper/
    └── OrderDTOMapper.java            # Domain → DTO mapping
```

**OrderController.checkout()**:
- Validate all request fields
- Execute CheckoutUseCase
- Map Order to OrderDTO
- Return HTTP 201 CREATED on success
- Handle exceptions: 400, 500

#### Frameworks Layer
```
frameworks/
└── config/
    └── OrderConfig.java    # Spring beans configuration
```

**Beans Configured**:
- `orderRepository` - OrderRepositoryAdapter
- `checkoutUseCase` - CheckoutUseCaseImpl with dependencies

### Testing Commands

```bash
# Step 1: Add items to cart
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 1, "quantity": 2}'

curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "productId": 2, "quantity": 1}'

# Step 2: View cart before checkout
curl http://localhost:8080/api/cart/1

# Step 3: Checkout with COD payment
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "shippingAddress": "123 Nguyen Trai Street, Thanh Xuan",
    "shippingCity": "Hanoi",
    "shippingPhone": "0901234567",
    "paymentMethod": "COD"
  }'

# Step 4: Verify cart is empty after checkout
curl http://localhost:8080/api/cart/1

# Checkout with ONLINE payment
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "shippingAddress": "456 Le Duan, District 1",
    "shippingCity": "Ho Chi Minh City",
    "shippingPhone": "0987654321",
    "paymentMethod": "ONLINE"
  }'
```

### Business Rules

1. **Cart Must Have Items**:
   - Cannot checkout with empty cart
   - Clear error message if cart is empty
   - Prevents creating empty orders

2. **Payment Methods**:
   - Only COD (Cash On Delivery) and ONLINE supported
   - Case-insensitive validation
   - Stored in uppercase format

3. **Order Status Lifecycle**:
   - Initial status: PENDING
   - Can progress: PROCESSING → SHIPPED → DELIVERED
   - Can be cancelled: PENDING, PROCESSING only
   - Status stored as string for flexibility

4. **Cart Cleared After Order**:
   - Cart automatically emptied on successful checkout
   - Prevents accidental duplicate orders
   - User must add new items for next order

5. **Shipping Information Required**:
   - Address, city, and phone are mandatory
   - Validation at both controller and use case levels
   - Used for order fulfillment

6. **Atomic Transaction**:
   - Order creation and cart clearing in one transaction
   - Rollback if any step fails
   - Data consistency guaranteed

### Error Handling

| Error | HTTP Code | Message |
|-------|-----------|---------|
| Missing userId | 400 | "User ID is required" |
| Missing address | 400 | "Shipping address is required" |
| Missing city | 400 | "Shipping city is required" |
| Missing phone | 400 | "Shipping phone is required" |
| Missing payment | 400 | "Payment method is required" |
| Invalid payment | 400 | "Payment method must be COD or ONLINE" |
| Empty cart | 400 | "Cart is empty for user {id}. Cannot proceed with checkout." |
| Cart not found | 400 | "Cart is empty for user {id}. Cannot proceed with checkout." |
| Server error | 500 | "An error occurred: {message}" |

### Database Schema

**Table: `orders`**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    shipping_address NVARCHAR(500) NOT NULL,
    shipping_city NVARCHAR(100) NOT NULL,
    shipping_phone NVARCHAR(20) NOT NULL,
    payment_method NVARCHAR(20) NOT NULL,
    status NVARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_date DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

**Table: `order_items`**
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name NVARCHAR(255) NOT NULL,
    product_price DECIMAL(15,2) NOT NULL,
    quantity INT NOT NULL,
    subtotal DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

### State Transitions

```
[Cart with items] 
    → Checkout initiated
    → Order validation
    → Order created (PENDING)
    → Cart cleared
    → Success response

[Empty cart]
    → Checkout initiated
    → EmptyCartException
    → Error response (400)
```

### Performance Considerations

- **Eager Loading**: Order items loaded with order (one query)
- **Cascade Operations**: Items saved automatically with order
- **Single Transaction**: Order creation + cart clearing atomic
- **Calculated Fields**: Total amount computed automatically

### Security Notes

⚠️ **Current Implementation - Development Only:**
- No authentication check
- Anyone can checkout for any userId
- No fraud prevention
- No payment gateway integration

🔒 **Production Requirements:**
- Add authentication/authorization
- Verify userId matches authenticated user
- Integrate real payment gateway for ONLINE
- Add order confirmation email
- Implement fraud detection
- Add rate limiting for checkout endpoint
- Log all order transactions

### Integration with Other Use Cases

- **Use Case 4 (Add to Cart)**: Provides items for checkout
- **Use Case 6 (View Cart)**: User reviews cart before checkout
- **Use Case 7 (Update Cart)**: User adjusts quantities before checkout
- **Future Use Cases**: Order tracking, order history, order cancellation

### Future Enhancements

1. **Payment Gateway Integration**: Stripe, PayPal, VNPay for ONLINE payments
2. **Order History**: GET /api/orders/user/{userId} endpoint
3. **Order Details**: GET /api/orders/{orderId} endpoint
4. **Order Cancellation**: PUT /api/orders/{orderId}/cancel endpoint
5. **Order Status Update**: PUT /api/orders/{orderId}/status (Admin only)
6. **Email Notifications**: Send confirmation email after order
7. **SMS Notifications**: Send order updates via SMS
8. **Inventory Management**: Decrease product stock after order
9. **Coupon/Discount**: Apply promotional codes at checkout
10. **Multi-address**: Support multiple shipping addresses

---

## 📊 Database Schema

### Table: `products`
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url NVARCHAR(500),
    specifications TEXT,
    category NVARCHAR(50),
    stock_quantity INT,
    available BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE()
);
```

### Table: `users`
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    email NVARCHAR(255) NOT NULL UNIQUE,
    username NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    phone_number NVARCHAR(20),
    role NVARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    last_login_at DATETIME2,
    CONSTRAINT CHK_Users_Role CHECK (role IN ('CUSTOMER', 'ADMIN'))
);
```

---

## 🧪 Testing Guide

### Use Case 1: Get Product Detail
```bash
# Success case
curl http://localhost:8080/api/products/1

# Not found case
curl http://localhost:8080/api/products/999
```

### Use Case 2: Login
```bash
# Success - Customer
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@motorbike.com","password":"password123"}'

# Success - Admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@motorbike.com","password":"admin123"}'

# Fail - Wrong password
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@motorbike.com","password":"wrong"}'

# Fail - Invalid email
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"notexist@example.com","password":"password123"}'
```

### Use Case 3: Register
```bash
# Success case
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "username": "testuser",
    "password": "test123",
    "phoneNumber": "0987654321"
  }'

# Fail - Duplicate email
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@motorbike.com",
    "username": "newuser",
    "password": "test123"
  }'

# Fail - Duplicate username
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "new@example.com",
    "username": "customer1",
    "password": "test123"
  }'

# Fail - Password too short
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "new@example.com",
    "username": "newuser",
    "password": "123"
  }'
```

### Use Case 4: Add to Cart
```bash
# Success - Add product to cart
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 2
  }'

# Success - Add same product again (quantity merges)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1
  }'

# Fail - Product not found
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 999,
    "quantity": 1
  }'

# Fail - Out of stock
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1000
  }'
```

---

## 📈 Implementation Statistics

| Metric | Count |
|--------|-------|
| **Use Cases Implemented** | 4 |
| **Total Files** | 55+ |
| **Business Layer Files** | 19 |
| **Interface Adapters Files** | 15 |
| **Persistence Layer Files** | 13 |
| **Frameworks Layer Files** | 4 |
| **Entities** | 4 (Product, User, Cart, CartItem) |
| **API Endpoints** | 4 |
| **Database Tables** | 4 |

---

Được tạo bởi: Refactoring Clean Architecture - 4 Layers
Ngày: November 10, 2025
Cập nhật: Use Cases 1, 2, 3, 4 ✅ Completed
