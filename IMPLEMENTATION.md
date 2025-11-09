# Motorbike Shop - Clean Architecture Implementation

## 📋 Tổng quan

Dự án này implement Use Case "Xem chi tiết sản phẩm" theo kiến trúc Clean Architecture với Spring Boot.

## 🏗️ Kiến trúc

Dự án được tổ chức theo Clean Architecture với các layers:

```
com.motorbike/
├── domain/                          # Domain Layer (Business Logic)
│   ├── entity/
│   │   └── Product.java            # Domain entity thuần túy
│   └── repository/
│       └── ProductRepository.java   # Repository interface (contract)
│
├── application/                     # Application Layer (Use Cases)
│   └── usecase/
│       ├── GetProductDetailUseCase.java           # Input/Output Port
│       └── impl/
│           ├── GetProductDetailUseCaseImpl.java   # Use case implementation
│           └── ProductNotFoundException.java      # Business exception
│
├── infrastructure/                  # Infrastructure Layer (Technical Details)
│   ├── persistence/
│   │   ├── entity/
│   │   │   └── ProductJpaEntity.java             # JPA Entity
│   │   ├── repository/
│   │   │   └── ProductJpaRepository.java         # Spring Data JPA Repository
│   │   ├── mapper/
│   │   │   └── ProductEntityMapper.java          # Domain <-> JPA mapping
│   │   └── adapter/
│   │       └── ProductRepositoryAdapter.java     # Repository implementation
│   └── config/
│       └── DataInitializer.java                  # Sample data loader
│
├── presentation/                    # Presentation Layer (API/UI)
│   ├── controller/
│   │   └── ProductController.java               # REST Controller
│   ├── dto/
│   │   └── ProductDTO.java                      # API response DTO
│   └── mapper/
│       └── ProductMapper.java                   # Use case response -> DTO mapping
│
└── main/
    └── MainApplication.java                     # Spring Boot main class
```

## 🎯 Use Case: Xem chi tiết sản phẩm

**Actor**: Guest, Customer, Admin

**Flow**:
1. User gửi GET request đến `/api/products/{id}`
2. ProductController nhận request
3. Controller tạo ProductDetailRequest và gọi GetProductDetailUseCase
4. Use Case lấy Product từ ProductRepository
5. Use Case trả về ProductDetailResponse
6. Controller map response sang ProductDTO
7. Trả về JSON response cho client

## 🔧 Cấu hình

### Database
- **Type**: H2 In-Memory Database (development)
- **URL**: `jdbc:h2:mem:motorbikedb`
- **Console**: http://localhost:8080/h2-console

### Server
- **Port**: 8080
- **Context Path**: /

## 🚀 Cách chạy

### 1. Build project
```bash
./mvnw clean package
```

### 2. Run application
```bash
./mvnw spring-boot:run
```

Application sẽ chạy tại: http://localhost:8080

## 📡 API Endpoints

### Get Product Detail
```http
GET /api/products/{id}
```

**Response Success (200)**:
```json
{
  "id": 1,
  "name": "Honda Wave RSX",
  "description": "Xe số tiết kiệm nhiên liệu, phù hợp cho di chuyển trong thành phố",
  "price": 38000000,
  "imageUrl": "/images/honda-wave-rsx.jpg",
  "specifications": "{\"engine\":\"110cc\",\"fuelCapacity\":\"3.5L\",\"weight\":\"98kg\"}",
  "category": "MOTORCYCLE",
  "stockQuantity": 15,
  "available": true,
  "inStock": true
}
```

**Response Not Found (404)**:
```
Product not found
```

**Response Bad Request (400)**:
```
Invalid product ID
```

## 🧪 Test API

### Sử dụng cURL

```bash
# Get product with ID = 1
curl http://localhost:8080/api/products/1

# Get product with ID = 2
curl http://localhost:8080/api/products/2

# Test with non-existent ID
curl http://localhost:8080/api/products/999
```

### Sử dụng PowerShell

```powershell
# Get product with ID = 1
Invoke-RestMethod -Uri "http://localhost:8080/api/products/1" -Method Get

# Get product with ID = 2  
Invoke-RestMethod -Uri "http://localhost:8080/api/products/2" -Method Get
```

### Sử dụng Browser
Mở trực tiếp URL trong browser:
- http://localhost:8080/api/products/1
- http://localhost:8080/api/products/2
- http://localhost:8080/api/products/3

## 💾 Dữ liệu mẫu

Hệ thống khởi tạo với 5 sản phẩm mẫu:

1. **Honda Wave RSX** (ID: 1) - MOTORCYCLE - 38,000,000 VND
2. **Yamaha Exciter 155** (ID: 2) - MOTORCYCLE - 47,000,000 VND
3. **Suzuki Raider 150** (ID: 3) - MOTORCYCLE - 50,000,000 VND
4. **Mũ bảo hiểm Royal M139** (ID: 4) - ACCESSORY - 850,000 VND
5. **Găng tay Scoyco MC29** (ID: 5) - ACCESSORY - 350,000 VND

## 🔍 Dependency Injection Flow

```
ProductController
    ↓ (depends on)
GetProductDetailUseCase (interface)
    ↓ (implemented by)
GetProductDetailUseCaseImpl
    ↓ (depends on)
ProductRepository (interface)
    ↓ (implemented by)
ProductRepositoryAdapter
    ↓ (depends on)
ProductJpaRepository (Spring Data JPA)
```

## 📦 Dependencies chính

- Spring Boot 3.5.6
- Spring Data JPA
- H2 Database
- Spring Boot Starter Web
- Spring Boot Starter Thymeleaf

## 🎯 Clean Architecture Principles

1. **Dependency Rule**: Dependencies point inward (outer layers depend on inner layers)
2. **Domain Independence**: Domain layer không depend vào bất kỳ framework nào
3. **Use Case Driven**: Business logic được tập trung trong use cases
4. **Interface Segregation**: Sử dụng interfaces để define contracts
5. **Separation of Concerns**: Mỗi layer có trách nhiệm riêng biệt

## 📝 Notes

- Database được reset mỗi khi restart application (H2 in-memory)
- JPA show-sql được enable để xem generated SQL queries
- H2 Console có thể dùng để query database trực tiếp
- Use case responses là immutable objects
- Domain entities chứa business logic (isInStock, canPurchase, etc.)

## 🔜 Next Steps

Để tiếp tục phát triển các use cases khác:
1. Đăng nhập (Use Case 2)
2. Đăng ký tài khoản (Use Case 3)
3. Thêm vào giỏ hàng (Use Case 4)
4. Thanh toán (Use Case 5)
5. Xem giỏ hàng (Use Case 6)
6. Chỉnh số lượng sản phẩm trong giỏ hàng (Use Case 7)
