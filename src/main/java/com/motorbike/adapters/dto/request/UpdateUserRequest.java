package com.motorbike.adapters.dto.request;

public class UpdateUserRequest {
    public String email;
    public String username;
    public String password;     // để trống nếu không đổi
    public String phoneNumber;
    public String address;
    public String role;         // ví dụ "ADMIN" hoặc "USER"
    public Boolean active;      // true/false (nullable)
}