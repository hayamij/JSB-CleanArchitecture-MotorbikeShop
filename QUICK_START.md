# 🚀 QUICK START GUIDE

## Khởi động nhanh dự án trong 3 bước

### 📋 Yêu cầu hệ thống:
- ✅ Java 17 hoặc cao hơn
- ✅ Maven
- ✅ MySQL Server đang chạy
- ✅ Database đã được setup (chạy `database-setup.sql`)

---

## ⚡ CÁCH 1: Chạy từ Terminal/Command Line

### Bước 1: Mở Terminal tại thư mục dự án
```bash
cd JSB-CleanArchitecture-MotorbikeShop
```

### Bước 2: Chạy ứng dụng
```bash
# Windows
mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

### Bước 3: Mở trình duyệt
```
http://localhost:8080/static/index.html
```

---

## 🎯 CÁCH 2: Chạy từ IDE (IntelliJ IDEA / Eclipse)

### Bước 1: Mở dự án
- File → Open → Chọn thư mục `JSB-CleanArchitecture-MotorbikeShop`

### Bước 2: Tìm Main class
- Mở file: `src/main/java/.../Application.java`

### Bước 3: Run
- Click nút ▶️ Run (hoặc Shift+F10)

### Bước 4: Mở trình duyệt
```
http://localhost:8080/static/index.html
```

---

## 🧪 TEST NHANH CÁC CHỨC NĂNG

### ✅ Test 1: Đăng ký tài khoản mới
1. Vào http://localhost:8080/static/register.html
2. Điền form:
   - Email: `test@example.com`
   - Họ tên: `Test User`
   - Mật khẩu: `password123`
   - Xác nhận mật khẩu: `password123`
   - Số điện thoại: `0123456789`
   - Địa chỉ: `123 Test Street, HCM`
3. Click **Đăng ký tài khoản**
4. ✅ Chuyển về trang Login

### ✅ Test 2: Đăng nhập
1. Vào http://localhost:8080/static/login.html
2. Nhập:
   - Email: `test@example.com`
   - Mật khẩu: `password123`
3. Click **Đăng nhập**
4. ✅ Chuyển về trang Home

### ✅ Test 3: Xem danh sách sản phẩm
1. Tại trang Home, xem danh sách sản phẩm
2. ✅ Hiển thị các sản phẩm từ database

### ✅ Test 4: Xem chi tiết sản phẩm
1. Click vào một sản phẩm bất kỳ
2. ✅ Chuyển sang trang chi tiết với đầy đủ thông tin

### ✅ Test 5: Thêm vào giỏ hàng
1. Tại trang chi tiết sản phẩm
2. Điều chỉnh số lượng (nếu muốn)
3. Click **🛒 Thêm vào giỏ hàng**
4. ✅ Hiển thị thông báo thành công

### ✅ Test 6: Xem giỏ hàng
1. Click **Giỏ hàng** trên menu
2. ✅ Hiển thị các sản phẩm đã thêm

### ✅ Test 7: Cập nhật số lượng
1. Tại trang giỏ hàng
2. Click nút **+** hoặc **-** để thay đổi số lượng
3. Hoặc nhập trực tiếp số lượng
4. ✅ Giỏ hàng cập nhật tự động

### ✅ Test 8: Thanh toán
1. Tại trang giỏ hàng, click **💳 Thanh toán ngay**
2. Điền thông tin:
   - Tên người nhận: `Nguyen Van A`
   - Số điện thoại: `0987654321`
   - Địa chỉ: `456 Test Ave, HCM`
   - Ghi chú: (tùy chọn)
3. Click **✅ Đặt hàng**
4. ✅ Hiển thị popup thông báo đặt hàng thành công

---

## 🔧 TROUBLESHOOTING

### ❌ Lỗi: Port 8080 đã được sử dụng
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9
```

### ❌ Lỗi: Không kết nối được database
1. Kiểm tra MySQL đang chạy
2. Kiểm tra file `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/motorbike_shop
spring.datasource.username=root
spring.datasource.password=your_password
```

### ❌ Lỗi: Không tải được trang web
1. Đảm bảo Spring Boot đã khởi động thành công
2. Kiểm tra console log xem có lỗi không
3. Thử truy cập: http://localhost:8080/api/products

### ❌ Lỗi: API trả về 404
1. Kiểm tra URL đúng chưa
2. Kiểm tra CORS đã được enable
3. Xem log trong console của browser (F12)

---

## 📊 KIỂM TRA ỨNG DỤNG HOẠT ĐỘNG

### ✅ Checklist Backend:
```bash
# Test API Products
curl http://localhost:8080/api/products

# Nếu trả về JSON → Backend OK ✅
```

### ✅ Checklist Frontend:
1. Mở http://localhost:8080/static/index.html
2. Nếu hiển thị landing page → Frontend OK ✅

### ✅ Checklist Database:
```sql
-- Chạy trong MySQL
SELECT COUNT(*) FROM san_pham;
SELECT COUNT(*) FROM tai_khoan;

-- Nếu có dữ liệu → Database OK ✅
```

---

## 📱 DANH SÁCH CÁC TRANG

| Trang | URL | Mô tả |
|-------|-----|-------|
| Landing Page | `/static/index.html` | Trang chào mừng |
| Đăng nhập | `/static/login.html` | Login |
| Đăng ký | `/static/register.html` | Register |
| Trang chủ | `/static/home.html` | Danh sách SP |
| Chi tiết SP | `/static/product-detail.html?id=X` | Chi tiết |
| Giỏ hàng | `/static/cart.html` | Cart |
| Thanh toán | `/static/checkout.html` | Checkout |

---

## 🎯 DEMO SCENARIO

### Kịch bản demo hoàn chỉnh (5 phút):

1. **[0:00]** Mở landing page → Giới thiệu hệ thống
2. **[0:30]** Click Đăng ký → Tạo tài khoản mới
3. **[1:00]** Đăng nhập → Vào trang chủ
4. **[1:30]** Xem danh sách sản phẩm
5. **[2:00]** Click vào SP → Xem chi tiết
6. **[2:30]** Thêm vào giỏ hàng (x2 sản phẩm khác nhau)
7. **[3:00]** Vào giỏ hàng → Điều chỉnh số lượng
8. **[3:30]** Click Thanh toán
9. **[4:00]** Điền thông tin giao hàng
10. **[4:30]** Đặt hàng thành công → Show popup

---

## 📚 TÀI LIỆU THAM KHẢO

- **SUMMARY.md** - Tổng quan dự án
- **FRONTEND_GUIDE.md** - Hướng dẫn frontend chi tiết
- **API_TEST_GUIDE.md** - Hướng dẫn test API

---

## 💡 TIPS

### Mở DevTools của Browser:
- Press **F12** để xem:
  - Network tab: Xem API requests/responses
  - Console tab: Xem log/errors
  - Application tab: Xem sessionStorage/localStorage

### Clean Build:
```bash
# Nếu gặp lỗi lạ, thử clean build
mvnw clean install
mvnw spring-boot:run
```

### Hot Reload:
- Khi sửa file HTML/CSS/JS, chỉ cần **Refresh browser** (F5)
- Khi sửa Java code, cần **Restart Spring Boot**

---

## 🎉 CHÚC MỪNG!

Bạn đã sẵn sàng chạy và demo dự án **Motorbike Shop**!

**Có vấn đề gì?** Kiểm tra logs trong:
1. Terminal (Spring Boot logs)
2. Browser Console (JavaScript logs)
3. MySQL logs (Database errors)

---

**Happy Coding! 🚀**
