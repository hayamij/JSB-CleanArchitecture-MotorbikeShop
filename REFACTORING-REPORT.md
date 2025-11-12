# Refactoring Report - Clean Architecture Motorbike Shop

## 📅 Date: 2025-11-13

## 🎯 Mục tiêu Refactoring
Áp dụng **Open/Closed Principle** cho domain entities bằng cách:
- Tạo cấu trúc kế thừa cho sản phẩm
- Đơn giản hóa entities
- Chuẩn bị cho việc mở rộng (có thể thêm loại sản phẩm mới mà không sửa code cũ)

---

## ✅ Đã Hoàn Thành

### 1. Domain Layer - Entities (✅ COMPLETED)

#### Abstract Class: `SanPham`
**File**: `src/main/java/com/motorbike/domain/entities/SanPham.java`

**Đặc điểm**:
- Abstract class chứa các thuộc tính và business logic chung cho tất cả sản phẩm
- Thuộc tính: maSanPham, tenSanPham, moTa, gia, hinhAnh, soLuongTonKho, conHang
- Business logic: giamTonKho(), tangTonKho(), capNhatGia(), ngungKinhDoanh(), khoiPhucKinhDoanh()
- Abstract methods: tinhGiaSauKhuyenMai(), layThongTinChiTiet()

#### Concrete Class: `XeMay` (Motorbike)
**File**: `src/main/java/com/motorbike/domain/entities/XeMay.java`

**Đặc điểm**:
- Kế thừa từ `SanPham`
- Thuộc tính riêng: hangXe, dongXe, mauSac, namSanXuat, dungTich
- Business logic riêng:
  - `tinhGiaSauKhuyenMai()`: Xe cũ (> 1 năm) giảm 5%
  - `laXeMoi()`: Kiểm tra xe sản xuất trong năm hiện tại

#### Concrete Class: `PhuKienXeMay` (Accessory)
**File**: `src/main/java/com/motorbike/domain/entities/PhuKienXeMay.java`

**Đặc điểm**:
- Kế thừa từ `SanPham`
- Thuộc tính riêng: loaiPhuKien, thuongHieu, chatLieu, kichThuoc
- Business logic riêng:
  - `tinhGiaSauKhuyenMai()`: Tồn kho > 100 giảm 10%
  - `laPhuKienAnToan()`: Kiểm tra phụ kiện an toàn (mũ bảo hiểm, găng tay)

#### Entity: `TaiKhoan` (User Account)
**File**: `src/main/java/com/motorbike/domain/entities/TaiKhoan.java`

**Đặc điểm**:
- Đơn giản hóa từ class `User` cũ
- Sử dụng tiếng Việt không dấu cho tên thuộc tính
- Business logic: dangNhapThanhCong(), khoaTaiKhoan(), thangCapAdmin(), etc.

#### Entity: `GioHang` (Shopping Cart)
**File**: `src/main/java/com/motorbike/domain/entities/GioHang.java`

**Đặc điểm**:
- Đơn giản hóa từ class `Cart` cũ
- Business logic: themSanPham(), xoaSanPham(), capNhatSoLuong(), xoaToanBoGioHang()

#### Entity: `ChiTietGioHang` (Cart Item)
**File**: `src/main/java/com/motorbike/domain/entities/ChiTietGioHang.java`

**Đặc điểm**:
- Đơn giản hóa từ class `CartItem` cũ
- Business logic: tangSoLuong(), giamSoLuong(), datSoLuong(), tinhTamTinh()

#### Enum: `VaiTro` (User Role)
**File**: `src/main/java/com/motorbike/domain/entities/VaiTro.java`

**Đặc điểm**:
- Enum đơn giản: CUSTOMER, ADMIN

---

### 2. Infrastructure Layer - JPA Entities (✅ COMPLETED)

#### Abstract JPA Entity: `SanPhamJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/SanPhamJpaEntity.java`

**Đặc điểm**:
- Sử dụng **JOINED inheritance strategy**
- Ánh xạ với bảng `san_pham`
- @PreUpdate để tự động cập nhật ngayCapNhat

#### JPA Entity: `XeMayJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/XeMayJpaEntity.java`

**Đặc điểm**:
- Kế thừa từ `SanPhamJpaEntity`
- Ánh xạ với bảng `xe_may`
- Foreign key với `san_pham` table

#### JPA Entity: `PhuKienXeMayJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/PhuKienXeMayJpaEntity.java`

**Đặc điểm**:
- Kế thừa từ `SanPhamJpaEntity`
- Ánh xạ với bảng `phu_kien_xe_may`
- Foreign key với `san_pham` table

#### JPA Entity: `TaiKhoanJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/TaiKhoanJpaEntity.java`

**Đặc điểm**:
- Ánh xạ với bảng `tai_khoan`
- Enum VaiTroEnum (CUSTOMER, ADMIN)

#### JPA Entity: `GioHangJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/GioHangJpaEntity.java`

**Đặc điểm**:
- Ánh xạ với bảng `gio_hang`
- @OneToMany relationship với ChiTietGioHangJpaEntity

