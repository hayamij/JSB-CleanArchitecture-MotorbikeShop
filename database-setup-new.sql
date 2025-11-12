-- Database Schema cho Clean Architecture Motorbike Shop
-- Sử dụng JOINED inheritance strategy cho SanPham

-- Drop tables nếu tồn tại
DROP TABLE IF EXISTS chi_tiet_gio_hang;
DROP TABLE IF EXISTS gio_hang;
DROP TABLE IF EXISTS phu_kien_xe_may;
DROP TABLE IF EXISTS xe_may;
DROP TABLE IF EXISTS san_pham;
DROP TABLE IF EXISTS tai_khoan;

-- Bảng tai_khoan (User Account)
CREATE TABLE tai_khoan (
    ma_tai_khoan BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    so_dien_thoai VARCHAR(20),
    dia_chi TEXT,
    vai_tro VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    hoat_dong BOOLEAN NOT NULL DEFAULT TRUE,
    ngay_tao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    lan_dang_nhap_cuoi DATETIME,
    INDEX idx_email (email),
    INDEX idx_ten_dang_nhap (ten_dang_nhap)
);

-- Bảng san_pham (Abstract Product) - Parent table
CREATE TABLE san_pham (
    ma_san_pham BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_san_pham VARCHAR(255) NOT NULL,
    mo_ta TEXT,
    gia DECIMAL(15, 2) NOT NULL,
    hinh_anh VARCHAR(500),
    so_luong_ton_kho INT NOT NULL DEFAULT 0,
    con_hang BOOLEAN NOT NULL DEFAULT TRUE,
    ngay_tao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    loai_san_pham VARCHAR(50) NOT NULL,
    INDEX idx_ten_san_pham (ten_san_pham),
    INDEX idx_loai_san_pham (loai_san_pham),
    INDEX idx_con_hang (con_hang)
);

-- Bảng xe_may (Motorbike) - Child table
CREATE TABLE xe_may (
    ma_san_pham BIGINT PRIMARY KEY,
    hang_xe VARCHAR(100),
    dong_xe VARCHAR(100),
    mau_sac VARCHAR(50),
    nam_san_xuat INT,
    dung_tich INT,
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_hang_xe (hang_xe),
    INDEX idx_nam_san_xuat (nam_san_xuat)
);

-- Bảng phu_kien_xe_may (Accessory) - Child table
CREATE TABLE phu_kien_xe_may (
    ma_san_pham BIGINT PRIMARY KEY,
    loai_phu_kien VARCHAR(100),
    thuong_hieu VARCHAR(100),
    chat_lieu VARCHAR(100),
    kich_thuoc VARCHAR(50),
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_loai_phu_kien (loai_phu_kien),
    INDEX idx_thuong_hieu (thuong_hieu)
);

-- Bảng gio_hang (Shopping Cart)
CREATE TABLE gio_hang (
    ma_gio_hang BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_tai_khoan BIGINT,
    tong_tien DECIMAL(15, 2) DEFAULT 0.00,
    ngay_tao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_tai_khoan) REFERENCES tai_khoan(ma_tai_khoan) ON DELETE CASCADE,
    INDEX idx_ma_tai_khoan (ma_tai_khoan)
);

-- Bảng chi_tiet_gio_hang (Cart Item)
CREATE TABLE chi_tiet_gio_hang (
    ma_chi_tiet BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_gio_hang BIGINT NOT NULL,
    ma_san_pham BIGINT NOT NULL,
    ten_san_pham VARCHAR(255),
    gia_san_pham DECIMAL(15, 2) NOT NULL,
    so_luong INT NOT NULL DEFAULT 1,
    tam_tinh DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (ma_gio_hang) REFERENCES gio_hang(ma_gio_hang) ON DELETE CASCADE,
    FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_ma_gio_hang (ma_gio_hang),
    INDEX idx_ma_san_pham (ma_san_pham)
);

-- Insert sample data

-- Sample users
INSERT INTO tai_khoan (email, ten_dang_nhap, mat_khau, so_dien_thoai, dia_chi, vai_tro) VALUES
('admin@motorbike.com', 'admin', '$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', '0901234567', 'Hà Nội', 'ADMIN'),
('customer1@gmail.com', 'customer1', '$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', '0912345678', 'TP.HCM', 'CUSTOMER'),
('customer2@gmail.com', 'customer2', '$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', '0923456789', 'Đà Nẵng', 'CUSTOMER');

