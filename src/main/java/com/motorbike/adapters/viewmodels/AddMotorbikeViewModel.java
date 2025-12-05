package com.motorbike.adapters.viewmodels;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddMotorbikeViewModel {
    public boolean success;
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    
    public Long maSanPham;
    public String tenSanPham;
    public String hangXe;
    public String dongXe;
    public String mauSac;
    public int namSanXuat;
    public int dungTich;
    public BigDecimal gia;
    public String ngayTao;
    public String successMessage;
=======
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;

public class AddMotorbikeViewModel {

    public boolean hasError;
    public String errorCode;
    public String errorMessage;

    public AddMotorbikeOutputData.MotorbikeItem motorbike;

>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
}
