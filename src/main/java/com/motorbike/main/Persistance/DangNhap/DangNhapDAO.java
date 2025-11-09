package com.motorbike.main.Persistance.DangNhap;

public class DangNhapDAO implements DangNhapDAOGateway {
    public DangNhapDAO() {
    }

    @Override
    public boolean dangNhap(String email, String matKhau) {
        // Implementation code here
        System.out.println("Dang nhap user: " + email);
        return true;
    }
    
}
