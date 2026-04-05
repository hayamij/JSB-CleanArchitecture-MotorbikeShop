package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileInputData;

public interface GenerateExcelFileInputBoundary {
    void execute(GenerateExcelFileInputData inputData);
}
