# Quick Start Guide

## ✅ Implemented Use Cases (3/7)

1. ✅ **Get Product Detail** - `GET /api/products/{id}`
2. ✅ **Login** - `POST /api/auth/login`
3. ✅ **Register** - `POST /api/auth/register`
4. ⏭️ Add to Cart
5. ⏭️ Checkout
6. ⏭️ View Cart
7. ⏭️ Update Cart Quantity

---

## 🚀 Run Application

```bash
# Build
.\mvnw.cmd clean package

# Run
.\mvnw.cmd spring-boot:run

# Access
http://localhost:8080
```

---

## 🧪 Quick Tests

### 1. Get Product
```bash
curl http://localhost:8080/api/products/1
```

### 2. Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "username": "testuser",
    "password": "test123",
    "phoneNumber": "0987654321"
  }'
```

### 3. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@motorbike.com",
    "password": "password123"
  }'
```

---

## 📚 Full Documentation

See **ARCHITECTURE.md** for complete documentation of all use cases and architecture details.

---

## 🏗️ Project Structure

```
com.motorbike/
├── business/           # Core business logic
├── interfaceadapters/  # Controllers, DTOs
├── persistence/        # Database operations
└── frameworks/         # Spring Boot config
```

---

**Status**: ✅ BUILD SUCCESS - 36 source files compiled
