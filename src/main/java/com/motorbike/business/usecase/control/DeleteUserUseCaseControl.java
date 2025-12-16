package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.deleteuser.DeleteUserInputData;
import com.motorbike.business.dto.deleteuser.DeleteUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.DeleteUserInputBoundary;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.ValidationException;

public class DeleteUserUseCaseControl implements DeleteUserInputBoundary {

    private final DeleteUserOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    public DeleteUserUseCaseControl(DeleteUserOutputBoundary outputBoundary,
                                    UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(DeleteUserInputData inputData) {
        DeleteUserOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null || !inputData.isAdmin()) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getUserId() == null) {
                throw ValidationException.invalidUserId();
            }
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException == null) {
            try {
                Long id = inputData.getUserId();
                TaiKhoan user = userRepository.findById(id).orElse(null);
                if (user == null) {
                    throw new IllegalArgumentException("Không tìm thấy người dùng: " + id);
                }

                userRepository.deleteById(id);
                outputData = DeleteUserOutputData.forSuccess();
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }
            outputData = DeleteUserOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
