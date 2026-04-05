package com.motorbike.business.dto.accessory;

public class SearchAccessoriesInputData {
    public String keyword;
    public String loaiPhuKien;
    public String thuongHieu;
    public String chatLieu;
    public Double minPrice;
    public Double maxPrice;

    public SearchAccessoriesInputData(
            String keyword,
            String loaiPhuKien,
            String thuongHieu,
            String chatLieu,
            Double minPrice,
            Double maxPrice
    ) {
        this.keyword = keyword;
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
