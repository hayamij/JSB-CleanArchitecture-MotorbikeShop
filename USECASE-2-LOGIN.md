# Use Case 2: Login (Đăng nhập)

## 📋 Overview

**Use Case**: Đăng nhập  
**Actor**: Customer, Admin (đã đăng xuất)  
**Preconditions**: Người dùng chưa đăng nhập và có tài khoản hợp lệ  

---

## 🏗️ Architecture - 4 Layers Implementation

### 1. **Business Layer** (`business/`)

#### Entity
```
business/entity/User.java
```
- Domain entity với business logic
- Methods: `isCustomer()`, `isAdmin()`, `canLogin()`, `updateLastLogin()`
- Pure Java, không phụ thuộc framework

#### Repository Interface
```
business/repository/UserRepository.java
```
- Contract cho persistence operations
- Methods: `findByEmail()`, `findByUsername()`, `save()`, `existsByEmail()`

#### Use Case
```
business/usecase/LoginUseCase.java
business/usecase/impl/LoginUseCaseImpl.java
```
- Business logic cho authentication
- Verify credentials, update last login, generate token
- Exceptions: `InvalidCredentialsException`, `UserNotActiveException`

---

### 2. **Interface Adapters Layer** (`interfaceadapters/`)

#### Controller
```
interfaceadapters/controller/AuthController.java
```
- Endpoint: `POST /api/auth/login`
- Handle HTTP requests/responses
- Error handling (401, 403, 400, 500)

#### DTOs
```
interfaceadapters/dto/LoginRequestDTO.java
interfaceadapters/dto/LoginResponseDTO.java
```
- API request/response objects
- JSON serialization

#### Mapper
```
interfaceadapters/mapper/LoginMapper.java
```
- Convert `LoginResponse` → `LoginResponseDTO`

---

### 3. **Persistence Layer** (`persistence/`)

#### JPA Entity
```
persistence/entity/UserJpaEntity.java
```
- `@Entity` annotation
- Maps to `users` table
- Fields: id, email, username, password, role, active, timestamps

#### Repository
```
persistence/repository/UserJpaRepository.java
```
- Spring Data JPA repository
- Methods: `findByEmail()`, `existsByEmail()`

#### Mapper
```
persistence/mapper/UserEntityMapper.java
```
- Convert between `UserJpaEntity` ↔ `User`

#### Adapter
```
persistence/adapter/UserRepositoryAdapter.java
```
- Implements `UserRepository` interface
- Bridges domain and JPA repositories

---

### 4. **Frameworks Layer** (`frameworks/`)

#### Configuration
```
frameworks/config/DataInitializer.java
```
- Initialize sample users on startup
- Sample credentials:
  - Customer: `customer@motorbike.com` / `password123`
  - Admin: `admin@motorbike.com` / `admin123`

---

## 🔄 Flow of Events

### Main Flow

1. **Client** → POST request to `/api/auth/login` với email & password
2. **AuthController** nhận request, validate input
3. **Controller** tạo `LoginRequest` và gọi `LoginUseCase`
4. **LoginUseCaseImpl** thực hiện:
   - Tìm user by email qua `UserRepository`
   - Check user active status
   - Verify password
   - Update last login timestamp
   - Generate authentication token
5. **Use Case** trả về `LoginResponse`
6. **LoginMapper** convert → `LoginResponseDTO`
7. **Controller** trả về `ResponseEntity` với HTTP 200

### Alternative Flows

#### Invalid Credentials
- Use case throws `InvalidCredentialsException`
- Controller trả về HTTP 401 Unauthorized

#### User Not Active
- Use case throws `UserNotActiveException`
- Controller trả về HTTP 403 Forbidden

#### Invalid Input
- Validation fails
- Controller trả về HTTP 400 Bad Request

---

## 📡 API Specification

### Endpoint
```
POST /api/auth/login
Content-Type: application/json
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

#### 401 Unauthorized (Invalid Credentials)
```json
{
  "userId": null,
  "email": null,
  "username": null,
  "role": null,
  "token": null,
  "success": false,
  "message": "Invalid email or password"
}
```

#### 403 Forbidden (User Not Active)
```json
{
  "success": false,
  "message": "User account is not active"
}
```

#### 400 Bad Request (Invalid Input)
```json
{
  "success": false,
  "message": "Email and password are required"
}
```

---

## 💾 Database Schema

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

## 🧪 Testing

### Test Scenarios

1. **Successful Login - Customer**
   - Email: `customer@motorbike.com`
   - Password: `password123`
   - Expected: HTTP 200, token returned

2. **Successful Login - Admin**
   - Email: `admin@motorbike.com`
   - Password: `admin123`
   - Expected: HTTP 200, token returned, role = ADMIN

3. **Invalid Email**
   - Email: `notexist@example.com`
   - Expected: HTTP 401, error message

4. **Wrong Password**
   - Email: `customer@motorbike.com`
   - Password: `wrongpassword`
   - Expected: HTTP 401, error message

5. **Empty Email**
   - Email: `""`
   - Expected: HTTP 400, validation error

6. **Empty Password**
   - Password: `""`
   - Expected: HTTP 400, validation error

### Using cURL
```bash
# Successful login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@motorbike.com","password":"password123"}'

# Invalid credentials
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@motorbike.com","password":"wrong"}'
```

### Using Postman
1. Method: POST
2. URL: `http://localhost:8080/api/auth/login`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "email": "customer@motorbike.com",
  "password": "password123"
}
```

---

## 🔐 Security Notes

### Current Implementation (Demo Only)
- ⚠️ Passwords stored in plain text
- ⚠️ Simple token generation (UUID)
- ⚠️ No token expiration
- ⚠️ No rate limiting

### Production Recommendations
```java
// 1. Hash passwords with BCrypt
String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

// 2. Verify with BCrypt
boolean isValid = BCrypt.checkpw(plainPassword, hashedPassword);

// 3. Use JWT for tokens
String token = Jwts.builder()
    .setSubject(user.getEmail())
    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
    .signWith(SignatureAlgorithm.HS512, secretKey)
    .compact();

// 4. Add Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Configure authentication, authorization, CORS, CSRF
}

// 5. Implement rate limiting
@RateLimiter(name = "login", fallbackMethod = "loginFallback")
public LoginResponse login(LoginRequest request) {
    // ...
}
```

---

## 📝 Sample Users

After running `DataInitializer`, you can test with:

| Email | Password | Role | Status |
|-------|----------|------|--------|
| customer@motorbike.com | password123 | CUSTOMER | Active |
| admin@motorbike.com | admin123 | ADMIN | Active |
| john.doe@example.com | john123 | CUSTOMER | Active |

---

## 🎯 Next Steps

1. ✅ Use Case 2 (Login) - COMPLETED
2. ⏭️ Use Case 3 (Register) - Next
3. ⏭️ Use Case 4 (Add to Cart)
4. ⏭️ Use Case 5 (Checkout)

---

**Implemented by**: Clean Architecture 4 Layers  
**Date**: November 10, 2025  
**Status**: ✅ Completed & Tested
