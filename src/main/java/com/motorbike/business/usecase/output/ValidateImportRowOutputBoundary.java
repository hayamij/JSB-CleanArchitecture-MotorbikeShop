package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.validateimportrow.ValidateImportRowOutputData;

public interface ValidateImportRowOutputBoundary {
    void present(ValidateImportRowOutputData outputData);
}
