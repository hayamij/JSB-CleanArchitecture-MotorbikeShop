package com.motorbike.business.dto.accessory;

import java.util.List;

public class SearchAccessoriesOutputData {

    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;

<<<<<<< HEAD
    // Trường hợp thành công
=======
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
    public SearchAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
        this.accessories = accessories;
    }

<<<<<<< HEAD
    // Trường hợp lỗi
=======
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
    public SearchAccessoriesOutputData(String errorCode, String errorMessage) {
        this.hasError = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessories = null;
    }

<<<<<<< HEAD
    // DTO con cho từng phụ kiện
=======
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
    public static class AccessoryItem {
        public Long id;
        public String name;
        public String description;
        public java.math.BigDecimal price;
        public int stock;
        public String imageUrl;
<<<<<<< HEAD
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
=======
        public String type;
        public String brand;
        public String material;
        public String size;

        public AccessoryItem(Long id,
                             String name,
                             String description,
                             java.math.BigDecimal price,
                             int stock,
                             String imageUrl,
                             String type,
                             String brand,
                             String material,
                             String size) {
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.imageUrl = imageUrl;
<<<<<<< HEAD
            this.loaiPhuKien = loaiPhuKien;
            this.thuongHieu = thuongHieu;
            this.chatLieu = chatLieu;
            this.kichThuoc = kichThuoc;
=======
            this.type = type;
            this.brand = brand;
            this.material = material;
            this.size = size;
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
        }
    }
}