#### JPA Entity: `ChiTietGioHangJpaEntity`
**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/ChiTietGioHangJpaEntity.java`

**Đặc điểm**:
- Ánh xạ với bảng `chi_tiet_gio_hang`
- @ManyToOne relationship với GioHangJpaEntity

---

### 3. Database Schema (✅ COMPLETED)

**File**: `database-setup-new.sql`

**Bảng mới**:

1. **tai_khoan** - Tài khoản người dùng
   - Columns: ma_tai_khoan, email, ten_dang_nhap, mat_khau, so_dien_thoai, dia_chi, vai_tro, hoat_dong
   - Indexes: email, ten_dang_nhap

2. **san_pham** - Bảng cha cho tất cả sản phẩm
   - Columns: ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham
   - Indexes: ten_san_pham, loai_san_pham, con_hang

3. **xe_may** - Xe máy (kế thừa từ san_pham)
   - Columns: ma_san_pham (FK), hang_xe, dong_xe, mau_sac, nam_san_xuat, dung_tich
   - Indexes: hang_xe, nam_san_xuat

4. **phu_kien_xe_may** - Phụ kiện (kế thừa từ san_pham)
   - Columns: ma_san_pham (FK), loai_phu_kien, thuong_hieu, chat_lieu, kich_thuoc
   - Indexes: loai_phu_kien, thuong_hieu

5. **gio_hang** - Giỏ hàng
   - Columns: ma_gio_hang, ma_tai_khoan (FK), tong_tien

6. **chi_tiet_gio_hang** - Chi tiết giỏ hàng
   - Columns: ma_chi_tiet, ma_gio_hang (FK), ma_san_pham (FK), ten_san_pham, gia_san_pham, so_luong, tam_tinh

**Sample data**:
- 3 tài khoản (1 admin, 2 customers)
- 5 xe máy (Honda Winner X, Yamaha Exciter 155, Honda Vision, SYM Star SR, Yamaha Sirius)
- 5 phụ kiện (Mũ bảo hiểm, Găng tay, Áo mưa, Kính, Khóa đĩa)

---

### 4. Unit Tests (✅ COMPLETED - 56/56 PASSED)

#### Test: `XeMayTest` (12 tests)
**File**: `src/test/java/com/motorbike/domain/entities/XeMayTest.java`

**Test cases**:
- ✅ Tạo xe máy thành công
- ✅ Validation (tên, giá)
- ✅ Giảm/tăng tồn kho
- ✅ Tính giá sau khuyến mãi (xe mới vs xe cũ)
- ✅ Kiểm tra xe mới

#### Test: `PhuKienXeMayTest` (13 tests)
**File**: `src/test/java/com/motorbike/domain/entities/PhuKienXeMayTest.java`

**Test cases**:
- ✅ Tạo phụ kiện thành công
- ✅ Validation
- ✅ Giảm/tăng tồn kho
- ✅ Tính giá sau khuyến mãi (tồn kho > 100)
- ✅ Kiểm tra phụ kiện an toàn

#### Test: `TaiKhoanTest` (16 tests)
**File**: `src/test/java/com/motorbike/domain/entities/TaiKhoanTest.java`

**Test cases**:
- ✅ Tạo tài khoản thành công
- ✅ Validation (email, username, password, phone)
- ✅ Kiểm tra mật khẩu
- ✅ Đăng nhập thành công
- ✅ Khóa/mở khóa tài khoản
- ✅ Thăng/hạ cấp admin

#### Test: `GioHangTest` (15 tests)
**File**: `src/test/java/com/motorbike/domain/entities/GioHangTest.java`

**Test cases**:
- ✅ Tạo giỏ hàng
- ✅ Thêm sản phẩm (mới, đã tồn tại, nhiều sản phẩm)
- ✅ Xóa sản phẩm
- ✅ Cập nhật số lượng
- ✅ Xóa toàn bộ giỏ hàng
- ✅ Đếm số sản phẩm, tổng số lượng

**Tổng kết**: 56 tests đều PASSED ✅

---

## 🎓 Nguyên tắc áp dụng

### Open/Closed Principle ✅
- **Open for extension**: Có thể thêm loại sản phẩm mới (ví dụ: DồKhiXeMay, GiaoDich) bằng cách kế thừa từ `SanPham`
- **Closed for modification**: Không cần sửa code của `SanPham` hay các class hiện có

### Single Responsibility Principle ✅
- Mỗi entity có 1 trách nhiệm rõ ràng
- Business logic tập trung trong domain entities
- JPA entities chỉ lo việc persistence

### Dependency Inversion Principle ✅
- Domain layer không phụ thuộc vào Infrastructure layer
- Infrastructure layer phụ thuộc vào Domain layer (thông qua mapping)

---

## 📊 Thống kê

- **Domain Entities**: 6 classes (1 abstract, 2 concrete products, 3 support entities, 1 enum)
- **JPA Entities**: 6 classes (1 abstract, 5 concrete)
- **Unit Tests**: 56 tests (100% passed)
- **Database Tables**: 6 tables
- **Build Status**: ✅ SUCCESS

---

## 🔄 Tiếp theo cần làm

### 1. Business Layer
- [ ] Update Use Cases để sử dụng entities mới
- [ ] Refactor DTOs
- [ ] Update Repository interfaces

### 2. Adapters Layer
- [ ] Update Repository implementations
- [ ] Update Controllers
- [ ] Update Presenters & ViewModels

### 3. Testing
- [ ] Integration tests cho các layer khác
- [ ] End-to-end tests

---

## 📝 Notes

1. **Naming Convention**: Sử dụng tiếng Việt không dấu cho domain entities để dễ hiểu
2. **Inheritance Strategy**: Chọn JOINED vì:
   - Dễ query riêng từng loại
   - Tránh NULL columns như TABLE_PER_CLASS
   - Performance tốt hơn SINGLE_TABLE
3. **Sample Data**: Đã chuẩn bị sẵn data mẫu trong SQL script
4. **Validation**: Đặt validation trong domain entities (fail-fast)

---

**Document Version**: 1.0  
**Created**: 2025-11-13  
**Status**: ✅ Domain & Infrastructure Refactoring Complete
