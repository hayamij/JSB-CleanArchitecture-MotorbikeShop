-- SQL Script to create Users table for Login Use Case
-- Database: MotorbikeShop

USE MotorbikeShop;
GO

-- Create Users table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        id BIGINT PRIMARY KEY IDENTITY(1,1),
        email NVARCHAR(255) NOT NULL UNIQUE,
        username NVARCHAR(100) NOT NULL UNIQUE,
        password NVARCHAR(255) NOT NULL,
        phone_number NVARCHAR(20),
        role NVARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
        active BIT NOT NULL DEFAULT 1,
        created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
        last_login_at DATETIME2,
        CONSTRAINT CHK_Users_Role CHECK (role IN ('CUSTOMER', 'ADMIN'))
    );
    PRINT 'Users table created successfully';
END
ELSE
BEGIN
    PRINT 'Users table already exists';
END
GO

-- Insert sample users
-- Note: In production, passwords should be hashed with BCrypt
IF NOT EXISTS (SELECT * FROM users WHERE email = 'customer@motorbike.com')
BEGIN
    INSERT INTO users (email, username, password, phone_number, role, active)
    VALUES 
        ('customer@motorbike.com', 'customer1', 'password123', '0901234567', 'CUSTOMER', 1),
        ('admin@motorbike.com', 'admin', 'admin123', '0909999999', 'ADMIN', 1),
        ('john.doe@example.com', 'johndoe', 'john123', '0912345678', 'CUSTOMER', 1);
    
    PRINT 'Sample users inserted successfully';
END
ELSE
BEGIN
    PRINT 'Sample users already exist';
END
GO

-- Create index on email for faster login queries
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_Users_Email')
BEGIN
    CREATE INDEX IX_Users_Email ON users(email);
    PRINT 'Index on email created successfully';
END
GO

-- Display all users
SELECT * FROM users;
GO
