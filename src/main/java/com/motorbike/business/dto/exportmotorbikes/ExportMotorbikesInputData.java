package com.motorbike.business.dto.exportmotorbikes;

public class ExportMotorbikesInputData {
    private final String exportFormat;
    
    public ExportMotorbikesInputData(String exportFormat) {
        this.exportFormat = exportFormat;
    }
    
    public String getExportFormat() {
        return exportFormat;
    }
}
