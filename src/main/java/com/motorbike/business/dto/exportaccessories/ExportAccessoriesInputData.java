package com.motorbike.business.dto.exportaccessories;

public class ExportAccessoriesInputData {
    private final String exportFormat;
    
    public ExportAccessoriesInputData(String exportFormat) {
        this.exportFormat = exportFormat;
    }
    
    public String getExportFormat() {
        return exportFormat;
    }
}
