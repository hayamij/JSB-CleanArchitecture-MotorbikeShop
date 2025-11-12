-- Database Schema cho Clean Architecture Motorbike Shop
-- T-SQL cho SQL Server
-- Sử dụng JOINED inheritance strategy cho SanPham

-- Drop tables nếu tồn tại (SQL Server syntax)
IF OBJECT_ID('dbo.chi_tiet_gio_hang', 'U') IS NOT NULL DROP TABLE dbo.chi_tiet_gio_hang;
IF OBJECT_ID('dbo.gio_hang', 'U') IS NOT NULL DROP TABLE dbo.gio_hang;
IF OBJECT_ID('dbo.phu_kien_xe_may', 'U') IS NOT NULL DROP TABLE dbo.phu_kien_xe_may;
IF OBJECT_ID('dbo.xe_may', 'U') IS NOT NULL DROP TABLE dbo.xe_may;
IF OBJECT_ID('dbo.san_pham', 'U') IS NOT NULL DROP TABLE dbo.san_pham;
IF OBJECT_ID('dbo.tai_khoan', 'U') IS NOT NULL DROP TABLE dbo.tai_khoan;
GO

-- Bảng tai_khoan (User Account)
CREATE TABLE tai_khoan (
    ma_tai_khoan BIGINT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL UNIQUE,
    ten_dang_nhap NVARCHAR(50) NOT NULL UNIQUE,
    mat_khau NVARCHAR(255) NOT NULL,
    so_dien_thoai NVARCHAR(20),
    dia_chi NVARCHAR(MAX),
    vai_tro NVARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    hoat_dong BIT NOT NULL DEFAULT 1,
    ngay_tao DATETIME2 NOT NULL DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME2 NOT NULL DEFAULT GETDATE(),
    lan_dang_nhap_cuoi DATETIME2,
    INDEX idx_email (email),
    INDEX idx_ten_dang_nhap (ten_dang_nhap)
);
GO

-- Trigger để auto-update ngay_cap_nhat
CREATE TRIGGER trg_tai_khoan_update
ON tai_khoan
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE tai_khoan
    SET ngay_cap_nhat = GETDATE()
    FROM tai_khoan t
    INNER JOIN inserted i ON t.ma_tai_khoan = i.ma_tai_khoan;
END;
GO

-- Bảng san_pham (Abstract Product) - Parent table
CREATE TABLE san_pham (
    ma_san_pham BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_san_pham NVARCHAR(255) NOT NULL,
    mo_ta NVARCHAR(MAX),
    gia DECIMAL(15, 2) NOT NULL,
    hinh_anh NVARCHAR(500),
    so_luong_ton_kho INT NOT NULL DEFAULT 0,
    con_hang BIT NOT NULL DEFAULT 1,
    ngay_tao DATETIME2 NOT NULL DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME2 NOT NULL DEFAULT GETDATE(),
    loai_san_pham NVARCHAR(50) NOT NULL,
    INDEX idx_ten_san_pham (ten_san_pham),
    INDEX idx_loai_san_pham (loai_san_pham),
    INDEX idx_con_hang (con_hang)
);
GO

-- Trigger để auto-update ngay_cap_nhat
CREATE TRIGGER trg_san_pham_update
ON san_pham
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE san_pham
    SET ngay_cap_nhat = GETDATE()
    FROM san_pham t
    INNER JOIN inserted i ON t.ma_san_pham = i.ma_san_pham;
END;
GO

-- Bảng xe_may (Motorbike) - Child table
CREATE TABLE xe_may (
    ma_san_pham BIGINT PRIMARY KEY,
    hang_xe NVARCHAR(100),
    dong_xe NVARCHAR(100),
    mau_sac NVARCHAR(50),
    nam_san_xuat INT,
    dung_tich INT,
    CONSTRAINT FK_xe_may_san_pham FOREIGN KEY (ma_san_pham) 
        REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_hang_xe (hang_xe),
    INDEX idx_nam_san_xuat (nam_san_xuat)
);
GO

