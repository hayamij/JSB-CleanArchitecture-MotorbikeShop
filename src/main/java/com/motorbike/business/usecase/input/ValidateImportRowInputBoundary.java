package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.validateimportrow.ValidateImportRowInputData;

public interface ValidateImportRowInputBoundary {
    void execute(ValidateImportRowInputData inputData);
}
