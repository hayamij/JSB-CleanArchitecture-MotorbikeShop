# Motorbike Shop API Testing Guide

## Server Information
- **Base URL**: `http://localhost:8080`
- **Port**: 8080
- **API Prefix**: `/api`

---

## 📝 API Endpoints & Testing Examples

### 1. Authentication APIs (`/api/auth`)

#### 1.1 Register (Đăng ký tài khoản)
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "Nguyen Van A",
  "phone": "0123456789",
  "address": "123 Nguyen Trai, Quan 1, TP.HCM"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123","name":"Nguyen Van A","phone":"0123456789","address":"123 Nguyen Trai, Quan 1, TP.HCM"}'
```

**Success Response (201):**
```json
{
  "success": true,
  "userId": 1,
  "email": "user@example.com",
  "name": "Nguyen Van A",
  "role": "CUSTOMER",
  "message": "Đăng ký thành công"
}
```

#### 1.2 Login (Đăng nhập)
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

**Success Response (200):**
```json
{
  "success": true,
  "userId": 1,
  "email": "user@example.com",
  "name": "Nguyen Van A",
  "role": "CUSTOMER",
  "cartId": 1,
  "message": "Đăng nhập thành công"
}
```

---

### 2. Product APIs (`/api/products`)

#### 2.1 Get Product Detail (Xem chi tiết sản phẩm)
```bash
GET http://localhost:8080/api/products/1
```

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/products/1
```

**Success Response (200):**
```json
{
  "success": true,
  "productId": 1,
  "productName": "Honda Wave",
  "description": "Xe số tiết kiệm nhiên liệu",
  "price": 30000000,
  "imageUrl": "wave.jpg",
  "stockQuantity": 10,
  "available": true,
  "category": "XE_MAY",
  "brand": "Honda",
  "model": "Wave",
  "color": "Đỏ",
  "year": 2023,
  "engineCapacity": 110
}
```

---

### 3. Cart APIs (`/api/cart`)

#### 3.1 Add to Cart (Thêm sản phẩm vào giỏ hàng)
```bash
POST http://localhost:8080/api/cart/add
Content-Type: application/json

{
  "userId": 1,
  "productId": 1,
  "quantity": 2
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"quantity":2}'
```

**Success Response (200):**
```json
{
  "success": true,
  "cartId": 1,
  "productId": 1,
  "productName": "Honda Wave",
  "quantity": 2,
  "totalItemsInCart": 3,
  "message": "Đã thêm sản phẩm vào giỏ hàng"
}
```

#### 3.2 View Cart (Xem giỏ hàng)
```bash
GET http://localhost:8080/api/cart/1
```

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/cart/1
```

**Success Response (200):**
```json
{
  "success": true,
  "cartId": 1,
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "productName": "Honda Wave",
      "price": 30000000,
      "quantity": 2,
      "subtotal": 60000000
    }
  ],
  "totalAmount": 60000000,
  "totalItems": 1
}
```

#### 3.3 Update Cart Quantity (Cập nhật số lượng)
```bash
PUT http://localhost:8080/api/cart/update
Content-Type: application/json

{
  "userId": 1,
  "productId": 1,
  "newQuantity": 5
}
```

**Note:** Nếu `newQuantity = 0`, sản phẩm sẽ bị xóa khỏi giỏ hàng

**cURL Command:**
```bash
curl -X PUT http://localhost:8080/api/cart/update \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"newQuantity":5}'
```

**Success Response (200):**
```json
{
  "success": true,
  "cartId": 1,
  "productId": 1,
  "newQuantity": 5,
  "newSubtotal": 150000000,
  "newTotalAmount": 150000000,
  "message": "Đã cập nhật số lượng"
}
```

---

### 4. Order APIs (`/api/orders`)

#### 4.1 Checkout (Thanh toán đơn hàng)
```bash
POST http://localhost:8080/api/orders/checkout
Content-Type: application/json

{
  "userId": 1,
  "receiverName": "Nguyen Van A",
  "phoneNumber": "0123456789",
  "shippingAddress": "123 Nguyen Trai, Quan 1, TP.HCM",
  "note": "Giao giờ hành chính"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"receiverName":"Nguyen Van A","phoneNumber":"0123456789","shippingAddress":"123 Nguyen Trai, Quan 1, TP.HCM","note":"Giao giờ hành chính"}'
```

**Success Response (201):**
```json
{
  "success": true,
  "orderId": 1,
  "customerId": 1,
  "customerName": "Nguyen Van A",
  "customerPhone": "0123456789",
  "shippingAddress": "123 Nguyen Trai, Quan 1, TP.HCM",
  "orderStatus": "CHO_XAC_NHAN",
  "totalAmount": 60000000,
  "totalItems": 2,
  "totalQuantity": 3,
  "items": [
    {
      "productId": 1,
      "productName": "Honda Wave",
      "unitPrice": 30000000,
      "quantity": 2,
      "subtotal": 60000000
    }
  ],
  "orderDate": "2025-11-14T10:30:00"
}
```

---

## 🧪 Testing Flow (Luồng test đầy đủ)

### 1. Đăng ký tài khoản
```bash
POST /api/auth/register
```

### 2. Đăng nhập
```bash
POST /api/auth/login
# Lưu lại userId để dùng cho các bước sau
```

### 3. Xem chi tiết sản phẩm
```bash
GET /api/products/1
```

### 4. Thêm sản phẩm vào giỏ hàng
```bash
POST /api/cart/add
# userId từ bước 2
```

### 5. Xem giỏ hàng
```bash
GET /api/cart/{userId}
```

### 6. Cập nhật số lượng (optional)
```bash
PUT /api/cart/update
```

### 7. Thanh toán
```bash
POST /api/orders/checkout
# Giỏ hàng sẽ bị xóa sau khi thanh toán thành công
```

---

## 🛠️ Tools for Testing

### 1. **Postman**
- Import các endpoint trên vào Postman
- Tạo Collection để dễ quản lý

### 2. **cURL** (Command Line)
- Sử dụng các cURL command ở trên
- Chạy trực tiếp trong terminal

### 3. **Browser** (GET requests only)
```
http://localhost:8080/api/products/1
http://localhost:8080/api/cart/1
```

### 4. **VS Code REST Client Extension**
- Tạo file `.http` hoặc `.rest`
- Copy các request examples vào file

---

## ⚠️ Common Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "errorCode": "INVALID_INPUT",
  "errorMessage": "Dữ liệu không hợp lệ"
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "errorCode": "WRONG_PASSWORD",
  "errorMessage": "Mật khẩu không đúng"
}
```

### 404 Not Found
```json
{
  "success": false,
  "errorCode": "PRODUCT_NOT_FOUND",
  "errorMessage": "Không tìm thấy sản phẩm"
}
```

---

## 📌 Notes

1. **Database Setup**: Đảm bảo đã chạy `database-setup.sql` trước khi test
2. **Port**: Mặc định server chạy trên port 8080
3. **CORS**: Đã enable CORS cho tất cả origins (`*`)
4. **Business Rules**: 
   - Guest có thể xem sản phẩm và thêm vào giỏ
   - Chỉ user đăng nhập mới checkout được
   - Tồn kho được kiểm tra trước khi thanh toán
   - Giỏ hàng tự động xóa sau khi thanh toán thành công