-- Bảng phu_kien_xe_may (Accessory) - Child table
CREATE TABLE phu_kien_xe_may (
    ma_san_pham BIGINT PRIMARY KEY,
    loai_phu_kien NVARCHAR(100),
    thuong_hieu NVARCHAR(100),
    chat_lieu NVARCHAR(100),
    kich_thuoc NVARCHAR(50),
    CONSTRAINT FK_phu_kien_san_pham FOREIGN KEY (ma_san_pham) 
        REFERENCES san_pham(ma_san_pham) ON DELETE CASCADE,
    INDEX idx_loai_phu_kien (loai_phu_kien),
    INDEX idx_thuong_hieu (thuong_hieu)
);
GO

-- Bảng gio_hang (Shopping Cart)
CREATE TABLE gio_hang (
    ma_gio_hang BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_tai_khoan BIGINT,
    tong_tien DECIMAL(15, 2) DEFAULT 0.00,
    ngay_tao DATETIME2 NOT NULL DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_gio_hang_tai_khoan FOREIGN KEY (ma_tai_khoan) 
        REFERENCES tai_khoan(ma_tai_khoan) ON DELETE CASCADE,
    INDEX idx_ma_tai_khoan (ma_tai_khoan)
);
GO

-- Trigger để auto-update ngay_cap_nhat
CREATE TRIGGER trg_gio_hang_update
ON gio_hang
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE gio_hang
    SET ngay_cap_nhat = GETDATE()
    FROM gio_hang t
    INNER JOIN inserted i ON t.ma_gio_hang = i.ma_gio_hang;
END;
GO

-- Bảng chi_tiet_gio_hang (Cart Item)
CREATE TABLE chi_tiet_gio_hang (
    ma_chi_tiet BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_gio_hang BIGINT NOT NULL,
    ma_san_pham BIGINT NOT NULL,
    ten_san_pham NVARCHAR(255),
    gia_san_pham DECIMAL(15, 2) NOT NULL,
    so_luong INT NOT NULL DEFAULT 1,
    tam_tinh DECIMAL(15, 2) NOT NULL,
    CONSTRAINT FK_chi_tiet_gio_hang FOREIGN KEY (ma_gio_hang) 
        REFERENCES gio_hang(ma_gio_hang) ON DELETE CASCADE,
    CONSTRAINT FK_chi_tiet_san_pham FOREIGN KEY (ma_san_pham) 
        REFERENCES san_pham(ma_san_pham) ON DELETE NO ACTION,
    INDEX idx_ma_gio_hang (ma_gio_hang),
    INDEX idx_ma_san_pham (ma_san_pham)
);
GO

-- Insert sample data

