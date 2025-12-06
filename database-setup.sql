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
    ho_ten NVARCHAR(100),
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
INSERT INTO tai_khoan (ma_tai_khoan, ho_ten, email, ten_dang_nhap, mat_khau, so_dien_thoai, dia_chi, vai_tro) VALUES
(1, N'Quản Trị Viên', N'admin@motorbike.com', N'admin', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0901234567', N'Ba Dinh, Ha Noi', N'ADMIN'),
(2, N'Nguyễn Văn A', N'customer1@gmail.com', N'customer1', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0912345678', N'TP.HCM', N'CUSTOMER'),
(3, N'Trần Thị B', N'customer2@gmail.com', N'customer2', N'$2a$10$eImiTXuWVxfM37uY4JANjOhsjpKCwCNR.kUOCpljHhSuZ2qvBVeGG', N'0923456789', N'Da Nang', N'CUSTOMER'),
(4, N'Admin Two', N'admin@gmail.com', N'admin2', N'123', N'0909999999', N'Ba Dinh, Ha Noi', N'ADMIN'),
(5, N'Lê Văn C', N'user@gmail.com', N'user1', N'user', N'0911111111', N'TP.HCM', N'CUSTOMER'),
(6, N'Nguyen Quang Tuan Phuong', N'nqtuanp2005@gmail.com', N'hayami', N'123456', N'0369164853', N'Ba Dinh, Hanoi', N'CUSTOMER'),
(7, N'Phạm Thị D', N'phuong@gmail.com', N'hayamij', N'123456', N'0369164853', N'120 Yen Lang', N'CUSTOMER');
SET IDENTITY_INSERT tai_khoan OFF;
GO

-- Sample motorbikes (xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
-- (1, N'Honda Winner X', N'Xe the thao phan khoi 150cc, dong co manh me', 46000000.00, N'/images/honda-winner-x.jpg', 10, 1, N'XE_MAY'),
-- (2, N'Yamaha Exciter 155', N'Xe con tay the thao, thiet ke tre trung', 47000000.00, N'/images/yamaha-exciter-155.jpg', 15, 1, N'XE_MAY'),
-- (3, N'Honda Vision', N'Xe tay ga cao cap, tien nghi', 30000000.00, N'/images/honda-vision.jpg', 20, 1, N'XE_MAY'),
-- (4, N'SYM Star SR 170', N'Xe the thao phan khoi 170cc', 52000000.00, N'/images/sym-star-sr-170.jpg', 8, 1, N'XE_MAY'),
-- (5, N'Yamaha Sirius', N'Xe so tiet kiem nhien lieu', 21000000.00, N'/images/yamaha-sirius.jpg', 25, 1, N'XE_MAY'),
(6, N'Honda Wave Alpha 110', N'Xe so nho gon, tiet kiem', 18000000.00, N'https://www.hongliyangzhi.com/manufacturers/honda/honda-wave/honda-wave-110/honda-wave-110-3.jpg', 30, 1, N'XE_MAY'),
(7, N'Yamaha NVX 155', N'Xe tay ga the thao, dong co 155cc', 55000000.00, N'https://www.bikesrepublic.com/wp-content/uploads/2022/10/2022-yamaha-nvx-155-2-1024x683.jpg', 12, 1, N'XE_MAY'),
(8, N'Honda Air Blade 125', N'Xe tay ga thiet ke hien dai, tien nghi', 40000000.00, N'https://cdn.abphotos.link/photos/resized/640x/2024/06/01/1717213606_xZmz0vg6rVN3eFiP_1717217919-phpktceyd.png', 18, 1, N'XE_MAY'),
(9, N'SYM Attila Elizabeth', N'Xe tay ga thoi trang, phu hop di trong thanh pho', 28000000.00, N'https://imgcdn.zigwheels.my/large/gallery/color/70/987/sym-attila-elizabeth-color-342469.jpg', 22, 1, N'XE_MAY'),
(10, N'Yamaha Janus', N'Xe tay ga nhe nhang, tiet kiem nhien lieu', 25000000.00, N'https://yamahatownnamtien.com/uploads/source/san-pham/janus-tieu-chuan/2022/new-janus-red-metallic-004.png', 28, 1, N'XE_MAY'),
(11, N'Honda CBR150R', N'Xe the thao phan khoi lon, thiet ke aerodynamically', 75000000.00, N'https://product.hstatic.net/200000712539/product/honda-cbr150r-dac-biet-den-xam_2df0cdd3596d4f37874bc69ecf9767dc_master.png', 5, 1, N'XE_MAY'),
(12, N'Kawasaki Ninja 400', N'Xe the thao phan khoi lon, dong co manh me', 180000000.00, N'https://m.media-amazon.com/images/I/71By7iLJoxL._AC_SX679_.jpg', 7, 1, N'XE_MAY'),
(13, N'Suzuki GSX-R150', N'Xe the thao phan khoi 150cc, thiet ke hien dai', 60000000.00, N'https://www.suzukisingapore.com.sg/_next/image?url=%2F_next%2Fstatic%2Fmedia%2FGSX-R150_YSF_Diagonal.969e795b.jpeg&w=1920&q=75', 9, 1, N'XE_MAY'),
(14, N'Piaggio Liberty S 125', N'Xe tay ga thoi trang, phu hop di trong thanh pho', 70000000.00, N'https://images.piaggio.com/piaggio/vehicles/nclq000u28/nclqd27u28/nclqd27u28-01-m.png', 14, 1, N'XE_MAY'),
(15, N'Vespa Primavera 150', N'Xe tay ga cao cap, thiet ke doc dao', 120000000.00, N'https://wlassets.vespa.com/wlassets/vespa/master/APAC/Primavera/2025/2025-launch-Primavera-STD/(3840x1646)Vespa_Product_Prima/original/%283840x1646%29Vespa_Product_Prima.jpg?1748244709819', 6, 1, N'XE_MAY'),
(16, N'Honda SH 150i', N'Xe tay ga cao cap, dong co 150cc', 95000000.00, N'https://motonewsworld.com/wp-content/uploads/2022/11/2023-honda-sh150i-Hyper-Red-right-side-800x445.jpg', 11, 1, N'XE_MAY'),
(17, N'Yamaha TFX 150', N'Xe the thao phan khoi 150cc, thiet ke hung du', 52000000.00, N'https://insideracing.com.ph/wp-content/uploads/2019/01/RA6_3875.jpg', 13, 1, N'XE_MAY'),
(18, N'Honda CB150R Exmotion', N'Xe the thao phan khoi 150cc, thiet ke hien dai', 60000000.00, N'https://autobikes.vn/stores/photo_data/vantrinh/042019/12/17/5939_anh-thuc-te-honda-CB150R-Autobikes4.jpg', 4, 1, N'XE_MAY'),
(19, N'SYM Wolf 125', N'Xe so nhe nhang, tiet kiem nhien lieu', 22000000.00, N'https://autocity.com/wp-content/uploads/images/0/2/98502_sym-wolf-125-1.jpg', 16, 1, N'XE_MAY'),
(20, N'KTM Duke 200', N'Xe the thao phan khoi lon, dong co manh me', 90000000.00, N'https://media.zigcdn.com/media/model/2024/Oct/front-right-view-1888583955_930x620.jpg', 3, 1, N'XE_MAY'),
(21, N'Benelli TNT 150i', N'Xe the thao phan khoi 150cc, thiet ke an tuong', 45000000.00, N'https://ivoiremoto.com/267-large_default/benelli-tnt-150i.jpg', 17, 1, N'XE_MAY'),
(22, N'Hero Splendor Plus', N'Xe so tiet kiem nhien lieu, phu hop di lai hang ngay', 17000000.00, N'https://www.timesbull.com/wp-content/uploads/2024/09/SPLENDER.png', 19, 1, N'XE_MAY'),
(23, N'TVS Apache RTR 160 4V', N'Xe the thao phan khoi 160cc, dong co manh me', 40000000.00, N'https://www.bikes4sale.in/pictures/default/tvs-apache-rtr-160-4v/tvs-apache-rtr-160-4v-pic-1.jpg', 2, 1, N'XE_MAY'),
(24, N'Mahindra Mojo 300', N'Xe the thao phan khoi lon, thiet ke doc dao', 130000000.00, N'https://blog.gaadikey.com/wp-content/uploads/2015/10/Mahindra-MOJO-Front.jpg', 1, 1, N'XE_MAY'),
(25, N'Lifan KPR 150', N'Xe the thao phan khoi 150cc, gia ca phai chang', 35000000.00, N'https://www.motorcycle.com.bd/images/bikes/Lifan-KPR-150.jpg', 20, 1, N'XE_MAY');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO xe_may (ma_san_pham, hang_xe, dong_xe, mau_sac, nam_san_xuat, dung_tich) VALUES
(6, N'Honda', N'Wave Alpha', N'Xanh trang', 2024, 110),
(7, N'Yamaha', N'NVX 155', N'Den xam', 2025, 155),
(8, N'Honda', N'Air Blade', N'Bac', 2024, 125),
(9, N'SYM', N'Attila Elizabeth', N'Tim', 2023, 125),
(10, N'Yamaha', N'Janus', N'Trang', 2024, 125),
(11, N'Honda', N'CBR150R', N'Do den', 2025, 150),
(12, N'Kawasaki', N'Ninja 400', N'Xanh Kawasaki', 2024, 400),
(13, N'Suzuki', N'GSX-R150', N'Xanh GP', 2025, 150),
(14, N'Piaggio', N'Liberty S 125', N'Den trang', 2024, 125),
(15, N'Vespa', N'Primavera 150', N'Den nham', 2025, 150),
(16, N'Honda', N'SH 150i', N'Den titan', 2024, 150),
(17, N'Yamaha', N'TFX 150', N'Den do', 2025, 150),
(18, N'Honda', N'CB150R Exmotion', N'Den xam', 2024, 150),
(19, N'SYM', N'Wolf 125', N'Do trang', 2023, 125),
(20, N'KTM', N'Duke 200', N'Cam den', 2025, 200),
(21, N'Benelli', N'TNT 150i', N'Den do trang', 2024, 150),
(22, N'Hero', N'Splendor Plus', N'Den xanh', 2023, 100),
(23, N'TVS', N'Apache RTR 160 4V', N'Den do trang', 2025, 160),
(24, N'Mahindra', N'Mojo 300', N'Den xanh nham', 2024, 300),
(25, N'Lifan', N'KPR 150', N'Do den trang', 2023, 150);
GO

-- Sample accessories (phu_kien_xe_may)
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta, gia, hinh_anh, so_luong_ton_kho, con_hang, loai_san_pham) VALUES
-- (26, N'Mu bao hiem fullface Royal M139', N'Mu bao hiem cao cap, dat chuan an toan', 850000.00, N'/images/helmet-royal.jpg', 50, 1, N'PHU_KIEN'),
-- (27, N'Gang tay Komine GK-162', N'Gang tay bao ho chong truot', 450000.00, N'/images/gloves-komine.jpg', 100, 1, N'PHU_KIEN'),
-- (28, N'Ao mua Givi', N'Ao mua cao cap, chong tham tot', 250000.00, N'/images/raincoat-givi.jpg', 150, 1, N'PHU_KIEN'),
-- (29, N'Kinh mu bao hiem Bulldog', N'Kinh chong bui, chong tia UV', 120000.00, N'/images/visor-bulldog.jpg', 200, 1, N'PHU_KIEN'),
-- (30, N'Khoa dia Kinbar', N'Khoa dia chong trom cao cap', 350000.00, N'/images/lock-kinbar.jpg', 80, 1, N'PHU_KIEN'),
(31, N'Tui dung do xe may', N'Tui dung do tien ich cho xe may', 150000.00, N'https://img.lazcdn.com/g/ff/kf/S09c8be1913904a9eb1c2eab6b68e7de12.jpg_720x720q80.jpg_.webp', 120, 1, N'PHU_KIEN'),
(32, N'Dau nhot xe may Motul', N'Dau nhot cao cap cho dong co xe may', 300000.00, N'https://phutungchinhhieu.vn/wp-content/uploads/2020/06/nhot-xe-may-motul-scooter-5w-40-1l.jpg', 90, 1, N'PHU_KIEN'),
(33, N'Binh xit rua xe', N'Binh xit rua xe tien loi', 200000.00, N'https://maynenkhiruaxe.com/wp-content/uploads/2023/04/binh-xit-nuoc-rua-xe-1.jpg', 110, 1, N'PHU_KIEN'),
(34, N'Den led xe may Philips', N'Den led nang cao tam nhin ban dem', 400000.00, N'https://ledoto.net/wp-content/uploads/2020/07/Philips-led-m5-hs1-100.jpg', 70, 1, N'PHU_KIEN'),
(35, N'Bao ve binh xang xe may', N'Bao ve binh xang chong va cham', 180000.00, N'https://m.media-amazon.com/images/I/61nx21BfHpL._AC_SY300_SX300_QL70_FMwebp_.jpg', 130, 1, N'PHU_KIEN');
SET IDENTITY_INSERT san_pham OFF;
GO

INSERT INTO phu_kien_xe_may (ma_san_pham, loai_phu_kien, thuong_hieu, chat_lieu, kich_thuoc) VALUES
(31, N'Tui dung do', N'Tui dung do tien ich', N'Vai Oxford', N'Universal'),
(32, N'Dau nhot', N'Motul', N'Dau nhot tong hop', N'1L'),
(33, N'Binh xit rua xe', N'Tien loi', N'Nhua cao cap', N'1.5L'),
(34, N'Den led', N'Philips', N'LED', N'HS1'),
(35, N'Bao ve binh xang', N'Tien ich', N'Nhua PVC', N'Universal');
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

-- Add payment method column to don_hang table
ALTER TABLE don_hang
ADD phuong_thuc_thanh_toan VARCHAR(50);
GO

-- Set default value for existing records
UPDATE don_hang
SET phuong_thuc_thanh_toan = 'THANH_TOAN_TRUC_TIEP'
WHERE phuong_thuc_thanh_toan IS NULL;
GO
