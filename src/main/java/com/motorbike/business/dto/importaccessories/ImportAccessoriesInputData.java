package com.motorbike.business.dto.importaccessories;

import org.springframework.web.multipart.MultipartFile;

public class ImportAccessoriesInputData {
    private final MultipartFile file;
    
    public ImportAccessoriesInputData(MultipartFile file) {
        this.file = file;
    }
    
    public MultipartFile getFile() {
        return file;
    }
}
