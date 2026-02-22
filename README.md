# Motorbike Shop - Clean Architecture

## Project Overview

A motorbike shop management system built following Clean Architecture principles, adhering to SOLID principles and ensuring independence between layers.

### Technologies Used

- **Backend Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: SQL
- **Testing**: JUnit 5
- **Architecture Pattern**: Clean Architecture

### Project Structure

```
src/
├── main/
│   ├── java/com/motorbike/
│   │   ├── domain/           # Entities and Business Logic
│   │   ├── business/         # Use Cases and DTOs
│   │   ├── adapters/         # Controllers, Presenters, ViewModels
│   │   └── infrastructure/   # Database, Config
│   └── resources/
│       ├── static/           # CSS, JS
│       └── templates/        # HTML
└── test/
    └── java/com/motorbike/   # Unit Tests
```

## System Features

### 1. Customer Features

#### Account
- ✅ Register Account
- ✅ Login
  - Automatically creates a shopping cart upon registration
  - Merges guest cart into user cart upon login

#### Motorbike Products
- ✅ Get All Motorbikes
- ✅ Search Motorbikes
  - Search by keyword, brand, model, color, engine displacement
- ✅ Get Product Detail
  - Displays original price, discounted price, and discount percentage

#### Shopping Cart
- ✅ Add To Cart
  - Checks stock availability before adding
- ✅ View Cart
  - Warns if cart quantity exceeds available stock
- ✅ Update Cart Quantity
  - Automatically removes item if quantity = 0

#### Orders
- ✅ Checkout
  - Creates order
  - Automatically deducts stock
  - Clears cart after successful order placement
- ✅ Cancel Order
  - Restores stock
  - Verifies cancellation permission
- ✅ List All Orders
  - Sorted by order date

### 2. Motorbike Management (Admin) - 🔄 In Development

- 🔄 Search Motorbikes
- 🔄 Add Motorbike
- 🔄 Edit Motorbike Information
- 🔄 Delete Motorbike

### 3. Accessory Management (Admin) - 🔄 In Development

- 🔄 Search Accessories
- 🔄 Add Accessory
- 🔄 View Accessory List
- 🔄 Edit Accessory Information
- 🔄 Delete Accessory

### 4. Account Management (Admin) - 🔄 In Development

- 🔄 Search Accounts
- 🔄 View User List
- 🔄 Edit User Information
- 🔄 Delete User

**Legend**: ✅ = Completed | 🔄 = In Development

**Implementation Summary**:
- **11 Completed Use Cases**: Register, Login, AddToCart, GetProductDetail, ViewCart, UpdateCartQuantity, Checkout, CancelOrder, ListAllOrders, GetAllMotorbikes, SearchMotorbikes
- **Flow Pattern**: All Use Cases follow the flow pattern — always executing all steps and presenting results whether success or error
- **Error Handling**: Uses error-accumulation pattern instead of throwing exceptions

## Clean Architecture

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

### Main Components

#### 1. Domain Layer
- **Entities**: `Account`, `Product`, `Motorbike`, `ShoppingCart`, `Order`
- **Value Objects**: `Role`, `OrderStatus`
- **Exceptions**: Custom domain exceptions

#### 2. Business Layer
- **Use Cases**: Independent business logic
- **Input/Output DTOs**: Data transfer between layers
- **Ports (Interfaces)**: Repository interfaces

#### 3. Adapters Layer
- **Controllers**: Receive requests from clients
- **Presenters**: Format data for UI
- **ViewModels**: Display data
- **Repositories**: Implement ports

#### 4. Infrastructure Layer
- **Database**: JPA entities and repositories
- **Configuration**: Spring Boot config

## Use Case Implementation

### Flow Pattern

All Use Cases follow the complete flow pattern:

```
Controller → InputData → UseCaseControl → Entity
                ↓
            OutputData → Presenter → ViewModel
```

### Use Case Execution Flow

Each Use Case executes in the following steps:

```java
1. Validate Input (Step 2)
   - Check validity of input data
   - Catch exceptions but do not throw outward

2. Check Business Rules (Step 3)
   - Check business rules (only executed if Step 2 has no errors)
   - E.g.: check if email already exists, check stock availability

3. Execute Business Logic (Step 4)
   - Execute business logic (only if no errors exist)
   - E.g.: create account, add to cart, create order

4. Build Error Response (Step 4.1)
   - If errors exist from previous steps, build error response

5. Present Result (Step 5)
   - Always present result (success or error)

6. User Receives Notification (Step 6)
   - User receives notification
```

### Example: Add To Cart Use Case

```java
public void execute(AddToCartInputData inputData) {
    AddToCartOutputData outputData = null;
    Exception errorException = null;
    
    // Step 2: Validate
    try {
        validateInput(inputData);
    } catch (Exception e) {
        errorException = e;
    }
    
    // Step 3: Check business rules
    if (errorException == null) {
        try {
            checkProductAndStock(inputData);
        } catch (Exception e) {
            errorException = e;
        }
    }
    
    // Step 4: Execute
    if (errorException == null) {
        try {
            outputData = addToCart(inputData);
        } catch (Exception e) {
            errorException = e;
        }
    }
    
    // Step 4.1: Build error response
    if (errorException != null) {
        outputData = buildErrorResponse(errorException);
    }
    
    // Step 5: Present (always executed)
    outputBoundary.present(outputData);
}
```

