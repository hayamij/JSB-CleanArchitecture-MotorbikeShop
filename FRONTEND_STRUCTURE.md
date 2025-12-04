# Cấu trúc Frontend đã được Refactor

## 📁 Cấu trúc mới

```
src/main/resources/static/
├── css/                      # Tất cả file CSS
│   ├── common.css           # Styles dùng chung (alerts, loading, animations)
│   ├── index.css            # Landing page
│   ├── login.css            # Trang đăng nhập
│   ├── register.css         # Trang đăng ký
│   ├── home.css             # Trang chủ (danh sách sản phẩm)
│   ├── cart.css             # Giỏ hàng
│   ├── checkout.css         # Thanh toán
│   └── product-detail.css   # Chi tiết sản phẩm
│
├── js/                       # Tất cả file JavaScript
│   ├── common.js            # Functions dùng chung
│   ├── index.js             # Landing page logic
│   ├── login.js             # Xử lý đăng nhập
│   ├── register.js          # Xử lý đăng ký
│   ├── home.js              # Load và hiển thị sản phẩm
│   ├── cart.js              # Quản lý giỏ hàng
│   ├── checkout.js          # Xử lý thanh toán
│   └── product-detail.js    # Chi tiết sản phẩm
│
└── *.html                    # Các file HTML đã được refactor
```

## ✨ Những thay đổi chính

### 1. Tách biệt hoàn toàn HTML, CSS và JavaScript
- **Trước**: Tất cả code CSS và JS nằm trong thẻ `<style>` và `<script>` của file HTML
- **Sau**: CSS và JS được tách ra các file riêng biệt, dễ quản lý và tái sử dụng

### 2. Tạo file common.css và common.js
- Chứa các styles và functions dùng chung cho tất cả trang
- Giảm code trùng lặp
- Dễ dàng maintain và update

### 3. Cấu trúc rõ ràng
- Mỗi trang có riêng CSS và JS file
- Naming convention nhất quán
- Dễ dàng tìm kiếm và sửa lỗi

## 🔧 Cách sử dụng

### Link CSS và JS trong HTML

Tất cả các file HTML đều link CSS và JS theo pattern:

```html
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/page-specific.css">
</head>
<body>
    <!-- HTML content -->
    
    <script src="js/common.js"></script>
    <script src="js/page-specific.js"></script>
</body>
</html>
```

## 📝 Chi tiết các file

### CSS Files

#### common.css
- Reset styles
- Alert components (success, error, warning)
- Loading spinner
- Utility classes (.hidden)
- Animations (slideIn, slideOut, spin)

#### Specific CSS files
Mỗi file chứa styles cho từng trang cụ thể:
- Layout structures
- Component styles
- Responsive design
- Hover effects
- Media queries

### JavaScript Files

#### common.js
Chứa các utility functions:
- `formatCurrency(amount)` - Format số tiền VND
- `showAlert(message, type)` - Hiển thị alert
- `showLoading(show)` - Toggle loading state
- `showToast(message, type)` - Toast notification
- `checkAuth()` - Kiểm tra authentication
- `logout()` - Đăng xuất

#### Specific JS files
Mỗi file chứa logic cho từng trang:
- Event handlers
- API calls
- Form validation
- DOM manipulation
- Business logic

## 🎯 Lợi ích

### 1. Maintainability (Dễ bảo trì)
- Code được tổ chức rõ ràng
- Dễ tìm và sửa lỗi
- Giảm thiểu code duplication

### 2. Performance (Hiệu năng)
- Browser có thể cache CSS và JS files
- Giảm kích thước HTML files
- Tải song song CSS và JS

### 3. Scalability (Khả năng mở rộng)
- Dễ dàng thêm trang mới
- Có thể tái sử dụng components
- Cấu trúc nhất quán

### 4. Collaboration (Làm việc nhóm)
- Team members có thể work trên các files khác nhau
- Merge conflicts ít hơn
- Code review dễ dàng hơn

### 5. Separation of Concerns
- HTML: Structure
- CSS: Presentation
- JavaScript: Behavior
- Tuân thủ best practices

## 🚀 Testing

Sau khi refactor, test các chức năng:

1. **Landing Page (index.html)**
   - Auto redirect nếu đã đăng nhập
   - Navigation links

2. **Login Page (login.html)**
   - Form validation
   - API integration
   - Remember me
   - Password toggle

3. **Register Page (register.html)**
   - Form validation
   - Password strength
   - API integration

4. **Home Page (home.html)**
   - Load products from API
   - Add to cart
   - Product navigation
   - Authentication check

5. **Cart Page (cart.html)**
   - Display cart items
   - Update quantity
   - Remove items
   - Calculate total

6. **Checkout Page (checkout.html)**
   - Form validation
   - Order summary
   - Place order

7. **Product Detail (product-detail.html)**
   - Display product info
   - Quantity selector
   - Add to cart
   - Buy now

## 📌 Notes

- Tất cả file paths sử dụng relative paths (`css/`, `js/`)
- Compatible với Spring Boot static resources
- Giữ nguyên tất cả business logic
- Không thay đổi API endpoints
- Responsive design được preserve

---

**Refactored by:** GitHub Copilot
**Date:** December 4, 2025
