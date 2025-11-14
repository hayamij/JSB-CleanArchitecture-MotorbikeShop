# 🏍️ Motorbike Shop - Clean Architecture

> Website giới thiệu, bán xe máy và phụ kiện trực tuyến

**Tác giả:** [hayamij](https://github.com/hayamij) (Nguyen Quang Tuan Phuong)

## 📋 Tổng quan

Dự án xây dựng hệ thống website bán xe máy và phụ kiện trực tuyến, áp dụng kiến trúc **Clean Architecture** để đảm bảo code dễ bảo trì, mở rộng và kiểm thử.

### Đặc điểm nổi bật
- ✅ Phân tách rõ ràng các tầng logic nghiệp vụ
- ✅ Độc lập với framework và database
- ✅ Dễ dàng kiểm thử (Testable)
- ✅ Tuân thủ SOLID principles
- ✅ Dependency Rule được áp dụng nghiêm ngặt

---

## 🛠️ Công nghệ sử dụng

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **Java** | 17 | Ngôn ngữ lập trình |
| **Spring Boot** | 3.5.6 | Framework backend |
| **Spring Data JPA** | 3.5.6 | ORM và database access |
| **Thymeleaf** | 3.5.6 | Template engine |
| **SQL Server** | - | Database chính |
| **H2 Database** | - | Database cho testing |
| **Maven** | - | Build tool |
| **JUnit 5** | - | Unit testing |

---

## 🏗️ Kiến trúc

Dự án tuân thủ **Clean Architecture** của Uncle Bob với 4 tầng:

```
┌─────────────────────────────────────────────────────┐
│  Frameworks & Drivers (Adapters)                    │
│  - Web Controllers                                   │
│  - Database Repositories                             │
│  - External Services                                 │
├─────────────────────────────────────────────────────┤
│  Interface Adapters (Infrastructure)                │
│  - Gateways                                          │
│  - Presenters                                        │
│  - DTO Converters                                    │
├─────────────────────────────────────────────────────┤
│  Use Cases (Business Logic)                         │
│  - Application Services                              │
│  - Interactors                                       │
│  - Input/Output Boundaries                           │
├─────────────────────────────────────────────────────┤
│  Entities (Domain)                                   │
│  - Business Rules                                    │
│  - Domain Models                                     │
└─────────────────────────────────────────────────────┘
     Inner Layers ← Dependency Rule ← Outer Layers
```

### Cấu trúc thư mục

```
src/main/java/com/motorbike/
├── domain/              # Layer 1: Entities
│   ├── entities/        # Domain models
│   └── exceptions/      # Business exceptions
├── business/            # Layer 2: Use Cases
│   └── usecase/
│       ├── control/     # Use case implementations
│       ├── entity/      # Input/Output boundaries
│       └── boundary/    # Repository interfaces
├── infrastructure/      # Layer 3: Interface Adapters
│   ├── gateway/         # Repository implementations
│   └── presenter/       # Data transformers
└── adapters/            # Layer 4: Frameworks & Drivers
    ├── web/             # Web controllers
    └── persistence/     # Database entities
```

---

## 📚 Thu thập yêu cầu

### Actors (Người dùng hệ thống)

1. **Guest** - Khách vãng lai (chưa đăng nhập)
2. **Customer** - Khách hàng đã đăng ký
3. **Admin** - Quản trị viên

### Use Cases chính

#### 🔹 Guest Features
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Thêm sản phẩm vào giỏ hàng
- Xem và chỉnh sửa giỏ hàng
- Đăng ký tài khoản

#### 🔹 Customer Features
- Tất cả chức năng của Guest
- Đăng nhập/Đăng xuất
- Thanh toán đơn hàng
- Xem lịch sử đơn hàng

#### 🔹 Admin Features
- Tất cả chức năng của Customer
- Quản lý sản phẩm (CRUD)
- Quản lý loại sản phẩm
- Quản lý tài khoản người dùng
- Quản lý đơn hàng

### Business Rules chính

1. **Sản phẩm:**
   - Giá > 0
   - Số lượng tồn kho ≥ 0
   - Phải thuộc một loại sản phẩm

2. **Giỏ hàng:**
   - Số lượng thêm vào phải > 0
   - Không vượt quá tồn kho
   - Guest: lưu trong session
   - Customer: lưu trong database

3. **Thanh toán:**
   - Bắt buộc đăng nhập
   - Giỏ hàng phải có sản phẩm
   - Kiểm tra tồn kho trước khi thanh toán
   - Trừ tồn kho sau khi đặt hàng thành công

4. **Tài khoản:**
   - Email phải unique
   - Mật khẩu được mã hóa
   - Phân quyền: Customer/Admin

---

## 🎯 Nguyên tắc thiết kế (SOLID)

| Nguyên tắc | Áp dụng trong dự án |
|------------|---------------------|
| **S**RP | Mỗi Use Case chỉ xử lý một nghiệp vụ cụ thể |
| **O**CP | Mở rộng qua interfaces, không sửa code cũ |
| **L**SP | Các implementation tuân thủ contract của interface |
| **I**SP | Interfaces nhỏ, chỉ chứa methods cần thiết |
| **D**IP | Use Cases phụ thuộc vào abstractions, không phụ thuộc vào implementations cụ thể |

### Dependency Rule
```
❌ Entities không phụ thuộc Use Cases
❌ Use Cases không phụ thuộc Frameworks
✅ Outer Layers → Inner Layers (qua Interfaces)
```

---

## 🚀 Cài đặt và chạy

### Yêu cầu hệ thống
- Java 17+
- Maven 3.6+
- SQL Server 2019+ (hoặc bất kỳ phiên bản tương thích)

### Bước 1: Clone repository
```bash
git clone https://github.com/hayamij/JSB-CleanArchitecture-MotorbikeShop.git
cd JSB-CleanArchitecture-MotorbikeShop
```

### Bước 2: Cấu hình Database

1. Tạo database trong SQL Server:
```sql
CREATE DATABASE MotorcycleShop;
```

2. Chạy script khởi tạo:
```bash
sqlcmd -S localhost -d MotorcycleShop -i database-setup.sql
```

3. Cấu hình kết nối trong `application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=MotorcycleShop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Bước 3: Build và chạy

#### Sử dụng Maven Wrapper (khuyên dùng)
```bash
# Windows
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

#### Hoặc sử dụng Maven
```bash
mvn clean install
mvn spring-boot:run
```

### Bước 4: Truy cập ứng dụng
Mở trình duyệt và truy cập: **http://localhost:8080**

---

## 🧪 Chạy Tests

### Chạy tất cả tests
```bash
mvnw test
```

### Chạy test với coverage
```bash
mvnw test jacoco:report
```

### Test structure
```
src/test/java/com/motorbike/
├── business/usecase/control/    # Use case tests
├── domain/entities/              # Entity tests
└── infrastructure/               # Gateway tests
```

---

## 📖 API Documentation

### Endpoints chính

#### Products
- `GET /` - Trang chủ, danh sách sản phẩm
- `GET /product/{id}` - Chi tiết sản phẩm

#### Cart
- `POST /cart/add` - Thêm vào giỏ hàng
- `GET /cart` - Xem giỏ hàng
- `POST /cart/update` - Cập nhật số lượng

#### Authentication
- `GET /login` - Trang đăng nhập
- `POST /login` - Xử lý đăng nhập
- `GET /register` - Trang đăng ký
- `POST /register` - Xử lý đăng ký

#### Checkout
- `POST /checkout` - Thanh toán đơn hàng

---

## 📂 Tài liệu tham khảo

- [clean_arch_summary.md](clean_arch_summary.md) - Hướng dẫn chi tiết về Clean Architecture
- [database-setup.sql](database-setup.sql) - Script khởi tạo database

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Tác giả

**Nguyen Quang Tuan Phuong** (hayamij)
- GitHub: [@hayamij](https://github.com/hayamij)

---

## 🙏 Acknowledgments

- Clean Architecture by Robert C. Martin (Uncle Bob)
- Spring Boot Documentation
- Java Clean Architecture Community

---

*Last updated: November 2025*
