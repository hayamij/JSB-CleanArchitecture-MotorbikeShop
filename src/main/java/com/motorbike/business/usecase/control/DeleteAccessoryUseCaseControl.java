package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;
import com.motorbike.business.dto.accessory.DeleteAccessoryOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.DeleteAccessoryInputBoundary;
import com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary;
import com.motorbike.domain.exceptions.*;

public class DeleteAccessoryUseCaseControl implements DeleteAccessoryInputBoundary {

    private final DeleteAccessoryOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public DeleteAccessoryUseCaseControl(
            DeleteAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(DeleteAccessoryInputData inputData) {
        DeleteAccessoryOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaSanPham() == null) {
                throw ValidationException.nullProductId();
            }
            
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException == null) {
            try {
                if (!productRepository.existsById(inputData.getMaSanPham())) {
                    throw DomainException.productNotFound(inputData.getMaSanPham().toString());
                }

                productRepository.deleteById(inputData.getMaSanPham());

                outputData = DeleteAccessoryOutputData.forSuccess(
                        inputData.getMaSanPham(),
                        "Xóa phụ kiện thành công"
                );
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof SystemException) {
                errorCode = ((SystemException) errorException).getErrorCode();
            }

            outputData = DeleteAccessoryOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}
