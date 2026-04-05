package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.archiveproduct.ArchiveProductOutputData;

public interface ArchiveProductOutputBoundary {
    void present(ArchiveProductOutputData outputData);
}
