package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.formatdataforexport.FormatDataForExportOutputData;

public interface FormatDataForExportOutputBoundary {
    void present(FormatDataForExportOutputData outputData);
}
