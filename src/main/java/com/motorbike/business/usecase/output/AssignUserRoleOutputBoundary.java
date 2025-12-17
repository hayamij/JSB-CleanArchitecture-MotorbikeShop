package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.assignuserrole.AssignUserRoleOutputData;

public interface AssignUserRoleOutputBoundary {
    
    void present(AssignUserRoleOutputData outputData);
}
