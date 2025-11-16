-- Database Schema cho Clean Architecture Motorbike Shop
-- T-SQL cho SQL Server
-- Sử dụng JOINED inheritance strategy cho SanPham

-- Set database collation to support Unicode
USE master;
GO
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'MotorcycleShop')
BEGIN
    ALTER DATABASE MotorcycleShop SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE MotorcycleShop;
END
GO

CREATE DATABASE MotorcycleShop
COLLATE Vietnamese_CI_AS;
GO

USE MotorcycleShop;
GO

-- Drop tables nếu tồn tại (SQL Server syntax)
IF OBJECT_ID('dbo.chi_tiet_gio_hang', 'U') IS NOT NULL DROP TABLE dbo.chi_tiet_gio_hang;
IF OBJECT_ID('dbo.gio_hang', 'U') IS NOT NULL DROP TABLE dbo.gio_hang;
IF OBJECT_ID('dbo.phu_kien_xe_may', 'U') IS NOT NULL DROP TABLE dbo.phu_kien_xe_may;
IF OBJECT_ID('dbo.xe_may', 'U') IS NOT NULL DROP TABLE dbo.xe_may;
IF OBJECT_ID('dbo.san_pham', 'U') IS NOT NULL DROP TABLE dbo.san_pham;
IF OBJECT_ID('dbo.tai_khoan', 'U') IS NOT NULL DROP TABLE dbo.tai_khoan;
IF OBJECT_ID('dbo.chi_tiet_don_hang', 'U') IS NOT NULL DROP TABLE dbo.chi_tiet_don_hang;
IF OBJECT_ID('dbo.don_hang', 'U') IS NOT NULL DROP TABLE dbo.don_hang;
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

-- ===== TẠO BẢNG CHI_TIET_DON_HANG (Order Item) =====
CREATE TABLE chi_tiet_don_hang (
    ma_chi_tiet BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_don_hang BIGINT NOT NULL,
    ma_san_pham BIGINT NOT NULL,
    ten_san_pham NVARCHAR(255) NOT NULL,
    gia_san_pham DECIMAL(15, 2) NOT NULL,
    so_luong INT NOT NULL DEFAULT 1,
    tam_tinh DECIMAL(15, 2) NOT NULL,
    
    -- Indexes
    INDEX idx_chi_tiet_don_hang_ma_don_hang (ma_don_hang),
    INDEX idx_chi_tiet_don_hang_ma_san_pham (ma_san_pham)
);
GO

-- ===== TẠO BẢNG DON_HANG (Order) =====
CREATE TABLE don_hang (
    ma_don_hang BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_tai_khoan BIGINT NOT NULL,
    tong_tien DECIMAL(15, 2) NOT NULL DEFAULT 0,
    trang_thai NVARCHAR(50) NOT NULL DEFAULT 'CHO_XAC_NHAN',
    ten_nguoi_nhan NVARCHAR(255) NOT NULL,
    so_dien_thoai NVARCHAR(20) NOT NULL,
    dia_chi_giao_hang NVARCHAR(MAX) NOT NULL,
    ghi_chu NVARCHAR(MAX),
    ngay_dat DATETIME2 NOT NULL DEFAULT GETDATE(),
    ngay_cap_nhat DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    -- Foreign Key
    CONSTRAINT FK_don_hang_tai_khoan 
        FOREIGN KEY (ma_tai_khoan) 
        REFERENCES tai_khoan(ma_tai_khoan)
        ON DELETE CASCADE,
    
    -- Constraint kiểm tra trạng thái hợp lệ
    CONSTRAINT CHK_trang_thai_don_hang 
        CHECK (trang_thai IN ('CHO_XAC_NHAN', 'DA_XAC_NHAN', 'DANG_GIAO', 'DA_GIAO', 'DA_HUY')),
    
    -- Indexes
    INDEX idx_don_hang_ma_tai_khoan (ma_tai_khoan),
    INDEX idx_don_hang_trang_thai (trang_thai),
    INDEX idx_don_hang_ngay_dat (ngay_dat DESC),
    INDEX idx_don_hang_ma_tai_khoan_trang_thai (ma_tai_khoan, trang_thai)
);
GO
-- ===== TRIGGER AUTO-UPDATE ngay_cap_nhat =====
CREATE TRIGGER trg_don_hang_update
ON don_hang
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE don_hang
    SET ngay_cap_nhat = GETDATE()
    FROM don_hang t
    INNER JOIN inserted i ON t.ma_don_hang = i.ma_don_hang;
END;
GO

