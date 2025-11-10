-- ============================================
-- SQL Server Database Setup Script
-- Motorbike Shop Clean Architecture
-- ============================================

-- Create database if not exists
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MotorcycleShop')
BEGIN
    CREATE DATABASE MotorcycleShop;
    PRINT 'Database MotorcycleShop created successfully.';
END
ELSE
BEGIN
    PRINT 'Database MotorcycleShop already exists.';
END
GO

-- Use the database
USE MotorcycleShop;
GO

-- Drop existing tables if they exist (for clean setup)
IF OBJECT_ID('products', 'U') IS NOT NULL
    DROP TABLE products;
GO

-- Create products table with NVARCHAR for Unicode support
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) COLLATE Vietnamese_CI_AS NOT NULL,
    description NVARCHAR(MAX) COLLATE Vietnamese_CI_AS,
    price DECIMAL(10,2) NOT NULL,
    image_url NVARCHAR(500),
    specifications NVARCHAR(MAX),
    category NVARCHAR(50),
    stock_quantity INT,
    available BIT NOT NULL DEFAULT 1,
    created_at DATETIME2(6) NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2(6)
);
GO

-- Insert sample data
INSERT INTO products (name, description, price, image_url, specifications, category, stock_quantity, available, created_at, updated_at)
VALUES 
(
    N'Honda Wave RSX',
    N'Xe số tiết kiệm nhiên liệu, phù hợp cho di chuyển trong thành phố',
    38000000,
    '/images/honda-wave-rsx.jpg',
    '{"engine":"110cc","fuelCapacity":"3.5L","weight":"98kg"}',
    'MOTORCYCLE',
    15,
    1,
    GETDATE(),
    GETDATE()
),
(
    N'Yamaha Exciter 155',
    N'Xe côn tay thể thao, động cơ mạnh mẽ',
    47000000,
    '/images/yamaha-exciter-155.jpg',
    '{"engine":"155cc","fuelCapacity":"4.6L","weight":"118kg"}',
    'MOTORCYCLE',
    10,
    1,
    GETDATE(),
    GETDATE()
),
(
    N'Suzuki Raider 150',
    N'Xe côn tay cơ bắp, thiết kế nam tính',
    50000000,
    '/images/suzuki-raider-150.jpg',
    '{"engine":"150cc","fuelCapacity":"4.5L","weight":"125kg"}',
    'MOTORCYCLE',
    8,
    1,
    GETDATE(),
    GETDATE()
),
(
    N'Mũ bảo hiểm Royal M139',
    N'Mũ bảo hiểm fullface cao cấp, tiêu chuẩn Châu Âu',
    850000,
    '/images/helmet-royal.jpg',
    '{"size":"L, XL","weight":"1.5kg","standard":"ECE 22.05"}',
    'ACCESSORY',
    50,
    1,
    GETDATE(),
    GETDATE()
),
(
    N'Găng tay Scoyco MC29',
    N'Găng tay đi xe máy, chống trượt và bảo vệ tốt',
    350000,
    '/images/gloves-scoyco.jpg',
    '{"material":"Leather + Fabric","protection":"Knuckle Guard"}',
    'ACCESSORY',
    30,
    1,
    GETDATE(),
    GETDATE()
);
GO

-- Verify data
SELECT COUNT(*) as TotalProducts FROM products;
GO

SELECT * FROM products;
GO

PRINT 'Database setup completed successfully!';
PRINT 'Total products inserted: 5';
GO
