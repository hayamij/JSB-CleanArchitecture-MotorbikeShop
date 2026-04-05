package com.motorbike.business.dto.archiveproduct;

import com.motorbike.domain.entities.SanPham;

public class ArchiveProductInputData {
    private final Long productId;
    private final String reason;

    public ArchiveProductInputData(Long productId, String reason) {
        this.productId = productId;
        this.reason = reason;
    }

    public ArchiveProductInputData(Long productId) {
        this.productId = productId;
        this.reason = null;
    }
    
    // Constructor from SanPham entity
    public ArchiveProductInputData(SanPham sanPham) {
        this.productId = sanPham != null ? sanPham.getMaSanPham() : null;
        this.reason = null;
    }

    public Long getProductId() {
        return productId;
    }

    public String getReason() {
        return reason;
    }
}
