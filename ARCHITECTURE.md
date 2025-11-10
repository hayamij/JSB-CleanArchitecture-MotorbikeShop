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

---

## 📈 Implementation Statistics

| Metric | Count |
|--------|-------|
| **Use Cases Implemented** | 3 |
| **Total Files** | 35+ |
| **Business Layer Files** | 13 |
| **Interface Adapters Files** | 10 |
| **Persistence Layer Files** | 8 |
| **Entities** | 2 (Product, User) |
| **API Endpoints** | 3 |
| **Database Tables** | 2 |

---

Được tạo bởi: Refactoring Clean Architecture - 4 Layers
Ngày: November 10, 2025
Cập nhật: Use Cases 1, 2, 3 ✅ Completed