-- ===== TẠO FOREIGN KEY CHO CHI_TIET_DON_HANG =====
ALTER TABLE chi_tiet_don_hang
ADD CONSTRAINT FK_chi_tiet_don_hang_don_hang 
    FOREIGN KEY (ma_don_hang) 
    REFERENCES don_hang(ma_don_hang)
    ON DELETE CASCADE;

ALTER TABLE chi_tiet_don_hang
ADD CONSTRAINT FK_chi_tiet_don_hang_san_pham 
    FOREIGN KEY (ma_san_pham) 
    REFERENCES san_pham(ma_san_pham)
    ON DELETE NO ACTION;
GO

-- Insert sample data

-- Sample users
SET IDENTITY_INSERT tai_khoan ON;
INSERT INTO tai_khoan (ma_tai_khoan, email, ten_dang_nhap, mat_khau, so_dien_thoai, dia_chi, vai_tro) VALUES
(1, N'admin@motorbike.com', N'admin', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0901234567', N'Ha Noi', N'ADMIN'),
(2, N'customer1@gmail.com', N'customer1', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0912345678', N'TP.HCM', N'CUSTOMER'),
(3, N'customer2@gmail.com', N'customer2', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0923456789', N'Da Nang', N'CUSTOMER');
SET IDENTITY_INSERT tai_khoan OFF;
GO

-- Sample motorbikes (xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
(1, N'Honda Winner X', N'Xe the thao phan khoi 150cc, dong co manh me', 46000000.00, N'/images/honda-winner-x.jpg', 10, 1, N'XE_MAY'),
(2, N'Yamaha Exciter 155', N'Xe con tay the thao, thiet ke tre trung', 47000000.00, N'/images/yamaha-exciter-155.jpg', 15, 1, N'XE_MAY'),
(3, N'Honda Vision', N'Xe tay ga cao cap, tien nghi', 30000000.00, N'/images/honda-vision.jpg', 20, 1, N'XE_MAY'),
(4, N'SYM Star SR 170', N'Xe the thao phan khoi 170cc', 52000000.00, N'/images/sym-star-sr-170.jpg', 8, 1, N'XE_MAY'),
(5, N'Yamaha Sirius', N'Xe so tiet kiem nhien lieu', 21000000.00, N'/images/yamaha-sirius.jpg', 25, 1, N'XE_MAY');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO xe_may (ma_san_pham, hang_xe, dong_xe, mau_sac, nam_san_xuat, dung_tich) VALUES
(1, N'Honda', N'Winner X', N'Do den', 2025, 150),
(2, N'Yamaha', N'Exciter 155', N'Xanh GP', 2025, 155),
(3, N'Honda', N'Vision', N'Trang', 2025, 110),
(4, N'SYM', N'Star SR', N'Den', 2024, 170),
(5, N'Yamaha', N'Sirius', N'Xanh den', 2025, 110);
GO

-- Sample accessories (phu_kien_xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
(6, N'Mu bao hiem fullface Royal M139', N'Mu bao hiem cao cap, dat chuan an toan', 850000.00, N'/images/helmet-royal.jpg', 50, 1, N'PHU_KIEN'),
(7, N'Gang tay Komine GK-162', N'Gang tay bao ho chong truot', 450000.00, N'/images/gloves-komine.jpg', 100, 1, N'PHU_KIEN'),
(8, N'Ao mua Givi', N'Ao mua cao cap, chong tham tot', 250000.00, N'/images/raincoat-givi.jpg', 150, 1, N'PHU_KIEN'),
(9, N'Kinh mu bao hiem Bulldog', N'Kinh chong bui, chong tia UV', 120000.00, N'/images/visor-bulldog.jpg', 200, 1, N'PHU_KIEN'),
(10, N'Khoa dia Kinbar', N'Khoa dia chong trom cao cap', 350000.00, N'/images/lock-kinbar.jpg', 80, 1, N'PHU_KIEN');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO phu_kien_xe_may (ma_san_pham, loai_phu_kien, thuong_hieu, chat_lieu, kich_thuoc) VALUES
(6, N'Mu bao hiem', N'Royal', N'ABS + EPS', N'L'),
(7, N'Gang tay', N'Komine', N'Da + vai', N'XL'),
(8, N'Ao mua', N'Givi', N'Vai PVC', N'L'),
(9, N'Kinh mu bao hiem', N'Bulldog', N'Polycarbonate', N'Universal'),
(10, N'Khoa dia', N'Kinbar', N'Thep hop kim', N'Universal');
GO

-- ===== INSERT SAMPLE DATA - ĐƠN HÀNG =====

-- Don hang 1: Da giao hang
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    2,                                  -- User 2 (customer1)
    60000000.00,                        -- Total
    N'DA_GIAO',                         -- Status: Delivered
    N'Nguyen Van A',                    -- Receiver
    N'0912345678',                      -- Phone
    N'123 Nguyen Trai, Q1, TP.HCM',    -- Address
    N'Giao buoi sang',                 -- Note
    DATEADD(DAY, -5, GETDATE())         -- 5 days ago
);

-- Don hang 2: Cho xac nhan
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    2,                                  -- User 2
    130000000.00,                       -- Total
    N'CHO_XAC_NHAN',                    -- Status: Pending
    N'Nguyen Van A',                    -- Receiver
    N'0912345678',                      -- Phone
    N'456 Le Loi, Q1, TP.HCM',         -- Address
    N'Giao buoi chieu',                -- Note
    DATEADD(DAY, -3, GETDATE())         -- 3 days ago
);

-- Don hang 3: Dang giao hang
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    2,                                  -- User 2
    50000000.00,                        -- Total
    N'DANG_GIAO',                       -- Status: Shipping
    N'Nguyen Van A',                    -- Receiver
    N'0912345678',                      -- Phone
    N'789 Cach Mang Thang 8, Q10, TP.HCM', -- Address
    NULL,                               -- No note
    DATEADD(DAY, -1, GETDATE())         -- 1 day ago
);

-- Don hang 4: Da xac nhan
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    2,                                  -- User 2
    30000000.00,                        -- Total
    N'DA_XAC_NHAN',                     -- Status: Confirmed
    N'Nguyen Van A',                    -- Receiver
    N'0912345678',                      -- Phone
    N'321 Tran Hung Dao, Q5, TP.HCM',  -- Address
    N'Can giao khan',                  -- Note
    DATEADD(HOUR, -6, GETDATE())        -- 6 hours ago
);

-- Don hang 5: Da huy
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    2,                                  -- User 2
    100000000.00,                       -- Total
    N'DA_HUY',                          -- Status: Cancelled
    N'Nguyen Van A',                    -- Receiver
    N'0912345678',                      -- Phone
    N'654 Pham Van Dong, Q. Thu Duc, TP.HCM', -- Address
    N'Khach huy',                       -- Note
    DATEADD(DAY, -10, GETDATE())        -- 10 days ago
);

