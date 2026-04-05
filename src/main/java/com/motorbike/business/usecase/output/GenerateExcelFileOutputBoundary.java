package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileOutputData;

public interface GenerateExcelFileOutputBoundary {
    void present(GenerateExcelFileOutputData outputData);
}
