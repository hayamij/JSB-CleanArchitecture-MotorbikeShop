package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileInputData;

public interface ValidateExcelFileInputBoundary {
    void execute(ValidateExcelFileInputData inputData);
}
