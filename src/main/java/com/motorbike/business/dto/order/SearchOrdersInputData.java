package com.motorbike.business.dto.order;

public class SearchOrdersInputData {
    private final String keyword;
    private final String trangThai;
    private final Long maTaiKhoan;

    public SearchOrdersInputData(String keyword, String trangThai, Long maTaiKhoan) {
        this.keyword = keyword;
        this.trangThai = trangThai;
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getKeyword() {return keyword;}
    public String getTrangThai() {return trangThai;}
    public Long getMaTaiKhoan() {return maTaiKhoan;}
}