-- Sample motorbikes (xe_may)
INSERT INTO san_pham (ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
('Honda Winner X', 'Xe thể thao phân khối 150cc, động cơ mạnh mẽ', 46000000.00, '/images/honda-winner-x.jpg', 10, TRUE, 'XE_MAY'),
('Yamaha Exciter 155', 'Xe côn tay thể thao, thiết kế trẻ trung', 47000000.00, '/images/yamaha-exciter-155.jpg', 15, TRUE, 'XE_MAY'),
('Honda Vision', 'Xe tay ga cao cấp, tiện nghi', 30000000.00, '/images/honda-vision.jpg', 20, TRUE, 'XE_MAY'),
('SYM Star SR 170', 'Xe thể thao phân khối 170cc', 52000000.00, '/images/sym-star-sr-170.jpg', 8, TRUE, 'XE_MAY'),
('Yamaha Sirius', 'Xe số tiết kiệm nhiên liệu', 21000000.00, '/images/yamaha-sirius.jpg', 25, TRUE, 'XE_MAY');

INSERT INTO xe_may (ma_san_pham, hang_xe, dong_xe, mau_sac, nam_san_xuat, dung_tich) VALUES
(1, 'Honda', 'Winner X', 'Đỏ đen', 2025, 150),
(2, 'Yamaha', 'Exciter 155', 'Xanh GP', 2025, 155),
(3, 'Honda', 'Vision', 'Trắng', 2025, 110),
(4, 'SYM', 'Star SR', 'Đen', 2024, 170),
(5, 'Yamaha', 'Sirius', 'Xanh đen', 2025, 110);

-- Sample accessories (phu_kien_xe_may)
INSERT INTO san_pham (ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
('Mũ bảo hiểm fullface Royal M139', 'Mũ bảo hiểm cao cấp, đạt chuẩn an toàn', 850000.00, '/images/helmet-royal.jpg', 50, TRUE, 'PHU_KIEN'),
('Găng tay Komine GK-162', 'Găng tay bảo hộ chống trượt', 450000.00, '/images/gloves-komine.jpg', 100, TRUE, 'PHU_KIEN'),
('Áo mưa Givi', 'Áo mưa cao cấp, chống thấm tốt', 250000.00, '/images/raincoat-givi.jpg', 150, TRUE, 'PHU_KIEN'),
('Kính mũ bảo hiểm Bulldog', 'Kính chống bụi, chống tia UV', 120000.00, '/images/visor-bulldog.jpg', 200, TRUE, 'PHU_KIEN'),
('Khóa đĩa Kinbar', 'Khóa đĩa chống trộm cao cấp', 350000.00, '/images/lock-kinbar.jpg', 80, TRUE, 'PHU_KIEN');

INSERT INTO phu_kien_xe_may (ma_san_pham, loai_phu_kien, thuong_hieu, chat_lieu, kich_thuoc) VALUES
(6, 'Mũ bảo hiểm', 'Royal', 'ABS + EPS', 'L'),
(7, 'Găng tay', 'Komine', 'Da + vải', 'XL'),
(8, 'Áo mưa', 'Givi', 'Vải PVC', 'L'),
(9, 'Kính mũ bảo hiểm', 'Bulldog', 'Polycarbonate', 'Universal'),
(10, 'Khóa đĩa', 'Kinbar', 'Thép hợp kim', 'Universal');

-- Sample carts
INSERT INTO gio_hang (ma_tai_khoan, tong_tien) VALUES
(2, 0.00),
(3, 0.00);

COMMIT;

-- Verify data
SELECT 'Tài khoản:' as 'Thống kê', COUNT(*) as 'Số lượng' FROM tai_khoan
UNION ALL
SELECT 'Xe máy:', COUNT(*) FROM xe_may
UNION ALL
SELECT 'Phụ kiện:', COUNT(*) FROM phu_kien_xe_may
UNION ALL
SELECT 'Sản phẩm (tổng):', COUNT(*) FROM san_pham
UNION ALL
SELECT 'Giỏ hàng:', COUNT(*) FROM gio_hang;
