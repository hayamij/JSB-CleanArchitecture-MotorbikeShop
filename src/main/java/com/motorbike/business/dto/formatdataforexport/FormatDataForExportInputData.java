package com.motorbike.business.dto.formatdataforexport;

import com.motorbike.domain.entities.SanPham;
import java.util.List;

public class FormatDataForExportInputData {
    private final List<?> rawData;
    private final String exportType; // "motorbike", "accessory", "user", "order", etc.

    public FormatDataForExportInputData(List<?> rawData, String exportType) {
        this.rawData = rawData;
        this.exportType = exportType;
    }

    public FormatDataForExportInputData(List<SanPham> products) {
        this.rawData = products;
        this.exportType = "product";
    }

    public List<?> getRawData() {
        return rawData;
    }

    public String getExportType() {
        return exportType;
    }
}
