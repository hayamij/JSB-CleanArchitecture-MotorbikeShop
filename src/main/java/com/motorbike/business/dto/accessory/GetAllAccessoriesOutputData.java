package com.motorbike.business.dto.accessory;

import java.util.List;

public class GetAllAccessoriesOutputData {

    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;

    // Trường hợp thành công
    public GetAllAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
        this.accessories = accessories;
    }

    // Trường hợp lỗi
    public GetAllAccessoriesOutputData(String errorCode, String errorMessage) {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessories = null;
    }

    // DTO con cho từng phụ kiện
    public static class AccessoryItem {
        public Long id;
        public String name;
        public String description;
        public java.math.BigDecimal price;
        public int stock;
        public String imageUrl;
        public String loaiPhuKien;
        public String thuongHieu;
        public String chatLieu;
        public String kichThuoc;

        public AccessoryItem(Long id,
                            String name,
                            String description,
                            java.math.BigDecimal price,
                            int stock,
                            String imageUrl,
                            String loaiPhuKien,
                            String thuongHieu,
                            String chatLieu,
                            String kichThuoc) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.imageUrl = imageUrl;
            this.loaiPhuKien = loaiPhuKien;
            this.thuongHieu = thuongHieu;
            this.chatLieu = chatLieu;
            this.kichThuoc = kichThuoc;
        }
    }
}