-- Sample users
SET IDENTITY_INSERT tai_khoan ON;
INSERT INTO tai_khoan (ma_tai_khoan, email, ten_dang_nhap, mat_khau, so_dien_thoai, dia_chi, vai_tro) VALUES
(1, N'admin@motorbike.com', N'admin', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0901234567', N'Hà Nội', N'ADMIN'),
(2, N'customer1@gmail.com', N'customer1', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0912345678', N'TP.HCM', N'CUSTOMER'),
(3, N'customer2@gmail.com', N'customer2', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0923456789', N'Đà Nẵng', N'CUSTOMER');
SET IDENTITY_INSERT tai_khoan OFF;
GO

-- Sample motorbikes (xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
(1, N'Honda Winner X', N'Xe thể thao phân khối 150cc, động cơ mạnh mẽ', 46000000.00, N'/images/honda-winner-x.jpg', 10, 1, N'XE_MAY'),
(2, N'Yamaha Exciter 155', N'Xe côn tay thể thao, thiết kế trẻ trung', 47000000.00, N'/images/yamaha-exciter-155.jpg', 15, 1, N'XE_MAY'),
(3, N'Honda Vision', N'Xe tay ga cao cấp, tiện nghi', 30000000.00, N'/images/honda-vision.jpg', 20, 1, N'XE_MAY'),
(4, N'SYM Star SR 170', N'Xe thể thao phân khối 170cc', 52000000.00, N'/images/sym-star-sr-170.jpg', 8, 1, N'XE_MAY'),
(5, N'Yamaha Sirius', N'Xe số tiết kiệm nhiên liệu', 21000000.00, N'/images/yamaha-sirius.jpg', 25, 1, N'XE_MAY');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO xe_may (ma_san_pham, hang_xe, dong_xe, mau_sac, nam_san_xuat, dung_tich) VALUES
(1, N'Honda', N'Winner X', N'Đỏ đen', 2025, 150),
(2, N'Yamaha', N'Exciter 155', N'Xanh GP', 2025, 155),
(3, N'Honda', N'Vision', N'Trắng', 2025, 110),
(4, N'SYM', N'Star SR', N'Đen', 2024, 170),
(5, N'Yamaha', N'Sirius', N'Xanh đen', 2025, 110);
GO

-- Sample accessories (phu_kien_xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
(6, N'Mũ bảo hiểm fullface Royal M139', N'Mũ bảo hiểm cao cấp, đạt chuẩn an toàn', 850000.00, N'/images/helmet-royal.jpg', 50, 1, N'PHU_KIEN'),
(7, N'Găng tay Komine GK-162', N'Găng tay bảo hộ chống trượt', 450000.00, N'/images/gloves-komine.jpg', 100, 1, N'PHU_KIEN'),
(8, N'Áo mưa Givi', N'Áo mưa cao cấp, chống thấm tốt', 250000.00, N'/images/raincoat-givi.jpg', 150, 1, N'PHU_KIEN'),
(9, N'Kính mũ bảo hiểm Bulldog', N'Kính chống bụi, chống tia UV', 120000.00, N'/images/visor-bulldog.jpg', 200, 1, N'PHU_KIEN'),
(10, N'Khóa đĩa Kinbar', N'Khóa đĩa chống trộm cao cấp', 350000.00, N'/images/lock-kinbar.jpg', 80, 1, N'PHU_KIEN');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO phu_kien_xe_may (ma_san_pham, loai_phu_kien, thuong_hieu, chat_lieu, kich_thuoc) VALUES
(6, N'Mũ bảo hiểm', N'Royal', N'ABS + EPS', N'L'),
(7, N'Găng tay', N'Komine', N'Da + vải', N'XL'),
(8, N'Áo mưa', N'Givi', N'Vải PVC', N'L'),
(9, N'Kính mũ bảo hiểm', N'Bulldog', N'Polycarbonate', N'Universal'),
(10, N'Khóa đĩa', N'Kinbar', N'Thép hợp kim', N'Universal');
GO

-- Sample carts
SET IDENTITY_INSERT gio_hang ON;
INSERT INTO gio_hang (ma_gio_hang, ma_tai_khoan, tong_tien) VALUES
(1, 2, 0.00),
(2, 3, 0.00);
SET IDENTITY_INSERT gio_hang OFF;
GO

-- Verify data
SELECT N'Tài khoản:' as [Thống kê], COUNT(*) as [Số lượng] FROM tai_khoan
UNION ALL
SELECT N'Xe máy:', COUNT(*) FROM xe_may
UNION ALL
SELECT N'Phụ kiện:', COUNT(*) FROM phu_kien_xe_may
UNION ALL
SELECT N'Sản phẩm (tổng):', COUNT(*) FROM san_pham
UNION ALL
SELECT N'Giỏ hàng:', COUNT(*) FROM gio_hang;
GO

PRINT N'✅ Database setup completed successfully!';
PRINT N'📊 Sample data inserted:';
PRINT N'   - 3 user accounts (1 admin, 2 customers)';
PRINT N'   - 5 motorbikes';
PRINT N'   - 5 accessories';
PRINT N'   - 2 empty shopping carts';
GO
