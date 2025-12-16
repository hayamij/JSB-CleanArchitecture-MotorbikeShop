package com.motorbike.adapters.viewmodels;

import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import com.motorbike.business.dto.updateuser.UpdateUserOutputData;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.adapters.dto.response.UpdateUserResponse;

@Component
public class UpdateUserViewModel implements UpdateUserOutputBoundary {

    private ResponseEntity<UpdateUserResponse> response;

    @Override
    public void present(UpdateUserOutputData outputData) {
        if (outputData == null) {
            response = ResponseEntity.status(500).body(UpdateUserResponse.error("SYSTEM_ERROR", "Null output"));
            return;
        }

        if (!outputData.isSuccess()) {
            response = ResponseEntity.badRequest().body(UpdateUserResponse.error(outputData.getErrorCode(), outputData.getMessage()));
            return;
        }

        UpdateUserResponse.SuccessData s = new UpdateUserResponse.SuccessData(
                outputData.getId(),
                outputData.getEmail(),
                outputData.getUsername(),
                outputData.getRole(),
                outputData.isActive(),
                outputData.getUpdatedAt()
        );

        response = ResponseEntity.ok(UpdateUserResponse.success(s));
    }

    public ResponseEntity<UpdateUserResponse> getResponse() {
        return response;
    }
}