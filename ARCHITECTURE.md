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

Được tạo bởi: Refactoring Clean Architecture - 4 Layers
Ngày: November 10, 2025
