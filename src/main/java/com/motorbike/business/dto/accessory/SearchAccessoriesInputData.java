package com.motorbike.business.dto.accessory;

public class SearchAccessoriesInputData {
    public String keyword;
    public String loaiPhuKien;
    public String thuongHieu;
    public String chatLieu;
    public String kichThuoc;

    public SearchAccessoriesInputData(
            String keyword,
            String loaiPhuKien,
            String thuongHieu,
            String chatLieu,
            String kichThuoc
    ) {
        this.keyword = keyword;
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
    }
}