### Key Principles

- ✅ **No throw pattern**: Never throw exceptions outside the use case
- ✅ **Error accumulation**: Use `errorException` variable to track errors
- ✅ **Always present**: Always call `outputBoundary.present()` regardless of success or error
- ✅ **Sequential flow**: Steps execute sequentially; each step only runs if the previous step had no errors
- ✅ **Complete execution**: Always execute all steps (validate → check → execute → present)

## Testing

### Test Coverage

All Use Cases have Unit Tests:
- ✅ `RegisterUseCaseControlTest`
- ✅ `LoginUseCaseControlTest`
- ✅ `AddToCartUseCaseControlTest`
- ✅ `GetProductDetailUseCaseControlTest`
- ✅ `ViewCartUseCaseControlTest`
- ✅ `UpdateCartQuantityUseCaseControlTest`
- ✅ `CheckoutUseCaseControlTest`
- ✅ `CancelOrderUseCaseControlTest`
- ✅ `ListAllOrdersUseCaseControlTest`

**Domain Entities with Unit Tests**:
- ✅ `AccountTest`
- ✅ `ShoppingCartTest`
- ✅ `MotorbikeTest`

### Core Entities

```
Account
├── Role: CUSTOMER | ADMIN
├── Authentication: email, password (BCrypt hashed)
├── Status: isActive (boolean)
└── Relationships:
    └── 1:1 → ShoppingCart

Product (Abstract)
├── Motorbike
│   ├── brand, model, color
│   ├── yearOfManufacture, engineDisplacement
│   └── discount logic
└── Accessory
```

## Implemented Use Cases

### 1. Authentication & User Management
- **RegisterUseCase**: Register a new account
  - Validate email, username, password, phone
  - Check if email already exists
  - Automatically create a shopping cart for the new user
  
- **LoginUseCase**: Log in
  - Verify email and password
  - Check account status
  - Merge guest cart into user cart

### 2. Product Management
- **GetAllMotorbikesUseCase**: Get list of all motorbikes
- **SearchMotorbikesUseCase**: Search motorbikes by criteria
  - Filters: keyword, brand, model, color, CC range
- **GetProductDetailUseCase**: View product detail
  - Calculate discounted price
  - Calculate discount percentage

### 3. Shopping Cart Management
- **AddToCartUseCase**: Add product to cart
  - Validate input
  - Check stock availability
  - Accumulate quantity if product already in cart
  
- **ViewCartUseCase**: View shopping cart
  - Display list of products
  - Warn if quantity exceeds stock
  - Calculate total amount
  
- **UpdateCartQuantityUseCase**: Update quantity
  - Automatically remove if quantity = 0
  - Validate quantity

### 4. Order Management
- **CheckoutUseCase**: Checkout and create order
  - Validate delivery information
  - Check cart and stock
  - Create order
  - Automatically deduct stock
  - Clear cart after successful order
  
- **CancelOrderUseCase**: Cancel order
  - Verify cancellation permission
  - Check order status
  - Restore stock
  
- **ListAllOrdersUseCase**: View list of orders
  - Sorted by order date (newest first)

## System Requirements

- Java 11 or higher
- Maven 3.6+
- MySQL/PostgreSQL

## Installation

```bash
# Clone repository
git clone https://github.com/hayamij/JSB-CleanArchitecture-MotorbikeShop.git

# Navigate to project directory
cd JSB-CleanArchitecture-MotorbikeShop

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

## Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/motorbike_shop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Design Principles

### SOLID Principles

- **Single Responsibility**: Each class has a single responsibility
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subclasses can replace their parent class
- **Interface Segregation**: Small, focused interfaces
- **Dependency Inversion**: Depend on abstractions, not concrete implementations

### Clean Architecture Benefits

- ✅ **Testability**: Easy to write unit tests
- ✅ **Maintainability**: Code is easy to maintain and extend
- ✅ **Independence**: Layers are independent of each other
- ✅ **Flexibility**: Easy to swap UI, database, or framework

## Domain Model

```
Account
├── Role: CUSTOMER | ADMIN
└── ShoppingCart

Product
├── Motorbike
└── Accessory

Order
├── Status: PENDING | DELIVERING | COMPLETED | CANCELLED
└── OrderItems
```

## API Endpoints (Planned)

### Customer APIs
```
GET    /products/{id}           - View product detail
POST   /cart/add                - Add to cart
GET    /cart                    - View cart
PUT    /cart/update             - Update cart
POST   /auth/register           - Register
POST   /auth/login              - Login
POST   /checkout                - Checkout
```

### Admin APIs
```
GET    /admin/products          - Product list
POST   /admin/products          - Add product
PUT    /admin/products/{id}     - Edit product
DELETE /admin/products/{id}     - Delete product
GET    /admin/orders            - Order list
GET    /admin/users             - User list
```

## Contributing

When contributing to the project, please follow:

1. **Code Style**: Follow Java conventions
2. **Testing**: Write tests for every use case
3. **Documentation**: Comment code where necessary
4. **Clean Architecture**: Adhere to layering principles

## License

This project is licensed under the MIT License.

## Team

- **Project Type**: University Course Project
- **Architecture**: Clean Architecture Pattern
- **Focus**: Learning best practices in software design

---

**Note**: This is a learning project focused on applying Clean Architecture and SOLID principles in practice.