-- Don hang 6: Cho xac nhan (User 3)
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    3,                                  -- User 3 (customer2)
    96000000.00,                        -- Total
    N'CHO_XAC_NHAN',                    -- Status: Pending
    N'Tran Thi B',                      -- Receiver
    N'0923456789',                      -- Phone
    N'100 Ton Duc Thang, Da Nang',     -- Address
    NULL,                               -- No note
    DATEADD(HOUR, -2, GETDATE())        -- 2 hours ago
);

-- Don hang 7: Da giao (User 3)
INSERT INTO don_hang (
    ma_tai_khoan, tong_tien, trang_thai,
    ten_nguoi_nhan, so_dien_thoai, dia_chi_giao_hang, ghi_chu,
    ngay_dat
) VALUES (
    3,                                  -- User 3
    47000000.00,                        -- Total
    N'DA_GIAO',                         -- Status: Delivered
    N'Tran Thi B',                      -- Receiver
    N'0923456789',                      -- Phone
    N'200 Hung Vuong, Da Nang',        -- Address
    NULL,                               -- No note
    DATEADD(DAY, -7, GETDATE())         -- 7 days ago
);

GO

-- Sample carts
SET IDENTITY_INSERT gio_hang ON;
INSERT INTO gio_hang (ma_gio_hang, ma_tai_khoan, tong_tien) VALUES
(1, 2, 0.00),
(2, 3, 0.00);
SET IDENTITY_INSERT gio_hang OFF;
GO

-- Verify data
SELECT N'Tai khoan:' as [Thong ke], COUNT(*) as [So luong] FROM tai_khoan
UNION ALL
SELECT N'Xe may:', COUNT(*) FROM xe_may
UNION ALL
SELECT N'Phu kien:', COUNT(*) FROM phu_kien_xe_may
UNION ALL
SELECT N'San pham (tong):', COUNT(*) FROM san_pham
UNION ALL
SELECT N'Gio hang:', COUNT(*) FROM gio_hang;
GO

PRINT N'Database setup completed successfully!';
PRINT N'Sample data inserted:';
PRINT N'   - 3 user accounts (1 admin, 2 customers)';
PRINT N'   - 5 motorbikes';
PRINT N'   - 5 accessories';
PRINT N'   - 2 empty shopping carts';
GO
