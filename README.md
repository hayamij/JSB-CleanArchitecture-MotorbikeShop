# JSB-CleanArchitecture-MotorbikeShop

🏍️ **Motorbike Shop E-Commerce Application** built with **Clean Architecture** and **Spring Boot**

## 📋 Project Overview

An e-commerce web application for motorbike and accessories shop with three user roles:
- **Guest**: Browse products and view details
- **Customer**: Shopping cart, checkout, order management
- **Admin**: Product management, inventory control

Built following Clean Architecture principles for maintainability, testability, and independence from frameworks.

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                      │
│           (Controllers, DTOs, Mappers, Views)               │
├─────────────────────────────────────────────────────────────┤
│                     APPLICATION LAYER                       │
│              (Use Cases, Input/Output Ports)                │
├─────────────────────────────────────────────────────────────┤
│                       DOMAIN LAYER                          │
│          (Entities, Repository Interfaces, Rules)           │
├─────────────────────────────────────────────────────────────┤
│                   INFRASTRUCTURE LAYER                      │
│    (JPA Entities, Repository Impl, Database, Adapters)      │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- SQL Server 2019+ (or SQL Server Express)
- IDE: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/hayamij/JSB-CleanArchitecture-MotorbikeShop.git
   cd JSB-CleanArchitecture-MotorbikeShop
   ```

2. **Configure Database**
   - Copy `src/main/resources/application.properties.example` to `application.properties`
   - Update database credentials:
     ```properties
     spring.datasource.username=YOUR_USERNAME
     spring.datasource.password=YOUR_PASSWORD
     ```

3. **Setup Database Schema**
   ```bash
   # Run database setup script (PowerShell recommended for UTF-8 support)
   ./database-setup.ps1
   
   # Or use SQL script directly
   sqlcmd -S localhost -U your_user -P your_password -i database-setup.sql
   ```

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the application**
   - API: http://localhost:8080/api/products
   - H2 Console: http://localhost:8080/h2-console

## 📁 Project Structure

```
src/main/java/com/motorbike/
├── domain/                          # Domain Layer
│   ├── entity/                      # Business entities
│   │   └── Product.java
│   └── repository/                  # Repository interfaces
│       └── ProductRepository.java
│
├── application/                     # Application Layer
│   └── usecase/                     # Use cases
│       ├── GetProductDetailUseCase.java
│       └── impl/
│           ├── GetProductDetailUseCaseImpl.java
│           └── ProductNotFoundException.java
│
├── infrastructure/                  # Infrastructure Layer
│   ├── persistence/
│   │   ├── entity/                  # JPA entities
│   │   │   └── ProductJpaEntity.java
│   │   ├── repository/              # Spring Data JPA
│   │   │   └── ProductJpaRepository.java
│   │   ├── mapper/                  # Domain ↔ JPA mapping
│   │   │   └── ProductEntityMapper.java
│   │   └── adapter/                 # Repository implementations
│   │       └── ProductRepositoryAdapter.java
│   └── config/
│       └── DataInitializer.java
│
├── presentation/                    # Presentation Layer
│   ├── controller/                  # REST controllers
│   │   └── ProductController.java
│   ├── dto/                         # API DTOs
│   │   └── ProductDTO.java
│   └── mapper/                      # Use case ↔ DTO mapping
│       └── ProductMapper.java
│
└── main/
    └── MainApplication.java         # Spring Boot entry point
```

## 🎯 Implemented Use Cases

### ✅ Use Case 1: View Product Detail
- **Endpoint**: `GET /api/products/{id}`
- **Actors**: Guest, Customer, Admin
- **Flow**: User requests product details → System retrieves from database → Returns product information

**Example Request:**
```bash
curl http://localhost:8080/api/products/1
```

**Example Response:**
```json
{
  "id": 1,
  "name": "Honda Wave RSX",
  "description": "Xe số tiết kiệm nhiên liệu, phù hợp cho di chuyển trong thành phố",
  "price": 38000000.00,
  "imageUrl": "/images/honda-wave-rsx.jpg",
  "specifications": "{\"engine\":\"110cc\",\"fuelCapacity\":\"3.5L\",\"weight\":\"98kg\"}",
  "category": "MOTORCYCLE",
  "stockQuantity": 15,
  "available": true,
  "inStock": true
}
```

## 🔧 Development

### Commit Changes
```bash
# Edit commit-message.txt with your message
# Then run:
commit.cmd
```

### Running Tests
```bash
./mvnw test
```

### Build for Production
```bash
./mvnw clean package -DskipTests
```

## 📚 Documentation

- [Implementation Details](IMPLEMENTATION.md) - Detailed architecture and flow documentation
- [Use Cases](usecases.md) - Complete use case specifications

## 🛠️ Technologies

- **Framework**: Spring Boot 3.5.6
- **Architecture**: Clean Architecture
- **Database**: SQL Server 2022
- **ORM**: Hibernate / Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 17

## 👥 Team

- Project Owner: hayamij
- Contributors: Welcome!

## 📄 License

See [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
