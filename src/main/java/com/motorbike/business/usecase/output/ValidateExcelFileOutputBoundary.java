package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileOutputData;

public interface ValidateExcelFileOutputBoundary {
    void present(ValidateExcelFileOutputData outputData);
}
