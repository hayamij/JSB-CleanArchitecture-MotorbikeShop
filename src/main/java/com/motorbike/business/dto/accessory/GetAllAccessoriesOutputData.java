package com.motorbike.business.dto.accessory;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
import java.util.List;

public class GetAllAccessoriesOutputData {

<<<<<<< HEAD
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;

    // Trường hợp thành công
    public GetAllAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.hasError = false;
=======
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<AccessoryItem> accessories;

    public GetAllAccessoriesOutputData(List<AccessoryItem> accessories) {
        this.success = true;
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
        this.errorCode = null;
        this.errorMessage = null;
        this.accessories = accessories;
    }

<<<<<<< HEAD
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
=======
    public GetAllAccessoriesOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.accessories = java.util.Collections.emptyList();
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public List<AccessoryItem> getAccessories() { return accessories; }

    public static class AccessoryItem {
        private final Long id;
        private final String name;
        private final String description;
        private final BigDecimal price;
        private final int stock;
        private final String imageUrl;
        private final String type;
        private final String brand;
        private final String material;
        private final String size;

        public AccessoryItem(Long id,
                             String name,
                             String description,
                             BigDecimal price,
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
        }
=======
            this.type = type;
            this.brand = brand;
            this.material = material;
            this.size = size;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public int getStock() { return stock; }
        public String getImageUrl() { return imageUrl; }
        public String getType() { return type; }
        public String getBrand() { return brand; }
        public String getMaterial() { return material; }
        public String getSize() { return size; }
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
    }
}
