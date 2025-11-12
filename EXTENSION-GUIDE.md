# 📚 Hướng dẫn mở rộng hệ thống - Open/Closed Principle

## 🎯 Tại sao thiết kế này tốt?

### ✅ Ưu điểm của cấu trúc hiện tại

1. **Dễ thêm loại sản phẩm mới** mà không cần sửa code cũ
2. **Business logic** được đóng gói trong từng loại sản phẩm
3. **Database schema** linh hoạt với JOINED inheritance
4. **Testing** dễ dàng vì mỗi loại có test riêng

---

## 🚀 Ví dụ: Thêm loại sản phẩm mới "Đồ bảo hộ"

Giả sử bạn muốn thêm một loại sản phẩm mới là **Đồ bảo hộ** (áo giáp, quần bảo hộ, giày bảo hộ).

### Bước 1: Tạo Domain Entity mới

**File**: `src/main/java/com/motorbike/domain/entities/DoBaoHo.java`

```java
package com.motorbike.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DoBaoHo extends SanPham {
    private String loaiBaoHo; // áo giáp, quần, giày
    private String thuongHieu;
    private String kichCo; // S, M, L, XL, XXL
    private String capDoBaoVe; // CE Level 1, CE Level 2

    public DoBaoHo(String tenSanPham, String moTa, BigDecimal gia, 
                   String hinhAnh, int soLuongTonKho,
                   String loaiBaoHo, String thuongHieu, 
                   String kichCo, String capDoBaoVe) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.loaiBaoHo = loaiBaoHo;
        this.thuongHieu = thuongHieu;
        this.kichCo = kichCo;
        this.capDoBaoVe = capDoBaoVe;
    }

    // Constructor đầy đủ...

    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        // Business logic: Giảm 15% cho đồ bảo hộ CE Level 2
        if (this.capDoBaoVe != null && this.capDoBaoVe.contains("Level 2")) {
            return this.gia.multiply(BigDecimal.valueOf(0.85));
        }
        return this.gia;
    }

    @Override
    public String layThongTinChiTiet() {
        return String.format(
            "Đồ bảo hộ: %s\n" +
            "Loại: %s\n" +
            "Thương hiệu: %s\n" +
            "Kích cỡ: %s\n" +
            "Cấp độ bảo vệ: %s\n" +
            "Giá: %s VND",
            tenSanPham, loaiBaoHo, thuongHieu, kichCo, capDoBaoVe,
            gia.toString()
        );
    }

    // Getters and Setters...
}
```

### Bước 2: Tạo JPA Entity

**File**: `src/main/java/com/motorbike/infrastructure/persistence/jpa/entities/DoBaoHoJpaEntity.java`

```java
package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "do_bao_ho")
@PrimaryKeyJoinColumn(name = "ma_san_pham")
public class DoBaoHoJpaEntity extends SanPhamJpaEntity {

    @Column(name = "loai_bao_ho", length = 100)
    private String loaiBaoHo;

    @Column(name = "thuong_hieu", length = 100)
    private String thuongHieu;

    @Column(name = "kich_co", length = 20)
    private String kichCo;

    @Column(name = "cap_do_bao_ve", length = 50)
    private String capDoBaoVe;

    // Constructors, Getters, Setters...
}
```

### Bước 3: Thêm bảng vào Database

**File**: `database-setup-new.sql` (thêm vào cuối file)

```sql
-- Bảng do_bao_ho
CREATE TABLE do_bao_ho (
    ma_san_pham BIGINT PRIMARY KEY,
    loai_bao_ho VARCHAR(100),
    thuong_hieu VARCHAR(100),
    kich_co VARCHAR(20),
    cap_do_bao_ve VARCHAR(50),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_loai_bao_ho (loai_bao_ho),
    INDEX idx_cap_do_bao_ve (cap_do_bao_ve)
);

-- Sample data
INSERT INTO san_pham (ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
('Áo giáp Komine JK-006', 'Áo giáp cao cấp CE Level 2', 3500000.00, '/images/jacket-komine.jpg', 30, TRUE, 'DO_BAO_HO');

INSERT INTO do_bao_ho (ma_san_pham, loai_bao_ho, thuong_hieu, kich_co, cap_do_bao_ve) VALUES
(11, 'Áo giáp', 'Komine', 'XL', 'CE Level 2');
```

### Bước 4: Tạo Unit Test

**File**: `src/test/java/com/motorbike/domain/entities/DoBaoHoTest.java`

