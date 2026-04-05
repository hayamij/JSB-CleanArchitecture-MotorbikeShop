package com.motorbike.business.dto.motorbike;

/**
 * UC-75: Validate Motorbike Specific Fields
 * Input data for validating motorbike-specific fields
 */
public class ValidateMotorbikeFieldsInputData {
    
    private final String hangXe;
    private final String dongXe;
    private final String mauSac;
    private final Integer namSanXuat;
    private final Integer dungTich;
    
    public ValidateMotorbikeFieldsInputData(String hangXe, String dongXe, String mauSac, 
                                             Integer namSanXuat, Integer dungTich) {
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }
    
    public String getHangXe() {
        return hangXe;
    }
    
    public String getDongXe() {
        return dongXe;
    }
    
    public String getMauSac() {
        return mauSac;
    }
    
    public Integer getNamSanXuat() {
        return namSanXuat;
    }
    
    public Integer getDungTich() {
        return dungTich;
    }
}