```java
package com.motorbike.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class DoBaoHoTest {

    @Test
    void testCreateDoBaoHo_Success() {
        // Given & When
        DoBaoHo doBaoHo = new DoBaoHo(
            "Áo giáp Komine", "Mô tả", 
            BigDecimal.valueOf(3500000), "/images/jacket.jpg", 30,
            "Áo giáp", "Komine", "XL", "CE Level 2"
        );

        // Then
        assertNotNull(doBaoHo);
        assertEquals("Áo giáp", doBaoHo.getLoaiBaoHo());
        assertTrue(doBaoHo.coConHang());
    }

    @Test
    void testTinhGiaSauKhuyenMai_CELevel2_HasDiscount() {
        // Given
        DoBaoHo doBaoHo = new DoBaoHo(
            "Áo giáp Komine", "Mô tả", 
            BigDecimal.valueOf(3500000), "/images/jacket.jpg", 30,
            "Áo giáp", "Komine", "XL", "CE Level 2"
        );

        // When
        BigDecimal giaSauKhuyenMai = doBaoHo.tinhGiaSauKhuyenMai();

        // Then
        BigDecimal expectedPrice = BigDecimal.valueOf(3500000)
            .multiply(BigDecimal.valueOf(0.85));
        assertEquals(0, expectedPrice.compareTo(giaSauKhuyenMai));
    }
}
```

### ✅ Hoàn tất!

Bạn vừa thêm một loại sản phẩm mới **mà không cần sửa bất kỳ code nào** của:
- ❌ `SanPham.java`
- ❌ `XeMay.java`
- ❌ `PhuKienXeMay.java`
- ❌ `GioHang.java`
- ❌ Bất kỳ Use Case nào

👉 **Đây chính là Open/Closed Principle!**

---

## 🎨 Các loại sản phẩm có thể thêm

### 1. Dầu nhớt (LubricantOil)
```java
public class DauNhot extends SanPham {
    private String loaiDauNhot; // Fully Synthetic, Semi-Synthetic, Mineral
    private String doNhot; // 5W-30, 10W-40, etc.
    private int dungTich; // ml
    // ...
}
```

### 2. Phụ tùng thay thế (SparePart)
```java
public class PhuTung extends SanPham {
    private String loaiPhuTung; // Lốp, Phanh, Lọc gió, etc.
    private String maPhuTung; // Part number
    private String tuongThichVoiXe; // Compatible với xe nào
    // ...
}
```

### 3. Combo sản phẩm (ProductBundle)
```java
public class ComboSanPham extends SanPham {
    private List<Long> danhSachSanPhamID;
    private BigDecimal giamGiaCombo; // % giảm giá khi mua combo
    // ...
}
```

---

## 📝 Best Practices khi mở rộng

### ✅ DOs (Nên làm)

1. **Luôn kế thừa từ `SanPham`**
   ```java
   public class NewProduct extends SanPham { ... }
   ```

2. **Override abstract methods**
   ```java
   @Override
   public BigDecimal tinhGiaSauKhuyenMai() {
       // Logic riêng cho loại sản phẩm này
   }
   ```

3. **Tạo JPA Entity tương ứng**
   ```java
   @Entity
   @Table(name = "new_product")
   @PrimaryKeyJoinColumn(name = "ma_san_pham")
   public class NewProductJpaEntity extends SanPhamJpaEntity { ... }
   ```

4. **Viết Unit Test đầy đủ**
   - Test constructors
   - Test validation
   - Test business logic riêng

### ❌ DON'Ts (Không nên)

1. **Không sửa `SanPham.java`** để thêm logic riêng cho 1 loại
2. **Không hardcode loại sản phẩm** trong các Use Case
3. **Không bỏ qua việc tạo test**
4. **Không quên update database migration**

---

## 🔍 So sánh trước và sau refactoring

### ❌ Trước (Bad Design)

```java
public class Product {
    private ProductCategory category; // Enum cố định
    
    public BigDecimal calculateDiscount() {
        // IF-ELSE cho từng loại - vi phạm OCP!
        if (category == MOTORCYCLE) {
            // logic xe máy
        } else if (category == ACCESSORY) {
            // logic phụ kiện
        }
        // Mỗi khi thêm loại mới phải sửa ở đây!
    }
}
```

### ✅ Sau (Good Design)

```java
public abstract class SanPham {
    // Không cần biết có bao nhiêu loại con
    public abstract BigDecimal tinhGiaSauKhuyenMai();
}

public class XeMay extends SanPham {
    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        // Logic riêng cho xe máy
    }
}

public class PhuKienXeMay extends SanPham {
    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        // Logic riêng cho phụ kiện
    }
}

// Thêm loại mới - KHÔNG cần sửa code cũ!
public class DoBaoHo extends SanPham {
    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        // Logic riêng cho đồ bảo hộ
    }
}
```

---

## 🎓 Kết luận

Với thiết kế hiện tại, bạn có thể:
- ✅ Thêm loại sản phẩm mới trong **10-15 phút**
- ✅ Không lo ảnh hưởng đến code cũ
- ✅ Test độc lập từng loại sản phẩm
- ✅ Business logic rõ ràng, dễ maintain

**Remember**: "Software entities should be open for extension, but closed for modification" - Bertrand Meyer

---

**Document Version**: 1.0  
**Created**: 2025-11-13  
**For**: Educational Purpose
